package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class UpdateAccountValue implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          accountName;
	private String	          currency;
	private String	          key;
	private String	          value;
	
	@Deprecated
	public UpdateAccountValue() {
	}
	
	public UpdateAccountValue(final String key, final String value,
	        final String currency, final String accountName) {
		this.key = key;
		this.value = value;
		this.currency = currency;
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}
	
	public void setCurrency(final String currency) {
		this.currency = currency;
	}
	
	public void setKey(final String key) {
		this.key = key;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
