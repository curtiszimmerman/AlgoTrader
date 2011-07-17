package com.algoTrader.entity.marketData;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.algoTrader.util.CustomToStringStyle;

public abstract class MarketDataEventImpl extends MarketDataEvent {

	private static final long serialVersionUID = 8758212212560594623L;

	public String toString() {
	
		return ToStringBuilder.reflectionToString(this, CustomToStringStyle.getInstance());
	}
}
