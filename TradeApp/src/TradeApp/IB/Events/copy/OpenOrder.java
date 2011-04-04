
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

public class OpenOrder implements DataItem {
	private static long	count	= 0;
	private Contract		contract;
	private Order			order;
	private int				orderId;
	
	private OrderState	orderState;
	
	@Deprecated
	public OpenOrder() {}
	
	public OpenOrder(final int orderId, final Contract contract,
			final Order order, final OrderState orderState) {
		this.orderId = orderId;
		this.contract = contract;
		this.order = order;
		this.orderState = orderState;
		
		OpenOrder.count++;
	}
	
	public Contract getContract() {
		return this.contract;
	}
	
	public long getCount() {
		return OpenOrder.count;
	}
	
	public Order getOrder() {
		return this.order;
	}
	
	public int getOrderId() {
		return this.orderId;
	}
	
	public OrderState getOrderState() {
		return this.orderState;
	}
	
	public void setContract(final Contract contract) {
		this.contract = contract;
	}
	
	public void setOrder(final Order order) {
		this.order = order;
	}
	
	public void setOrderId(final int orderId) {
		this.orderId = orderId;
	}
	
	public void setOrderState(final OrderState orderState) {
		this.orderState = orderState;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
