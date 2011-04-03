package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class TickSize implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              field;
	private int	              size;
	private int	              tickerId;
	
	@Deprecated
	public TickSize() {
	}
	
	public TickSize(final int tickerId, final int field, final int size) {
		this.tickerId = tickerId;
		this.field = field;
		this.size = size;
	}
	
	public int getField() {
		return field;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getTickerId() {
		return tickerId;
	}
	
	public void setField(final int field) {
		this.field = field;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
