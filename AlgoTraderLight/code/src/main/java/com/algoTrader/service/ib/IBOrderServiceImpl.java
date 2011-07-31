package com.algoTrader.service.ib;

import org.apache.log4j.Logger;

import com.algoTrader.entity.trade.LimitOrderInterface;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.Contract;

public class IBOrderServiceImpl extends IBOrderServiceBase {

	private static IBClient client;
	private static Logger logger = MyLogger.getLogger(IBOrderServiceImpl.class.getName());
	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	public IBOrderServiceImpl() {
		super();
		if (!simulation) {
			client = IBClient.getInstance();
		}
	}

	protected void handleSendExternalOrder(Order order) throws Exception {

		if (!client.getIbAdapter().getState().equals(ConnectionState.READY)) {
			logger.error("transaction cannot be executed, because IB is not connected");
			return;
		}

		Contract contract = IBUtil.getContract(order.getSecurity());

		com.ib.client.Order ibOrder = new com.ib.client.Order();
		ibOrder.m_action = order.getSide().getValue();
		ibOrder.m_totalQuantity = (int) order.getQuantity();
		ibOrder.m_orderType = IBUtil.getIBOrderType(order);
		ibOrder.m_transmit = true;

		//set the limit price if order is a limit order or stop limit order
		if (order instanceof LimitOrderInterface) {
	        ibOrder.m_lmtPrice = ((LimitOrderInterface)order).getLimit().doubleValue();
		}
		
		int orderId = RequestIDGenerator.singleton().getNextOrderId();
		order.setNumber(orderId);

		// progapate the order to all corresponding esper engines
		propagateOrder(order);

		// place the order through IBClient
		client.placeOrder(orderId, contract, ibOrder);

		logger.info("placed order: " + order);
	}
}
