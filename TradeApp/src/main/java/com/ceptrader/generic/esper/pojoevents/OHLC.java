package com.ceptrader.generic.esper.pojoevents;


public class OHLC implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private long	          duration;
	private double	          open;
	private double	          high;
	private double	          low;
	private double	          close;
	private double	          WAP;
	private int	              numberOfTrades;
	private boolean	          isClean;
	
	@Deprecated
	public OHLC() {
	}
	
	public OHLC(
	        final int ref,
	        final String ticker,
	        final double open,
	        final double high,
	        final double low,
	        final double close) {
		this.ref = ref;
		this.ticker = ticker;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
	
	public OHLC(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final double open,
	        final double high,
	        final double low,
	        final double close) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
	
	public OHLC(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final long duration,
	        final double open,
	        final double high,
	        final double low,
	        final double close) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.duration = duration;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
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
	
	public void setHigh(final double high) {
		this.high = high;
	}
	
	public double getHigh() {
		return high;
	}
	
	public void setOpen(final double open) {
		this.open = open;
	}
	
	public double getOpen() {
		return open;
	}
	
	public void setLow(final double low) {
		this.low = low;
	}
	
	public double getLow() {
		return low;
	}
	
	public void setClose(final double close) {
		this.close = close;
	}
	
	public double getClose() {
		return close;
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
	
	public void setNumberOfTrades(final int numberOfTrades) {
		this.numberOfTrades = numberOfTrades;
	}
	
	public int getNumberOfTrades() {
		return numberOfTrades;
	}
	
	public void setWAP(final double wAP) {
		WAP = wAP;
	}
	
	public double getWAP() {
		return WAP;
	}
	
	public void setClean(final boolean isClean) {
		this.isClean = isClean;
	}
	
	public boolean isClean() {
		return isClean;
	}
}
