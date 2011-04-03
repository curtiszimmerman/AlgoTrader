package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class NextValidId implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              orderId;
	
	@Deprecated
	public NextValidId() {
	}
	
	public NextValidId(final int orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(final int orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
