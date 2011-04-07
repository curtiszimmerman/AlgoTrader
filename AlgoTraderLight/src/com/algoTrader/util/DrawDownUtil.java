package com.algoTrader.util;

public class DrawDownUtil {
	
	private static long	drawDownPeriod;
	
	public static long resetDrawDownPeriod() {
		
		return DrawDownUtil.drawDownPeriod = 0;
	}
	
	public static long increaseDrawDownPeriod(final long milliseconds) {
		
		DrawDownUtil.drawDownPeriod += milliseconds;
		
		return DrawDownUtil.drawDownPeriod;
	}
}
