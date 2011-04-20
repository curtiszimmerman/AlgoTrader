package com.algoTrader.service.mov;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.service.PositionService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class MovServiceImpl {

	private static Logger logger = MyLogger.getLogger(MovServiceImpl.class.getName());

	private PositionService positionService;
	private LookupService lookupService;
	private OrderService orderService;
	private static double initialStopLoss = ConfigurationUtil.getStrategyConfig("MOV").getDouble("initialStopLoss");

	public MovServiceImpl(PositionService positionService, LookupService lookupService, OrderService orderService) {

		this.positionService = positionService;
		this.lookupService = lookupService;
		this.orderService = orderService;
	}

	public void openPosition(String strategyName, int securityId, BigDecimal currentValue) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(strategyName);
		Security security = this.lookupService.getSecurity(securityId);

		int qty = (int) (strategy.getAvailableFundsDouble() / currentValue.doubleValue());
		
		if (qty <= 0)
			return;

		Order order = new MarketOrderImpl();
		order.setSecurity(security);
		order.setQuantity(qty);
		order.setSide(Side.BUY);

		this.orderService.sendOrder(strategy.getName(), order);
		
		// if a position was open (or already existed) set margin and exitValue
		Position position = this.lookupService.getPositionBySecurityAndStrategy(securityId, strategyName);
		if (position != null && position.isOpen()) {
			this.positionService.setMargin(position.getId());

			double exitValue = initialStopLoss * currentValue.doubleValue();
			this.positionService.setExitValue(position.getId(), exitValue, true);
		}
	}

	public static class OpenPositionSubscriber {
	
		public void update(String strategyName, int securityId, BigDecimal currentValue) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("openPosition start");
	
			MovServiceImpl movService = ((MovServiceImpl) ServiceLocator.commonInstance().getService("movService"));
			movService.openPosition(strategyName, securityId, currentValue);
	
			logger.debug("openPosition end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
}