package com.algoTrader.service.ray;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.LimitOrder;
import com.algoTrader.entity.trade.MarketOrder;
import com.algoTrader.entity.trade.StopOrder;
import com.algoTrader.enumeration.OCOType;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.service.PositionService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class RayServiceImpl {
	private static final String SERVICE = "rayService";
	private static final String STRATEGY = "RAY";

	private static Logger logger = MyLogger.getLogger(RayServiceImpl.class.getName());
	private static int lots = ConfigurationUtil.getStrategyConfig(STRATEGY).getInt("positionSize");
	private static int target = ConfigurationUtil.getStrategyConfig(STRATEGY).getInt("profitTarget");
	private static int stop = ConfigurationUtil.getStrategyConfig(STRATEGY).getInt("stopLoss");
	private static String underlayingIsin = ConfigurationUtil.getStrategyConfig(STRATEGY).getString("underlayingIsin");

	private final PositionService positionService;
	private final LookupService lookupService;
	private final OrderService orderService;
	private final Security security;
	private final double Points;
	private final NumberFormat priceFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");

	private boolean Initialized = false;
	private boolean Running = false;
	private Date BarTime;
	private double Bid = 0.0;
	private double Ask = 0.0;
	private double Open = 0.0;
	private boolean LastDirn = false;
	private double Close = 0.0;

	private MarketOrder entryOrder = null; 
	private LimitOrder targetOrder = null; 
	private StopOrder stopOrder = null; 

	public RayServiceImpl(PositionService positionService, LookupService lookupService, OrderService orderService) {

		this.positionService = positionService;
		this.lookupService = lookupService;
		this.orderService = orderService;
		this.security = this.lookupService.getSecurityByIsin(underlayingIsin);
		// An elaborate way of avoiding LazyInitializationException
		Points = this.lookupService.getSecurityFetched(security.getId()).getSecurityFamily().getTickSize();

		Init();

	}

	//+------------------------------------------------------------------+
	//| Places an order                                                  |
	//+------------------------------------------------------------------+
	public boolean order(Side side, double entry, int quantity, double target, double stop) {

		Side exitSide;
		BigDecimal entryPrice = BigDecimal.valueOf(entry);
		BigDecimal limitPrice = BigDecimal.valueOf(target);
		BigDecimal stopPrice = BigDecimal.valueOf(stop);

		logger.info("Entering " + side + ", Entry " + entryPrice + ", Target " + limitPrice + ", Stop " + stopPrice);
		if (side == Side.BUY) {
			exitSide = Side.SELL;
		} else {
			exitSide = Side.BUY;
		}

		entryOrder = createMarketOrder(side, quantity);
		targetOrder = createLimitOrder(exitSide, quantity, limitPrice);
		if (!entryOrder.addChildOrders(targetOrder))
			logger.error("Problem adding child order " + entryOrder);
		targetOrder.setOcoType(OCOType.REDUCE_OTHERS);
		stopOrder = createStopOrder(exitSide, quantity, stopPrice);
		if (!entryOrder.addChildOrders(stopOrder))
			logger.error("Problem adding child order " + stopOrder);
		stopOrder.setOcoType(OCOType.REDUCE_OTHERS);

		this.orderService.sendOrder(entryOrder);

		return true;
	}

	public MarketOrder createMarketOrder(Side side, int quantity) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		MarketOrder order = MarketOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(side);

		logger.info("Created " + order);
		
		return (order);

	}

	public LimitOrder createLimitOrder(Side side, int quantity, BigDecimal limit) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		LimitOrder order = LimitOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(side);
		order.setLimit(limit.setScale(4, BigDecimal.ROUND_HALF_UP));
		order.setOcoType(OCOType.CANCEL_OTHERS);
		
		logger.info("Created " + order);

		return (order);
	}

	public StopOrder createStopOrder(Side side, int quantity, BigDecimal stop) {


		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		StopOrder order = StopOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(side);
		order.setStop(stop.setScale(4, BigDecimal.ROUND_HALF_UP));
		order.setOcoType(OCOType.CANCEL_OTHERS);
		
		logger.info("Created " + order);

		return (order);
	}

	//+------------------------------------------------------------------+
	//	| Performs strategy initialisation                                 |
	//+------------------------------------------------------------------+
	void InitSystem() {
		if (CheckExit()) {
			if (stopOrder != null) {
				this.orderService.cancelOrder(stopOrder);
				stopOrder = null;
			}
			if (targetOrder != null) {
				this.orderService.cancelOrder(targetOrder);
				targetOrder = null;
			}
			Running = false;
		} else {
			Running = true;
		}

		Initialized = true;
		
		logger.info("Initialised " + security.getSymbol() + " OK. Running = " + Running);
	}

	//+------------------------------------------------------------------+
	//| Checks for entry to a trade                                      |
	//+------------------------------------------------------------------+
	boolean CheckEntry(int Size) {
		boolean Short;

		if (Close > Open) // Short term uptrend so GO LONG!
			Short = false;
		else if (Close < Open) // Short term downtrend so GO SHORT!
			Short = true;
		else
			Short = LastDirn;
		LastDirn = Short;

		logger.info("Entry - Open: " + priceFormat.format(Open) + ", Close: " + priceFormat.format(Close) + ", Bid: " + priceFormat.format(Bid) + ", Ask: "
				+ priceFormat.format(Ask) + ", Short: " + Short + " at " + dateFormat.format(BarTime));

		if (Short) {
			return (order(Side.SELL, Bid, Size, Bid - (Points * target), Ask + (Points * stop)));
		} else {
			return (order(Side.BUY, Ask, Size, Ask + (Points * target), Bid - (Points * stop)));
		}
	}

	//+------------------------------------------------------------------+
	//| Checks for exit from a trade                                     |
	//+------------------------------------------------------------------+
	boolean CheckExit() {
		Position position = this.lookupService.getPositionBySecurityAndStrategy(security.getId(), STRATEGY);

		if (position != null && !position.isFlat())
			return (false);
		else
			return (true);
	}

	//+------------------------------------------------------------------+
	//| Performs one time strategy initialisation                          |
	//+------------------------------------------------------------------+
	public boolean Init() {
		Initialized = false;

		priceFormat.setMaximumFractionDigits(5);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		logger.info("Started " + security.getSymbol() + ", Points = " + priceFormat.format(Points));

		return true;
	}

	//+------------------------------------------------------------------+
	//| Execute our strategy                                             |
	//+------------------------------------------------------------------+
	public boolean Execute(Bar bar) {
		BarTime = bar.getDateTime();
		Open = bar.getOpen().doubleValue();
		Close = bar.getClose().doubleValue();
		Bid = Close;
		// bar.security is currently null, so cheat!
		Ask = security.getLastAsk().getPrice().doubleValue();
//		Ask = bar.getSecurity().getLastAsk().getPrice().doubleValue();

		if (!Initialized) {
			InitSystem(); 				//  Start our strategy
		}

		if (Running) { 					// Are we in a trade at the moment?
			if (CheckExit()) { 			// Yes - Last trade complete?
				Initialized = false; 	// Yes - Indicate we need to reinitialise
				InitSystem(); 			//  and start all over again!
			}
		}

		if (!Running) { 				// Do we need to decide whether to enter?
			if (CheckEntry(lots)) 		// Entered a trade?
				Running = true; 		// Yes - Indicate that we're in a trade
		}

		return true;
	}

	public static class OnBarSubscriber {

		public void update(Bar bar) {

			long startTime = System.currentTimeMillis();
			logger.debug("RAY OnBar");

			RayServiceImpl rayService = (RayServiceImpl) ServiceLocator.commonInstance().getService(SERVICE);
			rayService.Execute(bar);

			logger.debug("RAY OnBar end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}

	public static class OnTickSubscriber {

		public void update(Tick tick) {

			long startTime = System.currentTimeMillis();
			logger.debug("RAY OnTick");

			RayServiceImpl rayService = (RayServiceImpl) ServiceLocator.commonInstance().getService(SERVICE);

			logger.debug("RAY OnTick end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
}
