package com.algoTrader.service;

import java.math.BigDecimal;

import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.Fill;
import com.algoTrader.entity.trade.FillImpl;
import com.algoTrader.entity.trade.LimitOrderInterface;
import com.algoTrader.entity.trade.MarketOrder;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.RoundUtil;

public abstract class OrderServiceImpl extends OrderServiceBase {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	protected void handleSendOrder(String strategyName, Order order) throws Exception {

		if (simulation) {

			// process the order internally
			sendInternalOrder(strategyName, order);
		} else {

			// use broker specific functionality to execute the order
			sendExternalOrder(strategyName, order);
		}
	}

	protected void handleSendOrder(String strategyName, Order order, int securityId) throws Exception {

		Security security = getSecurityDao().findByIdFetched(securityId);

		order.setSecurity(security);

		sendOrder(strategyName, order);
	}

	private void sendInternalOrder(String strategyName, Order order) {

		if (order.getQuantity() < 0) {
			throw new IllegalArgumentException("quantity has to be positive");
		}

		Security security = order.getSecurity();

		// create one fill per order
		Fill fill = new FillImpl();
		fill.setDateTime(DateUtil.getCurrentEPTime());
		fill.setSide(order.getSide());
		fill.setQuantity(order.getQuantity());

		// for MarketOrders get the price from the last tick
		if (order instanceof MarketOrder) {
			double entry = 0.0;
			if (Side.SELL.equals(order.getSide())) {
				entry = security.getLastBid().getPrice().doubleValue();

			} else if (Side.BUY.equals(order.getSide())) {
				entry = security.getLastAsk().getPrice().doubleValue();
			}
			fill.setPrice(RoundUtil.getBigDecimal(entry));

			// for limit orders get the price from the orderLimit
		} else if (order instanceof LimitOrderInterface) {
			LimitOrderInterface limitOrder = (LimitOrderInterface) order;
			fill.setPrice(limitOrder.getLimit());
		}

		// set the commission
		if (Side.SELL.equals(order.getSide()) || Side.BUY.equals(order.getSide())) {

			if (security.getSecurityFamily().getCommission() == null) {
				throw new RuntimeException("commission is undefined for " + security.getSymbol());
			}

			double commission = Math.abs(order.getQuantity() * security.getSecurityFamily().getCommission().doubleValue());
			fill.setCommission(RoundUtil.getBigDecimal(commission));
		} else {
			fill.setCommission(new BigDecimal(0));
		}

		fill.setOrder(order);

		// create the transaction based on the fill
		getTransactionService().createTransaction(strategyName, fill);
	}
}
