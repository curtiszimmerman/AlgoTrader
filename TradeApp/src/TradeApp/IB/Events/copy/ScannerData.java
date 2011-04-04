
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.ContractDetails;

public class ScannerData implements DataItem {
	private String				benchmark;
	private ContractDetails	contractDetails;
	private String				distance;
	private String				legsStr;
	private String				projection;
	private int					rank;
	private int					reqId;
	
	@Deprecated
	public ScannerData() {}
	
	public ScannerData(final int reqId, final int rank,
			final ContractDetails contractDetails, final String distance,
			final String benchmark, final String projection, final String legsStr) {
		this.reqId = reqId;
		this.rank = rank;
		this.contractDetails = contractDetails;
		this.distance = distance;
		this.benchmark = benchmark;
		this.projection = projection;
		this.legsStr = legsStr;
	}
	
	public String getBenchmark() {
		return this.benchmark;
	}
	
	public ContractDetails getContractDetails() {
		return this.contractDetails;
	}
	
	public String getDistance() {
		return this.distance;
	}
	
	public String getLegsStr() {
		return this.legsStr;
	}
	
	public String getProjection() {
		return this.projection;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public int getReqId() {
		return this.reqId;
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
