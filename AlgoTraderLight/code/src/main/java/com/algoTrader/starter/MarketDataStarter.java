package com.algoTrader.starter;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.service.MarketDataService;
import com.algoTrader.service.OrderService;
import com.algoTrader.service.RuleService;

public class MarketDataStarter {

	public static void main(String[] args) {

		start();
	}

	public static void start() {

		// start all BASE rules
		RuleService ruleService = ServiceLocator.serverInstance().getRuleService();
		ruleService.initServiceProvider(StrategyImpl.BASE);
		ruleService.setInternalClock(StrategyImpl.BASE, true);
		ruleService.deployAllModules(StrategyImpl.BASE);

		// initialize the IB services
		MarketDataService marketDataService = ServiceLocator.serverInstance().getMarketDataService();
		marketDataService.init();
		
		OrderService orderService = ServiceLocator.serverInstance().getOrderService();
		orderService.init();

		// subscribe marketData for all securities on the watchlist (needs to be invoked after all Spring Services have been properly initialized)
		marketDataService.initWatchlist();
	}
}
