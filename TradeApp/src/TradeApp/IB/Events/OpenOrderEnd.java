
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class OpenOrderEnd implements DataItem {
	private static long	count	= 0;
	
	public OpenOrderEnd() {
		OpenOrderEnd.count++;
	}
	
	public long getCount() {
		return OpenOrderEnd.count;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
