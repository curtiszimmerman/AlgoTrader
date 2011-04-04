
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class ScannerDataEnd implements DataItem {
	private int	reqId;
	
	@Deprecated
	public ScannerDataEnd() {}
	
	public ScannerDataEnd(final int reqId) {
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
