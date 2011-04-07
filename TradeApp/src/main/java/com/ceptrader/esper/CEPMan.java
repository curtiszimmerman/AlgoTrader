package com.ceptrader.esper;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.ceptrader.util.Loggable;
import com.ceptrader.util.Logger;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.ConfigurationEventTypeXMLDOM;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.deploy.DeploymentOptions;
import com.espertech.esper.client.deploy.DeploymentResult;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.deploy.Module;
import com.espertech.esperio.AdapterCoordinator;
import com.espertech.esperio.AdapterCoordinatorImpl;
import com.espertech.esperio.AdapterInputSource;
import com.espertech.esperio.csv.CSVInputAdapter;
import com.espertech.esperio.csv.CSVInputAdapterSpec;

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
	
	public synchronized void addCSVDataSource(
	        final Map<String, List<String>> eventNameToSource,
	        final Map<String, String> timeStampCol,
	        final Map<String, Integer> eventsPerSecond) {
		try {
			for (final String key : eventNameToSource.keySet()) {
				for (final String s : eventNameToSource.get(key)) {
					addCSVDataSource(timeStampCol, eventsPerSecond, key, s);
				}
			}
		} catch (final Throwable t) {
			throw new RuntimeException("Exception in registering data sorces",
			        t);
		}
	}
	
	public synchronized void addACSVDataSource(
	        final Map<String, String> eventNameToSource,
	        final Map<String, String> timeStampCol,
	        final Map<String, Integer> eventsPerSecond) {
		try {
			for (final String key : eventNameToSource.keySet()) {
				final String s = eventNameToSource.get(key);
				addCSVDataSource(timeStampCol, eventsPerSecond, key, s);
			}
		} catch (final Throwable t) {
			throw new RuntimeException("Exception in registering data sorces",
			        t);
		}
	}
	
	private void addCSVDataSource(final Map<String, String> timeStampCol,
	        final Map<String, Integer> eventsPerSecond, final String key,
	        final String s) throws MalformedURLException {
		final AdapterInputSource source = new AdapterInputSource(
		        new URL(s));
		final CSVInputAdapterSpec input = new CSVInputAdapterSpec(
		        source, key);
		
		addSingleCSVDataSource(key, input, timeStampCol, eventsPerSecond);
	}
	
	public synchronized <T> void addCSVGenericDataSource(
	        final Map<String, List<T>> eventNameToSource,
	        final Map<String, String> timeStampCol,
	        final Map<String, Integer> eventsPerSecond) {
		try {
			for (final String key : eventNameToSource.keySet()) {
				for (final T obj : eventNameToSource.get(key)) {
					AdapterInputSource source = null;
					
					final Class<?> sourceClassObj = obj.getClass();
					
					if (sourceClassObj.isAssignableFrom(URL.class)) {
						source = new AdapterInputSource(
						        (URL) obj);
					} else if (sourceClassObj.isAssignableFrom(File.class)) {
						source = new AdapterInputSource(
						        (File) obj);
					} else if (sourceClassObj
					        .isAssignableFrom(InputStream.class)) {
						source = new AdapterInputSource(
						        (InputStream) obj);
					} else if (sourceClassObj.isAssignableFrom(Reader.class)) {
						source = new AdapterInputSource(
						        (Reader) obj);
					} else if (sourceClassObj.isAssignableFrom(String.class)) {
						source = new AdapterInputSource(
						        (String) obj);
					} else {
						new IllegalArgumentException(
						        "HasSource can only be: File, InputStream, Reader, String, URL. Current type: "
						                +
						                sourceClassObj.getName());
					}
					
					final CSVInputAdapterSpec input = new CSVInputAdapterSpec(
					        source, key);
					
					addSingleCSVDataSource(key, input, timeStampCol,
					        eventsPerSecond);
				}
			}
		} catch (final Throwable t) {
			throw new RuntimeException("Exception in registering data sorces",
			        t);
		}
	}
	
	public DeploymentResult deploy(final String url) {
		try {
			final EPDeploymentAdmin deployAdmin = epService
			        .getEPAdministrator()
			        .getDeploymentAdmin();
			final Module module = deployAdmin
			        .read(url);
			
			final DeploymentOptions options = new DeploymentOptions();
			options.setFailFast(false);
			options.setCompile(true);
			
			final DeploymentResult dr = deployAdmin.deploy(module, options);
			return dr;
		} catch (final Throwable t) {
			throw new IllegalStateException("Deployment error", t);
		}
	}
	
	public DeploymentResult deploy(final URL url) {
		try {
			final EPDeploymentAdmin deployAdmin = epService
			        .getEPAdministrator()
			        .getDeploymentAdmin();
			final Module module = deployAdmin
			        .read(url);
			
			final DeploymentOptions options = new DeploymentOptions();
			options.setFailFast(false);
			options.setCompile(true);
			
			final DeploymentResult dr = deployAdmin.deploy(module, options);
			return dr;
		} catch (final Throwable t) {
			throw new IllegalStateException("Deployment error", t);
		}
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
	
	public synchronized EPStatement[] registerStatementListner(
	        final String[] stmtSupplier,
	        final StatementAwareUpdateListener[] listnerList) {
		if (epService == null) { throw new IllegalStateException(
		            "CEP Service not started : call start(..)"); }
		
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final StatementAwareUpdateListener ul : listnerList) {
				stm[i].addListener(ul);
			}
			
			i++;
		}
		return stm;
	}
	
	public synchronized EPStatement[] registerStatementListner(
	        final String[] stmtSupplier, final UpdateListener[] listnerList) {
		if (epService == null) { throw new IllegalStateException(
		            "CEP Service not started : call start(..)"); }
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final UpdateListener ul : listnerList) {
				stm[i].addListener(ul);
			}
			
			i++;
		}
		return stm;
	}
	
	public synchronized EPStatement[] registerStatementListnerWithReplay(
	        final String[] stmtSupplier, final UpdateListener[] listnerList) {
		if (epService == null) { throw new IllegalStateException(
		            "CEP Service not started : call start(..)"); }
		
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final UpdateListener ul : listnerList) {
				stm[i].addListenerWithReplay(ul);
			}
			
			i++;
		}
		return stm;
	}
	
	public synchronized void resetCoordinater() {
		if (dataCord != null) {
			dataCord.destroy();
		}
		dataCord = new AdapterCoordinatorImpl(epService, true);
	}
	
	public synchronized void shutdown() {
		if (dataCord != null) {
			dataCord.destroy();
		}
		if (epService != null) {
			epService.destroy();
		}
	}
	
	private boolean	isStarted	= false;
	
	public CEPMan start() {
		return start(new Configuration());
	}
	
	public CEPMan start(final Configuration config) {
		if (epService == null) {
			epService = EPServiceProviderManager
			        .getDefaultProvider(config);
			isStarted = true;
		} else {
			throw new IllegalStateException("CEP Provider Already Started");
		}
		
		resetCoordinater();
		
		return this;
	}
	
	public static Configuration addAutoNames(final Package[] eventAutoNames,
	         Configuration config) {
		if (config == null) {
			config = new Configuration();
		}
		final String eventPackage[] = new String[eventAutoNames.length];
		int j = 0;
		for (final Package i : eventAutoNames) {
			eventPackage[j++] = i.getName();
		}
		
		return config;
	}
	
	public static Configuration addEventNames(
	        final Map<String, Map<String, Object>> events,
	         Configuration config) {
		if (config == null) {
			config = new Configuration();
		}
		
		for (final Entry<String, Map<String, Object>> itm : events.entrySet()) {
			final Object val = itm.getValue();
			final String name = itm.getKey();
			
			if (val instanceof Class) {
				config.addEventType(name, (Class<?>) val);
			} else if (val instanceof ConfigurationEventTypeXMLDOM) {
				config.addEventType(name,
				        (ConfigurationEventTypeXMLDOM) val);
			} else if (val instanceof Map<?, ?>) {
				try {
					config.addEventType(name, (Map<String, Object>) val);
				} catch (final Throwable t) {
					Logger.log(t);
				}
			} else if (val instanceof Properties) {
				config.addEventType(name, (Properties) val);
			} else if (val instanceof String) {
				config.addEventType(name, (String) val);
			}
		}
		
		return config;
	}
	
	public static Configuration addAutoNames(final String eventAutoNames,
	         Configuration config) {
		if (config == null) {
			config = new Configuration();
		}
		
		return CEPMan.addAutoNames(new String[] {
			    eventAutoNames
		}, config);
	}
	
	public static Configuration addAutoNames(final String[] eventAutoNames,
	         Configuration config) {
		if (config == null) {
			config = new Configuration();
		}
		
		for (final String i : eventAutoNames) {
			config.addEventTypeAutoName(i);
		}
		
		return config;
	}
	
	public synchronized void startSimulation() {
		if (dataCord == null) { throw new IllegalStateException(
		            "Cordinator not initilised: call resetCoordinater(..) or start(..)"); }
		dataCord.start();
	}
	
	protected synchronized void addSingleCSVDataSource(
	        final String eventSource,
	        final CSVInputAdapterSpec input,
	        final Map<String, String> timeStampCol,
	        final Map<String, Integer> eventsPerSecond) {
		if (timeStampCol != null && timeStampCol.containsKey(eventSource)) {
			input.setTimestampColumn(timeStampCol.get(eventSource));
		} else if (eventsPerSecond != null
		        && eventsPerSecond.containsKey(eventSource)) {
			input.setEventsPerSec(eventsPerSecond.get(eventSource));
		}
		
		try {
			dataCord.coordinate(new CSVInputAdapter(input));
		} catch (final Throwable e) {
			Logger.log(e);
		}
	}
	
	public void setStarted(final boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public EPServiceProvider getEpService() {
		return epService;
	}
	
	public String getName() {
		return name;
	}
}
