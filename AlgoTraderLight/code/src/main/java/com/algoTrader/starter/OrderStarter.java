package com.algoTrader.starter;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.OrderService;
import com.algoTrader.util.MyLogger;

public class OrderStarter {
	
	public static Logger logger = MyLogger.getLogger(OrderStarter.class.getName());
	
	public static void main(String[] args) {
		
		int quantity = Integer.parseInt(args[0]);
		int securityId = Integer.parseInt(args[1]);
		
		LookupService lookupService = ServiceLocator.commonInstance().getLookupService();
		
		ServiceLocator.commonInstance().getRuleService().initServiceProvider(StrategyImpl.BASE);
		ServiceLocator.commonInstance().getRuleService().deployAllModules(StrategyImpl.BASE);
		
		Security security = lookupService.getSecurity(securityId);
		
		//submits a simple market order of 1000 shares
		Order order = new MarketOrderImpl();
		order.setSecurity(security);
		order.setQuantity(quantity);
		order.setSide(Side.BUY);
		
		OrderService orderService = ServiceLocator.commonInstance().getOrderService();
		orderService.sendOrder(StrategyImpl.BASE, order);
	}
}
