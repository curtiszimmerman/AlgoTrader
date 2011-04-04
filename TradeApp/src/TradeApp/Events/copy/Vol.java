
package TradeApp.Events.copy;

import TradeApp.Util.BasicUtils;

public class Vol {
	private double	vol;
	
	@Deprecated
	public Vol() {}
	
	public Vol(final long vol) {
		this.vol = vol;
	}
	
	public double getVol() {
		return this.vol;
	}
	
	public void setVol(final double vol) {
		this.vol = vol;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
