package com.algoTrader.entity.trade;

import org.apache.commons.lang.ClassUtils;

public class TrailingStopOrderImpl extends TrailingStopOrder {

	private static final long serialVersionUID = -7260306708056150268L;

	public String toString() {

		return getSide() + " " + getQuantity() + " " + ClassUtils.getShortClassName(this.getClass()) + " " + getSecurity().getSymbol() + " trailingAmount "
				+ getTrailingAmount();
	}
}
