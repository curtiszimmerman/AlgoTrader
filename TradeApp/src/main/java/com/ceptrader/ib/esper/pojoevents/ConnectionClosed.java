package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class ConnectionClosed implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private static long	      count	             = 0;
	
	public ConnectionClosed() {
		ConnectionClosed.count++;
	}
	
	public long getCount() {
		return ConnectionClosed.count;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
