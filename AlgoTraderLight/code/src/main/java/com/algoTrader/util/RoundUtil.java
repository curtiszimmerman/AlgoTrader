package com.algoTrader.util;

import java.math.BigDecimal;

import org.apache.commons.math.util.MathUtils;

import com.algoTrader.enumeration.OptionType;

public class RoundUtil {

	private static final int PORTFOLIO_DIGITS = ConfigurationUtil.getBaseConfig().getInt("portfolioDigits");

	public static double roundToNextN(double value, double n) {

		return MathUtils.round(value / n, 0) * n;
	}

	public static BigDecimal roundToNextN(BigDecimal value, double n) {

		return RoundUtil.getBigDecimal(roundToNextN(value.doubleValue(), n), getDigits(n));
	}

	public static double roundToNextN(double value, double n, int roundingMethod) {

		return MathUtils.round(value / n, 0, roundingMethod) * n;
	}

	public static BigDecimal roundToNextN(BigDecimal value, double n, int roundingMethod) {

		return RoundUtil.getBigDecimal(roundToNextN(value.doubleValue(), n, roundingMethod), getDigits(n));
	}

	public static BigDecimal getBigDecimal(double value) {

		if (!Double.isNaN(value)) {
			BigDecimal decimal = new BigDecimal(value);
			return decimal.setScale(PORTFOLIO_DIGITS, BigDecimal.ROUND_HALF_UP);
		} else {
			return null;
		}
	}

	public static BigDecimal getBigDecimal(double value, int scale) {

		if (!Double.isNaN(value)) {
			BigDecimal decimal = new BigDecimal(value);
			return decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
		} else {
			return null;
		}
	}

	public static BigDecimal roundStockOptionStrikeToNextN(BigDecimal spot, double n, OptionType type) {

		if (OptionType.CALL.equals(type)) {
			// increase by strikeOffset and round to upper n
			return roundToNextN(spot, n, BigDecimal.ROUND_CEILING);
		} else {
			// reduce by strikeOffset and round to lower n
			return roundToNextN(spot, n, BigDecimal.ROUND_FLOOR);
		}
	}

	public static int getDigits(double n) {
		int exponent = -(int) Math.floor(Math.log10(n));
		int digits = exponent >= 0 ? exponent : 0;
		return digits;
	}
}
