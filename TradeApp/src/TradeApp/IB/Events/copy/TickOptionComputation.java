
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickOptionComputation implements DataItem {
	private double	delta;
	private int		field;
	private double	impliedVol;
	private double	optPrice;
	private double	pvDividend;
	private int		tickerId;
	private double	gamma;
	private double	vega;
	private double	theta;
	private double	undPrice;
	
	@Deprecated
	public TickOptionComputation() {}
	
	public TickOptionComputation(final int tickerId, final int field,
			final double impliedVol, final double delta, final double modelPrice,
			final double pvDividend, final double gamma, final double vega,
			final double theta, final double undPrice) {
		this.tickerId = tickerId;
		this.field = field;
		this.impliedVol = impliedVol;
		this.delta = delta;
		this.optPrice = modelPrice;
		this.pvDividend = pvDividend;
		this.gamma = gamma;
		this.vega = vega;
		this.theta = theta;
		this.undPrice = undPrice;
	}
	
	public double getDelta() {
		return this.delta;
	}
	
	public int getField() {
		return this.field;
	}
	
	public double getImpliedVol() {
		return this.impliedVol;
	}
	
	public double getOptPrice() {
		return this.optPrice;
	}
	
	public double getPvDividend() {
		return this.pvDividend;
	}
	
	public int getTickerId() {
		return this.tickerId;
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
		this.optPrice = modelPrice;
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
		return this.gamma;
	}
	
	public void setVega(final double vega) {
		this.vega = vega;
	}
	
	public double getVega() {
		return this.vega;
	}
	
	public void setTheta(final double theta) {
		this.theta = theta;
	}
	
	public double getTheta() {
		return this.theta;
	}
	
	public void setUndPrice(final double undPrice) {
		this.undPrice = undPrice;
	}
	
	public double getUndPrice() {
		return this.undPrice;
	}
}
