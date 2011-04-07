package com.ceptrader.tradeapp.portfolio;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.ceptrader.tradeapp.esper.generic.pojoevents.Ask;
import com.ceptrader.tradeapp.esper.generic.pojoevents.Bid;
import com.ceptrader.tradeapp.esper.generic.pojoevents.Fill;
import com.ceptrader.tradeapp.esper.generic.pojoevents.LastPrice;
import com.ceptrader.tradeapp.util.Logger;

public class Portfolio implements Serializable, Cloneable {
	private static final long	serialVersionUID	= 1L;
	
	public static class SubPortfolio extends Possition {
		private static final long	serialVersionUID	= 1L;
		private final Portfolio		portfolio;
		
		public SubPortfolio(final Portfolio p) {
			super(p.getName());
			portfolio = p;
		}
		
		public Portfolio getPortfolio() {
			return portfolio;
		}
		
		@Override
		public synchronized Object clone() throws CloneNotSupportedException {
			final SubPortfolio sp = (SubPortfolio) super.clone();
			
			return sp;
		}
		
		@Override
		public Ask getLastAsk() {
			return null;
		}
		
		@Override
		public Bid getLastBid() {
			return null;
		}
		
		@Override
		public Fill getLastFilled() {
			return null;
		}
		
		@Override
		public LastPrice getLastTraded() {
			return null;
		}
		
		@Override
		public synchronized double getMarketPricing() {
			return portfolio.exposure();
		}
		
		@Override
		public double getPrice() {
			return portfolio.exposure();
		}
		
		@Override
		public double getSize() {
			return 1;
		}
	}
	
	public static class Cash extends Possition {
		private static final long	serialVersionUID	= 1L;
		public static final String	CASH		     = "CASH";
		
		public Cash() {
			super(Cash.CASH);
		}
		
		@Override
		public Ask getLastAsk() {
			return null;
		}
		
		@Override
		public Bid getLastBid() {
			return null;
		}
		
		@Override
		public Fill getLastFilled() {
			return null;
		}
		
		@Override
		public LastPrice getLastTraded() {
			return null;
		}
		
		@Override
		public synchronized double getMarketPricing() {
			return 1;
		}
		
		@Override
		public double getPrice() {
			return 1;
		}
	}
	
	public static class Possition implements Serializable, Cloneable {
		private static final long	serialVersionUID	= 1L;
		
		private String		      ticker;
		
		public Possition(final String ticker) {
			this.ticker = ticker;
		}
		
		private double		                  size		    = 0;
		private double		                  price		    = 0;
		
		private Bid		                      lastBid;
		private Ask		                      lastAsk;
		private LastPrice		              lastTraded;
		private Fill		                  lastFilled;
		
		private final HashMap<String, String>	otherSymbol	= new HashMap<String, String>();
		
		public void setOtherSymbol(
		        final String exchangeOrPlatform,
		        final String exchangeOrPlatformTicker) {
			synchronized (otherSymbol) {
				otherSymbol.put(exchangeOrPlatform, exchangeOrPlatformTicker);
			}
		}
		
		public String getOtherSymbol(final String exchangeOrPlatform) {
			return otherSymbol.get(exchangeOrPlatform);
		}
		
		public String getTicker() {
			return ticker;
		}
		
		public synchronized void setTicker(final String ticker) {
			this.ticker = ticker;
		}
		
		@Override
		public synchronized Object clone() throws CloneNotSupportedException {
			final Possition p = (Possition) super.clone();
			
			return p;
		}
		
		public synchronized double getMarketPricing() {
			final boolean noAsk = lastAsk == null;
			final boolean noBid = lastBid == null;
			final boolean noTrade = lastTraded == null;
			final boolean noFill = lastFilled == null;
			
			if (noAsk) {
				if (noBid) {
					if (noTrade) {
						if (noFill) {
							return price;
						} else {
							return lastFilled.getPrice();
						}
					} else if (noFill) {
						return price;
					} else {
						return lastFilled.getPrice();
					}
				} else if (noTrade) {
					if (noFill) {
						return lastBid.getPrice();
					} else if (lastBid.getTimeStamp() > lastFilled
					        .getTimeStamp()) {
						return lastBid.getPrice();
					} else {
						return lastFilled.getPrice();
					}
				} else if (lastBid.getTimeStamp() > lastTraded.getTimeStamp()) {
					return lastBid.getPrice();
				} else {
					return lastTraded.getPrice();
				}
			} else if (noBid) {
				if (noTrade) {
					if (noFill) {
						return price;
					} else if (lastAsk.getTimeStamp() > lastAsk.getTimeStamp()) {
						return lastAsk.getPrice();
					} else {
						return lastFilled.getPrice();
					}
				} else if (lastAsk.getTimeStamp() > lastAsk.getTimeStamp()) {
					return lastAsk.getPrice();
				} else {
					return lastTraded.getPrice();
				}
			} else {
				return (lastAsk.getPrice() + lastBid.getPrice()) / 2;
			}
		}
		
