package com.ceptrader.tradeapp.portfolio;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ConsolidatedPortfolio implements Serializable, Cloneable {
	private static final long	             serialVersionUID	= 1L;
	
	private final HashMap<String, Portfolio>	portColl	  = new HashMap<String, Portfolio>();
	
	private final String	                 name;
	
	public ConsolidatedPortfolio(final String name) {
		this.name = name;
	}
	
	public ConsolidatedPortfolio(final String name,
	        final Map<String, Portfolio> init) {
		this(name);
		portColl.putAll(init);
	}
	
	public void add(final String name, final Portfolio p) {
		portColl.put(name, p);
	}
	
	public void remove(final String name) {
		portColl.remove(name);
	}
	
	public Portfolio consolidate() throws CloneNotSupportedException {
		final Portfolio p = new Portfolio("Consolidate: " + name);
		
		synchronized (portColl) {
			for (final Portfolio i : portColl.values()) {
				p.consolidatePossitions(i);
			}
			
			return p;
		}
	}
	
	public Map<String, Double> subExposure() {
		final HashMap<String, Double> m = new HashMap<String, Double>();
		
		synchronized (portColl) {
			for (final Map.Entry<String, Portfolio> i : portColl.entrySet()) {
				m.put(i.getKey(), i.getValue().exposure());
			}
			
			return m;
		}
	}
	
	public double exposure() {
		double total = 0;
		synchronized (portColl) {
			for (final Double d : subExposure().values()) {
				total += d;
			}
			
			return total;
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		final ConsolidatedPortfolio p = new ConsolidatedPortfolio(name);
		for (final Map.Entry<String, Portfolio> i : portColl.entrySet()) {
			p.portColl.put(i.getKey(), (Portfolio) i.getValue().clone());
		}
		
		p.timeStamp = new DateTime(DateTimeZone.UTC).getMillis();
		return p;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	private long	timeStamp;
}
