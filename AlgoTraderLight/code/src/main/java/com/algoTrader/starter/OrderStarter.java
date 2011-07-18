package com.algoTrader.starter;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.OrderService;

public class OrderStarter {

	public static void main(String[] args) {

		int quantity = Integer.parseInt(args[0]);
		int securityId = Integer.parseInt(args[1]);

		ServiceLocator.serverInstance().getRuleService().initServiceProvider(StrategyImpl.BASE);
		ServiceLocator.serverInstance().getRuleService().deployAllModules(StrategyImpl.BASE);

		Strategy strategy = ServiceLocator.serverInstance().getLookupService().getStrategyByName("MOV");
		Security security = ServiceLocator.serverInstance().getLookupService().getSecurity(securityId);

		//submits a market order 
		Order order = new MarketOrderImpl();
		order.setQuantity(quantity);
		order.setSide(Side.BUY);
		order.setSecurity(security);
		order.setStrategy(strategy);

		OrderService orderService = ServiceLocator.serverInstance().getOrderService();
		orderService.sendOrder(order);
	}
}
