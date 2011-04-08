package com.ceptrader.generic.esper.pojoevents;


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
