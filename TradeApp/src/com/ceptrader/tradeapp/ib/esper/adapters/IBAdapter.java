package com.ceptrader.tradeapp.ib.esper.adapters;

import java.util.Collections;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.tradeapp.ib.esper.pojoevents.AccountDownloadEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.BondContractDetails;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ConnectionClosed;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ContractDetailsCommon;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ContractDetailsEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.CurrentTime;
import com.ceptrader.tradeapp.ib.esper.pojoevents.DeltaNeutralValidation;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ExecDetails;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ExecDetailsEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.FundamentalData;
import com.ceptrader.tradeapp.ib.esper.pojoevents.HistoricalData;
import com.ceptrader.tradeapp.ib.esper.pojoevents.IBErrors;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ManagedAccounts;
import com.ceptrader.tradeapp.ib.esper.pojoevents.NextValidId;
import com.ceptrader.tradeapp.ib.esper.pojoevents.OpenOrder;
import com.ceptrader.tradeapp.ib.esper.pojoevents.OpenOrderEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.OrderStatus;
import com.ceptrader.tradeapp.ib.esper.pojoevents.RealtimeBar;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ReceiveFA;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ScannerData;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ScannerDataEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.ScannerParameters;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickEFP;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickGeneric;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickOptionComputation;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickPrice;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickSize;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickSnapshotEnd;
import com.ceptrader.tradeapp.ib.esper.pojoevents.TickString;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdateAccountTime;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdateAccountValue;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdateMktDepth;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdateMktDepthL2;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdateNewsBulletin;
import com.ceptrader.tradeapp.ib.esper.pojoevents.UpdatePortfolio;
import com.ceptrader.tradeapp.util.Loggable;
import com.ceptrader.tradeapp.util.Logger;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class IBAdapter implements EWrapper, Loggable {
	private static IBAdapter	ibAdp;
	
	public static IBAdapter getAdapter() {
		if (IBAdapter.ibAdp == null) {
			IBAdapter.ibAdp = new IBAdapter();
		}
		
		return IBAdapter.ibAdp;
	}
	
	private IBAdapter() {
		IBAdapter.ibAdp = this;
	}
	
	@Override
	public void accountDownloadEnd(final String accountName) {
		final AccountDownloadEnd o = new AccountDownloadEnd(accountName);
		CEPMan.getCEPMan().pumpEvent(o);
		
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void bondContractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final BondContractDetails o = new BondContractDetails(reqId,
		        contractDetails);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void connectionClosed() {
		final ConnectionClosed o = new ConnectionClosed();
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void contractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final ContractDetailsCommon o = new ContractDetailsCommon(reqId,
		        contractDetails);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void contractDetailsEnd(final int reqId) {
		final ContractDetailsEnd o = new ContractDetailsEnd(reqId);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void currentTime(final long time) {
		final CurrentTime o = new CurrentTime(time);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void deltaNeutralValidation(final int reqId,
	        final UnderComp underComp) {
		final DeltaNeutralValidation o = new DeltaNeutralValidation(reqId,
		        underComp);
		CEPMan.getCEPMan()
		        .pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void error(final Exception e) {
		final IBErrors ex = new IBErrors(new IBErrors.EExp(e));
		CEPMan.getCEPMan().pumpEvent(ex);
		Logger.log(ex);
	}
	
	@Override
	public void error(final int id, final int errorCode, final String errorMsg) {
		final IBErrors ex = new IBErrors(new IBErrors.ECode(id, errorCode,
		        errorMsg));
		CEPMan.getCEPMan().pumpEvent(ex);
		Logger.log(ex);
	}
	
	@Override
	public void error(final String str) {
		final IBErrors ex = new IBErrors(new IBErrors.EStr(str));
		CEPMan.getCEPMan().pumpEvent(ex);
		Logger.log(ex);
	}
	
	@Override
	public void execDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		final ExecDetails o = new ExecDetails(reqId, contract, execution);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void execDetailsEnd(final int reqId) {
		final ExecDetailsEnd o = new ExecDetailsEnd(reqId);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void fundamentalData(final int reqId, final String data) {
		final FundamentalData o = new FundamentalData(reqId, data);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void historicalData(final int reqId, final String date,
	        final double open, final double high, final double low,
	        final double close, final int volume, final int count,
	        final double WAP, final boolean hasGaps) {
		final HistoricalData o = new HistoricalData(reqId, date, open, high,
		        low, close, volume,
		        count, WAP, hasGaps);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void managedAccounts(final String accountsList) {
		final ManagedAccounts o = new ManagedAccounts(accountsList);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	private static int	orderId	= 0;
	
	public synchronized static int getNextValidId() {
		return IBAdapter.orderId;
	}
	
	@Override
	public synchronized void nextValidId(final int orderId) {
		IBAdapter.orderId = orderId;
		final NextValidId o = new NextValidId(orderId);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void openOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		final OpenOrder o = new OpenOrder(orderId, contract, order, orderState);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void openOrderEnd() {
		final OpenOrderEnd o = new OpenOrderEnd();
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void orderStatus(final int orderId, final String status,
	        final int filled, final int remaining, final double avgFillPrice,
	        final int permId, final int parentId, final double lastFillPrice,
	        final int clientId, final String whyHeld) {
		final OrderStatus o = new OrderStatus(orderId, status, filled,
		        remaining,
		        avgFillPrice,
		        permId, parentId, lastFillPrice, clientId, whyHeld);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void realtimeBar(final int reqId, final long time,
	        final double open,
	        final double high, final double low, final double close,
	        final long volume, final double wap, final int count) {
		final RealtimeBar o = new RealtimeBar(reqId, time, open, high, low,
		        close, volume,
		        wap,
		        count);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void receiveFA(final int faDataType, final String xml) {
		final ReceiveFA o = new ReceiveFA(faDataType, xml);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void scannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails, final String distance,
	        final String benchmark, final String projection,
	        final String legsStr) {
		final ScannerData o = new ScannerData(reqId, rank, contractDetails,
		        distance,
		        benchmark,
		        projection, legsStr);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void scannerDataEnd(final int reqId) {
		final ScannerDataEnd o = new ScannerDataEnd(reqId);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void scannerParameters(final String xml) {
		final ScannerParameters o = new ScannerParameters(xml);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickEFP(final int tickerId, final int tickType,
	        final double basisPoints, final String formattedBasisPoints,
	        final double impliedFuture, final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	        final double dividendsToExpiry) {
		final TickEFP o = new TickEFP(tickerId, tickType, basisPoints,
		        formattedBasisPoints,
		        impliedFuture, holdDays, futureExpiry, dividendImpact,
		        dividendsToExpiry);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickGeneric(final int tickerId, final int tickType,
	        final double value) {
		final TickGeneric o = new TickGeneric(tickerId, tickType, value);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickOptionComputation(final int tickerId, final int field,
	        final double impliedVol, final double delta, final double optPrice,
	        final double pvDividend, final double gamma, final double vega,
	        final double theta, final double undPrice) {
		final TickOptionComputation o = new TickOptionComputation(tickerId,
		        field, impliedVol, delta,
		        optPrice, pvDividend, gamma, vega, theta, undPrice);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickPrice(final int tickerId, final int field,
	        final double price, final int canAutoExecute) {
		final TickPrice o = new TickPrice(tickerId, field, price,
		        canAutoExecute);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		final TickSize o = new TickSize(tickerId, field, size);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickSnapshotEnd(final int reqId) {
		final TickSnapshotEnd o = new TickSnapshotEnd(reqId);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void tickString(final int tickerId, final int tickType,
	        final String value) {
		final TickString o = new TickString(tickerId, tickType, value);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updateAccountTime(final String timeStamp) {
		final UpdateAccountTime o = new UpdateAccountTime(timeStamp);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updateAccountValue(final String key, final String value,
	        final String currency, final String accountName) {
		final UpdateAccountValue o = new UpdateAccountValue(key, value,
		        currency, accountName);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updateMktDepth(final int tickerId, final int position,
	        final int operation, final int side, final double price,
	        final int size) {
		final UpdateMktDepth o = new UpdateMktDepth(tickerId, position,
		        operation, side,
		        price, size);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updateMktDepthL2(final int tickerId, final int position,
	        final String marketMaker, final int operation, final int side,
	        final double price, final int size) {
		final UpdateMktDepthL2 o = new UpdateMktDepthL2(tickerId, position,
		        marketMaker,
		        operation,
		        side, price, size);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		final UpdateNewsBulletin o = new UpdateNewsBulletin(msgId, msgType,
		        message, origExchange);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
	
	@Override
	public void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		final UpdatePortfolio o = new UpdatePortfolio(contract, position,
		        marketPrice,
		        marketValue,
		        averageCost, unrealizedPNL, realizedPNL, accountName);
		CEPMan.getCEPMan().pumpEvent(o);
		Logger.log(Collections.singletonMap(o.getClass().getName(), o));
	}
}
