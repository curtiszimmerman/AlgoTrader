package com.algoTrader.starter;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.service.MarketDataService;
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

		// subscribe marketData for all securities on the watchlist
		MarketDataService marketDataService = ServiceLocator.serverInstance().getMarketDataService();
		marketDataService.initWatchlist();
	}
}
