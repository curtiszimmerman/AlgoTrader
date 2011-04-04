
package TradeApp.IB.copy;

import TradeApp.CEP.CEPMan;
import TradeApp.IB.Events.AccountDownloadEnd;
import TradeApp.IB.Events.BondContractDetails;
import TradeApp.IB.Events.ConnectionClosed;
import TradeApp.IB.Events.ContractDetailsCommon;
import TradeApp.IB.Events.ContractDetailsEnd;
import TradeApp.IB.Events.CurrentTime;
import TradeApp.IB.Events.DeltaNeutralValidation;
import TradeApp.IB.Events.ExecDetails;
import TradeApp.IB.Events.ExecDetailsEnd;
import TradeApp.IB.Events.FundamentalData;
import TradeApp.IB.Events.HistoricalData;
import TradeApp.IB.Events.IBErrors;
import TradeApp.IB.Events.ManagedAccounts;
import TradeApp.IB.Events.NextValidId;
import TradeApp.IB.Events.OpenOrder;
import TradeApp.IB.Events.OpenOrderEnd;
import TradeApp.IB.Events.OrderStatus;
import TradeApp.IB.Events.RealtimeBar;
import TradeApp.IB.Events.ReceiveFA;
import TradeApp.IB.Events.ScannerData;
import TradeApp.IB.Events.ScannerDataEnd;
import TradeApp.IB.Events.ScannerParameters;
import TradeApp.IB.Events.TickEFP;
import TradeApp.IB.Events.TickGeneric;
import TradeApp.IB.Events.TickOptionComputation;
import TradeApp.IB.Events.TickPrice;
import TradeApp.IB.Events.TickSize;
import TradeApp.IB.Events.TickSnapshotEnd;
import TradeApp.IB.Events.TickString;
import TradeApp.IB.Events.UpdateAccountTime;
import TradeApp.IB.Events.UpdateAccountValue;
import TradeApp.IB.Events.UpdateMktDepth;
import TradeApp.IB.Events.UpdateMktDepthL2;
import TradeApp.IB.Events.UpdateNewsBulletin;
import TradeApp.IB.Events.UpdatePortfolio;
import TradeApp.Util.Loggable;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class IBAdapter implements EWrapper, Loggable {
	private static IBClient		eSoc;
	private static IBAdapter	ibAdp;
	
	public static IBAdapter getAdapter() {
		if (IBAdapter.ibAdp == null) IBAdapter.ibAdp = new IBAdapter();
		
		return IBAdapter.ibAdp;
	}
	
	public static IBClient getClient() {
		return IBAdapter.eSoc;
	}
	
	private IBAdapter() {
		IBAdapter.ibAdp = this;
		IBAdapter.eSoc = IBClient.getIBClient();
		
		CEPMan.getCEPMan().start(
				new String[] { "TradeApp.IB.Event", "TradeApp.Event" });
		
	}
	
	@Override
	public void accountDownloadEnd(final String accountName) {
		CEPMan.getCEPMan().pumpEvent(new AccountDownloadEnd(accountName));
	}
	
	@Override
	public void bondContractDetails(final int reqId,
			final ContractDetails contractDetails) {
		CEPMan.getCEPMan().pumpEvent(
				new BondContractDetails(reqId, contractDetails));
	}
	
	@Override
	public void connectionClosed() {
		CEPMan.getCEPMan().pumpEvent(new ConnectionClosed());
	}
	
	@Override
	public void contractDetails(final int reqId,
			final ContractDetails contractDetails) {
		CEPMan.getCEPMan().pumpEvent(
				new ContractDetailsCommon(reqId, contractDetails));
	}
	
	@Override
	public void contractDetailsEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ContractDetailsEnd(reqId));
	}
	
	@Override
	public void currentTime(final long time) {
		CEPMan.getCEPMan().pumpEvent(new CurrentTime(time));
	}
	
	@Override
	public void deltaNeutralValidation(final int reqId, final UnderComp underComp) {
		CEPMan.getCEPMan()
				.pumpEvent(new DeltaNeutralValidation(reqId, underComp));
	}
	
	@Override
	public void error(final Exception e) {
		final IBErrors ex = new IBErrors(new IBErrors.EExp(e));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void error(final int id, final int errorCode, final String errorMsg) {
		final IBErrors ex = new IBErrors(new IBErrors.ECode(id, errorCode,
				errorMsg));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void error(final String str) {
		final IBErrors ex = new IBErrors(new IBErrors.EStr(str));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void execDetails(final int reqId, final Contract contract,
			final Execution execution) {
		CEPMan.getCEPMan().pumpEvent(new ExecDetails(reqId, contract, execution));
	}
	
	@Override
	public void execDetailsEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ExecDetailsEnd(reqId));
	}
	
	@Override
	public void fundamentalData(final int reqId, final String data) {
		CEPMan.getCEPMan().pumpEvent(new FundamentalData(reqId, data));
	}
	
	@Override
	public void historicalData(final int reqId, final String date,
			final double open, final double high, final double low,
			final double close, final int volume, final int count,
			final double WAP, final boolean hasGaps) {
		CEPMan.getCEPMan().pumpEvent(
				new HistoricalData(reqId, date, open, high, low, close, volume,
						count, WAP, hasGaps));
	}
	
	@Override
	public void managedAccounts(final String accountsList) {
		CEPMan.getCEPMan().pumpEvent(new ManagedAccounts(accountsList));
	}
	
	private static int	orderId	= 0;
	
	public synchronized static int getNextValidId() {
		return IBAdapter.orderId;
	}
	
	@Override
	public void nextValidId(final int orderId) {
		IBAdapter.orderId = orderId;
		CEPMan.getCEPMan().pumpEvent(new NextValidId(orderId));
	}
	
	@Override
	public void openOrder(final int orderId, final Contract contract,
			final Order order, final OrderState orderState) {
		CEPMan.getCEPMan().pumpEvent(
				new OpenOrder(orderId, contract, order, orderState));
	}
	
	@Override
	public void openOrderEnd() {
		CEPMan.getCEPMan().pumpEvent(new OpenOrderEnd());
	}
	
	@Override
	public void orderStatus(final int orderId, final String status,
			final int filled, final int remaining, final double avgFillPrice,
			final int permId, final int parentId, final double lastFillPrice,
			final int clientId, final String whyHeld) {
		CEPMan.getCEPMan().pumpEvent(
				new OrderStatus(orderId, status, filled, remaining, avgFillPrice,
						permId, parentId, lastFillPrice, clientId, whyHeld));
	}
	
	@Override
	public void realtimeBar(final int reqId, final long time, final double open,
			final double high, final double low, final double close,
			final long volume, final double wap, final int count) {
		CEPMan.getCEPMan().pumpEvent(
				new RealtimeBar(reqId, time, open, high, low, close, volume, wap,
						count));
	}
	
	@Override
	public void receiveFA(final int faDataType, final String xml) {
		CEPMan.getCEPMan().pumpEvent(new ReceiveFA(faDataType, xml));
	}
	
	@Override
	public void scannerData(final int reqId, final int rank,
			final ContractDetails contractDetails, final String distance,
			final String benchmark, final String projection, final String legsStr) {
		CEPMan.getCEPMan().pumpEvent(
				new ScannerData(reqId, rank, contractDetails, distance, benchmark,
						projection, legsStr));
	}
	
	@Override
	public void scannerDataEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ScannerDataEnd(reqId));
	}
	
	@Override
	public void scannerParameters(final String xml) {
		CEPMan.getCEPMan().pumpEvent(new ScannerParameters(xml));
	}
	
	@Override
	public void tickEFP(final int tickerId, final int tickType,
			final double basisPoints, final String formattedBasisPoints,
			final double impliedFuture, final int holdDays,
			final String futureExpiry, final double dividendImpact,
			final double dividendsToExpiry) {
		CEPMan.getCEPMan().pumpEvent(
				new TickEFP(tickerId, tickType, basisPoints, formattedBasisPoints,
						impliedFuture, holdDays, futureExpiry, dividendImpact,
						dividendsToExpiry));
	}
	
	@Override
	public void tickGeneric(final int tickerId, final int tickType,
			final double value) {
		CEPMan.getCEPMan().pumpEvent(new TickGeneric(tickerId, tickType, value));
	}
	
	@Override
	public void tickOptionComputation(final int tickerId, final int field,
			final double impliedVol, final double delta, final double optPrice,
			final double pvDividend, final double gamma, final double vega,
			final double theta, final double undPrice) {
		CEPMan.getCEPMan().pumpEvent(
				new TickOptionComputation(tickerId, field, impliedVol, delta,
						optPrice, pvDividend, gamma, vega, theta, undPrice));
	}
	
	@Override
	public void tickPrice(final int tickerId, final int field,
			final double price, final int canAutoExecute) {
		CEPMan.getCEPMan().pumpEvent(
				new TickPrice(tickerId, field, price, canAutoExecute));
	}
	
	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		CEPMan.getCEPMan().pumpEvent(new TickSize(tickerId, field, size));
	}
	
	@Override
	public void tickSnapshotEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new TickSnapshotEnd(reqId));
	}
	
	@Override
	public void tickString(final int tickerId, final int tickType,
			final String value) {
		CEPMan.getCEPMan().pumpEvent(new TickString(tickerId, tickType, value));
	}
	
	@Override
	public void updateAccountTime(final String timeStamp) {
		CEPMan.getCEPMan().pumpEvent(new UpdateAccountTime(timeStamp));
	}
	
	@Override
	public void updateAccountValue(final String key, final String value,
			final String currency, final String accountName) {
		CEPMan.getCEPMan().pumpEvent(
				new UpdateAccountValue(key, value, currency, accountName));
	}
	
	@Override
	public void updateMktDepth(final int tickerId, final int position,
			final int operation, final int side, final double price, final int size) {
		CEPMan.getCEPMan()
				.pumpEvent(
						new UpdateMktDepth(tickerId, position, operation, side,
								price, size));
	}
	
	@Override
	public void updateMktDepthL2(final int tickerId, final int position,
			final String marketMaker, final int operation, final int side,
			final double price, final int size) {
		CEPMan.getCEPMan().pumpEvent(
				new UpdateMktDepthL2(tickerId, position, marketMaker, operation,
						side, price, size));
	}
	
	@Override
	public void updateNewsBulletin(final int msgId, final int msgType,
			final String message, final String origExchange) {
		CEPMan.getCEPMan().pumpEvent(
				new UpdateNewsBulletin(msgId, msgType, message, origExchange));
	}
	
	@Override
	public void updatePortfolio(final Contract contract, final int position,
			final double marketPrice, final double marketValue,
			final double averageCost, final double unrealizedPNL,
			final double realizedPNL, final String accountName) {
		CEPMan.getCEPMan().pumpEvent(
				new UpdatePortfolio(contract, position, marketPrice, marketValue,
						averageCost, unrealizedPNL, realizedPNL, accountName));
	}
}
