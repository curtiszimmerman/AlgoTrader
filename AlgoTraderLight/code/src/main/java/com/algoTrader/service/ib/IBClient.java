package com.algoTrader.service.ib;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;
import com.ib.client.ScannerSubscription;

public final class IBClient extends EClientSocket {

	private static Logger logger = MyLogger.getLogger(IBClient.class.getName());

	private static int clientId = ConfigurationUtil.getBaseConfig().getInt("ib.clientId"); //0
	private static long connectionTimeout = ConfigurationUtil.getBaseConfig().getInt("ib.connectionTimeout"); //10000;//
	private static int port = ConfigurationUtil.getBaseConfig().getInt("ib.port"); //7496;//
	private static String host = ConfigurationUtil.getBaseConfig().getString("ib.host"); // "127.0.0.1";

	private static IBClient ibClient;
	private IBAdapter ibAdapter;

	private IBClient(IBAdapter ibAdapter) {
		super(ibAdapter);
		setIbAdapter(ibAdapter);
		this.connect();
	}

	public static IBClient getInstance() {

		if (ibClient == null) {
			IBAdapter ibAdapter = IBAdapter.getInstance(clientId);
			ibClient = new IBClient(ibAdapter);
		}
		return ibClient;
	}

	public IBAdapter getIbAdapter() {
		return this.ibAdapter;
	}

	public void setIbAdapter(IBAdapter ibAdapter) {
		this.ibAdapter = ibAdapter;
	}

	public void connect() {

		if (isConnected()) {
			eDisconnect();

			sleep();
		}

		this.ibAdapter.setRequested(false);

		while (!connectionAvailable()) {

			sleep();
		}

		eConnect(null, port, clientId);

		if (isConnected()) {
			this.ibAdapter.setState(ConnectionState.CONNECTED);
			logger.info("connectionState: " + this.ibAdapter.getState());
		}
	}

