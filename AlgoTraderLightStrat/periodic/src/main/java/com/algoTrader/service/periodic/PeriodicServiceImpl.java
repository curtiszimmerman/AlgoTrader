package com.algoTrader.service.periodic;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class PeriodicServiceImpl {

	private static Logger logger = MyLogger.getLogger(PeriodicServiceImpl.class.getName());
	private static String underlayingIsin = ConfigurationUtil.getStrategyConfig("MOV").getString("underlayingIsin");

	private LookupService lookupService;
	private OrderService orderService;

	public PeriodicServiceImpl(LookupService lookupService, OrderService orderService) {

		this.lookupService = lookupService;
		this.orderService = orderService;
	}

	public void createOrder(int quantity) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched("MOV");
		Security security = this.lookupService.getSecurityByIsin(underlayingIsin);

		Order order = new MarketOrderImpl();
		order.setSecurity(security);
		order.setStrategy(strategy);
		order.setQuantity(Math.abs(quantity));
		order.setSide(quantity > 0 ? Side.BUY : Side.SELL);

		logger.info("placed order for quantity: " + quantity);

		this.orderService.sendOrder(order);
	}

	public static class CreateOrderSubscriber {
	
		public void update(int quantity) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("createOrder start");
	
			PeriodicServiceImpl movService = (PeriodicServiceImpl) ServiceLocator.commonInstance().getService("movService");
			movService.createOrder(quantity);
	
			logger.debug("createOrder end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
}