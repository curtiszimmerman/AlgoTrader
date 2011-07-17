package com.algoTrader.entity.trade;

public abstract class OrderImpl extends Order {

	private static final long serialVersionUID = -6501807818853981164L;

	public String toString() {
		return String.valueOf(getNumber());
	}
}
