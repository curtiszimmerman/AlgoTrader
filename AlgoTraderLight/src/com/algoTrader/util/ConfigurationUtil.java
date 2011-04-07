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
	
	private static String	                           baseFileName	      = "conf-base.properties";
	
	private static CompositeConfiguration	           baseConfig	      = null;
	private static Map<String, CompositeConfiguration>	strategyConfigMap	= new HashMap<String, CompositeConfiguration>();
	
	private static Logger	                           logger	          = MyLogger
	                                                                              .getLogger(ConfigurationUtil.class
	                                                                                      .getName());
	
	public static Configuration getBaseConfig() {
		
		if (ConfigurationUtil.baseConfig == null) {
			ConfigurationUtil.baseConfig = new CompositeConfiguration();
			
			ConfigurationUtil.baseConfig
			        .addConfiguration(new SystemConfiguration());
			try {
				ConfigurationUtil.baseConfig
				        .addConfiguration(new PropertiesConfiguration(
				                ConfigurationUtil.baseFileName));
			} catch (final ConfigurationException e) {
				ConfigurationUtil.logger.error("error loading base.properties",
				        e);
			}
		}
		return ConfigurationUtil.baseConfig;
	}
	
	public static Configuration getStrategyConfig(final String strategyName) {
		
		if (StrategyImpl.BASE.equals(strategyName.toUpperCase())) { return ConfigurationUtil
		        .getBaseConfig(); }
		
		CompositeConfiguration strategyConfig = ConfigurationUtil.strategyConfigMap
		        .get(strategyName.toUpperCase());
		if (strategyConfig == null) {
			
			strategyConfig = new CompositeConfiguration();
			strategyConfig.addConfiguration(new SystemConfiguration());
			try {
				strategyConfig.addConfiguration(new PropertiesConfiguration(
				        "conf-" + strategyName.toLowerCase() + ".properties"));
				strategyConfig.addConfiguration(new PropertiesConfiguration(
				        ConfigurationUtil.baseFileName));
			} catch (final ConfigurationException e) {
				ConfigurationUtil.logger.error(
				        "error loading " + strategyName.toLowerCase() +
				                ".properties", e);
			}
			ConfigurationUtil.strategyConfigMap.put(strategyName.toUpperCase(),
			        strategyConfig);
		}
		return strategyConfig;
	}
	
	public static void resetConfig() {
		
		ConfigurationUtil.baseConfig = null;
		ConfigurationUtil.strategyConfigMap = new HashMap<String, CompositeConfiguration>();
	}
}
