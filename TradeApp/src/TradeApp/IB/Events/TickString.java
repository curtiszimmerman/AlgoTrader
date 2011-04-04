
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickString implements DataItem {
	private int		tickerId;
	private int		tickType;
	private String	value;
	
	@Deprecated
	public TickString() {}
	
	public TickString(final int tickerId, final int tickType, final String value) {
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
	
	public String getValue() {
		return this.value;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	public void setTickType(final int tickType) {
		this.tickType = tickType;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
