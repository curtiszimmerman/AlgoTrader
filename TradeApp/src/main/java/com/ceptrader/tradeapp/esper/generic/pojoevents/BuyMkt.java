package com.ceptrader.tradeapp.esper.generic.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class BuyMkt implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private int	              size;
	private String	          goodUntil;
	
	private String	          goodAfter;
	private String	          orderType;
	private String	          group;
	private int	              parent	         = 0;
	private double	          touch	             = 0;
	
	public BuyMkt(
	        final int ref,
	        final String ticker,
	        final int size,
	        final String goodUntil) {
		this.ref = ref;
		this.ticker = ticker;
		this.size = size;
		this.goodUntil = goodUntil;
	}
	
	public BuyMkt(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final int size,
	        final String goodUntil) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.size = size;
		this.goodUntil = goodUntil;
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setGoodUntil(final String goodUntil) {
		this.goodUntil = goodUntil;
	}
	
	public String getGoodUntil() {
		return goodUntil;
	}
	
	public void setRef(final int ref) {
		this.ref = ref;
	}
	
	public int getRef() {
		return ref;
	}
	
	public void setTimeStamp(final long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setGoodAfter(final String goodAfter) {
		this.goodAfter = goodAfter;
	}
	
	public String getGoodAfter() {
		return goodAfter;
	}
	
	public void setOrderType(final String orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderType() {
		return orderType;
	}
	
	public void setGroup(final String group) {
		this.group = group;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setParent(final int parent) {
		this.parent = parent;
	}
	
	public int getParent() {
		return parent;
	}
	
	public void setTouch(final double touch) {
		this.touch = touch;
	}
	
	public double getTouch() {
		return touch;
	}
}
