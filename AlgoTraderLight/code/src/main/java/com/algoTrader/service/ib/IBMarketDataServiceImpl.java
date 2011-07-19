package com.algoTrader.service.ib;

import org.apache.log4j.Logger;

import com.algoTrader.entity.security.Security;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.Contract;

public class IBMarketDataServiceImpl extends IBMarketDataServiceBase {

	private static Logger logger = MyLogger.getLogger(IBMarketDataServiceImpl.class.getName());

	private static IBClient client;
	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	public IBMarketDataServiceImpl() {
		if (!simulation) {
			client = IBClient.getInstance();
		}
	}

	@Override
	protected int handlePutOnExternalWatchlist(Security security) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.CONNECTED) 
				&& !client.getIbAdapter().getState().equals(ConnectionState.READY)) {
			logger.error("IB is not connected");
			return 0;
		}
		
		Contract contract = IBUtil.getContract(security);
		int tickerId = RequestIDGenerator.singleton().getNextRequestId();

		client.reqMktData(tickerId, contract, "", false);

		logger.debug("requested market data for : " + security.getSymbol());

		return tickerId;
	}

	@Override
	protected void handleRemoveFromExternalWatchlist(Security security) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.CONNECTED) 
				&& !client.getIbAdapter().getState().equals(ConnectionState.READY)) {
			logger.error("IB is not connected");
			return;
		}

		int tickerId = security.getId();
		client.cancelMktData(tickerId);
	}
}
