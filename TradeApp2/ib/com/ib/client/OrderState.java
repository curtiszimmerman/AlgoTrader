/*
 * OrderState.java
 */
package com.ib.client;

public class OrderState {
	
	public String	m_status;
	
	public String	m_initMargin;
	public String	m_maintMargin;
	public String	m_equityWithLoan;
	
	public double	m_commission;
	public double	m_minCommission;
	public double	m_maxCommission;
	public String	m_commissionCurrency;
	
	public String	m_warningText;
	
	OrderState() {
		this(null, null, null, null, 0.0, 0.0, 0.0, null, null);
	}
	
	OrderState(final String status, final String initMargin,
	        final String maintMargin,
	        final String equityWithLoan, final double commission,
	        final double minCommission,
	        final double maxCommission, final String commissionCurrency,
	        final String warningText) {
		
		m_initMargin = initMargin;
		m_maintMargin = maintMargin;
		m_equityWithLoan = equityWithLoan;
		m_commission = commission;
		m_minCommission = minCommission;
		m_maxCommission = maxCommission;
		m_commissionCurrency = commissionCurrency;
		m_warningText = warningText;
	}
	
	@Override
	public boolean equals(final Object other) {
		
		if (this == other) { return true; }
		
		if (other == null) { return false; }
		
		final OrderState state = (OrderState) other;
		
		if (m_commission != state.m_commission ||
		        m_minCommission != state.m_minCommission ||
		        m_maxCommission != state.m_maxCommission) { return false; }
		
		if (Util.StringCompare(m_status, state.m_status) != 0 ||
		        Util.StringCompare(m_initMargin, state.m_initMargin) != 0 ||
		        Util.StringCompare(m_maintMargin, state.m_maintMargin) != 0 ||
		        Util.StringCompare(m_equityWithLoan, state.m_equityWithLoan) != 0 ||
		        Util.StringCompare(m_commissionCurrency,
		                state.m_commissionCurrency) != 0) { return false; }
		
		return true;
	}
}
