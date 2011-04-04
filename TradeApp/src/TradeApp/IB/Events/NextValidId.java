
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class NextValidId implements DataItem {
	private int	orderId;
	
	@Deprecated
	public NextValidId() {}
	
	public NextValidId(final int orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderId() {
		return this.orderId;
	}
	
	public void setOrderId(final int orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
