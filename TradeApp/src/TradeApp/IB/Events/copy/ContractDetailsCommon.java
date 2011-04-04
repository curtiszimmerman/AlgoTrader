
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.ContractDetails;

public class ContractDetailsCommon implements DataItem {
	private ContractDetails	contractDetails;
	private int					reqId;
	
	@Deprecated
	public ContractDetailsCommon() {}
	
	public ContractDetailsCommon(final int reqId,
			final ContractDetails contractDetails) {
		this.reqId = reqId;
		this.contractDetails = contractDetails;
	}
	
	public ContractDetails getContractDetails() {
		return this.contractDetails;
	}
	
	public int getReqId() {
		return this.reqId;
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
