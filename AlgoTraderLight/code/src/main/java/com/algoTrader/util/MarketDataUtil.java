package com.algoTrader.util;

import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.security.Forex;
import com.algoTrader.entity.security.Future;
import com.algoTrader.entity.security.StockOption;
import com.espertech.esper.collection.Pair;

public class MarketDataUtil {

	public static boolean isTickValid(Pair<Tick, Object> pair) {

		Tick tick = pair.getFirst();

		if (tick.getSecurity() instanceof StockOption) {

			// stockOptions need to have a bis/ask volume / openIntrest
			// but might not have a last/lastDateTime yet on the current day
			if (tick.getVolBid() == 0) {
				return false;
			}
			if (tick.getVolAsk() == 0) {
				return false;
			}
			if (tick.getOpenIntrest() == 0) {
				return false;
			}
			if (tick.getBid() != null && tick.getBid().doubleValue() <= 0) {
				return false;
			}
			if (tick.getAsk() != null && tick.getAsk().doubleValue() <= 0) {
				return false;
			}
			if (tick.getSettlement() == null) {
				return false;
			}

		} else if (tick.getSecurity() instanceof Future) {

			// futures need to have a bis/ask volume
			// but might not have a last/lastDateTime yet on the current day
			if (tick.getVolBid() == 0) {
				return false;
			}
			if (tick.getVolAsk() == 0) {
				return false;
			}
			if (tick.getBid() != null && tick.getBid().doubleValue() <= 0) {
				return false;
			}
			if (tick.getAsk() != null && tick.getAsk().doubleValue() <= 0) {
				return false;
			}
			if (tick.getSettlement() == null) {
				return false;
			}

		} else if (tick.getSecurity() instanceof Forex) {

			// no special checks
			if (tick.getVolBid() == 0) {
				return false;
			}
			if (tick.getVolAsk() == 0) {
				return false;
			}

		} else {

			if (tick.getLast() == null) {
				return false;
			}
			if (tick.getLastDateTime() == null) {
				return false;
			}
		}

		// check these fields for all security-types
		if (tick.getBid() == null) {
			return false;
		}
		if (tick.getAsk() == null) {
			return false;
		}

		return true;
	}
}
