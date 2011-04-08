package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

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
