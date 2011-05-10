package com.algoTrader.service;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.WatchListItem;
import com.algoTrader.entity.WatchListItemImpl;
import com.algoTrader.entity.marketData.MarketDataEvent;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.RawTickVO;

public abstract class MarketDataServiceImpl extends MarketDataServiceBase {

	private static Logger logger = MyLogger.getLogger(MarketDataServiceImpl.class.getName());

	protected Tick handleCompleteRawTick(RawTickVO rawTick) {

		return getTickDao().rawTickVOToEntity(rawTick);
	}

	protected void handlePropagateMarketDataEvent(MarketDataEvent marketDataEvent) {
	
		logger.debug(marketDataEvent.getSecurity().getSymbol() + " " + marketDataEvent);

		getRuleService().sendEvent(StrategyImpl.BASE, marketDataEvent);
	
		Collection<WatchListItem> watchListItems = marketDataEvent.getSecurity().getWatchListItems();
		for (WatchListItem watchListItem : watchListItems) {
			getRuleService().sendEvent(watchListItem.getStrategy().getName(), marketDataEvent);
		}
	}

	protected void handlePutOnWatchlist(String strategyName, int securityId) throws Exception {
	
		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = getSecurityDao().load(securityId);
		putOnWatchlist(strategy, security);
	}

	protected void handlePutOnWatchlist(Strategy strategy, Security security) throws Exception {
	
		if (getWatchListItemDao().findByStrategyAndSecurity(strategy, security) == null) {

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
	
			logger.info("removed security from watchlist " + security.getSymbol());
		}
	}
}