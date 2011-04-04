
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class ManagedAccounts implements DataItem {
	private String	accountsList;
	
	@Deprecated
	public ManagedAccounts() {}
	
	public ManagedAccounts(final String accountsList) {
		this.accountsList = accountsList;
	}
	
	public String getAccountsList() {
		return this.accountsList;
	}
	
	public void setAccountsList(final String accountsList) {
		this.accountsList = accountsList;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
