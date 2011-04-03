package com.ceptrader.tradeapp.ib.esper.adapters;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	private static int	     port	  = 7496; // 7496; 4001;
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
		if (IBClient.ibc == null) {
			IBClient.getIBClient();
		}
		
		if (!IBClient.ibc.isConnected()) {
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
		
		Logger.log(Collections.singletonMap("CalculateImpliedVolatility",
		        event));
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
		
		Logger.log(Collections.singletonMap("CalculateOptionPrice",
		        event));
	}
	
	@Override
	public synchronized void cancelCalculateImpliedVolatility(final int reqId) {
		super.cancelCalculateImpliedVolatility(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelCalculateImpliedVolatility");
		
		Logger.log(Collections.singletonMap("CancelCalculateImpliedVolatility",
		        event));
	}
	
	@Override
	public synchronized void cancelCalculateOptionPrice(final int reqId) {
		super.cancelCalculateOptionPrice(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelCalculateOptionPrice");
		
		Logger.log(Collections.singletonMap("CancelCalculateOptionPrice",
		        event));
	}
	
	@Override
	public synchronized void cancelFundamentalData(final int reqId) {
		super.cancelFundamentalData(reqId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelFundamentalData");
		
		Logger.log(Collections.singletonMap("CancelFundamentalData",
		        event));
	}
	
	@Override
	public synchronized void cancelHistoricalData(final int tickerId) {
		super.cancelHistoricalData(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelHistoricalData");
		
		Logger.log(Collections.singletonMap("CancelHistoricalData",
		        event));
	}
	
	@Override
	public synchronized void cancelMktData(final int tickerId) {
		super.cancelMktData(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelMktData");
		
		Logger.log(Collections.singletonMap("CancelMktData",
		        event));
	}
	
	@Override
	public synchronized void cancelMktDepth(final int tickerId) {
		super.cancelMktDepth(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelMktDepth");
		
		Logger.log(Collections.singletonMap("CancelMktDepth",
		        event));
	}
	
	@Override
	public synchronized void cancelNewsBulletins() {
		super.cancelNewsBulletins();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "CancelNewsBulletins");
		
		Logger.log(Collections.singletonMap("CancelNewsBulletins",
		        event));
	}
	
	@Override
	public synchronized void cancelOrder(final int id) {
		super.cancelOrder(id);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("id", id);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelOrder");
		
		Logger.log(Collections.singletonMap("CancelOrder",
		        event));
	}
	
	@Override
	public void cancelRealTimeBars(final int tickerId) {
		super.cancelRealTimeBars(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelRealTimeBars");
		
		Logger.log(Collections.singletonMap("CancelRealTimeBars",
		        event));
	}
	
	@Override
	public synchronized void cancelScannerSubscription(final int tickerId) {
		super.cancelScannerSubscription(tickerId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		
		CEPMan.getCEPMan().pumpEvent(event, "CancelScannerSubscription");
		
		Logger.log(Collections.singletonMap("CancelScannerSubscription",
		        event));
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
		event.put("host", socket.getLocalSocketAddress().toString());
		event.put("clientId", clientId);
		
		CEPMan.getCEPMan().pumpEvent(event, "EConnect");
		
		Logger.log(Collections.singletonMap("EConnect",
		        event));
	}
	
	@Override
	public synchronized void eConnect(final String host, final int port,
	        final int clientId) {
		super.eConnect(host, port, clientId);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("host", host);
		event.put("clientId", clientId);
		
		CEPMan.getCEPMan().pumpEvent(event, "EConnect");
		
		Logger.log(Collections.singletonMap("EConnect",
		        event));
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
		
		Logger.log(Collections.singletonMap("ExerciseOptions",
		        event));
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
		
		Logger.log(Collections.singletonMap("PlaceOrder",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReplaceFA",
		        event));
	}
	
	@Override
	public synchronized void reqAccountUpdates(final boolean subscribe,
	        final String acctCode) {
		super.reqAccountUpdates(subscribe, acctCode);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("subscribe", subscribe);
		event.put("acctCode", acctCode);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqAccountUpdates");
		
		Logger.log(Collections.singletonMap("ReqAccountUpdates",
		        event));
	}
	
	@Override
	public synchronized void reqAllOpenOrders() {
		super.reqAllOpenOrders();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "ReqAllOpenOrders");
		
		Logger.log(Collections.singletonMap("ReqAllOpenOrders",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqAutoOpenOrders",
		        event));
	}
	
	@Override
	public synchronized void reqContractDetails(final int reqId,
	        final Contract contract) {
		super.reqContractDetails(reqId, contract);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("contract", contract);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqContractDetails");
		
		Logger.log(Collections.singletonMap("ReqContractDetails",
		        event));
	}
	
	@Override
	public synchronized void reqCurrentTime() {
		super.reqCurrentTime();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "ReqCurrentTime");
		
		Logger.log(Collections.singletonMap("ReqCurrentTime",
		        event));
	}
	
	@Override
	public synchronized void reqExecutions(final int reqId,
	        final ExecutionFilter filter) {
		super.reqExecutions(reqId, filter);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("reqId", reqId);
		event.put("filter", filter);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqExecutions");
		
		Logger.log(Collections.singletonMap("ReqExecutions",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqFundamentalData",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqHistoricalData",
		        event));
	}
	
	@Override
	public synchronized void reqIds(final int numIds) {
		super.reqIds(numIds);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("numIds", numIds);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqIds");
		
		Logger.log(Collections.singletonMap("ReqIds",
		        event));
	}
	
	@Override
	public synchronized void reqManagedAccts() {
		super.reqManagedAccts();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "ReqManagedAccts");
		
		Logger.log(Collections.singletonMap("ReqManagedAccts",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqMktData",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqMktDepth",
		        event));
	}
	
	@Override
	public synchronized void reqNewsBulletins(final boolean allMsgs) {
		super.reqNewsBulletins(allMsgs);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("allMsgs", allMsgs);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqNewsBulletins");
		
		Logger.log(Collections.singletonMap("ReqNewsBulletins",
		        event));
	}
	
	@Override
	public synchronized void reqOpenOrders() {
		super.reqOpenOrders();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "ReqOpenOrders");
		
		Logger.log(Collections.singletonMap("ReqOpenOrders",
		        event));
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
		
		Logger.log(Collections.singletonMap("ReqRealTimeBars",
		        event));
	}
	
	@Override
	public synchronized void reqScannerParameters() {
		super.reqScannerParameters();
		
		final Map<String, Object> event = Collections
		        .<String, Object> emptyMap();
		CEPMan.getCEPMan().pumpEvent(event,
		        "ReqScannerParameters");
		
		Logger.log(Collections.singletonMap("ReqScannerParameters",
		        event));
	}
	
	@Override
	public synchronized void reqScannerSubscription(final int tickerId,
	        final ScannerSubscription subscription) {
		super.reqScannerSubscription(tickerId, subscription);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("tickerId", tickerId);
		event.put("subscription", subscription);
		
		CEPMan.getCEPMan().pumpEvent(event, "ReqScannerSubscription");
		
		Logger.log(Collections.singletonMap("ReqScannerSubscription",
		        event));
	}
	
	@Override
	public synchronized void requestFA(final int faDataType) {
		super.requestFA(faDataType);
		
		final HashMap<String, Object> event = new HashMap<String, Object>();
		event.put("faDataType", faDataType);
		
		CEPMan.getCEPMan().pumpEvent(event, "RequestFA");
		
		Logger.log(Collections.singletonMap("RequestFA",
		        event));
	}
	
	@Override
	public synchronized void setServerLogLevel(final int logLevel) {
		super.setServerLogLevel(logLevel);
	}
	
	public static int getClientid() {
		return IBClient.clientId;
	}
}
