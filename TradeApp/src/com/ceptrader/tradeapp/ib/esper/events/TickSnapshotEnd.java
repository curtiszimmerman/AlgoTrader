package com.ceptrader.tradeapp.ib.esper.events;

import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;

public class TickSnapshotEnd implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              reqId;
	
	@Deprecated
	public TickSnapshotEnd() {
	}
	
	public TickSnapshotEnd(final int reqId) {
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
