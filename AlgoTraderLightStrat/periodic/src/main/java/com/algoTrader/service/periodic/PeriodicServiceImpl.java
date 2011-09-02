package com.algoTrader.service.periodic;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.LimitOrder;
import com.algoTrader.entity.trade.MarketOrder;
import com.algoTrader.entity.trade.SteppingLimitOrder;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class PeriodicServiceImpl {

	private static final String SERVICE = "periodicService";
	private static final String STRATEGY = "PERIODIC";

	private static Logger logger = MyLogger.getLogger(PeriodicServiceImpl.class.getName());
	private static String underlayingIsin = ConfigurationUtil.getStrategyConfig(STRATEGY).getString("underlayingIsin");

	private LookupService lookupService;
	private OrderService orderService;

	public PeriodicServiceImpl(LookupService lookupService, OrderService orderService) {

		this.lookupService = lookupService;
		this.orderService = orderService;
	}

	public void createMarketOrder(int quantity) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		MarketOrder order = MarketOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(quantity > 0 ? Side.BUY : Side.SELL);

		logger.info("placed " + order);

		this.orderService.sendOrder(order);
	}

	public void createLimitOrder(int quantity, BigDecimal limit) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		LimitOrder order = LimitOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(quantity > 0 ? Side.BUY : Side.SELL);
		order.setLimit(limit.setScale(2, BigDecimal.ROUND_HALF_UP));

		logger.info("placed " + order);

		this.orderService.sendOrder(order);
	}

	public void createSteppingLimitOrder(int quantity, BigDecimal limit, BigDecimal maxLimit, BigDecimal increment) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(STRATEGY);
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		SteppingLimitOrder order = SteppingLimitOrder.Factory.newInstance();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(quantity > 0 ? Side.BUY : Side.SELL);
		order.setLimit(limit.setScale(2, BigDecimal.ROUND_HALF_UP));
		order.setMaxLimit(maxLimit.setScale(2, BigDecimal.ROUND_HALF_UP));
		order.setIncrement(increment.setScale(2, BigDecimal.ROUND_HALF_UP));

		logger.info("placed " + order);

		this.orderService.sendOrder(order);
	}

	public static class CreateMarketOrderSubscriber {

		public void update(int quantity) {

			PeriodicServiceImpl movService = (PeriodicServiceImpl) ServiceLocator.commonInstance().getService(SERVICE);
			movService.createMarketOrder(quantity);
		}
	}
	
	public static class CreateLimitOrderSubscriber {

		public void update(int quantity, BigDecimal limit) {
	
			PeriodicServiceImpl movService = (PeriodicServiceImpl) ServiceLocator.commonInstance().getService(SERVICE);
			movService.createLimitOrder(quantity, limit);
		}
	}

	public static class CreateSteppingLimitOrderSubscriber {

		public void update(int quantity, BigDecimal limit, BigDecimal maxLimit, BigDecimal increment) {

			PeriodicServiceImpl movService = (PeriodicServiceImpl) ServiceLocator.commonInstance().getService(SERVICE);
			movService.createSteppingLimitOrder(quantity, limit, maxLimit, increment);
		}
	}
}