		public void setSize(final double size) {
			this.size = size;
		}
		
		public double getSize() {
			return size;
		}
		
		public void setPrice(final double price) {
			this.price = price;
		}
		
		public double getPrice() {
			return price;
		}
		
		public void setLastBid(final Bid lastBid) {
			this.lastBid = lastBid;
		}
		
		public Bid getLastBid() {
			return lastBid;
		}
		
		public void setLastAsk(final Ask lastAsk) {
			this.lastAsk = lastAsk;
		}
		
		public Ask getLastAsk() {
			return lastAsk;
		}
		
		public void setLastTraded(final LastPrice lastTraded) {
			this.lastTraded = lastTraded;
		}
		
		public LastPrice getLastTraded() {
			return lastTraded;
		}
		
		public void setLastFilled(final Fill lastFilled) {
			this.lastFilled = lastFilled;
		}
		
		public Fill getLastFilled() {
			return lastFilled;
		}
	}
	
	private final HashMap<String, Possition>	initPoss	= new HashMap<String, Portfolio.Possition>();
	
	private static int	                     count	     = 0;
	private final long	                     ID;
	private final String	                 name;
	
	public Portfolio(final String name) {
		this.name = name;
		int tmpID = 0;
		do {
			tmpID = Portfolio.count++;
		} while (PortfolioEventsBroadcast.validID(tmpID));
		ID = tmpID;
		PortfolioEventsBroadcast.reg(this, ID, name);
		
		initPoss.put(Cash.CASH, new Cash());
	}
	
	public Portfolio(final String name, final List<Possition> initPoss)
	        throws CloneNotSupportedException {
		this(name);
		
		consolidatePossitions(initPoss);
	}
	
	public void addCash(final double cash) {
		final Possition p = initPoss.get(Cash.CASH);
		p.setSize(p.size + cash);
	}
	
	public void consolidatePossitions(final Portfolio p,
	        final boolean isRecursive)
	        throws CloneNotSupportedException {
		consolidatePossitions(p.initPoss.values(), isRecursive);
	}
	
	public void consolidatePossitions(final Collection<Possition> initPoss,
	        final boolean isRecursive)
	        throws CloneNotSupportedException {
		Portfolio.consolidatePossitions(initPoss, this.initPoss, isRecursive);
	}
	
	public void consolidatePossitions(final Portfolio p)
	        throws CloneNotSupportedException {
		consolidatePossitions(p.initPoss.values());
	}
	
	public void consolidatePossitions(final Collection<Possition> initPoss)
	        throws CloneNotSupportedException {
		Portfolio.consolidatePossitions(initPoss, this.initPoss, true);
	}
	
	public static void consolidatePossitions(
	        final Collection<Possition> initPoss,
	        final Map<String, Possition> intoPoss, final boolean isRecursive)
	        throws CloneNotSupportedException {
		if (initPoss != null) {
			synchronized (intoPoss) {
				for (final Possition i : initPoss) {
					final Portfolio port = i instanceof SubPortfolio ?
					        ((SubPortfolio) i).getPortfolio()
					        : null;
					if (port != null && isRecursive) {
						Portfolio.consolidatePossitions(port.initPoss.values(),
						        intoPoss, true);
						continue;
					}
					
					final Possition p = (Possition) i.clone();
					final String ticker = p.getTicker();
					
					final Possition currPossition = intoPoss.get(ticker);
					
					if (currPossition != null) {
						p.setPrice(p.getPrice() * p.getSize() +
						        currPossition.getPrice() *
						        currPossition.getSize());
						p.setSize(p.getSize() + currPossition.getSize());
						
						p.setPrice(p.getPrice() / p.getSize());
					}
					
					intoPoss.put(ticker, p);
				}
			}
		}
	}
	
