package com.algoTrader.entity.trade;

import org.apache.commons.lang.ClassUtils;

public abstract class OrderImpl extends Order {

	private static final long serialVersionUID = -6501807818853981164L;

	public String toString() {

		return getSide() + " " + getQuantity() + " " + ClassUtils.getShortClassName(this.getClass()) + " " + getSecurity().getSymbol();
	}
}
