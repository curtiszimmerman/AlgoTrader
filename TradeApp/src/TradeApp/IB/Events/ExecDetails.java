
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.Contract;
import com.ib.client.Execution;

public class ExecDetails implements DataItem {
	private Contract	contract;
	private Execution	execution;
	private int			reqId;
	
	@Deprecated
	public ExecDetails() {}
	
	public ExecDetails(final int reqId, final Contract contract,
			final Execution execution) {
		this.reqId = reqId;
		this.contract = contract;
		this.execution = execution;
	}
	
	public Contract getContract() {
		return this.contract;
	}
	
	public Execution getExecution() {
		return this.execution;
	}
	
	public int getReqId() {
		return this.reqId;
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
