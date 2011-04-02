package com.ceptrader.tradeapp.ib.esper.adapters;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.tradeapp.util.Logger;
import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EWrapper;
import com.ib.client.ExecutionFilter;
import com.ib.client.Order;
import com.ib.client.ScannerSubscription;

public class IBClient extends EClientSocket {
	private static int	     clientId	= 0;
	private static String	 host	  = null;
	private static int	     port	  = 4001; // 7496;
	private static IBClient	 ibc;
	private static IBAdapter	ibAdp;
	
	public synchronized static IBClient getIBClient() {
		if (IBClient.ibc == null) {
			IBClient.ibAdp = IBAdapter.getAdapter();
			
			IBClient.ibc = new IBClient(IBClient.ibAdp);
		}
		
		return IBClient.ibc;
	}
	
	private IBClient(final EWrapper ew) {
		super(ew);
	}
	
	public synchronized static void connect() {
		if (IBClient.ibc == null || !IBClient.ibc.isConnected()) {
			IBClient.ibc.eConnect(IBClient.host, IBClient.port,
			        IBClient.clientId);
			Logger.log("Connected", IBClient.class);
			
			IBClient.clientId++;
		} else {
			throw new IllegalStateException("Already connected.");
		}
	}
	
	private static final String	broker	= "IB";
	private static final String	account	= "LoggedInAccount";
	
	public synchronized static void disconnect() {
		if (IBClient.ibc != null) {
			IBClient.ibc.eDisconnect();
		}
	}
	
	@Override
	public synchronized void calculateImpliedVolatility(final int reqId,
	        final Contract contract, final double optionPrice,
	        final double underPrice) {
		super.calculateImpliedVolatility(reqId, contract, optionPrice,
		        underPrice);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("contract", contract);
		event.put("optionPrice", optionPrice);
		event.put("underPrice", underPrice);
		
		CEPMan.getCEPMan().pumpEvent(event, "CalculateImpliedVolatility");
	}
	
	@Override
	public synchronized void calculateOptionPrice(final int reqId,
	        final Contract contract,
	        final double volatility, final double underPrice) {
		super.calculateOptionPrice(reqId, contract, volatility, underPrice);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("contract", contract);
		event.put("volatility", volatility);
		event.put("underPrice", underPrice);
		
		CEPMan.getCEPMan().pumpEvent(event, "CalculateOptionPrice");
	}
	
