package com.algoTrader.util;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.security.Security;
import com.algoTrader.vo.PortfolioValueVO;

public class LookupUtil {

	public static Security[] getSecuritiesInPortfolio() {

		return ServiceLocator.commonInstance().getLookupService().getAllSecuritiesInPortfolio();
	}

	public static Security[] getSecuritiesOnWatchlist() {

		return ServiceLocator.commonInstance().getLookupService().getSecuritiesOnWatchlist();
	}

	public static Security getSecurityByIsin(String isin) {

		return ServiceLocator.commonInstance().getLookupService().getSecurityByIsin(isin);
	}

	public static Security getSecurityBySymbol(String symbol) {
		return ServiceLocator.commonInstance().getLookupService().getSecurityBySymbol(symbol);
	}

	public static Position[] getPositions(Security security) {

		return security.getPositions().toArray(new Position[0]);
	}

	public static Position[] getOpenPositions() {

		return ServiceLocator.commonInstance().getLookupService().getOpenPositions();
	}

	public static Strategy[] getAllStrategies() {

		return ServiceLocator.commonInstance().getLookupService().getAllStrategies();
	}

	public static boolean hasOpenPositions() {

		return ServiceLocator.commonInstance().getLookupService().getOpenPositions().length != 0;
	}

	public static PortfolioValueVO getPortfolioValue() {

		return ServiceLocator.commonInstance().getLookupService().getPortfolioValue();
	}

	public static boolean hasLastTicks() {

		return ServiceLocator.commonInstance().getRuleService().getLastEvent(StrategyUtil.getStartedStrategyName(), "GET_LAST_TICK") != null;
	}
}
