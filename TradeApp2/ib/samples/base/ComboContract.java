/*
 * ComboContract.java
 */
package samples.base;

import com.ib.client.Contract;

public class ComboContract extends Contract {
	
	public ComboContract(final String symbol) {
		m_symbol = symbol;
		m_secType = "BAG";
		m_currency = "USD";
		m_exchange = "SMART";
	}
	
	public ComboContract(final String symbol, final String currency) {
		m_symbol = symbol;
		m_secType = "BAG";
		m_currency = currency;
		m_exchange = "SMART";
	}
	
	public ComboContract(final String symbol, final String currency,
	        final String exchange) {
		m_symbol = symbol;
		m_secType = "BAG";
		m_currency = currency;
		m_exchange = exchange;
	}
}
