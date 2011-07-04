package com.algoTrader.util;


public class DrawDownUtil {

	private static long drawDownPeriod;
	
	public static long resetDrawDownPeriod() {
		
		drawDownPeriod = 0;

		return drawDownPeriod;
	}

	public static long increaseDrawDownPeriod(long milliseconds) {
				
		drawDownPeriod += milliseconds;

		return drawDownPeriod;
	}
}
