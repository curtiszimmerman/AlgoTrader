package com.ceptrader.tradeapp.ib.esper.pojoevents;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ib.client.Contract;
import com.ib.client.Execution;

public class ExecDetails implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private Contract	      contract;
	private Execution	      execution;
	private int	              reqId;
	
	@Deprecated
	public ExecDetails() {
	}
	
	public ExecDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		this.reqId = reqId;
		this.contract = contract;
		this.execution = execution;
	}
	
	public Contract getContract() {
		return contract;
	}
	
	public Execution getExecution() {
		return execution;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public void setContract(final Contract contract) {
		this.contract = contract;
	}
	
	public void setExecution(final Execution execution) {
		this.execution = execution;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
