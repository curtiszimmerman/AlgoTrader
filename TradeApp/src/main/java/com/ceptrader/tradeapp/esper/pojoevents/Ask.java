package com.ceptrader.tradeapp.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class Ask implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private long	          duration;
	private int	              size;
	private double	          price;
	private boolean	          isBest	         = false;
	
	private boolean	          isMarketDepth	     = false;
	private int	              level	             = -1;
	private final int	      row	             = -1;
	private String	          marketMaker	     = null;
	
	private Operator	      operator	         = Operator.NA;
	
	public static enum Operator {
		INSERT, UPDATE, DELETE, NA
	};
	
	@Deprecated
	public Ask() {
	}
	
	public Ask(
	        final int ref,
	        final String ticker,
	        final int size,
	        final double price) {
		this.ref = ref;
		this.ticker = ticker;
		this.size = size;
		this.price = price;
	}
	
	public Ask(
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
	
	public Ask(
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
	
	public Ask(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final long duration,
	        final int size,
	        final double price,
	        final boolean isBest) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.duration = duration;
		this.size = size;
		this.price = price;
		this.isBest = isBest;
	}
	
	public Ask(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final long duration,
	        final int size,
	        final double price,
	        final boolean isBest,
	        final int level) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.duration = duration;
		this.size = size;
		this.price = price;
		this.isBest = isBest;
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
	
	public void setBest(final boolean isBest) {
		this.isBest = isBest;
	}
	
	public boolean isBest() {
		return isBest;
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
	
	public void setMarketMaker(final String marketMaker) {
		this.marketMaker = marketMaker;
	}
	
	public String getMarketMaker() {
		return marketMaker;
	}
	
	public void setOperator(final Operator operator) {
		this.operator = operator;
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public void setMarketDepth(final boolean isMarketDepth) {
		this.isMarketDepth = isMarketDepth;
	}
	
	public boolean isMarketDepth() {
		return isMarketDepth;
	}
	
	public int getRow() {
		return row;
	}
}
