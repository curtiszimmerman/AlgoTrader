package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.esper.generic.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class TickEFP implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private double	          basisPoints;
	private double	          dividendImpact;
	private double	          dividendsToExpiry;
	private String	          formattedBasisPoints;
	private String	          futureExpiry;
	private int	              holdDays;
	private double	          impliedFuture;
	private int	              tickerId;
	private int	              tickType;
	
	@Deprecated
	public TickEFP() {
	}
	
	public TickEFP(final int tickerId, final int tickType,
	        final double basisPoints, final String formattedBasisPoints,
	        final double impliedFuture, final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	        final double dividendsToExpiry) {
		this.tickerId = tickerId;
		this.tickType = tickType;
		this.basisPoints = basisPoints;
		this.formattedBasisPoints = formattedBasisPoints;
		this.impliedFuture = impliedFuture;
		this.holdDays = holdDays;
		this.futureExpiry = futureExpiry;
		this.dividendImpact = dividendImpact;
		this.dividendsToExpiry = dividendsToExpiry;
	}
	
	public double getBasisPoints() {
		return basisPoints;
	}
	
	public double getDividendImpact() {
		return dividendImpact;
	}
	
	public double getDividendsToExpiry() {
		return dividendsToExpiry;
	}
	
	public String getFormattedBasisPoints() {
		return formattedBasisPoints;
	}
	
	public String getFutureExpiry() {
		return futureExpiry;
	}
	
	public int getHoldDays() {
		return holdDays;
	}
	
	public double getImpliedFuture() {
		return impliedFuture;
	}
	
	public int getTickerId() {
		return tickerId;
	}
	
	public int getTickType() {
		return tickType;
	}
	
	public void setBasisPoints(final double basisPoints) {
		this.basisPoints = basisPoints;
	}
	
	public void setDividendImpact(final double dividendImpact) {
		this.dividendImpact = dividendImpact;
	}
	
	public void setDividendsToExpiry(final double dividendsToExpiry) {
		this.dividendsToExpiry = dividendsToExpiry;
	}
	
	public void setFormattedBasisPoints(final String formattedBasisPoints) {
		this.formattedBasisPoints = formattedBasisPoints;
	}
	
	public void setFutureExpiry(final String futureExpiry) {
		this.futureExpiry = futureExpiry;
	}
	
	public void setHoldDays(final int holdDays) {
		this.holdDays = holdDays;
	}
	
	public void setImpliedFuture(final double impliedFuture) {
		this.impliedFuture = impliedFuture;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	public void setTickType(final int tickType) {
		this.tickType = tickType;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
