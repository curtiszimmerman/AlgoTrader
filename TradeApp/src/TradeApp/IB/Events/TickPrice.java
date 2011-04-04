
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickPrice implements DataItem {
	private int		canAutoExecute;
	private int		field;
	private double	price;
	private int		tickerId;
	
	@Deprecated
	public TickPrice() {}
	
	public TickPrice(final int tickerId, final int field, final double price,
			final int canAutoExecute) {
		this.tickerId = tickerId;
		this.field = field;
		this.price = price;
		this.canAutoExecute = canAutoExecute;
	}
	
	public int getCanAutoExecute() {
		return this.canAutoExecute;
	}
	
	public int getField() {
		return this.field;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getTickerId() {
		return this.tickerId;
	}
	
	public void setCanAutoExecute(final int canAutoExecute) {
		this.canAutoExecute = canAutoExecute;
	}
	
	public void setField(final int field) {
		this.field = field;
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}
	
	public void setTickerId(final int tickerId) {
		this.tickerId = tickerId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
