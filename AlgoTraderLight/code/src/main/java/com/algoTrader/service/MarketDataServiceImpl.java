package com.algoTrader.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.WatchListItem;
import com.algoTrader.entity.WatchListItemImpl;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.entity.marketData.MarketDataEvent;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.security.Security;
import com.algoTrader.esper.io.CsvTickWriter;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.HibernateUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.BarVO;
import com.algoTrader.vo.RawTickVO;
import com.algoTrader.vo.UnsubscribeTickVO;
import com.espertech.esper.collection.Pair;

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

		// lock the security and initialize collections
		HibernateUtil.lock(this.getSessionFactory(), security);
		//Hibernate.initialize(security.getWatchListItems());
		Hibernate.initialize(security.getPositions());

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
			if (!simulation) {
				putOnExternalWatchlist(security);
			}
		}

		this.initialized = true;
	}

	protected void handlePutOnWatchlist(String strategyName, int securityId) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = getSecurityDao().load(securityId);

		if (getWatchListItemDao().findByStrategyAndSecurity(strategyName, securityId) == null) {

			// only put on external watchlist if nobody was watching this security so far
			if (security.getWatchListItems().size() == 0) {
				if (!simulation) {
					putOnExternalWatchlist(security);
				}
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

		WatchListItem watchListItem = getWatchListItemDao().findByStrategyAndSecurity(strategyName, securityId);

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

	public static class PropagateMarketDataEventSubscriber {

		public void update(MarketDataEvent marketDataEvent) {

			ServiceLocator.serverInstance().getMarketDataService().propagateMarketDataEvent(marketDataEvent);
		}
	}

	public static class PersistTickSubscriber {

		@SuppressWarnings("rawtypes")
		public void update(Pair<Tick, Object> insertStream, Map removeStream) {

			// get the current Date rounded to MINUTES
			Date date = DateUtils.round(DateUtil.getCurrentEPTime(), Calendar.MINUTE);

			Tick tick = insertStream.getFirst();
			tick.setDateTime(date);

			ServiceLocator.serverInstance().getMarketDataService().persistTick(tick);
		}
	}
}
