package com.ceptrader.tradeapp.portfolio;

import java.util.HashMap;

import com.ceptrader.tradeapp.esper.pojoevents.Ask;
import com.ceptrader.tradeapp.esper.pojoevents.Bid;
import com.ceptrader.tradeapp.esper.pojoevents.Fill;
import com.ceptrader.tradeapp.esper.pojoevents.LastPrice;
import com.ceptrader.tradeapp.portfolio.Portfolio.Possition;
import com.ceptrader.tradeapp.util.BasicUtils;

public class PortfolioEventsBroadcast {
	private static HashMap<Long, Portfolio>	  portfolio	   = new HashMap<Long, Portfolio>();
	private static HashMap<String, Portfolio>	portfolio2	= new HashMap<String, Portfolio>();
	
	public static boolean validID(final long ID) {
		return !PortfolioEventsBroadcast.portfolio.containsKey(ID);
	}
	
	public static void reg(final Portfolio p, final long ID, final String name) {
		PortfolioEventsBroadcast.portfolio.put(ID, p);
		PortfolioEventsBroadcast.portfolio2.put(name, p);
	}
	
	public static Portfolio get(final long ID) {
		return PortfolioEventsBroadcast.portfolio.get(ID);
	}
	
	public static Portfolio get(final String name) {
		return PortfolioEventsBroadcast.portfolio2.get(name);
	}
	
	public static void onAsk(final Ask ask) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					p.onAsk(ask);
				}
			});
		}
	}
	
	public static void onBid(final Bid bid) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					p.onBid(bid);
				}
			});
		}
	}
	
	public static void onLast(final LastPrice lp) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					p.onLast(lp);
				}
			});
		}
	}
	
	public static void onFill(final Fill fill, final long ID) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio.get(ID).onFill(fill);
				}
			});
		}
	}
	
	public static void onFill(final Fill fill, final String name) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio2.get(name).onFill(fill);
				}
			});
		}
	}
	
	public static void addCash(final double cash, final long ID) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio.get(ID).addCash(cash);
				}
			});
		}
	}
	
	public static void addCash(final double cash, final String name) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio2.get(name).addCash(cash);
				}
			});
		}
	}
	
	public static void replace(final String ticker, final Possition poss,
	        final long ID) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio.get(ID).replace(ticker,
					        poss);
				}
			});
		}
	}
	
	public static void replace(final String ticker, final Possition poss,
	        final String name) {
		for (final Portfolio p : PortfolioEventsBroadcast.portfolio.values()) {
			BasicUtils.getThreadPool().execute(new Runnable() {
				
				public void run() {
					PortfolioEventsBroadcast.portfolio2.get(name).replace(
					        ticker,
					        poss);
				}
			});
		}
	}
}
