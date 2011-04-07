package com.algoTrader;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.algoTrader.util.CustomToStringStyle;

public class BaseObject {
	
	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this,
		        CustomToStringStyle.getInstance());
	}
}