package com.algoTrader.service;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.WatchListItem;
import com.algoTrader.entity.WatchListItemImpl;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.entity.marketData.MarketDataEvent;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.security.Security;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.BarVO;
import com.algoTrader.vo.RawTickVO;

public abstract class MarketDataServiceImpl extends MarketDataServiceBase {

	private static Logger logger = MyLogger.getLogger(MarketDataServiceImpl.class.getName());

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	protected Tick handleCompleteRawTick(RawTickVO rawTick) {

		return getTickDao().rawTickVOToEntity(rawTick);
	}

	protected Bar handleCompleteBar(BarVO barVO) {

		return getBarDao().barVOToEntity(barVO);
	}

	protected void handlePropagateMarketDataEvent(MarketDataEvent marketDataEvent) {
	
		// marketDataEvent.toString is expensive, so only log if debug is anabled
		if (!logger.getParent().getLevel().isGreaterOrEqual(Level.INFO)) {
			logger.debug(marketDataEvent.getSecurity().getSymbol() + " " + marketDataEvent);
		}
	
		Collection<WatchListItem> watchListItems = marketDataEvent.getSecurity().getWatchListItems();
		for (WatchListItem watchListItem : watchListItems) {
			getRuleService().sendEvent(watchListItem.getStrategy().getName(), marketDataEvent);
		}
	}

	protected void handleInitWatchlist() {
	
		if (!simulation) {

			List<Security> securities = getSecurityDao().findSecuritiesOnWatchlist();
			for (Security security : securities) {
				putOnExternalWatchlist(security);
			}
		}
	}

	protected void handlePutOnWatchlist(String strategyName, int securityId) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = getSecurityDao().load(securityId);
		putOnWatchlist(strategy, security);
	}

	protected void handlePutOnWatchlist(Strategy strategy, Security security) throws Exception {

		if (getWatchListItemDao().findByStrategyAndSecurity(strategy, security) == null) {

			// only put on external watchlist if nobody was watching this security so far
			if (security.getWatchListItems().size() == 0) {
				putOnExternalWatchlist(security);
			}

			// update links
			WatchListItem watchListItem = new WatchListItemImpl();
			watchListItem.setSecurity(security);
			watchListItem.setStrategy(strategy);
			watchListItem.setPersistent(false);
			getWatchListItemDao().create(watchListItem);

			security.getWatchListItems().add(watchListItem);
			getSecurityDao().update(security);

			strategy.getWatchListItems().add(watchListItem);
			getStrategyDao().update(strategy);

			logger.info("put security on watchlist " + security.getSymbol());
		}
	}

	protected void handleRemoveFromWatchlist(String strategyName, int securityId) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = getSecurityDao().load(securityId);

		removeFromWatchlist(strategy, security);
	}

	protected void handleRemoveFromWatchlist(Strategy strategy, Security security) throws Exception {

		WatchListItem watchListItem = getWatchListItemDao().findByStrategyAndSecurity(strategy, security);

		if (watchListItem != null && !watchListItem.isPersistent()) {

			// update links
			security.getWatchListItems().remove(watchListItem);
			getSecurityDao().update(security);

			strategy.getWatchListItems().remove(watchListItem);
			getStrategyDao().update(strategy);

			getWatchListItemDao().remove(watchListItem);

			// only remove from external watchlist if nobody is watching this security anymore
			if (security.getWatchListItems().size() == 0) {
				removeFromExternalWatchlist(security);
			}

			logger.info("removed security from watchlist " + security.getSymbol());
		}
	}

	public static class PropagateTickSubscriber {

		public void update(MarketDataEvent marketDataEvent) {

			ServiceLocator.serverInstance().getMarketDataService().propagateMarketDataEvent(marketDataEvent);
		}
	}
}
