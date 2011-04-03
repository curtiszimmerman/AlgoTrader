package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class OpenOrderEnd implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private static long	      count	             = 0;
	
	public OpenOrderEnd() {
		OpenOrderEnd.count++;
	}
	
	public long getCount() {
		return OpenOrderEnd.count;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
