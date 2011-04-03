package com.ceptrader.tradeapp.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class Price implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private long	          duration;
	private int	              size;
	private double	          price;
	private int	              level	             = -1;
	
	@Deprecated
	public Price() {
	}
	
	public Price(
	        final int ref,
	        final String ticker,
	        final int size,
	        final double price) {
		this.ref = ref;
		this.ticker = ticker;
		this.size = size;
		this.price = price;
	}
	
	public Price(
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
	
	public Price(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final long duration,
	        final int size,
	        final double price) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.duration = duration;
		this.size = size;
		this.price = price;
	}
	
	public Price(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final long duration,
	        final int size,
	        final double price,
	        final int level) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.duration = duration;
		this.size = size;
		this.price = price;
		this.level = level;
	}
	
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setDuration(final long duration) {
		this.duration = duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setLevel(final int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setRef(final int ref) {
		this.ref = ref;
	}
	
	public int getRef() {
		return ref;
	}
}
