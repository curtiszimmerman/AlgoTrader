package com.algoTrader.service.ib;

import java.io.EOFException;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.ib.AccountDownloadEnd;
import com.algoTrader.vo.ib.ContractDetailsCommon;
import com.algoTrader.vo.ib.ContractDetailsEnd;
import com.algoTrader.vo.ib.CurrentTime;
import com.algoTrader.vo.ib.DeltaNeutralValidation;
import com.algoTrader.vo.ib.ExecDetails;
import com.algoTrader.vo.ib.ExecDetailsEnd;
import com.algoTrader.vo.ib.FundamentalData;
import com.algoTrader.vo.ib.HistoricalData;
import com.algoTrader.vo.ib.ManagedAccounts;
import com.algoTrader.vo.ib.OpenOrder;
import com.algoTrader.vo.ib.OrderStatus;
import com.algoTrader.vo.ib.RealtimeBar;
import com.algoTrader.vo.ib.ReceiveFA;
import com.algoTrader.vo.ib.ScannerData;
import com.algoTrader.vo.ib.ScannerDataEnd;
import com.algoTrader.vo.ib.ScannerParameters;
import com.algoTrader.vo.ib.TickEFP;
import com.algoTrader.vo.ib.TickGeneric;
import com.algoTrader.vo.ib.TickOptionComputation;
import com.algoTrader.vo.ib.TickPrice;
import com.algoTrader.vo.ib.TickSize;
import com.algoTrader.vo.ib.TickSnapshotEnd;
import com.algoTrader.vo.ib.TickString;
import com.algoTrader.vo.ib.UpdateAccountTime;
import com.algoTrader.vo.ib.UpdateAccountValue;
import com.algoTrader.vo.ib.UpdateMktDepth;
import com.algoTrader.vo.ib.UpdateMktDepthL2;
import com.algoTrader.vo.ib.UpdateNewsBulletin;
import com.algoTrader.vo.ib.UpdatePortfolio;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.EWrapperMsgGenerator;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public final class IBAdapter implements EWrapper {

	private static Logger logger = MyLogger.getLogger(IBAdapter.class.getName());
	private static IBAdapter ibAdapter;

	private boolean requested;
	private ConnectionState state;

	private IBAdapter(int clientId) {

		setState(ConnectionState.DISCONNECTED);
	}

	public static IBAdapter getInstance(int clientId) {

		if (ibAdapter == null) {
			ibAdapter = new IBAdapter(clientId);
		}

		return ibAdapter;
	}

	@Override
	public void connectionClosed() {

		setState(ConnectionState.DISCONNECTED);

		//if connection gets closed, try to reconnect
		IBClient.getInstance().connect();
		logger.debug(EWrapperMsgGenerator.connectionClosed());
	}

	@Override
	public void error(final Exception e) {

		// we get EOFException and SocketException when TWS is closed
		if (!(e instanceof EOFException || e instanceof SocketException)) {
			logger.error("ib error", e);
		}
	}

	@Override
	public void error(final int id, final int code, final String errorMsg) {

		String message = "id: " + id + " code: " + code + " " + errorMsg.replaceAll("\n", " ");
		if (code < 1000) {
			logger.error(message, new RuntimeException(message));
		} else {
			logger.info(message);
		}
	
		if (code == 502) {
	
			// Couldn't connect to TWS
			setState(ConnectionState.DISCONNECTED);
	
		} else if (code == 1100) {
	
			// Connectivity between IB and TWS has been lost.
			setState(ConnectionState.CONNECTED);
	
		} else if (code == 1101) {
	
			// Connectivity between IB and TWS has been restored data lost.
			setRequested(false);
			setState(ConnectionState.READY);
			ServiceLocator.commonInstance().getMarketDataService().reinitWatchlist();
	
		} else if (code == 1102) {
	
			// Connectivity between IB and TWS has been restored data maintained.
			if (isRequested()) {
				setState(ConnectionState.SUBSCRIBED);
			} else {
				setState(ConnectionState.READY);
				ServiceLocator.commonInstance().getMarketDataService().reinitWatchlist();
			}
	
		} else if (code == 2110) {
	
			// Connectivity between TWS and server is broken. It will be restored automatically.
			setState(ConnectionState.CONNECTED);
	
		} else if (code == 2104) {
	
			// A market data farm is connected.
			if (isRequested()) {
				setState(ConnectionState.SUBSCRIBED);
			} else {
				setState(ConnectionState.READY);
				ServiceLocator.commonInstance().getMarketDataService().reinitWatchlist();
			}
		}
	}

	@Override
	public void error(final String str) {

		logger.error(str, new RuntimeException(str));
	}

	@Override
	public synchronized void nextValidId(final int orderId) {

		RequestIDGenerator.singleton().initializeOrderId(orderId);
		logger.debug(EWrapperMsgGenerator.nextValidId(orderId));
	}

	public boolean isRequested() {
		return this.requested;
	}

	public void setRequested(boolean requested) {

		if (this.requested != requested) {
			logger.debug("requested: " + requested);
		}

		this.requested = requested;
	}

	public ConnectionState getState() {
		return this.state;
	}

	public void setState(ConnectionState state) {

		if (this.state != state) {
			logger.debug("connectionState: " + state);
		}

		this.state = state;

	}

	// Override EWrapper methos

	@Override
	public void accountDownloadEnd(final String accountName) {
		final AccountDownloadEnd o = new AccountDownloadEnd(accountName);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.accountDownloadEnd(accountName));
	}

	@Override
	public void bondContractDetails(final int reqId, final ContractDetails contractDetails) {
		final ContractDetailsCommon o = new ContractDetailsCommon(reqId, contractDetails);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
	}

	@Override
	public void contractDetails(final int reqId, final ContractDetails contractDetails) {
		final ContractDetailsCommon o = new ContractDetailsCommon(reqId, contractDetails);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
	}

	@Override
	public void contractDetailsEnd(final int reqId) {
		final ContractDetailsEnd o = new ContractDetailsEnd(reqId);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.contractDetailsEnd(reqId));
	}

	@Override
	public void currentTime(final long time) {
		final CurrentTime o = new CurrentTime(time);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.currentTime(time));
	}

	@Override
	public void deltaNeutralValidation(final int reqId, final UnderComp underComp) {
		final DeltaNeutralValidation o = new DeltaNeutralValidation(reqId, underComp);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.deltaNeutralValidation(reqId, underComp));
	}

	@Override
	public void execDetails(final int reqId, final Contract contract, final Execution execution) {
		final ExecDetails o = new ExecDetails(reqId, contract, execution);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.execDetails(reqId, contract, execution));
	}

	@Override
	public void execDetailsEnd(final int reqId) {
		final ExecDetailsEnd o = new ExecDetailsEnd(reqId);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.execDetailsEnd(reqId));
	}

	@Override
	public void fundamentalData(final int reqId, final String data) {
		final FundamentalData o = new FundamentalData(reqId, data);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.fundamentalData(reqId, data));
	}

	@Override
	public void historicalData(final int reqId, final String date, final double open, final double high, final double low, final double close,
			final int volume, final int count, final double wap, final boolean hasGaps) {
		final HistoricalData o = new HistoricalData(reqId, date, open, high, low, close, volume, count, wap, hasGaps);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.historicalData(reqId, date, open, high, low, close, volume, count, wap, hasGaps));
	}

	@Override
	public void managedAccounts(final String accountsList) {
		final ManagedAccounts o = new ManagedAccounts(accountsList);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.managedAccounts(accountsList));
	}

	@Override
	public void openOrder(final int orderId, final Contract contract, final Order order, final OrderState orderState) {
		final OpenOrder o = new OpenOrder(orderId, contract, order, orderState);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
	}

	@Override
	public void openOrderEnd() {
		//final OpenOrderEnd o = new OpenOrderEnd();
		//ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.openOrderEnd());
	}

	@Override
	public void orderStatus(final int orderId, final String status, final int filled, final int remaining, final double avgFillPrice, final int permId,
			final int parentId, final double lastFillPrice, final int clientId, final String whyHeld) {
		final OrderStatus o = new OrderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld));
	}

	@Override
	public void realtimeBar(final int reqId, final long time, final double open, final double high, final double low, final double close, final long volume,
			final double wap, final int count) {
		final RealtimeBar o = new RealtimeBar(reqId, time, open, high, low, close, volume, wap, count);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
	}

	@Override
	public void receiveFA(final int faDataType, final String xml) {
		final ReceiveFA o = new ReceiveFA(faDataType, xml);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.receiveFA(faDataType, xml));
	}

	@Override
	public void scannerData(final int reqId, final int rank, final ContractDetails contractDetails, final String distance, final String benchmark,
			final String projection, final String legsStr) {
		final ScannerData o = new ScannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.scannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr));
	}

	@Override
	public void scannerDataEnd(final int reqId) {
		final ScannerDataEnd o = new ScannerDataEnd(reqId);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.scannerDataEnd(reqId));
	}

	@Override
	public void scannerParameters(final String xml) {
		final ScannerParameters o = new ScannerParameters(xml);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.scannerParameters(xml));
	}

	@Override
	public void tickEFP(final int tickerId, final int tickType, final double basisPoints, final String formattedBasisPoints, final double impliedFuture,
			final int holdDays, final String futureExpiry, final double dividendImpact, final double dividendsToExpiry) {
		final TickEFP o = new TickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureExpiry, dividendImpact,
				dividendsToExpiry);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureExpiry, dividendImpact,
				dividendsToExpiry));
	}

	@Override
	public void tickGeneric(final int tickerId, final int tickType, final double value) {
		final TickGeneric o = new TickGeneric(tickerId, tickType, value);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));
	}

	@Override
	public void tickOptionComputation(final int tickerId, final int field, final double impliedVol, final double delta, final double optPrice,
			final double pvDividend, final double gamma, final double vega, final double theta, final double undPrice) {
		final TickOptionComputation o = new TickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice));
	}

	@Override
	public void tickPrice(final int tickerId, final int field, final double price, final int canAutoExecute) {
		final TickPrice o = new TickPrice(tickerId, field, price, canAutoExecute);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickPrice(tickerId, field, price, canAutoExecute));
	}

	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		final TickSize o = new TickSize(tickerId, field, size);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickSize(tickerId, field, size));
	}

	@Override
	public void tickSnapshotEnd(final int reqId) {
		final TickSnapshotEnd o = new TickSnapshotEnd(reqId);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickSnapshotEnd(reqId));
	}

	@Override
	public void tickString(final int tickerId, final int tickType, final String value) {
		final TickString o = new TickString(tickerId, tickType, value);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.tickString(tickerId, tickType, value));
	}

	@Override
	public void updateAccountTime(final String timeStamp) {
		final UpdateAccountTime o = new UpdateAccountTime(timeStamp);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updateAccountTime(timeStamp));
	}

	@Override
	public void updateAccountValue(final String key, final String value, final String currency, final String accountName) {
		final UpdateAccountValue o = new UpdateAccountValue(key, value, currency, accountName);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updateAccountValue(key, value, currency, accountName));
	}

	@Override
	public void updateMktDepth(final int tickerId, final int position, final int operation, final int side, final double price, final int size) {
		final UpdateMktDepth o = new UpdateMktDepth(tickerId, position, operation, side, price, size);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updateMktDepth(tickerId, position, operation, side, price, size));
	}

	@Override
	public void updateMktDepthL2(final int tickerId, final int position, final String marketMaker, final int operation, final int side, final double price,
			final int size) {
		final UpdateMktDepthL2 o = new UpdateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size));
	}

	@Override
	public void updateNewsBulletin(final int msgId, final int msgType, final String message, final String origExchange) {
		final UpdateNewsBulletin o = new UpdateNewsBulletin(msgId, msgType, message, origExchange);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange));
	}

	@Override
	public void updatePortfolio(final Contract contract, final int position, final double marketPrice, final double marketValue, final double averageCost,
			final double unrealizedPNL, final double realizedPNL, final String accountName) {
		final UpdatePortfolio o = new UpdatePortfolio(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName);
		ServiceLocator.commonInstance().getRuleService().sendEvent(StrategyImpl.BASE, o);
		logger.debug(EWrapperMsgGenerator.updatePortfolio(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName));
	}
}
