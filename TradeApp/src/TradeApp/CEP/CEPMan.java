package TradeApp.CEP;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import TradeApp.Util.Loggable;
import TradeApp.Util.Logger;

import com.espertech.esper.client.*;
import com.espertech.esperio.AdapterCoordinator;
import com.espertech.esperio.AdapterCoordinatorImpl;
import com.espertech.esperio.AdapterInputSource;
import com.espertech.esperio.csv.CSVInputAdapter;
import com.espertech.esperio.csv.CSVInputAdapterSpec;

public class CEPMan implements Loggable {
	private static CEPMan	v;
	
	synchronized public static CEPMan getCEPMan() {
		if (CEPMan.v == null) CEPMan.v = new CEPMan();
		
		return CEPMan.v;
	}
	
	private AdapterCoordinator	dataCord;
	private EPServiceProvider	epService;
	
	private CEPMan() {
	}
	
	public synchronized void addCSVDataSource(
			final Map<String, List<String>> eventNameToSource,
			final Map<String, String> timeStampCol,
			final Map<String, Integer> eventsPerSecond) {
		try {
			for (final String key : eventNameToSource.keySet())
				for (final String s : eventNameToSource.get(key))
					addCSVDataSource(timeStampCol, eventsPerSecond, key, s);
		} catch (final Throwable t) {
			throw new RuntimeException("Exception in registering data sorces", t);
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
			throw new RuntimeException("Exception in registering data sorces", t);
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
			for (final String key : eventNameToSource.keySet())
				for (final T obj : eventNameToSource.get(key)) {
					AdapterInputSource source = null;
					
					final Class<?> sourceClassObj = obj.getClass();
					
					if (sourceClassObj.isAssignableFrom(URL.class)) source = new AdapterInputSource(
						(URL) obj);
					else if (sourceClassObj.isAssignableFrom(File.class)) source = new AdapterInputSource(
						(File) obj);
					else if (sourceClassObj.isAssignableFrom(InputStream.class)) source = new AdapterInputSource(
						(InputStream) obj);
					else if (sourceClassObj.isAssignableFrom(Reader.class)) source = new AdapterInputSource(
						(Reader) obj);
					else if (sourceClassObj.isAssignableFrom(String.class)) source = new AdapterInputSource(
						(String) obj);
					else new IllegalArgumentException(
						"HasSource can only be: File, InputStream, Reader, String, URL. Current type: "
								+ sourceClassObj.getName());
					
					final CSVInputAdapterSpec input = new CSVInputAdapterSpec(
						source, key);
					
					addSingleCSVDataSource(key, input, timeStampCol, eventsPerSecond);
				}
		} catch (final Throwable t) {
			throw new RuntimeException("Exception in registering data sorces", t);
		}
	}
	
	public <T> boolean pumpEvent(final T event) {
		if (epService == null)
			throw new IllegalStateException(
					"CEP Service not started : call start(..)");
		
		try {
			epService.getEPRuntime().sendEvent(event);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}
	
	public boolean pumpEvent(final Map<String, Object> event, final String name) {
		if (epService == null)
			throw new IllegalStateException(
					"CEP Service not started : call start(..)");
		
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
		if (epService == null)
			throw new IllegalStateException(
					"CEP Service not started : call start(..)");
		
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final StatementAwareUpdateListener ul : listnerList)
				stm[i].addListener(ul);
			
			i++;
		}
		return stm;
	}
	
	public synchronized EPStatement[] registerStatementListner(
			final String[] stmtSupplier, final UpdateListener[] listnerList) {
		if (epService == null)
			throw new IllegalStateException(
					"CEP Service not started : call start(..)");
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final UpdateListener ul : listnerList)
				stm[i].addListener(ul);
			
			i++;
		}
		return stm;
	}
	
	public synchronized EPStatement[] registerStatementListnerWithReplay(
			final String[] stmtSupplier, final UpdateListener[] listnerList) {
		if (epService == null)
			throw new IllegalStateException(
					"CEP Service not started : call start(..)");
		
		int i = 0;
		final EPStatement stm[] = new EPStatement[stmtSupplier.length];
		for (final String s : stmtSupplier) {
			stm[i] = epService.getEPAdministrator().createEPL(s);
			
			for (final UpdateListener ul : listnerList)
				stm[i].addListenerWithReplay(ul);
			
			i++;
		}
		return stm;
	}
	
	public synchronized void resetCoordinater() {
		if (dataCord != null) dataCord.destroy();
		dataCord = new AdapterCoordinatorImpl(epService, true);
	}
	
	public synchronized void shutdown() {
		if (dataCord != null) dataCord.destroy();
		if (epService != null) epService.destroy();
	}
	
	public CEPMan start(final Configuration config) {
		if (CEPMan.v == null) CEPMan.v = new CEPMan();
		
		if (epService == null) epService = EPServiceProviderManager
				.getDefaultProvider(config);
		else throw new IllegalStateException("CEP Provider Already Started");
		
		resetCoordinater();
		
		return CEPMan.v;
	}
	
	public CEPMan start(final Package[] eventAutoNames) {
		final String eventPackage[] = new String[eventAutoNames.length];
		int j = 0;
		for (final Package i : eventAutoNames)
			eventPackage[j++] = i.getName();
		
		return this.start(eventPackage);
	}
	
	public CEPMan start(final Map<String, Map<String, Object>> eventAutoNames) {
		final Configuration config = new Configuration();
		
		for (final Entry<String, Map<String, Object>> i : eventAutoNames
				.entrySet())
			config.addEventType(i.getKey(), i.getValue());
		
		return this.start(config);
	}
	
	public CEPMan start(final String eventAutoNames) {
		return this.start(new String[] { eventAutoNames });
	}
	
	public CEPMan start(final String[] eventAutoNames) {
		final Configuration config = new Configuration();
		for (final String i : eventAutoNames)
			config.addEventTypeAutoName(i);
		
		return this.start(config);
	}
	
	public synchronized void startSimulation() {
		if (dataCord == null)
			throw new IllegalStateException(
					"Cordinator not initilised: call resetCoordinater(..) or start(..)");
		dataCord.start();
	}
	
	protected synchronized void addSingleCSVDataSource(final String eventSource,
			final CSVInputAdapterSpec input,
			final Map<String, String> timeStampCol,
			final Map<String, Integer> eventsPerSecond) {
		if (timeStampCol != null && timeStampCol.containsKey(eventSource)) input
				.setTimestampColumn(timeStampCol.get(eventSource));
		else if (eventsPerSecond != null
				&& eventsPerSecond.containsKey(eventSource))
			input.setEventsPerSec(eventsPerSecond.get(eventSource));
		
		try {
			dataCord.coordinate(new CSVInputAdapter(input));
		} catch (final Throwable e) {
			Logger.log(e);
		}
	}
}
