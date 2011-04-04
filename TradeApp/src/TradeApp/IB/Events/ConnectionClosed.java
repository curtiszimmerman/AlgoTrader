
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class ConnectionClosed implements DataItem {
	private static long	count	= 0;
	
	public ConnectionClosed() {
		ConnectionClosed.count++;
	}
	
	public long getCount() {
		return ConnectionClosed.count;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
