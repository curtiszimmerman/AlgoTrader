/*
 * OptContract.java
 */
package samples.base;

import com.ib.client.Contract;

public class OptContract extends Contract {
	
	public OptContract(final String symbol, final String expiry,
	        final double strike,
	        final String right) {
		m_symbol = symbol;
		m_secType = "OPT";
		m_exchange = "SMART";
		m_currency = "USD";
		m_expiry = expiry;
		m_strike = strike;
		m_right = right;
	}
	
	public OptContract(final String symbol, final String exchange,
	        final String expiry, final double strike, final String right) {
		m_symbol = symbol;
		m_secType = "OPT";
		m_exchange = exchange;
		m_currency = "USD";
		m_expiry = expiry;
		m_strike = strike;
		m_right = right;
	}
	
}
