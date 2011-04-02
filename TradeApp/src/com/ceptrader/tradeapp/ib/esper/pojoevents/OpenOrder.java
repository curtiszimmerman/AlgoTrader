package com.ceptrader.tradeapp.ib.esper.pojoevents;


import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

public class OpenOrder implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private static long	      count	             = 0;
	private Contract	      contract;
	private Order	          order;
	private int	              orderId;
	
	private OrderState	      orderState;
	
	@Deprecated
	public OpenOrder() {
	}
	
	public OpenOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		this.orderId = orderId;
		this.contract = contract;
		this.order = order;
		this.orderState = orderState;
		
		OpenOrder.count++;
	}
	
	public Contract getContract() {
		return contract;
	}
	
	public long getCount() {
		return OpenOrder.count;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public OrderState getOrderState() {
		return orderState;
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
