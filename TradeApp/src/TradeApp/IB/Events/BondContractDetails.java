package TradeApp.IB.Events;

import TradeApp.Util.BasicUtils;

import com.ib.client.ContractDetails;

public class BondContractDetails extends ContractDetailsCommon {
	@Deprecated
	public BondContractDetails() {}
	
	public BondContractDetails(final int reqId,
			final ContractDetails contractDetails) {
		super(reqId, contractDetails);
	}

	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
