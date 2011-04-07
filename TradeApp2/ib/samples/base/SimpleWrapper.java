package samples.base;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class SimpleWrapper implements EWrapper {
	
	private static final int	   MAX_MESSAGES	   = 1000000;
	
	// main client
	private final EClientSocket	   m_client	       = new EClientSocket(this);
	
	// utils
	private long	               ts;
	private PrintStream	           m_output;
	private int	                   m_outputCounter	= 0;
	private int	                   m_messageCounter;
	private final SimpleDateFormat	m_df	       = new SimpleDateFormat(
	                                                       "HH:mm:ss");
	
	protected EClientSocket client() {
		return m_client;
	}
	
	protected SimpleWrapper() {
		/* */
		initNextOutput();
		SimpleWrapper.attachDisconnectHook(this);
	}
	
	public void connect() {
		connect(1);
	}
	
	public void connect(final int clientId) {
		String host = System.getProperty("jts.host");
		host = host != null ? host : "";
		m_client.eConnect(host, 7496, clientId);
	}
	
	public void disconnect() {
		m_client.eDisconnect();
	}
	
	/* ***************************************************************
	 * AnyWrapper
	 * ***************************************************************
	 */

	@Override
	public void error(final Exception e) {
		e.printStackTrace(m_output);
	}
	
	@Override
	public void error(final String str) {
		m_output.println(str);
	}
	
	@Override
	public void error(final int id, final int errorCode, final String errorMsg) {
		logIn("Error id=" + id + " code=" + errorCode + " msg=" + errorMsg);
	}
	
	@Override
	public void connectionClosed() {
		m_output.println("--------------------- CLOSED ---------------------");
	}
	
	/* ***************************************************************
	 * EWrapper
	 * ***************************************************************
	 */

	@Override
	public void tickPrice(final int tickerId, final int field,
	        final double price, final int canAutoExecute) {
		logIn("tickPrice");
	}
	
	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		logIn("tickSize");
	}
	
	@Override
	public void tickGeneric(final int tickerId, final int tickType,
	        final double value) {
		logIn("tickGeneric");
	}
	
	@Override
	public void tickString(final int tickerId, final int tickType,
	        final String value) {
		logIn("tickString");
	}
	
	@Override
	public void tickSnapshotEnd(final int tickerId) {
		logIn("tickSnapshotEnd");
	}
	
	@Override
	public void tickOptionComputation(final int tickerId, final int field,
	        final double impliedVol,
	        final double delta, final double optPrice, final double pvDividend,
	        final double gamma, final double vega, final double theta,
	        final double undPrice) {
		logIn("tickOptionComputation");
	}
	
	@Override
	public void tickEFP(final int tickerId, final int tickType,
	        final double basisPoints,
	        final String formattedBasisPoints, final double impliedFuture,
	        final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	        final double dividendsToExpiry) {
		logIn("tickEFP");
	}
	
	@Override
	public void orderStatus(final int orderId, final String status,
	        final int filled, final int remaining,
	        final double avgFillPrice, final int permId, final int parentId,
	        final double lastFillPrice,
	        final int clientId, final String whyHeld) {
		logIn("orderStatus");
	}
	
	@Override
	public void openOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		logIn("openOrder");
	}
	
	@Override
	public void openOrderEnd() {
		logIn("openOrderEnd");
	}
	
	@Override
	public void updateAccountValue(final String key, final String value,
	        final String currency, final String accountName) {
		logIn("updateAccountValue");
	}
	
	@Override
	public void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		logIn("updatePortfolio");
	}
	
	@Override
	public void updateAccountTime(final String timeStamp) {
		logIn("updateAccountTime");
	}
	
	@Override
	public void accountDownloadEnd(final String accountName) {
		logIn("accountDownloadEnd");
	}
	
	@Override
	public void nextValidId(final int orderId) {
		logIn("nextValidId");
	}
	
	@Override
	public void contractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		logIn("contractDetails");
	}
	
	@Override
	public void contractDetailsEnd(final int reqId) {
		logIn("contractDetailsEnd");
	}
	
	@Override
	public void bondContractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		logIn("bondContractDetails");
	}
	
	@Override
	public void execDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		logIn("execDetails");
	}
	
	@Override
	public void execDetailsEnd(final int reqId) {
		logIn("execDetailsEnd");
	}
	
	@Override
	public void updateMktDepth(final int tickerId, final int position,
	        final int operation, final int side, final double price,
	        final int size) {
		logIn("updateMktDepth");
	}
	
	@Override
	public void updateMktDepthL2(final int tickerId, final int position,
	        final String marketMaker, final int operation,
	        final int side, final double price, final int size) {
		logIn("updateMktDepthL2");
	}
	
	@Override
	public void updateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		logIn("updateNewsBulletin");
	}
	
	@Override
	public void managedAccounts(final String accountsList) {
		logIn("managedAccounts");
	}
	
	@Override
	public void receiveFA(final int faDataType, final String xml) {
		logIn("receiveFA");
	}
	
	@Override
	public void historicalData(final int reqId, final String date,
	        final double open, final double high, final double low,
	        final double close, final int volume, final int count,
	        final double WAP, final boolean hasGaps) {
		logIn("historicalData");
	}
	
	@Override
	public void scannerParameters(final String xml) {
		logIn("scannerParameters");
	}
	
	@Override
	public void scannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails, final String distance,
	        final String benchmark, final String projection,
	        final String legsStr) {
		logIn("scannerData");
	}
	
	@Override
	public void scannerDataEnd(final int reqId) {
		logIn("scannerDataEnd");
	}
	
	@Override
	public void realtimeBar(final int reqId, final long time,
	        final double open, final double high, final double low,
	        final double close,
	        final long volume, final double wap, final int count) {
		logIn("realtimeBar");
	}
	
	@Override
	public void currentTime(final long millis) {
		logIn("currentTime");
	}
	
	@Override
	public void fundamentalData(final int reqId, final String data) {
		logIn("fundamentalData");
	}
	
	@Override
	public void deltaNeutralValidation(final int reqId,
	        final UnderComp underComp) {
		logIn("deltaNeutralValidation");
	}
	
	/* ***************************************************************
	 * Helpers
	 * ***************************************************************
	 */

	protected void logIn(final String method) {
		m_messageCounter++;
		if (m_messageCounter == SimpleWrapper.MAX_MESSAGES) {
			m_output.close();
			initNextOutput();
			m_messageCounter = 0;
		}
		m_output.println("[W] > " + method);
	}
	
	protected void consoleMsg(final String str) {
		System.out.println(Thread.currentThread().getName() + " (" + tsStr() +
		        "): " + str);
	}
	
	protected String tsStr() {
		synchronized (m_df) {
			return m_df.format(new Date());
		}
	}
	
	protected void sleepSec(final int sec) {
		sleep(sec * 1000);
	}
	
	protected void sleep(final int msec) {
		try {
			Thread.sleep(msec);
		} catch (final Exception e) { /* noop */
		}
	}
	
	protected void swStart() {
		ts = System.currentTimeMillis();
	}
	
	protected void swStop() {
		final long dt = System.currentTimeMillis() - ts;
		m_output.println("[API]" + " Time=" + dt);
	}
	
	private void initNextOutput() {
		try {
			m_output = new PrintStream(new File("sysout_" + ++m_outputCounter +
			        ".log"));
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static void attachDisconnectHook(final SimpleWrapper ut) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				ut.disconnect();
			}
		});
	}
}
