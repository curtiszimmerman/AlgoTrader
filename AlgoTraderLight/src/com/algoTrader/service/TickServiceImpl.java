package com.algoTrader.service;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.Tick;
import com.algoTrader.entity.WatchListItem;
import com.algoTrader.entity.WatchListItemImpl;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.RawTickVO;

public class TickServiceImpl extends TickServiceBase {

	private static Logger logger = MyLogger.getLogger(TickServiceImpl.class.getName());

	protected Tick handleCompleteRawTick(RawTickVO rawTick) {

		return getTickDao().rawTickVOToEntity(rawTick);
	}

	@SuppressWarnings("unchecked")
	protected void handlePropagateTick(Tick tick) {
	
		logger.debug(tick.getSecurity().getSymbol() + " " + tick);

		getRuleService().sendEvent(StrategyImpl.BASE, tick);
	
		Collection<WatchListItem> watchListItems = tick.getSecurity().getWatchListItems();
		for (WatchListItem watchListItem : watchListItems) {
			getRuleService().sendEvent(watchListItem.getStrategy().getName(), tick);
		}
	}

	protected void handlePutOnWatchlist(String strategyName, int securityId) throws Exception {
	
		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = getSecurityDao().load(securityId);
		putOnWatchlist(strategy, security);
	}

	@SuppressWarnings("unchecked")
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