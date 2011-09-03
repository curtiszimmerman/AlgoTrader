package com.algoTrader.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.LockOptions;
import org.hibernate.Session;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.WatchListItem;
import com.algoTrader.entity.WatchListItemImpl;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.entity.marketData.MarketDataEvent;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.security.Security;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.io.CsvTickWriter;
import com.algoTrader.vo.BarVO;
import com.algoTrader.vo.RawTickVO;
import com.algoTrader.vo.SubscribeTickVO;
import com.algoTrader.vo.UnsubscribeTickVO;

public abstract class MarketDataServiceImpl extends MarketDataServiceBase {

	private static Logger logger = MyLogger.getLogger(MarketDataServiceImpl.class.getName());
	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	private Map<Security, CsvTickWriter> csvWriters = new HashMap<Security, CsvTickWriter>();
	private boolean initialized;

	protected Tick handleCompleteRawTick(RawTickVO rawTick) {

		return getTickDao().rawTickVOToEntity(rawTick);
	}

	protected Bar handleCompleteBar(BarVO barVO) {

		return getBarDao().barVOToEntity(barVO);
	}

	protected void handlePropagateMarketDataEvent(MarketDataEvent marketDataEvent) {
	
		// marketDataEvent.toString is expensive, so only log if debug is anabled
		if (!logger.getParent().getLevel().isGreaterOrEqual(Level.DEBUG)) {
			logger.trace(marketDataEvent.getSecurity().getSymbol() + " " + marketDataEvent);
		}
	
		Security security = marketDataEvent.getSecurity();

		// lock and initialize the security
		Session session = this.getSessionFactory().getCurrentSession();
		session.buildLockRequest(LockOptions.NONE).lock(security);
		//Hibernate.initialize(watchListItems);

		for (WatchListItem watchListItem : security.getWatchListItems()) {
			getRuleService().sendEvent(watchListItem.getStrategy().getName(), marketDataEvent);
		}
	}


	protected void handlePersistTick(Tick tick) throws IOException {
	
		Security security = tick.getSecurity();
	
		// retrieve ticks only between marketOpen & close
		if (DateUtil.compareToTime(security.getSecurityFamily().getMarketOpen()) >= 0
				&& DateUtil.compareToTime(security.getSecurityFamily().getMarketClose()) <= 0) {
	
			// write the tick to file
			CsvTickWriter csvWriter = this.csvWriters.get(security);
			if (csvWriter == null) {
				csvWriter = new CsvTickWriter(security.getIsin());
				this.csvWriters.put(security, csvWriter);
			}
			csvWriter.write(tick);
	
			// write the tick to the DB (even if not valid)
			getTickDao().create(tick);
		}
	}

	protected void handleReinitWatchlist() {

		if (this.initialized) {
			initWatchlist();
		}
	}

	protected void handleInitWatchlist() {
	
		List<Security> securities = getSecurityDao().findSecuritiesOnActiveWatchlist();

		for (Security security : securities) {
			putOnWatchlist(security);
		}

		this.initialized = true;
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
				putOnWatchlist(security);
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

	private void putOnWatchlist(Security security) {

		if (!simulation) {

			int tickerId = putOnExternalWatchlist(security);

			Tick tick = Tick.Factory.newInstance();
			tick.setSecurity(security);

			SubscribeTickVO subscribeTickEvent = new SubscribeTickVO();
			subscribeTickEvent.setTick(tick);
			subscribeTickEvent.setTickerId(tickerId);

			getRuleService().sendEvent(StrategyImpl.BASE, subscribeTickEvent);
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

			UnsubscribeTickVO unsubscribeTickEvent = new UnsubscribeTickVO();
			unsubscribeTickEvent.setSecurityId(security.getId());
			getRuleService().sendEvent(StrategyImpl.BASE, unsubscribeTickEvent);
			
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

	public static class PersistTickSubscriber {

		public void update(Tick tick) {

			ServiceLocator.serverInstance().getMarketDataService().persistTick(tick);
		}
	}
}
