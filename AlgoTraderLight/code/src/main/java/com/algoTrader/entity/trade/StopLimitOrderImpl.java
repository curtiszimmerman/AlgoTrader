package com.algoTrader.entity.trade;

import org.apache.commons.lang.ClassUtils;

public class StopLimitOrderImpl extends StopLimitOrder {

	private static final long serialVersionUID = -6796363895406178181L;

	public String toString() {

		return getSide() + " " + getQuantity() + " " + ClassUtils.getShortClassName(this.getClass()) + " " + getSecurity().getSymbol() + " stop " + getStop()
				+ " limit " + getLimit();
	}
}
