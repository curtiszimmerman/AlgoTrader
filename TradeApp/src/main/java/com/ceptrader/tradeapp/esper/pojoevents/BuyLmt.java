package com.ceptrader.tradeapp.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;

public class BuyLmt implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              ref;
	private String	          ticker;
	private long	          timeStamp;
	private double	          level;
	private int	              size;
	private String	          goodUntil;
	
	private String	          goodAfter;
	private String	          orderType;
	private String	          group;
	private int	              parent	         = 0;
	private double	          touch	             = 0;
	private double	          trail	             = 0;
	private boolean	          trailPct	         = false;
	
	@Deprecated
	public BuyLmt() {
	}
	
	public BuyLmt(
	        final int ref,
	        final String ticker,
	        final double level,
	        final int size,
	        final String goodUntil) {
		this.ref = ref;
		this.ticker = ticker;
		this.level = level;
		this.size = size;
		this.goodUntil = goodUntil;
	}
	
	public BuyLmt(
	        final int ref,
	        final String ticker,
	        final long timeStamp,
	        final double level,
	        final int size,
	        final String goodUntil) {
		this.ref = ref;
		this.ticker = ticker;
		this.timeStamp = timeStamp;
		this.level = level;
		this.size = size;
		this.goodUntil = goodUntil;
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setLevel(final double level) {
		this.level = level;
	}
	
	public double getLevel() {
		return level;
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
	
	public void setGoodAfter(final String goodAfter) {
		this.goodAfter = goodAfter;
	}
	
	public String getGoodAfter() {
		return goodAfter;
	}
	
	public void setTrail(final double trail) {
		this.trail = trail;
	}
	
	public double getTrail() {
		return trail;
	}
	
	public void setTrailPct(final boolean trailPct) {
		this.trailPct = trailPct;
	}
	
	public boolean isTrailPct() {
		return trailPct;
	}
}
