package com.algoTrader.util;

import java.util.Date;

public class CustomDate1 extends Date {

	private static final long serialVersionUID = -1910877406220278376L;

	public CustomDate1(String date) {
		super();
		
		if (!"".equals(date)) {
			setTime(Long.parseLong(date));
		}
	}

	public CustomDate1(long date) {
		super(date);
	}

}
