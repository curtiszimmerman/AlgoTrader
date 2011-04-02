package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class TickString implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              tickerId;
	private int	              tickType;
	private String	          value;
	
	@Deprecated
	public TickString() {
	}
	
	public TickString(final int tickerId, final int tickType, final String value) {
		this.tickerId = tickerId;
		this.tickType = tickType;
		this.value = value;
	}
	
	public int getTickerId() {
		return tickerId;
	}
	
	public int getTickType() {
		return tickType;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	public void setTickType(final int tickType) {
		this.tickType = tickType;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
