package com.ceptrader.tradeapp.esper.generic.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class CancelOrder implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	
	public CancelOrder(final int ref) {
		this.ref = ref;
	}
	
	public void setRef(final int ref) {
		this.ref = ref;
	}
	
	public int getRef() {
		return ref;
	}
}
