package com.ceptrader.tradeapp.ib.esper.pojoevents;


import com.ceptrader.tradeapp.datastream.DataItem;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ib.client.ContractDetails;

public class ScannerData implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          benchmark;
	private ContractDetails	  contractDetails;
	private String	          distance;
	private String	          legsStr;
	private String	          projection;
	private int	              rank;
	private int	              reqId;
	
	@Deprecated
	public ScannerData() {
	}
	
	public ScannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails, final String distance,
	        final String benchmark, final String projection,
	        final String legsStr) {
		this.reqId = reqId;
		this.rank = rank;
		this.contractDetails = contractDetails;
		this.distance = distance;
		this.benchmark = benchmark;
		this.projection = projection;
		this.legsStr = legsStr;
	}
	
	public String getBenchmark() {
		return benchmark;
	}
	
	public ContractDetails getContractDetails() {
		return contractDetails;
	}
	
	public String getDistance() {
		return distance;
	}
	
	public String getLegsStr() {
		return legsStr;
	}
	
	public String getProjection() {
		return projection;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public void setBenchmark(final String benchmark) {
		this.benchmark = benchmark;
	}
	
	public void setContractDetails(final ContractDetails contractDetails) {
		this.contractDetails = contractDetails;
	}
	
	public void setDistance(final String distance) {
		this.distance = distance;
	}
	
	public void setLegsStr(final String legsStr) {
		this.legsStr = legsStr;
	}
	
	public void setProjection(final String projection) {
		this.projection = projection;
	}
	
	public void setRank(final int rank) {
		this.rank = rank;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
