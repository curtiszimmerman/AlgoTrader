package com.algoTrader.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.log4j.Logger;

import com.algoTrader.entity.StrategyImpl;

public class ConfigurationUtil {

	private static String baseFileName = "conf-base.properties";

	private static CompositeConfiguration baseConfig = null;
	private static Map<String, CompositeConfiguration> strategyConfigMap = new HashMap<String, CompositeConfiguration>();

	private static Logger logger = MyLogger.getLogger(ConfigurationUtil.class.getName());


	public static Configuration getBaseConfig() {

		if (baseConfig == null) {
			baseConfig = new CompositeConfiguration();

			baseConfig.addConfiguration(new SystemConfiguration());
			try {
				baseConfig.addConfiguration(new PropertiesConfiguration(baseFileName));
			} catch (ConfigurationException e) {
				logger.error("error loading base.properties", e);
			}
		}
		return baseConfig;
	}

	public static Configuration getStrategyConfig(String strategyName) {

		if (StrategyImpl.BASE.equals(strategyName.toUpperCase()))
			return getBaseConfig();

		CompositeConfiguration strategyConfig = strategyConfigMap.get(strategyName.toUpperCase());
		if (strategyConfig == null) {

			strategyConfig = new CompositeConfiguration();
			strategyConfig.addConfiguration(new SystemConfiguration());
			try {
				strategyConfig.addConfiguration(new PropertiesConfiguration("conf-" + strategyName.toLowerCase() + ".properties"));
				strategyConfig.addConfiguration(new PropertiesConfiguration(baseFileName));
			} catch (ConfigurationException e) {
				logger.error("error loading " + strategyName.toLowerCase() + ".properties", e);
			}
			strategyConfigMap.put(strategyName.toUpperCase(), strategyConfig);
		}
		return strategyConfig;
	}

	public static void resetConfig() {

		baseConfig = null;
		strategyConfigMap = new HashMap<String, CompositeConfiguration>();
	}
}
