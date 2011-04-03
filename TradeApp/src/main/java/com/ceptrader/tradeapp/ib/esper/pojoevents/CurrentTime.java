package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class CurrentTime implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private long	          time;
	
	@Deprecated
	public CurrentTime() {
	}
	
	public CurrentTime(final long time) {
		this.time = time;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(final long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
