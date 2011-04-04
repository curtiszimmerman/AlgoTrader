package com.algoTrader.subscriber;

import java.util.Map;

import org.apache.log4j.Logger;

import com.algoTrader.util.MyLogger;

public class TestSubscriber {

	private static Logger logger = MyLogger.getLogger(TestSubscriber.class.getName());

	public void update(Map<?, ?> map) {

		logger.info(map);
	}
}
