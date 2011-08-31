package com.algoTrader.service.ib;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Position;
import com.algoTrader.entity.trade.LimitOrderInterface;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.entity.trade.StopOrderInterface;
import com.algoTrader.enumeration.ConnectionState;
import com.algoTrader.enumeration.Side;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.ib.client.Contract;

public class IBOrderServiceImpl extends IBOrderServiceBase {

	private static boolean faEnabled = ConfigurationUtil.getBaseConfig().getBoolean("ib.faEnabled");
	private static String faAccount = ConfigurationUtil.getBaseConfig().getString("ib.faAccount");
	private static String group = ConfigurationUtil.getBaseConfig().getString("ib.group");
	private static String openMethod = ConfigurationUtil.getBaseConfig().getString("ib.openMethod");
	private static String closeMethod = ConfigurationUtil.getBaseConfig().getString("ib.closeMethod");

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

		int orderNumber = RequestIDGenerator.singleton().getNextOrderId();
		order.setNumber(orderNumber);
		sendOrModifyOrder(order);
	}

	@Override
	protected void handleModifyExternalOrder(Order order) throws Exception {

		sendOrModifyOrder(order);
	}

	@Override
	protected void handleCancelExternalOrder(int orderNumber) throws Exception {
	
		if (!(client.getIbAdapter().getState().equals(ConnectionState.READY) || client.getIbAdapter().getState().equals(ConnectionState.SUBSCRIBED))) {
			logger.error("transaction cannot be executed, because IB is not connected");
			return;
		}
	
		client.cancelOrder(orderNumber);

		logger.info("requested order cancallation for order number: " + orderNumber);
	}

	/**
	 * helper method to be used in both sendorder and modifyorder.
	 * @throws Exception
	 */
	private void sendOrModifyOrder(Order order) throws Exception {

		if (!(client.getIbAdapter().getState().equals(ConnectionState.READY) || client.getIbAdapter().getState().equals(ConnectionState.SUBSCRIBED))) {
			logger.error("transaction cannot be executed, because IB is not connected");
			return;
		}

		Contract contract = IBUtil.getContract(order.getSecurity());

		com.ib.client.Order ibOrder = new com.ib.client.Order();
		ibOrder.m_action = order.getSide().getValue();
		ibOrder.m_orderType = IBUtil.getIBOrderType(order);
		ibOrder.m_transmit = true;

		// handling for financial advisor accounts
		if (faEnabled) {

			long existingQuantity = 0;
			for (Position position : order.getSecurity().getPositions()) {
				existingQuantity += position.getQuantity();
			}

			// evaluate weather the transaction is opening or closing
			boolean opening = false;
			if (existingQuantity > 0 && Side.SELL.equals(order.getSide())) {
				opening = false;
			} else if (existingQuantity <= 0 && Side.SELL.equals(order.getSide())) {
				opening = true;
			} else if (existingQuantity < 0 && Side.BUY.equals(order.getSide())) {
				opening = false;
			} else if (existingQuantity >= 0 && Side.BUY.equals(order.getSide())) {
				opening = true;
			}

			ibOrder.m_faGroup = group;

			if (opening) {

				// open by specifying the actual quantity
				ibOrder.m_faMethod = openMethod;
				ibOrder.m_totalQuantity = (int) order.getQuantity();

			} else {

				// reduce by percentage
				ibOrder.m_faMethod = closeMethod;
				ibOrder.m_faPercentage = "-" + Math.abs(order.getQuantity() * 100 / (existingQuantity - order.getQuantity()));
			}
		} else {

			ibOrder.m_totalQuantity = (int) order.getQuantity();

			// if fa is disabled, it is still possible to work with an IB FA setup if a single client account is specified
			if (faAccount != null) {
				ibOrder.m_account = faAccount;
			}
		}

		//set the limit price if order is a limit order or stop limit order
		if (order instanceof LimitOrderInterface) {
			ibOrder.m_lmtPrice = ((LimitOrderInterface) order).getLimit().doubleValue();
		}

		//set the stop price if order is a stop order or stop limit order
		if (order instanceof StopOrderInterface) {
			ibOrder.m_auxPrice = ((StopOrderInterface) order).getStop().doubleValue();
		}

		// progapate the order to all corresponding esper engines
		propagateOrder(order);

		// place the order through IBClient
		client.placeOrder(order.getNumber(), contract, ibOrder);

		logger.info("placed or modified order: " + order);
	}
}
