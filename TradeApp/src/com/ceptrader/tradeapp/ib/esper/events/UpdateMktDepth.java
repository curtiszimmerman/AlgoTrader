package com.ceptrader.tradeapp.ib.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class UpdateMktDepth implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              operation;
	private int	              position;
	private double	          price;
	private int	              side;
	private int	              size;
	private int	              tickerId;
	
	@Deprecated
	public UpdateMktDepth() {
	}
	
	public UpdateMktDepth(final int tickerId, final int position,
	        final int operation, final int side, final double price,
	        final int size) {
		this.tickerId = tickerId;
		this.position = position;
		this.operation = operation;
		this.side = side;
		this.price = price;
		this.size = size;
	}
	
	public int getOperation() {
		return operation;
	}
	
	public int getPosition() {
		return position;
	}
	
	public double getPrice() {
		return price;
	}
	
	public int getSide() {
		return side;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getTickerId() {
		return tickerId;
	}
	
	public void setOperation(final int operation) {
		this.operation = operation;
	}
	
	public void setPosition(final int position) {
		this.position = position;
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}
	
	public void setSide(final int side) {
		this.side = side;
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
