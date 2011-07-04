package com.algoTrader.listener;

import org.apache.log4j.Logger;

import com.algoTrader.util.MyLogger;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class TestListener implements UpdateListener {

	private static Logger logger = MyLogger.getLogger(TestListener.class.getName());

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		long startTime = System.currentTimeMillis();
		logger.info("testListener start");

		EventBean event = newEvents[0];
		logger.info("event underlying object: " + event.getUnderlying() + " event " + event.toString());

		logger.info("testListener end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
	}
}
