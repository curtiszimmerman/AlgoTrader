package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class ManagedAccounts implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          accountsList;
	
	@Deprecated
	public ManagedAccounts() {
	}
	
	public ManagedAccounts(final String accountsList) {
		this.accountsList = accountsList;
	}
	
	public String getAccountsList() {
		return accountsList;
	}
	
	public void setAccountsList(final String accountsList) {
		this.accountsList = accountsList;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
