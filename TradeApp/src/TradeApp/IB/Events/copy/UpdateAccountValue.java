
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class UpdateAccountValue implements DataItem {
	private String	accountName;
	private String	currency;
	private String	key;
	private String	value;
	
	@Deprecated
	public UpdateAccountValue() {}
	
	public UpdateAccountValue(final String key, final String value,
			final String currency, final String accountName) {
		this.key = key;
		this.value = value;
		this.currency = currency;
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}
	
	public void setCurrency(final String currency) {
		this.currency = currency;
	}
	
	public void setKey(final String key) {
		this.key = key;
	}
	
	public void setValue(final String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
