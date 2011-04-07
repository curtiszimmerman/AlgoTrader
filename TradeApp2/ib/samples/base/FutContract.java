/*
 * FutContract.java
 */
package samples.base;

import com.ib.client.Contract;

public class FutContract extends Contract {
	
	public FutContract(final String symbol, final String expiry) {
		m_symbol = symbol;
		m_secType = "FUT";
		m_exchange = "ONE";
		m_currency = "USD";
		m_expiry = expiry;
	}
	
	public FutContract(final String symbol, final String expiry,
	        final String currency) {
		m_symbol = symbol;
		m_secType = "FUT";
		m_currency = currency;
		m_expiry = expiry;
	}
}
