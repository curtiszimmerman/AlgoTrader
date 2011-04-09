package com.ceptrader.esper;

import java.util.HashMap;
import java.util.Map;

import com.algoTrader.service.RuleServiceImpl;
import com.ceptrader.util.Loggable;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class CEPMan extends RuleServiceImpl implements Loggable {
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
	
	private final String	        name;
	
	private final EPServiceProvider	epService;
	
	private CEPMan(final String name) {
		this.name = name;
		final Configuration configuration = new Configuration();
		configuration.configure("esper-" + name +
		        ".cfg.xml");
		epService = EPServiceProviderManager.getProvider(name, configuration);
	}
	
	public String getName() {
		return name;
	}
	
	public <T> boolean pumpEvent(final T event) {
		
		try {
			epService.getEPRuntime().sendEvent(event);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
	
	public boolean
	        pumpEvent(final Map<String, Object> event, final String name) {
		try {
			epService.getEPRuntime().sendEvent(event, name);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
}
