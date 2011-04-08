package com.algoTrader.entity;



public class PositionImpl extends Position {

	private static final long serialVersionUID = -2679980079043322328L;

	public boolean isOpen() {

		return getQuantity() != 0;
	}

	public boolean isLong() {

		return getQuantity() > 0;
	}

	public boolean isShort() {

		return getQuantity() < 0;
	}

	public boolean isFlat() {

		return getQuantity() == 0;
	}

	/**
	 * always positive
	 */
	public double getMarketPriceDouble() {

		if (isOpen()) {

			Tick tick = getSecurity().getLastTick();
			if (tick != null) {
				if (getQuantity() < 0) {

					// short position
					return tick.getAsk().doubleValue();
				} else {

					// short position
					return tick.getBid().doubleValue();
				}
			} else {
				return Double.NaN;
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * short positions: negative long positions: positive
	 */
	public double getMarketValueDouble() {
	
		if (isOpen()) {
	
			return getQuantity() * getSecurity().getSecurityFamily().getContractSize() * getMarketPriceDouble();
		} else {
			return 0.0;
		}
	}

	public double getExitValueDouble() {

		return getExitValue().doubleValue();
	}

	public double getMaintenanceMarginDouble() {

		if (isOpen() && getMaintenanceMargin() != null) {
				return getMaintenanceMargin().doubleValue();
		} else {
			return 0.0;
		}
	}
}