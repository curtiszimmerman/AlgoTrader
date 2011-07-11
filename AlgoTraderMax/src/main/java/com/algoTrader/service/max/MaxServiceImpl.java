package com.algoTrader.service.max;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.service.PositionService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class MaxServiceImpl {

	private static Logger logger = MyLogger.getLogger(MaxServiceImpl.class.getName());

	private final PositionService positionService;
	private final LookupService lookupService;
	private final OrderService orderService;
	private static int positionSize = ConfigurationUtil.getStrategyConfig("MAX").getInt("positionSize");

	public MaxServiceImpl(PositionService positionService, LookupService lookupService, OrderService orderService) {

		this.positionService = positionService;
		this.lookupService = lookupService;
		this.orderService = orderService;
	}

	public void openPosition(String strategyName, int securityId, BigDecimal currentValue, boolean side) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(strategyName);
		Security security = this.lookupService.getSecurity(securityId);
		Position position = this.lookupService.getPositionBySecurityAndStrategy(securityId, strategyName);

		if (position != null && position.isOpen()) {
			this.positionService.closePosition(position.getId());
		}

		Order order = new MarketOrderImpl();
		order.setSecurity(security);
		order.setQuantity(positionSize);
		if (side)
			order.setSide(Side.SELL);
		else	
			order.setSide(Side.BUY);

		this.orderService.sendOrder(strategy.getName(), order);
		
		if (position != null && position.isOpen()) {
			this.positionService.setMargin(position.getId());
		}
	}

	public static class OpenLongSubscriber {
	
		public void update(String strategyName, int securityId, BigDecimal currentValue) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("MAX openLong start");
	
			MaxServiceImpl maxService = ((MaxServiceImpl) ServiceLocator.commonInstance().getService("maxService"));
			maxService.openPosition(strategyName, securityId, currentValue, true);
	
			logger.debug("MAX openLong end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
	
	public static class OpenShortSubscriber {
		
		public void update(String strategyName, int securityId, BigDecimal currentValue) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("MAX openShort start");
	
			MaxServiceImpl maxService = ((MaxServiceImpl) ServiceLocator.commonInstance().getService("maxService"));
			maxService.openPosition(strategyName, securityId, currentValue, false);
	
			logger.debug("MAX openShort end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}

}