package com.algoTrader.service.ib;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.EClientSocket;

public final class IBClient extends EClientSocket {

	private static Logger logger = MyLogger.getLogger(IBClient.class.getName());

	private static int defaultClientId = ConfigurationUtil.getBaseConfig().getInt("ib.defaultClientId"); //0
	private static int port = ConfigurationUtil.getBaseConfig().getInt("ib.port"); //7496;//
	private static String host = ConfigurationUtil.getBaseConfig().getString("ib.host"); // "127.0.0.1";
	private static long connectionTimeout = ConfigurationUtil.getBaseConfig().getInt("ib.connectionTimeout"); //10000;//

	private static IBClient instance;

	private int clientId;

	public IBClient(int clientId, IBDefaultAdapter wrapper) {

		super(wrapper);
		this.clientId = clientId;
	}

	public static IBClient getDefaultInstance() {

		if (instance == null) {

			instance = new IBClient(defaultClientId, new IBEsperAdapter(defaultClientId));

			// connect in a separate thread, because it might take a while
			(new Thread() {
				@Override
				public void run() {
					instance.connect();
				}
			}).start();
		}
		return instance;
	}

	public IBDefaultAdapter getIbAdapter() {
		return (IBDefaultAdapter) super.wrapper();
	}

	public void connect() {

		if (isConnected()) {
			eDisconnect();

			sleep();
		}

		this.getIbAdapter().setRequested(false);

		while (!connectionAvailable()) {
			sleep();
		}

		eConnect(host, port, this.clientId);

		if (isConnected()) {
			this.getIbAdapter().setState(ConnectionState.READY);

			// in case there is no 2104 message from the IB Gateway (Market data farm connection is OK)
			// manually invoke initWatchlist after some time
			sleep();
			ServiceLocator.commonInstance().getMarketDataService().initWatchlist();
		}
	}

	private void sleep() {

		try {
			Thread.sleep(connectionTimeout);
		} catch (InterruptedException e1) {
			try {
				// during eDisconnect this thread get's interrupted so sleep again
				Thread.sleep(connectionTimeout);
			} catch (InterruptedException e2) {
				logger.error("problem sleeping", e2);
			}
		}
	}

	public void disconnect() {

		if (isConnected()) {
			eDisconnect();
		}
	}

	private static synchronized boolean connectionAvailable() {
		try {
			Socket socket = new Socket(host, port);
			socket.close();
			return true;
		} catch (ConnectException e) {
			// do nothing, gateway is down
			return false;
		} catch (IOException e) {
			logger.error("connection error", e);
			return false;
		}
	}
}
