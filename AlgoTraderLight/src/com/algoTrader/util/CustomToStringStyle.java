package com.algoTrader.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.hibernate.Hibernate;

import com.algoTrader.BaseObject;

public class CustomToStringStyle extends StandardToStringStyle {
	
	private static final long	       serialVersionUID	= 4268907286858926178L;
	
	private static SimpleDateFormat	   format	        = new SimpleDateFormat(
	                                                            "yyyy-MM-dd kk:mm:ss,SSS");
	
	private static CustomToStringStyle	style;
	
	public static CustomToStringStyle getInstance() {
		
		if (CustomToStringStyle.style == null) {
			CustomToStringStyle.style = new CustomToStringStyle();
			CustomToStringStyle.style.setUseClassName(false);
			CustomToStringStyle.style.setUseIdentityHashCode(false);
		}
		return CustomToStringStyle.style;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void appendDetail(final StringBuffer buffer,
	        final String fieldName, final Collection col) {
		
		buffer.append(col.size());
	}
	
	@Override
	protected void appendDetail(final StringBuffer buffer,
	        final String fieldName, final Object value) {
		
		if (value instanceof BaseObject) {
			return;
		} else if (value instanceof Date) {
			buffer.append(CustomToStringStyle.format.format(value));
		} else {
			super.appendDetail(buffer, fieldName, value);
		}
	}
	
	@Override
	public void append(final StringBuffer buffer, final String fieldName,
	        final Object value, final Boolean fullDetail) {
		
		if (value instanceof BaseObject) {
			return;
		} else if (Hibernate.isInitialized(value)) {
			super.append(buffer, fieldName, value, fullDetail);
		}
	}
}
