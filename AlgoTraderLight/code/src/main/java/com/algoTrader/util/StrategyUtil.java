package com.algoTrader.util;

import com.algoTrader.entity.StrategyImpl;

public class StrategyUtil {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	/**
	 * returns the "main" started startegy. in simulation this is always BASE in realtime this is whatever has been specified on the command-line
	 */
	public static String getStartedStrategyName() {

		String strategyName;
		if (simulation) {
			return StrategyImpl.BASE;
		} else {
			strategyName = ConfigurationUtil.getBaseConfig().getString("strategyName");
			if (strategyName != null) {
				return strategyName;
			} else {
				throw new RuntimeException("no strategy defined on commandline");
			}
		}
	}
}
