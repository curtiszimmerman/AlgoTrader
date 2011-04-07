package com.ceptrader.tradeapp.esper.generic.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class Fill implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private int	              size;
	private double	          price;
	
	@Deprecated
	public Fill() {
	}
	
	public Fill(
	        final int ref,
	        final String ticker,
	        final int size,
	        final double price) {
		this.ref = ref;
		this.ticker = ticker;
		this.size = size;
		this.price = price;
	}
	
	public Fill(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final int size,
	        final double price) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.size = size;
		this.price = price;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public int getSize() {
		return size;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setRef(final int ref) {
		this.ref = ref;
	}
	
	public int getRef() {
		return ref;
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}
}
