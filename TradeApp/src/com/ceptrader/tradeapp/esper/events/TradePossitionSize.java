package com.ceptrader.tradeapp.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;

public class TradePossitionSize implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          ticker;
	private long	          timeStamp;
	private int	              size;
	
	public TradePossitionSize(final String ticker, final long timeStamp,
	        final int size) {
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.size = size;
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}
