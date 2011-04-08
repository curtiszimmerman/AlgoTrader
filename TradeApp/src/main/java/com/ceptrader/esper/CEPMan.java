package com.ceptrader.esper;

import java.util.HashMap;
import java.util.Map;

import com.ceptrader.util.Loggable;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class CEPMan implements Loggable {
	private static HashMap<String, CEPMan>	instances	    = new HashMap<String, CEPMan>();
	
	public static final String	           DEFAULT_INSTANCE	= "DEFAULT_INSTANCE";
	
	synchronized public static CEPMan getCEPMan() {
		return CEPMan.getCEPMan(CEPMan.DEFAULT_INSTANCE);
	}
	
	synchronized public static CEPMan getCEPMan(final String name) {
		if (!CEPMan.instances.containsKey(name)) {
			CEPMan.instances.put(name, new CEPMan(name));
		}
		
		return CEPMan.instances.get(name);
	}
	
	private final EPServiceProvider	epService;
	
	private final String	        name;
	
	private CEPMan(final String name) {
		this.name = name;
		
		// epService lookup to be provided by andy
		epService = EPServiceProviderManager
		        .getDefaultProvider(new Configuration());
	}
	
	public EPServiceProvider getEpService() {
		return epService;
	}
	
	public String getName() {
		return name;
	}
	
	public <T> boolean pumpEvent(final T event) {
		if (epService == null) { throw new IllegalStateException(
		            "CEP Service not started : call start(..)"); }
		
		try {
			epService.getEPRuntime().sendEvent(event);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
	
	public boolean
	        pumpEvent(final Map<String, Object> event, final String name) {
		if (epService == null) { throw new IllegalStateException(
		            "CEP Service not started : call start(..)"); }
		
		try {
			epService.getEPRuntime().sendEvent(event, name);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
}