	public void onAsk(final Ask ask) {
		Possition p;
		final String ticker = ask.getTicker();
		
		synchronized (initPoss) {
			if (initPoss.containsKey(ticker)) {
				p = initPoss.get(ticker);
			} else {
				p = new Possition(ticker);
			}
			
			p.setLastAsk(ask);
			
			initPoss.put(ticker, p);
		}
	}
	
	public void onBid(final Bid bid) {
		Possition p;
		final String ticker = bid.getTicker();
		
		synchronized (initPoss) {
			if (initPoss.containsKey(ticker)) {
				p = initPoss.get(ticker);
			} else {
				p = new Possition(ticker);
			}
			
			p.setLastBid(bid);
			
			initPoss.put(ticker, p);
		}
	}
	
	public void onLast(final LastPrice lp) {
		Possition p;
		final String ticker = lp.getTicker();
		
		synchronized (initPoss) {
			if (initPoss.containsKey(ticker)) {
				p = initPoss.get(ticker);
			} else {
				p = new Possition(ticker);
			}
			
			p.setLastTraded(lp);
			
			initPoss.put(ticker, p);
		}
	}
	
	public void onFill(final Fill fill) {
		Possition p;
		final String ticker = fill.getTicker();
		
		synchronized (initPoss) {
			if (initPoss.containsKey(ticker)) {
				p = initPoss.get(ticker);
			} else {
				p = new Possition(ticker);
			}
			
			final double cachMovememet = fill.getPrice() * fill.getSize();
			p.setLastFilled(fill);
			p.setPrice(p.getPrice() * p.getSize() + cachMovememet);
			p.setSize(p.getSize() + fill.getSize());
			
			if (p.getSize() == 0) {
				p.setPrice(0);
			} else {
				p.setPrice(p.getPrice() / p.getSize());
			}
			
			addCash(-cachMovememet);
			
			initPoss.put(ticker, p);
		}
	}
	
	public void replace(final String ticker, final Possition p) {
		synchronized (initPoss) {
			initPoss.put(ticker, p);
		}
	}
	
	public double exposure() {
		synchronized (initPoss) {
			double total = 0;
			for (final Map.Entry<String, Possition> i : initPoss.entrySet()) {
				final Possition p = i.getValue();
				total += p.getPrice() * p.getSize();
			}
			
			return total;
		}
	}
	
	public Map<String, Possition> getPossitions()
	        throws CloneNotSupportedException {
		final HashMap<String, Possition> m = new HashMap<String, Possition>();
		synchronized (initPoss) {
			for (final Map.Entry<String, Possition> i : initPoss.entrySet()) {
				m.put(i.getKey(), (Possition) i.getValue().clone());
			}
			
			return m;
		}
	}
	
	public Map<String, Double> getWeights() {
		final HashMap<String, Double> m = new HashMap<String, Double>();
		
		synchronized (initPoss) {
			final double total = exposure();
			
			for (final Map.Entry<String, Possition> i : initPoss.entrySet()) {
				final Possition p = i.getValue();
				m.put(i.getKey(), p.getPrice() * p.getSize() / total);
			}
			
			return m;
		}
	}
	
	public int getTradeSizeForWeightChange(final String ticker,
	        final double weightChange) {
		synchronized (initPoss) {
			final Possition p = initPoss.get(ticker);
			if (p == null) { throw new IllegalStateException(
			        "No possition data for ticker: " + ticker); }
			
			return getTradeSizeForWeightChange(weightChange,
			        p.getMarketPricing());
		}
	}
	
	public Map<String, Integer> getTradeSizeForWeightChange(
	        final Map<String, Double> changes) {
		final HashMap<String, Integer> trades = new HashMap<String, Integer>();
		
		synchronized (initPoss) {
			for (final Map.Entry<String, Double> i : changes.entrySet()) {
				try {
					trades.put(
					        i.getKey(),
					        getTradeSizeForWeightChange(i.getKey(),
					                i.getValue()));
				} catch (final Throwable t) {
					Logger.log(t);
				}
			}
			
			return trades;
		}
	}
	
	public int getTradeSizeForWeightChange(final double weightChange,
	        final double price) {
		return (int) (exposure() * weightChange / price);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		final Portfolio p = (Portfolio) super.clone();
		p.timeStamp = new DateTime(DateTimeZone.UTC).getMillis();
		
		return p;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public long getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	private long	timeStamp;
}
