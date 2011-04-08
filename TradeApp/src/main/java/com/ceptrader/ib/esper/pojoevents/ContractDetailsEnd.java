package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class ContractDetailsEnd implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              reqId;
	
	@Deprecated
	public ContractDetailsEnd() {
	}
	
	public ContractDetailsEnd(final int reqId) {
		this.reqId = reqId;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