	private static void sleep() {

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

	@Override
	public synchronized void calculateImpliedVolatility(final int reqId, final Contract contract, final double optionPrice, final double underPrice) {
		super.calculateImpliedVolatility(reqId, contract, optionPrice, underPrice);

	}

	@Override
	public synchronized void calculateOptionPrice(final int reqId, final Contract contract, final double volatility, final double underPrice) {
		super.calculateOptionPrice(reqId, contract, volatility, underPrice);

	}

	@Override
	public synchronized void cancelCalculateImpliedVolatility(final int reqId) {
		super.cancelCalculateImpliedVolatility(reqId);

	}

	@Override
	public synchronized void cancelCalculateOptionPrice(final int reqId) {
		super.cancelCalculateOptionPrice(reqId);

	}

	@Override
	public synchronized void cancelFundamentalData(final int reqId) {
		super.cancelFundamentalData(reqId);

	}

	@Override
	public synchronized void cancelHistoricalData(final int tickerId) {
		super.cancelHistoricalData(tickerId);

	}

	@Override
	public synchronized void cancelMktData(final int tickerId) {
		super.cancelMktData(tickerId);

	}

	@Override
	public synchronized void cancelMktDepth(final int tickerId) {
		super.cancelMktDepth(tickerId);

	}

	@Override
	public synchronized void cancelNewsBulletins() {
		super.cancelNewsBulletins();

	}

	@Override
	public synchronized void cancelOrder(final int id) {
		super.cancelOrder(id);

	}

	@Override
	public void cancelRealTimeBars(final int tickerId) {
		super.cancelRealTimeBars(tickerId);

	}

	@Override
	public synchronized void cancelScannerSubscription(final int tickerId) {
		super.cancelScannerSubscription(tickerId);

	}

	@Override
	public EReader createReader(final EClientSocket socket, final DataInputStream dis) {
		return super.createReader(socket, dis);
	}

	@Override
	public synchronized void eConnect(final Socket socket, final int clientId) throws IOException {
		super.eConnect(socket, clientId);

	}

	@Override
	public synchronized void eConnect(final String host, final int port, final int clientId) {
		super.eConnect(host, port, clientId);

	}

	@Override
	public synchronized void eDisconnect() {
		super.eDisconnect();
	}

	@Override
	public synchronized void exerciseOptions(final int tickerId, final Contract contract, final int exerciseAction, final int exerciseQuantity,
			final String account, final int override) {
		super.exerciseOptions(tickerId, contract, exerciseAction, exerciseQuantity, account, override);

	}

	@Override
	public synchronized void placeOrder(final int id, final Contract contract, final Order order) {
		super.placeOrder(id, contract, order);

	}

	@Override
	public boolean isConnected() {
		return super.isConnected();
	}

	@Override
	public synchronized void replaceFA(final int faDataType, final String xml) {
		super.replaceFA(faDataType, xml);

	}

	@Override
	public synchronized void reqAccountUpdates(final boolean subscribe, final String acctCode) {
		super.reqAccountUpdates(subscribe, acctCode);

	}

	@Override
	public synchronized void reqAllOpenOrders() {
		super.reqAllOpenOrders();

	}

	@Override
	public EReader reader() {
		return super.reader();
	}

	@Override
	public synchronized void reqAutoOpenOrders(final boolean bAutoBind) {
		super.reqAutoOpenOrders(bAutoBind);

	}

	@Override
	public synchronized void reqContractDetails(final int reqId, final Contract contract) {
		super.reqContractDetails(reqId, contract);

	}

	@Override
	public synchronized void reqCurrentTime() {
		super.reqCurrentTime();

	}

	@Override
	public synchronized void reqExecutions(final int reqId, final ExecutionFilter filter) {
		super.reqExecutions(reqId, filter);

	}

	@Override
	public synchronized void reqFundamentalData(final int reqId, final Contract contract, final String reportType) {
		super.reqFundamentalData(reqId, contract, reportType);

	}

	@Override
	public synchronized void reqHistoricalData(final int tickerId, final Contract contract, final String endDateTime, final String durationStr,
			final String barSizeSetting, final String whatToShow, final int useRTH, final int formatDate) {
		super.reqHistoricalData(tickerId, contract, endDateTime, durationStr, barSizeSetting, whatToShow, useRTH, formatDate);

	}

	@Override
	public synchronized void reqIds(final int numIds) {
		super.reqIds(numIds);

	}

	@Override
	public synchronized void reqManagedAccts() {
		super.reqManagedAccts();

	}

	@Override
	public synchronized void reqMktData(final int tickerId, final Contract contract, final String genericTickList, final boolean snapshot) {
		super.reqMktData(tickerId, contract, genericTickList, snapshot);

	}

	@Override
	public synchronized void reqMktDepth(final int tickerId, final Contract contract, final int numRows) {
		super.reqMktDepth(tickerId, contract, numRows);

	}

	@Override
	public synchronized void reqNewsBulletins(final boolean allMsgs) {
		super.reqNewsBulletins(allMsgs);

	}

	@Override
	public synchronized void reqOpenOrders() {
		super.reqOpenOrders();

	}

	@Override
	public synchronized void reqRealTimeBars(final int tickerId, final Contract contract, final int barSize, final String whatToShow, final boolean useRTH) {
		super.reqRealTimeBars(tickerId, contract, barSize, whatToShow, useRTH);

	}

	@Override
	public synchronized void reqScannerParameters() {
		super.reqScannerParameters();

	}

	@Override
	public synchronized void reqScannerSubscription(final int tickerId, final ScannerSubscription subscription) {
		super.reqScannerSubscription(tickerId, subscription);

	}

	@Override
	public synchronized void requestFA(final int faDataType) {
		super.requestFA(faDataType);

	}

	@Override
	public synchronized void setServerLogLevel(final int logLevel) {
		super.setServerLogLevel(logLevel);
	}

	public static int getClientid() {
		return IBClient.clientId;
	}
}
