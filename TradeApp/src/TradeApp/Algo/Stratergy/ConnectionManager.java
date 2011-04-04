
package TradeApp.Algo.Stratergy;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import TradeApp.Util.BasicUtils;

public class ConnectionManager {
	private static HashMap<Map<String, String>, HashSet<Stratergy>>	map	= new HashMap<Map<String, String>, HashSet<Stratergy>>();
	
	public static void registerStratergy(final String broker,
			final String account, final Stratergy stratergy) {
		final Map<String, String> brokerAccount = Collections.singletonMap(
				broker, account);
		
		if (!ConnectionManager.map.containsKey(brokerAccount))
			ConnectionManager.map.put(brokerAccount, new HashSet<Stratergy>());
		
		final HashSet<Stratergy> stratergyList = ConnectionManager.map
				.get(brokerAccount);
		
		stratergyList.add(stratergy);
	}
	
	public static void removeStratergy(final String broker,
			final String account, final Stratergy stratergy) {
		final Map<String, String> brokerAccount = Collections.singletonMap(
				broker, account);
		
		if (ConnectionManager.map.containsKey(brokerAccount)) {
			final HashSet<Stratergy> stratergyList = ConnectionManager.map
					.get(brokerAccount);
			stratergyList.remove(stratergy);
		}
	}
	
	public static void connected(final String broker, final String account) {
		final Map<String, String> brokerAccount = Collections.singletonMap(
				broker, account);
		
		if (ConnectionManager.map.containsKey(brokerAccount)) {
			final HashSet<Stratergy> stratergyList = ConnectionManager.map
					.get(brokerAccount);
			
			for (final Stratergy s : stratergyList)
				BasicUtils.getThreadPool().submit(new Runnable() {
					
					@Override
					public void run() {
						s.connected(broker, account);
					}
				});
		}
	}
	
	public static void disconnected(final String broker, final String account) {
		final Map<String, String> brokerAccount = Collections.singletonMap(
				broker, account);
		
		if (ConnectionManager.map.containsKey(brokerAccount)) {
			final HashSet<Stratergy> stratergyList = ConnectionManager.map
					.get(brokerAccount);
			
			for (final Stratergy s : stratergyList)
				BasicUtils.getThreadPool().submit(new Runnable() {
					
					@Override
					public void run() {
						s.disconnected(broker, account);
					}
				});
		}
	}
}
