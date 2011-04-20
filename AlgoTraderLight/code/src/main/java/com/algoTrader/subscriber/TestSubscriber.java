package com.algoTrader.subscriber;

import java.util.Map;

import org.apache.log4j.Logger;

import com.algoTrader.util.MyLogger;

public class TestSubscriber {
	
	private static Logger	logger	= MyLogger.getLogger(TestSubscriber.class
	                                       .getName());
	
	public void update(final Map<?, ?> map) {
		
		TestSubscriber.logger.info(map);
	}
}
