package com.algoTrader.entity.trade;

import org.apache.commons.lang.ClassUtils;

public class StopOrderImpl extends StopOrder {

	private static final long serialVersionUID = -9213820219309533525L;

	public String toString() {

		return getSide() + " " + getQuantity() + " " + ClassUtils.getShortClassName(this.getClass()) + " " + getSecurity().getSymbol() + " stop " + getStop();
	}
}
