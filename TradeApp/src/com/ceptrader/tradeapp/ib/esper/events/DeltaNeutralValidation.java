package com.ceptrader.tradeapp.ib.esper.events;


import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ib.client.UnderComp;

public class DeltaNeutralValidation implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              reqId;
	private UnderComp	      underComp;
	
	@Deprecated
	public DeltaNeutralValidation() {
	}
	
	public DeltaNeutralValidation(final int reqId, final UnderComp underComp) {
		this.reqId = reqId;
		this.underComp = underComp;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public UnderComp getUnderComp() {
		return underComp;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	public void setUnderComp(final UnderComp underComp) {
		this.underComp = underComp;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
