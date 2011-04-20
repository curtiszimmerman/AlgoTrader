package com.algoTrader.entity.marketData;

import java.math.BigDecimal;

import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.RoundUtil;

public class TickImpl extends Tick {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	private static final long serialVersionUID = 7518020445322413106L;

	/**
	 * Note: ticks that are not valid (i.e. low volume) are not fed into esper, so we don't need to check
	 */
	public BigDecimal getCurrentValue() {

		if (simulation) {
			if (!super.getBid().equals(new BigDecimal(0)) & !super.getAsk().equals(new BigDecimal(0))) {
				return RoundUtil.getBigDecimal((getAsk().doubleValue() + getBid().doubleValue()) / 2.0);
			} else {
				return getLast();
			}
		} else {
			if (this.getSecurity().getSecurityFamily().isTradeable()) {
				return RoundUtil.getBigDecimal((getAsk().doubleValue() + getBid().doubleValue()) / 2.0);
			} else {
				return getLast();
			}
		}
	}

	public double getCurrentValueDouble() {

		return getCurrentValue().doubleValue();
	}

	public BigDecimal getSettlement() {

		if (simulation && super.getSettlement().equals(new BigDecimal(0))) {
			return getLast();
		} else {
			return super.getSettlement();
		}
	}
}