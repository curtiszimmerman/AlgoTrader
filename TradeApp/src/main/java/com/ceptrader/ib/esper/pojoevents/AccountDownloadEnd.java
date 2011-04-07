package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.esper.generic.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class AccountDownloadEnd implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          accountName;
	
	@Deprecated
	public AccountDownloadEnd() {
	}
	
	public AccountDownloadEnd(final String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
