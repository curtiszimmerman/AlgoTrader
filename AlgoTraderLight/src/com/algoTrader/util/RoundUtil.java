package com.algoTrader.util;

import java.math.BigDecimal;

import org.apache.commons.math.util.MathUtils;

public class RoundUtil {
	
	public static BigDecimal roundTo10Cent(final BigDecimal decimal) {
		
		final double rounded = Math.round(decimal.doubleValue() * 10.0) / 10.0;
		return RoundUtil.getBigDecimal(rounded);
	}
	
	public static double roundToNextN(final double value, final double n) {
		
		return MathUtils.round(value / n, 0) * n;
	}
	
	public static BigDecimal
	        roundToNextN(final BigDecimal value, final double n) {
		
		return RoundUtil.getBigDecimal(RoundUtil.roundToNextN(
		        value.doubleValue(), n));
	}
	
	public static double roundToNextN(final double value, final double n,
	        final int roundingMethod) {
		
		return MathUtils.round(value / n, 0, roundingMethod) * n;
	}
	
	public static BigDecimal roundToNextN(final BigDecimal value,
	        final double n, final int roundingMethod) {
		
		return RoundUtil.getBigDecimal(RoundUtil.roundToNextN(
		        value.doubleValue(), n, roundingMethod));
	}
	
	public static BigDecimal getBigDecimal(final double value) {
		
		if (!Double.isNaN(value)) {
			final BigDecimal decimal = new BigDecimal(value);
			return decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		} else {
			return null;
		}
	}
}
