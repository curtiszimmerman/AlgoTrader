
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class CurrentTime implements DataItem {
	private long	time;
	
	@Deprecated
	public CurrentTime() {}
	
	public CurrentTime(final long time) {
		this.time = time;
	}
	
	public long getTime() {
		return this.time;
	}
	
	public void setTime(final long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
