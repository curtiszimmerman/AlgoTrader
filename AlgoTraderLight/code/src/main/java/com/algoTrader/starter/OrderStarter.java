package com.algoTrader.starter;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.OrderService;
import com.algoTrader.util.MyLogger;

public class OrderStarter {

	private static Logger logger = MyLogger.getLogger(OrderStarter.class.getName());

	public static void main(String[] args) {

		int quantity = Integer.parseInt(args[0]);
		int securityId = Integer.parseInt(args[1]);

		ServiceLocator.serverInstance().getRuleService().initServiceProvider(StrategyImpl.BASE);
		ServiceLocator.serverInstance().getRuleService().deployAllModules(StrategyImpl.BASE);

		//submits a simple market order of 1000 shares
		Order order = new MarketOrderImpl();
		order.setQuantity(quantity);
		order.setSide(Side.BUY);

		OrderService orderService = ServiceLocator.serverInstance().getOrderService();
		orderService.sendOrder(StrategyImpl.BASE, order, securityId);
	}
}
