
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickSize implements DataItem {
	private int	field;
	private int	size;
	private int	tickerId;
	
	@Deprecated
	public TickSize() {}
	
	public TickSize(final int tickerId, final int field, final int size) {
		this.tickerId = tickerId;
		this.field = field;
		this.size = size;
	}
	
	public int getField() {
		return this.field;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getTickerId() {
		return this.tickerId;
	}
	
	public void setField(final int field) {
		this.field = field;
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
