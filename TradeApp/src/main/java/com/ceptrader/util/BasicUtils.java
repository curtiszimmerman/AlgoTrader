package com.ceptrader.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class BasicUtils {
	private static ExecutorService	threadPool	= Executors
	                                                                    .newCachedThreadPool();
	
	public static ExecutorService getThreadPool() {
		return BasicUtils.threadPool;
	}
	
	public static String toString(final Object o) {
		final ToStringBuilder ts = new ToStringBuilder(o);
		
		return ts.toString();
	}
	
	private static final DateTimeFormatter	format;
	
	static {
		format = new DateTimeFormatterBuilder().appendYear(2, 4)
		        .appendMonthOfYear(2)
		        .appendDayOfMonth(2)
		        .appendLiteral(' ').appendHourOfDay(2).appendLiteral(':')
		        .appendMinuteOfHour(2).appendLiteral(':')
		        .appendSecondOfMinute(2).appendLiteral(' ')
		        .appendTimeZoneShortName().toFormatter();
		
	}
	
	public static DateTime checkDateTime(final String dt) {
		return BasicUtils.checkDateTime(dt, BasicUtils.format);
	}
	
	public static DateTime checkDateTime(final String dt,
	        final DateTimeFormatter format) {
		return format.parseDateTime(dt);
	}
}
