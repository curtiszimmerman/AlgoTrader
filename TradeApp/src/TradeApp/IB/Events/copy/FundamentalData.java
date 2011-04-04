
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class FundamentalData implements DataItem {
	private String	data;
	private int		reqId;
	
	@Deprecated
	public FundamentalData() {}
	
	public FundamentalData(final int reqId, final String data) {
		this.reqId = reqId;
		this.data = data;
	}
	
	public String getData() {
		return this.data;
	}
	
	public int getReqId() {
		return this.reqId;
	}
	
	public void setData(final String data) {
		this.data = data;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
