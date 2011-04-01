package com.ceptrader.tradeapp.ib.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class ScannerDataEnd implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              reqId;
	
	@Deprecated
	public ScannerDataEnd() {
	}
	
	public ScannerDataEnd(final int reqId) {
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
