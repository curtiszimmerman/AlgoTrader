package com.algoTrader.service.periodic;

import java.io.IOException;

import com.algoTrader.ServiceLocator;
import com.algoTrader.service.RuleService;
import com.algoTrader.service.StrategyService;
import com.algoTrader.util.ConfigurationUtil;

public class PeriodicStarter {

	public static void main(String[] args) throws IOException {

		start();
	}

	public static void start() throws IOException {

		String strategyName = ConfigurationUtil.getBaseConfig().getString("strategyName");

		RuleService ruleService = ServiceLocator.commonInstance().getRuleService();
		StrategyService strategyService = ServiceLocator.commonInstance().getStrategyService();

		ruleService.initServiceProvider(strategyName);

		// switch to internalClock
		ruleService.setInternalClock(strategyName, true);
		
		//activate the rest of the rules
		ruleService.deployModule(strategyName, "periodic-main");

		// register the strategy with BASE so we can receive events
		strategyService.registerStrategy(strategyName);
	}
}