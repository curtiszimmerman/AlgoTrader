package com.ceptrader.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

public class BasicUtils {
	private static ExecutorService	threadPool	= Executors
	                                                                    .newCachedThreadPool();
	
	public static ExecutorService getThreadPool() {
		return BasicUtils.threadPool;
	}
	
	public static String toString(final Object o) {
		if (o == null) { return "null"; }
		
		final Class<?> c = o.getClass();
		final Field[] fld = c.getDeclaredFields();
		final String clsName = c.getName();
		final StringBuffer sb = new StringBuffer();
		
		Throwable t = new Throwable();
		t = t.fillInStackTrace();
		final StackTraceElement[] sEle = t.getStackTrace();
		final StringBuffer nToStr = new StringBuffer();
		
		final String tabChar = "\t";
		for (final StackTraceElement ste : sEle) {
			final String fn = ste.getMethodName().toLowerCase();
			final String cl = ste.getClassName().toLowerCase();
			if (fn.contains("tostring") && cl.contains("tradeapp.util.util")) {
				nToStr.append(tabChar);
			}
		}
		
		if (nToStr.length() > 0) {
			sb.append("\n");
			sb.append(nToStr);
		}
		sb.append(clsName);
		sb.append(": { \n");
		
		for (final Field f : fld) {
			try {
				f.setAccessible(true);
				final Object tmpO = f.get(o);
				final Class<?> fc = tmpO.getClass();
				
				if (fc.isPrimitive() || fc == String.class) {
					sb.append(nToStr);
					sb.append(tabChar);
					sb.append("field: ");
					sb.append(f.getName());
					sb.append("; value: ");
					sb.append(tmpO);
				} else if (fc.isArray()) {
					final String oName = f.getName();
					
					sb.append(nToStr);
					sb.append(tabChar);
					
					sb.append(oName);
					
					sb.append(": [\n");
					
					sb.append(nToStr);
					sb.append(tabChar);
					sb.append(tabChar);
					
					sb.append(Arrays.deepToString((Object[]) tmpO));
					
					sb.append("\n");
					sb.append(nToStr);
					sb.append(tabChar);
					
					sb.append("] //:~ End Array:- ");
					sb.append(oName);
					sb.append(" ~://");
				} else {
					final String oName = f.getName();
					
					sb.append(nToStr);
					sb.append(tabChar);
					
					sb.append(oName);
					
					sb.append(": (\n");
					
					sb.append(nToStr);
					sb.append(tabChar);
					sb.append(tabChar);
					
					sb.append(tmpO);
					
					sb.append("\n");
					sb.append(nToStr);
					sb.append(tabChar);
					
					sb.append(") //:~ End Object :- ");
					sb.append(oName);
					sb.append(" ~://");
				}
				
				sb.append(", \n");
			} catch (final Exception e) {
			}
		}
		
		sb.append("\n");
		sb.append(nToStr);
		sb.append(" } //:~ End Class:- ");
		sb.append(clsName);
		sb.append(" ~://\n");
		
		return sb.toString();
	}
	
	public static DateTime checkDateTime(final String dt) {
		DateTime
	}
}
