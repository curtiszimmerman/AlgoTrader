package com.ceptrader.util;

public class Logger {
	public static <T extends Throwable> void log(final T logMessage,
	        final int level) {
		logMessage.printStackTrace();
		System.err.println(level);
	}
	
	public static void log(final String logMessage, final String className,
	        final int level) {
		System.err.println(logMessage);
		System.err.println(className);
		System.err.println(level);
	}
	
	public static <T extends Throwable> void log(final String logMessage,
	        final Class<?> cls, final int level) {
		System.err.println(logMessage);
		System.err.println(cls);
		System.err.println(level);
	}
	
	public static <T extends Throwable> void log(final T logMessage) {
		logMessage.printStackTrace();
	}
	
	public static void log(final Object logMessage) {
		System.err.println(BasicUtils.toString(logMessage));
	}
	
	public static void log(final String logMessage, final String className) {
		System.err.println(logMessage);
		System.err.println(className);
	}
	
	public static void log(final String logMessage, final Class<?> cls) {
		System.err.println(logMessage);
		System.err.println(cls);
	}
}
