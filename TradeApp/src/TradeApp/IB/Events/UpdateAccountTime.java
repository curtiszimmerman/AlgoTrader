
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class UpdateAccountTime implements DataItem {
	private String	timeStamp;
	
	@Deprecated
	public UpdateAccountTime() {}
	
	public UpdateAccountTime(final String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getTimeStamp() {
		return this.timeStamp;
	}
	
	public void setTimeStamp(final String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
