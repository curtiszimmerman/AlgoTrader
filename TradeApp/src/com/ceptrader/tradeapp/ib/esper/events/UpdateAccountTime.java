package com.ceptrader.tradeapp.ib.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class UpdateAccountTime implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          timeStamp;
	
	@Deprecated
	public UpdateAccountTime() {
	}
	
	public UpdateAccountTime(final String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(final String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
