
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickGeneric implements DataItem {
	private int		tickerId;
	private int		tickType;
	private double	value;
	
	@Deprecated
	public TickGeneric() {}
	
	public TickGeneric(final int tickerId, final int tickType, final double value) {
		this.tickerId = tickerId;
		this.tickType = tickType;
		this.value = value;
	}
	
	public int getTickerId() {
		return this.tickerId;
	}
	
	public int getTickType() {
		return this.tickType;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	public void setTickType(final int tickType) {
		this.tickType = tickType;
	}
	
	public void setValue(final double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
