package com.algoTrader.entity.security;

import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.RoundUtil;

public class SecurityFamilyImpl extends SecurityFamily {

	private static final long serialVersionUID = 3793512707002010914L;
	private static final int PORTFOLIO_DIGITS = ConfigurationUtil.getBaseConfig().getInt("portfolioDigits");

	public String toString() {

		return getName();
	}

	@Override
	public int getScale() {

		int digits = RoundUtil.getDigits(getTickSize());
		return Math.max(digits, PORTFOLIO_DIGITS);
	}
}
