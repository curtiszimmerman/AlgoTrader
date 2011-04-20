package com.algoTrader.util;

import java.util.Date;

public class CustomDate extends Date {
	
	private static final long	serialVersionUID	= -1910877406220278376L;
	
	public CustomDate(final String date) {
		super();
		
		if (!"".equals(date)) {
			setTime(Long.parseLong(date));
		}
	}
	
	public CustomDate(final long date) {
		super(date);
	}
	
}
