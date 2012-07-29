package com.algoTrader.service.fix;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quickfixj.jmx.JmxExporter;

import quickfix.CompositeLogFactory;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SLF4JLogFactory;
import quickfix.Session;
import quickfix.SessionFactory;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;

public class FixClient {

	private static Logger logger = MyLogger.getLogger(FixClient.class.getName());

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation"); 

	private static FixClient instance;

	private boolean initiatorStarted = false;
	private Initiator initiator = null;
	
	public FixClient() throws Exception {

		if (!simulation) {

			InputStream inputStream = this.getClass().getResourceAsStream("/fix.cfg");
			SessionSettings settings = new SessionSettings(inputStream);
			inputStream.close();

			FixApplicationFactory applicationFactory = new FixApplicationFactory(settings);

			MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);

			LogFactory logFactory = new CompositeLogFactory(new LogFactory[] { new SLF4JLogFactory(settings), new FileLogFactory(settings) });

			MessageFactory messageFactory = new DefaultMessageFactory();

			SessionFactory sessionFactory = new FixMultiApplicationSessionFactory(applicationFactory, messageStoreFactory, logFactory, messageFactory);
			this.initiator = new SocketInitiator(sessionFactory, settings);

			JmxExporter exporter = new JmxExporter();
			exporter.register(this.initiator);

			logon();
		}
	}
	
	public static FixClient getInstance() throws Exception {
		
		if (instance == null) {
			instance = new FixClient();			
		}
		return instance;
	}

	public synchronized void logon() {

		if (!this.initiatorStarted) {
			try {
				this.initiator.start();
				this.initiatorStarted = true;
			} catch (Exception e) {
				logger.error("Logon failed", e);
			}
		} else {
			for (SessionID sessionId : this.initiator.getSessions()) {
				Session.lookupSession(sessionId).logon();
			}
		}
	}

	public void logout() {

		for (SessionID sessionId : this.initiator.getSessions()) {
			Session.lookupSession(sessionId).logout("user requested");
		}
	}

	public Map<String, ConnectionState> getConnectionStates() {

		Map<String, ConnectionState> connectionStates = new HashMap<String, ConnectionState>();
		for (SessionID sessionId : this.initiator.getSessions()) {
			Session session = Session.lookupSession(sessionId);
			if (session.isLoggedOn()) {
				connectionStates.put(sessionId.getSessionQualifier(), ConnectionState.READY);
			} else {
				connectionStates.put(sessionId.getSessionQualifier(), ConnectionState.DISCONNECTED);
			}
		}
		return connectionStates;
	}

	public void sendMessage(Message message, String qualifier) throws SessionNotFound {

		for (SessionID sessionId : this.initiator.getSessions()) {
			if (sessionId.getSessionQualifier().equals(qualifier)) {
				Session session = Session.lookupSession(sessionId);
				if (session.isLoggedOn()) {
					session.send(message);
				} else {
					throw new RuntimeException("message cannot be sent, FIX Session is not logged on " + qualifier);
				}
				return;
			}
		}
		throw new RuntimeException("message cannot be sent, FIX Session does not exist " + qualifier);
	}
}