	@Override
	public synchronized void cancelCalculateImpliedVolatility(final int reqId) {
		super.cancelCalculateImpliedVolatility(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelCalculateImpliedVolatility");
	}
	
	@Override
	public synchronized void cancelCalculateOptionPrice(final int reqId) {
		super.cancelCalculateOptionPrice(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelCalculateOptionPrice");
	}
	
	@Override
	public synchronized void cancelFundamentalData(final int reqId) {
		super.cancelFundamentalData(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelFundamentalData");
	}
	
	@Override
	public synchronized void cancelHistoricalData(final int tickerId) {
		super.cancelHistoricalData(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelHistoricalData");
	}
	
	@Override
	public synchronized void cancelMktData(final int tickerId) {
		super.cancelMktData(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelMktData");
	}
	
	@Override
	public synchronized void cancelMktDepth(final int tickerId) {
		super.cancelMktDepth(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelMktDepth");
	}
	
	@Override
	public synchronized void cancelNewsBulletins() {
		super.cancelNewsBulletins();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "CancelNewsBulletins");
	}
	
	@Override
	public synchronized void cancelOrder(final int id) {
		super.cancelOrder(id);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("id", id);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelOrder");
	}
	
	@Override
	public void cancelRealTimeBars(final int tickerId) {
		super.cancelRealTimeBars(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelRealTimeBars");
	}
	
	@Override
	public synchronized void cancelScannerSubscription(final int tickerId) {
		super.cancelScannerSubscription(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelScannerSubscription");
	}
	
	@Override
	public EReader createReader(final EClientSocket socket,
	        final DataInputStream dis) {
		return super.createReader(socket, dis);
	}
	
	@Override
	public synchronized void eConnect(final Socket socket, final int clientId)
	        throws IOException {
		super.eConnect(socket, clientId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("socket", socket.getLocalSocketAddress().toString());
		event.put("clientId", clientId);
		
		CEPMan.getCEPMan().pumpEvent(event, "EConnect");
	}
	
	@Override
	public synchronized void eConnect(final String host, final int port,
	        final int clientId) {
		super.eConnect(host, port, clientId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("host", host);
		event.put("clientId", clientId);
		
		CEPMan.getCEPMan().pumpEvent(event, "EConnect");
	}
	
	@Override
	public synchronized void eDisconnect() {
		super.eDisconnect();
	}
	
	@Override
	public synchronized void exerciseOptions(final int tickerId,
	        final Contract contract,
	        final int exerciseAction, final int exerciseQuantity,
	        final String account,
	        final int override) {
		super.exerciseOptions(tickerId, contract, exerciseAction,
		        exerciseQuantity,
		        account, override);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("contract", contract);
		event.put("exerciseAction", exerciseAction);
		event.put("exerciseQuantity", exerciseQuantity);
		event.put("account", account);
		event.put("override", override);
		
		CEPMan.getCEPMan().pumpEvent(event, "ExerciseOptions");
	}
	
	@Override
	public synchronized void placeOrder(final int id, final Contract contract,
	        final Order order) {
		super.placeOrder(id, contract, order);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("id", id);
		event.put("contract", contract);
		event.put("order", order);
		
		CEPMan.getCEPMan().pumpEvent(event, "PlaceOrder");
	}
	
	@Override
	public boolean isConnected() {
		return super.isConnected();
	}
	
	@Override
	public synchronized void replaceFA(final int faDataType, final String xml) {
		super.replaceFA(faDataType, xml);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("faDataType", faDataType);
		event.put("xml", xml);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReplaceFA");
	}
	
	@Override
	public synchronized void reqAccountUpdates(final boolean subscribe,
	        final String acctCode) {
		super.reqAccountUpdates(subscribe, acctCode);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("subscribe", subscribe);
		event.put("acctCode", acctCode);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqAccountUpdates");
	}
	
	@Override
	public synchronized void reqAllOpenOrders() {
		super.reqAllOpenOrders();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "ReqAllOpenOrders");
	}
	
	@Override
	public EReader reader() {
		return super.reader();
	}
	
	@Override
	public synchronized void reqAutoOpenOrders(final boolean bAutoBind) {
		super.reqAutoOpenOrders(bAutoBind);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("bAutoBind", bAutoBind);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqAutoOpenOrders");
	}
	
	@Override
	public synchronized void reqContractDetails(final int reqId,
	        final Contract contract) {
		super.reqContractDetails(reqId, contract);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("contract", contract);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqContractDetails");
	}
	
	@Override
	public synchronized void reqCurrentTime() {
		super.reqCurrentTime();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "ReqCurrentTime");
	}
	
	@Override
	public synchronized void reqExecutions(final int reqId,
	        final ExecutionFilter filter) {
		super.reqExecutions(reqId, filter);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("filter", filter);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqExecutions");
	}
	
	@Override
	public synchronized void reqFundamentalData(final int reqId,
	        final Contract contract,
	        final String reportType) {
		super.reqFundamentalData(reqId, contract, reportType);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("contract", contract);
		event.put("reportType", reportType);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqFundamentalData");
	}
	
	@Override
	public synchronized void reqHistoricalData(final int tickerId,
	        final Contract contract,
	        final String endDateTime, final String durationStr,
	        final String barSizeSetting,
	        final String whatToShow, final int useRTH, final int formatDate) {
		super.reqHistoricalData(tickerId, contract, endDateTime, durationStr,
		        barSizeSetting, whatToShow, useRTH, formatDate);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("contract", contract);
		event.put("endDateTime", endDateTime);
		event.put("durationStr", durationStr);
		event.put("barSizeSetting", barSizeSetting);
		event.put("whatToShow", whatToShow);
		event.put("useRTH", useRTH);
		event.put("formatDate", formatDate);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqHistoricalData");
	}
	
	@Override
	public synchronized void reqIds(final int numIds) {
		super.reqIds(numIds);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("numIds", numIds);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqIds");
	}
	
	@Override
	public synchronized void reqManagedAccts() {
		super.reqManagedAccts();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "ReqManagedAccts");
	}
	
	@Override
	public synchronized void reqMktData(final int tickerId,
	        final Contract contract,
	        final String genericTickList, final boolean snapshot) {
		super.reqMktData(tickerId, contract, genericTickList, snapshot);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("contract", contract);
		event.put("genericTickList", genericTickList);
		event.put("snapshot", snapshot);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqMktData");
	}
	
	@Override
	public synchronized void reqMktDepth(final int tickerId,
	        final Contract contract,
	        final int numRows) {
		super.reqMktDepth(tickerId, contract, numRows);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("contract", contract);
		event.put("numRows", numRows);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqMktDepth");
	}
	
	@Override
	public synchronized void reqNewsBulletins(final boolean allMsgs) {
		super.reqNewsBulletins(allMsgs);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("allMsgs", allMsgs);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqNewsBulletins");
	}
	
	@Override
	public synchronized void reqOpenOrders() {
		super.reqOpenOrders();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "ReqOpenOrders");
	}
	
	@Override
	public synchronized void reqRealTimeBars(final int tickerId,
	        final Contract contract,
	        final int barSize, final String whatToShow, final boolean useRTH) {
		super.reqRealTimeBars(tickerId, contract, barSize, whatToShow, useRTH);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("contract", contract);
		event.put("barSize", barSize);
		event.put("whatToShow", whatToShow);
		event.put("useRTH", useRTH);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqRealTimeBars");
	}
	
	@Override
	public synchronized void reqScannerParameters() {
		super.reqScannerParameters();
		
		CEPMan.getCEPMan().pumpEvent(Collections.<String, Object> emptyMap(),
		        "ReqScannerParameters");
	}
	
	@Override
	public synchronized void reqScannerSubscription(final int tickerId,
	        final ScannerSubscription subscription) {
		super.reqScannerSubscription(tickerId, subscription);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("subscription", subscription);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqScannerSubscription");
	}
	
	@Override
	public synchronized void requestFA(final int faDataType) {
		super.requestFA(faDataType);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("faDataType", faDataType);
		
		CEPMan.getCEPMan().pumpEvent(event, "RequestFA");
	}
	
	@Override
	public synchronized void setServerLogLevel(final int logLevel) {
		super.setServerLogLevel(logLevel);
	}
	
	public static int getClientid() {
		return IBClient.clientId;
	}
}
