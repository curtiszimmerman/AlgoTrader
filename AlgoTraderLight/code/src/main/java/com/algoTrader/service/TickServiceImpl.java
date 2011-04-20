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
	
	private static Logger	logger	= MyLogger.getLogger(TickServiceImpl.class
	                                       .getName());
	
	@Override
	protected Tick handleCompleteRawTick(final RawTickVO rawTick) {
		
		return getTickDao().rawTickVOToEntity(rawTick);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void handlePropagateTick(final Tick tick) {
		
		TickServiceImpl.logger.debug(tick.getSecurity().getSymbol() + " " +
		        tick);
		
		getRuleService().sendEvent(StrategyImpl.BASE, tick);
		
		final Collection<WatchListItem> watchListItems = tick.getSecurity()
		        .getWatchListItems();
		for (final WatchListItem watchListItem : watchListItems) {
			getRuleService().sendEvent(watchListItem.getStrategy().getName(),
			        tick);
		}
	}
	
	@Override
	protected void handlePutOnWatchlist(final String strategyName,
	        final int securityId) throws Exception {
		
		final Strategy strategy = getStrategyDao().findByName(strategyName);
		final Security security = getSecurityDao().load(securityId);
		putOnWatchlist(strategy, security);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void handlePutOnWatchlist(final Strategy strategy,
	        final Security security) throws Exception {
		
		if (getWatchListItemDao().findByStrategyAndSecurity(strategy, security) == null) {
			
			// update links
			final WatchListItem watchListItem = new WatchListItemImpl();
			watchListItem.setSecurity(security);
			watchListItem.setStrategy(strategy);
			watchListItem.setPersistent(false);
			getWatchListItemDao().create(watchListItem);
			
			security.getWatchListItems().add(watchListItem);
			getSecurityDao().update(security);
			
			strategy.getWatchListItems().add(watchListItem);
			getStrategyDao().update(strategy);
			
			TickServiceImpl.logger.info("put security on watchlist " +
			        security.getSymbol());
		}
	}
	
	@Override
	protected void handleRemoveFromWatchlist(final String strategyName,
	        final int securityId) throws Exception {
		
		final Strategy strategy = getStrategyDao().findByName(strategyName);
		final Security security = getSecurityDao().load(securityId);
		
		removeFromWatchlist(strategy, security);
	}
	
	@Override
	protected void handleRemoveFromWatchlist(final Strategy strategy,
	        final Security security) throws Exception {
		
		final WatchListItem watchListItem = getWatchListItemDao()
		        .findByStrategyAndSecurity(strategy, security);
		
		if (watchListItem != null && !watchListItem.isPersistent()) {
			
			// update links
			security.getWatchListItems().remove(watchListItem);
			getSecurityDao().update(security);
			
			strategy.getWatchListItems().remove(watchListItem);
			getStrategyDao().update(strategy);
			
			getWatchListItemDao().remove(watchListItem);
			
			TickServiceImpl.logger.info("removed security from watchlist " +
			        security.getSymbol());
		}
	}
}