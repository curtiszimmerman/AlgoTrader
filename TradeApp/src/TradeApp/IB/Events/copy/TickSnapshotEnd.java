
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class TickSnapshotEnd implements DataItem {
	private int	reqId;
	
	@Deprecated
	public TickSnapshotEnd() {}
	
	public TickSnapshotEnd(final int reqId) {
		this.reqId = reqId;
	}
	
	public int getReqId() {
		return this.reqId;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
