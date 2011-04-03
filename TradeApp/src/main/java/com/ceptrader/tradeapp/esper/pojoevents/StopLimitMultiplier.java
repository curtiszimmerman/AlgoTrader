package com.ceptrader.tradeapp.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class StopLimitMultiplier implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          ticker;
	private long	          timeStamp;
	private int	              limitMultiplier;
	private int	              stopMultiplier;
	
	public StopLimitMultiplier(final String ticker, final long timeStamp,
	        final int limitMultiplier) {
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.limitMultiplier = limitMultiplier;
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
	
	public void setLimitMultiplier(final int limitMultiplier) {
		this.limitMultiplier = limitMultiplier;
	}
	
	public int getLimitMultiplier() {
		return limitMultiplier;
	}
	
	public void setStopMultiplier(final int stopMultiplier) {
		this.stopMultiplier = stopMultiplier;
	}
	
	public int getStopMultiplier() {
		return stopMultiplier;
	}
}
