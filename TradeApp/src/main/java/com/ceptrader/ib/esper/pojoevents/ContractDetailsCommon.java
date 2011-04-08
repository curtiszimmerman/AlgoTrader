package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;
import com.ib.client.ContractDetails;

public class ContractDetailsCommon implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private ContractDetails	  contractDetails;
	private int	              reqId;
	
	@Deprecated
	public ContractDetailsCommon() {
	}
	
	public ContractDetailsCommon(final int reqId,
	        final ContractDetails contractDetails) {
		this.reqId = reqId;
		this.contractDetails = contractDetails;
	}
	
	public ContractDetails getContractDetails() {
		return contractDetails;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public void setContractDetails(final ContractDetails contractDetails) {
		this.contractDetails = contractDetails;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
