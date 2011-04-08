package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class TickOptionComputation implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private double	          delta;
	private int	              field;
	private double	          impliedVol;
	private double	          optPrice;
	private double	          pvDividend;
	private int	              tickerId;
	private double	          gamma;
	private double	          vega;
	private double	          theta;
	private double	          undPrice;
	
	@Deprecated
	public TickOptionComputation() {
	}
	
	public TickOptionComputation(final int tickerId, final int field,
	        final double impliedVol, final double delta,
	        final double modelPrice,
	        final double pvDividend, final double gamma, final double vega,
	        final double theta, final double undPrice) {
		this.tickerId = tickerId;
		this.field = field;
		this.impliedVol = impliedVol;
		this.delta = delta;
		optPrice = modelPrice;
		this.pvDividend = pvDividend;
		this.gamma = gamma;
		this.vega = vega;
		this.theta = theta;
		this.undPrice = undPrice;
	}
	
	public double getDelta() {
		return delta;
	}
	
	public int getField() {
		return field;
	}
	
	public double getImpliedVol() {
		return impliedVol;
	}
	
	public double getOptPrice() {
		return optPrice;
	}
	
	public double getPvDividend() {
		return pvDividend;
	}
	
	public int getTickerId() {
		return tickerId;
	}
	
	public void setDelta(final double delta) {
		this.delta = delta;
	}
	
	public void setField(final int field) {
		this.field = field;
	}
	
	public void setImpliedVol(final double impliedVol) {
		this.impliedVol = impliedVol;
	}
	
	public void setOptPrice(final double modelPrice) {
		optPrice = modelPrice;
	}
	
	public void setPvDividend(final double pvDividend) {
		this.pvDividend = pvDividend;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	public void setGamma(final double gamma) {
		this.gamma = gamma;
	}
	
	public double getGamma() {
		return gamma;
	}
	
	public void setVega(final double vega) {
		this.vega = vega;
	}
	
	public double getVega() {
		return vega;
	}
	
	public void setTheta(final double theta) {
		this.theta = theta;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public void setUndPrice(final double undPrice) {
		this.undPrice = undPrice;
	}
	
	public double getUndPrice() {
		return undPrice;
	}
}
