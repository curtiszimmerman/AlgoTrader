package com.algoTrader.entity.trade;

import org.apache.commons.lang.ClassUtils;

public class SteppingLimitOrderImpl extends SteppingLimitOrder {

	private static final long serialVersionUID = 6631564632498034454L;

	public String toString() {

		return getSide() + " " + getQuantity() + " " + ClassUtils.getShortClassName(this.getClass()) + " " + getSecurity().getSymbol() + " limit " + getLimit()
				+ " maxLimit " + getMaxLimit() + " increment " + getIncrement();
	}
}