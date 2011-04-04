
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class AccountDownloadEnd implements DataItem {
	private String	accountName;
	
	@Deprecated
	public AccountDownloadEnd() {}
	
	public AccountDownloadEnd(final String accountName) {
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
	
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
