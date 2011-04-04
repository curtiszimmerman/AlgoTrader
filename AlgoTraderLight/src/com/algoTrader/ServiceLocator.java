package com.algoTrader;

import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.util.ConfigurationUtil;

public class ServiceLocator {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");
	private static String strategyName = ConfigurationUtil.getBaseConfig().getString("strategyName");

	public static CommonServiceLocator commonInstance() {

		if (!simulation && !StrategyImpl.BASE.equals(strategyName)) {
			return RemoteServiceLocator.instance();
		} else {
			return ServerServiceLocator.instance();
		}
	}

	public static ServerServiceLocator serverInstance() {

		if (!simulation && !StrategyImpl.BASE.equals(strategyName)) {
			throw new IllegalArgumentException("serverInstance cannot be called from the client");
		} else {
			return ServerServiceLocator.instance();
		}
	}
}
