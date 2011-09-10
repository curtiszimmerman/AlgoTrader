package com.algoTrader.service.ib;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;

import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.security.Security;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.Contract;

public class IBMarketDataServiceImpl extends IBMarketDataServiceBase implements DisposableBean {

	private static Logger logger = MyLogger.getLogger(IBMarketDataServiceImpl.class.getName());

	private static IBClient client;
	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	public IBMarketDataServiceImpl() {

		if (!simulation) {
			client = IBClient.getDefaultInstance();
		}
	}

	@Override
	protected void handleInitWatchlist() {

		super.handleInitWatchlist();

		client.getIbAdapter().setState(ConnectionState.SUBSCRIBED);
		client.getIbAdapter().setRequested(true);
	}

	@Override
	protected int handlePutOnExternalWatchlist(Security security) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.READY) && !client.getIbAdapter().getState().equals(ConnectionState.SUBSCRIBED)) {
			throw new IBMarketDataServiceException("IB is not ready for market data subscription on " + security.getSymbol());
		}

		Contract contract = IBUtil.getContract(security);
		int tickerId = RequestIDGenerator.singleton().getNextRequestId();

		client.reqMktData(tickerId, contract, "", false);

		logger.debug("requested market data for : " + security.getSymbol());

		return tickerId;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void handleRemoveFromExternalWatchlist(Security security) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.SUBSCRIBED)) {
			throw new IBMarketDataServiceException("IB ist not subscribed, security cannot be unsubscribed " + security.getSymbol());
		}

		// get the tickerId by querying the TickWindow
		List<Map> events = getRuleService().executeQuery(StrategyImpl.BASE, "select tickerId from TickWindow where security.id = " + security.getId());
        
		if (events.size() == 0) {
			Integer tickerId = (Integer) events.get(0).get("tickerId");
            client.cancelMktData(tickerId);
			logger.debug("cancelled market data for : " + security.getSymbol());
		} else {
			throw new IBMarketDataServiceException("tickerId for security " + security + " was not found");
        }
	}

	@Override
	public void destroy() throws Exception {

		if (client != null && client.isConnected()) {
			client.disconnect();
		}
	}
}
