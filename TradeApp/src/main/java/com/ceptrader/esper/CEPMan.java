package com.ceptrader.esper;

import java.util.HashMap;

import com.ceptrader.util.Loggable;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esperio.AdapterCoordinator;

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
	
	private AdapterCoordinator	dataCord;
	private EPServiceProvider	epService;
	
	private final String	   name;
	
	private CEPMan(final String name) {
		this.name = name;
	}
	
	public EPServiceProvider getEpService() {
		return epService;
	}
	
	public String getName() {
		return name;
	}
}
