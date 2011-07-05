package com.algoTrader.entity.marketData;

import java.math.BigDecimal;

public class BarImpl extends Bar {

	private static final long serialVersionUID = 1632204263120108428L;

	public BigDecimal getCurrentValue() {
		return getClose();
	}

	public double getCurrentValueDouble() {

		return getCurrentValue().doubleValue();
	}

}
