package com.algoTrader.entity.security;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.marketData.Ask;
import com.algoTrader.entity.marketData.Bar;
import com.algoTrader.entity.marketData.Bid;
import com.algoTrader.entity.marketData.Tick;
import com.algoTrader.entity.marketData.Trade;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.StrategyUtil;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.RoundUtil;
import com.espertech.esper.event.bean.BeanEventBean;

public class SecurityImpl extends Security {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	private static final long serialVersionUID = -6631052475125813394L;

	private static Logger logger = MyLogger.getLogger(SecurityImpl.class.getName());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Tick getLastTick() {
	
		List<Map> events = ServiceLocator.commonInstance().getRuleService().getAllEvents(StrategyUtil.getStartedStrategyName(), "GET_LAST_TICK");
	
		// try to see if the rule GET_LAST_TICK has the tick
		for (Map event : events) {
			Integer securityId = (Integer) event.get("securityId");
			if (securityId.equals(getId())) {
				return (Tick) ((BeanEventBean) event.get("tick")).getUnderlying();
			}
		}
	
		// if we did not get the tick up to now go to the db an get the last tick
		Tick tick = ServiceLocator.commonInstance().getLookupService().getLastTick(getId());
		return tick;
	}

	public boolean isOnWatchlist() {
	
		return Hibernate.isInitialized(getWatchListItems()) && (getWatchListItems().size() != 0);
	}

	/**
	 * generic default margin
	 */
	public double getMargin() {

		double value = getCurrentValue().doubleValue();

		double marginPerContract = 0;
		if (value > 0.0) {

			int contractSize = getSecurityFamily().getContractSize();
			marginPerContract = value * contractSize;
		} else {
			logger.warn("CurrentValue too low to set margin on " + getSymbol());
		}
		return marginPerContract;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Bar getLastBar() {
		Bar bar = null;
		
		List<Map> events = ServiceLocator.commonInstance().getRuleService().getAllEvents(StrategyUtil.getStartedStrategyName(), "GET_LAST_BAR");
	
		// try to see if the rule GET_LAST_BAR has the bar
		for (Map event : events) {
			Integer securityId = (Integer) event.get("securityId");
			if (securityId.equals(getId())) {
				bar = (Bar) ((BeanEventBean) event.get("bar")).getUnderlying();
			}
		}
		return (bar);	// TODO - Check database
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Bid getLastBid() {
		Bid bid = null;
		
		List<Map> events = ServiceLocator.commonInstance().getRuleService().getAllEvents(StrategyUtil.getStartedStrategyName(), "GET_LAST_BID");
	
		// try to see if the rule GET_LAST_BID has the bid
		for (Map event : events) {
			Integer securityId = (Integer) event.get("securityId");
			if (securityId.equals(getId())) {
				return (Bid) ((BeanEventBean) event.get("bid")).getUnderlying();
			}
		}
		
		Tick tick = getLastTick();
		if (tick != null) {
			bid = Bid.Factory.newInstance();
			bid.setPrice(tick.getBid());
			bid.setDateTime(tick.getDateTime());
			bid.setSecurity(this);
			return (bid);
		}
		
		Bar bar = getLastBar();
		if (bar != null) {
			bid = Bid.Factory.newInstance();
			bid.setPrice(bar.getClose());
			bid.setDateTime(bar.getDateTime());
			bid.setSecurity(this);
			return (bid);
		}
		
		return (bid);	// TODO - Check database
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Ask getLastAsk() {
		Ask ask = null;
		
		List<Map> events = ServiceLocator.commonInstance().getRuleService().getAllEvents(StrategyUtil.getStartedStrategyName(), "GET_LAST_ASK");
	
		// try to see if the rule GET_LAST_ASK has the ask
		for (Map event : events) {
			Integer securityId = (Integer) event.get("securityId");
			if (securityId.equals(getId())) {
				return (Ask) ((BeanEventBean) event.get("ask")).getUnderlying();
			}
		}
		
		Tick tick = getLastTick();
		if (tick != null) {
			ask = Ask.Factory.newInstance();
			ask.setPrice(tick.getAsk());
			ask.setDateTime(tick.getDateTime());
			ask.setSecurity(this);
			return (ask);
		}
		
		Bar bar = getLastBar();
		if (bar != null) {
			ask = Ask.Factory.newInstance();
			ask.setPrice(bar.getClose());
			ask.setDateTime(bar.getDateTime());
			ask.setSecurity(this);
			return (ask);
		}
		
		return (ask);	// TODO - Check database
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Trade getLastTrade() {
		Trade trade = null;
		
		List<Map> events = ServiceLocator.commonInstance().getRuleService().getAllEvents(StrategyUtil.getStartedStrategyName(), "GET_LAST_TRADE");
	
		// try to see if the rule GET_LAST_TRADE has the trade
		for (Map event : events) {
			Integer securityId = (Integer) event.get("securityId");
			if (securityId.equals(getId())) {
				return (Trade) ((BeanEventBean) event.get("trade")).getUnderlying();
			}
		}
		
		Tick tick = getLastTick();
		if (tick != null) {
			trade = Trade.Factory.newInstance();
			trade.setPrice(tick.getLast());
			trade.setSize(tick.getVol());
			trade.setDateTime(tick.getLastDateTime());
			trade.setSecurity(this);
			return (trade);
		}
		
		Bar bar = getLastBar();
		if (bar != null) {
			trade = Trade.Factory.newInstance();
			trade.setPrice(bar.getClose());
			trade.setDateTime(bar.getDateTime());
			trade.setSecurity(this);
			return (trade);
		}
		
		return (trade);	// TODO - Check database
	}

	public BigDecimal getCurrentValue() {
		
		if (simulation || this.getSecurityFamily().isTradeable()) {
			Bid bid = getLastBid();
			Ask ask = getLastAsk();
			if ((bid != null) && (ask != null))
				return RoundUtil.getBigDecimal((ask.getPrice().doubleValue() + bid.getPrice().doubleValue()) / 2.0);
		}
		return getLastTrade().getPrice();
	}
}
