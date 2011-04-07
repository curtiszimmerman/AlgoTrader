package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.esper.generic.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class TickGeneric implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              tickerId;
	private int	              tickType;
	private double	          value;
	
	@Deprecated
	public TickGeneric() {
	}
	
	public TickGeneric(final int tickerId, final int tickType,
	        final double value) {
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
	
	public double getValue() {
		return value;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	public void setTickType(final int tickType) {
		this.tickType = tickType;
	}
	
	public void setValue(final double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
