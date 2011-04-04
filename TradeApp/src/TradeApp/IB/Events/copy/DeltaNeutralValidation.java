
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.UnderComp;

public class DeltaNeutralValidation implements DataItem {
	private int			reqId;
	private UnderComp	underComp;
	
	@Deprecated
	public DeltaNeutralValidation() {}
	
	public DeltaNeutralValidation(final int reqId, final UnderComp underComp) {
		this.reqId = reqId;
		this.underComp = underComp;
	}
	
	public int getReqId() {
		return this.reqId;
	}
	
	public UnderComp getUnderComp() {
		return this.underComp;
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
