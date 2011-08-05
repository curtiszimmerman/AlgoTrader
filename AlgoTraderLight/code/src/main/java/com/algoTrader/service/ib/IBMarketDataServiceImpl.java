package com.algoTrader.service.ib;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.algoTrader.entity.StrategyImpl;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void handleRemoveFromExternalWatchlist(Security security) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.CONNECTED)
				&& !client.getIbAdapter().getState().equals(ConnectionState.READY)) {
			logger.error("IB is not connected");
			return;
		}

		List<Map> events = getRuleService().executeQuery(StrategyImpl.BASE, "select tickerId from TickWindow where security.id = " + security.getId());
        
		if (events.size() == 0) {
			Integer tickerId = (Integer) events.get(0).get("tickerId");
            client.cancelMktData(tickerId);
			logger.debug("cancelled market data for : " + security.getSymbol());
		} else {
			throw new IBMarketDataServiceException("tickerId for security " + security + " was not found");
        }
	}
}
