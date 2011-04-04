package com.algoTrader.util;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;

public class StrategyUtil {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");
	private static Strategy startedStrategy;

	/**
	 * returns the "main" started startegy. in simulation this is always BASE in realtime this is whatever has been specified on the command-line
	 */
	public static Strategy getStartedStrategy() {

		if (startedStrategy == null) {
			String strategyName;
			if (simulation) {
				strategyName = StrategyImpl.BASE;
			} else {
				strategyName = ConfigurationUtil.getBaseConfig().getString("strategyName");
				if (strategyName == null) {
					throw new RuntimeException("no strategy defined on commandline");
				}
			}
			startedStrategy = ServiceLocator.commonInstance().getLookupService().getStrategyByNameFetched(strategyName);
		}
		return startedStrategy;
	}

	public static String getStartedStrategyName() {

		return getStartedStrategy().getName();
	}
}