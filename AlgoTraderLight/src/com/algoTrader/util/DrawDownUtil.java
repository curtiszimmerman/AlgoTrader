package com.algoTrader.util;


public class DrawDownUtil {

	private static long drawDownPeriod;
	
	public static long resetDrawDownPeriod() {
		
		return drawDownPeriod = 0;
	}

	public static long increaseDrawDownPeriod(long milliseconds) {
				
		drawDownPeriod += milliseconds;

		return drawDownPeriod;
	}
}
