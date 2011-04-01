package com.ceptrader.tradeapp.ib.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class OrderStatus implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private double	          avgFillPrice;
	private int	              clientId;
	private int	              filled;
	private double	          lastFillPrice;
	private int	              orderId;
	private int	              parentId;
	private int	              permId;	                  ;
	private int	              remaining;
	private String	          status;
	private String	          whyHeld;
	
	@Deprecated
	public OrderStatus() {
	}
	
	public OrderStatus(final int orderId, final String status,
	        final int filled,
	        final int remaining, final double avgFillPrice, final int permId,
	        final int parentId, final double lastFillPrice, final int clientId,
	        final String whyHeld) {
		this.orderId = orderId;
		this.status = status;
		this.filled = filled;
		this.remaining = remaining;
		this.avgFillPrice = avgFillPrice;
		this.permId = permId;
		this.parentId = parentId;
		this.lastFillPrice = lastFillPrice;
		this.clientId = clientId;
		this.whyHeld = whyHeld;
	}
	
	public double getAvgFillPrice() {
		return avgFillPrice;
	}
	
	public int getClientId() {
		return clientId;
	}
	
	public int getFilled() {
		return filled;
	}
	
	public double getLastFillPrice() {
		return lastFillPrice;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public int getPermId() {
		return permId;
	}
	
	public int getRemaining() {
		return remaining;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getWhyHeld() {
		return whyHeld;
	}
	
	public void setAvgFillPrice(final double avgFillPrice) {
		this.avgFillPrice = avgFillPrice;
	}
	
	public void setClientId(final int clientId) {
		this.clientId = clientId;
	}
	
	public void setFilled(final int filled) {
		this.filled = filled;
	}
	
	public void setLastFillPrice(final double lastFillPrice) {
		this.lastFillPrice = lastFillPrice;
	}
	
	public void setOrderId(final int orderId) {
		this.orderId = orderId;
	}
	
	public void setParentId(final int parentId) {
		this.parentId = parentId;
	}
	
	public void setPermId(final int permId) {
		this.permId = permId;
	}
	
	public void setRemaining(final int remaining) {
		this.remaining = remaining;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
	public void setWhyHeld(final String whyHeld) {
		this.whyHeld = whyHeld;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
