package com.algoTrader.service;

import java.math.BigDecimal;

import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.Fill;
import com.algoTrader.entity.trade.FillImpl;
import com.algoTrader.entity.trade.LimitOrderInterface;
import com.algoTrader.entity.trade.MarketOrder;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.entity.trade.OrderStatus;
import com.algoTrader.enumeration.Side;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.RoundUtil;

public abstract class OrderServiceImpl extends OrderServiceBase {

	private static boolean simulation = ConfigurationUtil.getBaseConfig().getBoolean("simulation");

	protected void handleSendOrder(Order order) throws Exception {

		if (simulation) {

			// process the order internally
			sendInternalOrder(order);
		} else {

			Security security = order.getSecurity();
			Strategy strategy = order.getStrategy();

			// lock and initialize the security & strategy
			Session session = this.getSessionFactory().getCurrentSession();
			session.buildLockRequest(LockOptions.NONE).lock(security);
			session.buildLockRequest(LockOptions.NONE).lock(strategy);
			Hibernate.initialize(security);
			Hibernate.initialize(strategy);

			// in security and strategy are HibernateProxies convert them to objects
			if (security instanceof HibernateProxy) {
				HibernateProxy proxy = (HibernateProxy) security;
				security = (Security) proxy.getHibernateLazyInitializer().getImplementation();
				order.setSecurity(security);
			}

			if (strategy instanceof HibernateProxy) {
				HibernateProxy proxy = (HibernateProxy) strategy;
				strategy = (Strategy) proxy.getHibernateLazyInitializer().getImplementation();
				order.setStrategy(strategy);
			}

			// use broker specific functionality to execute the order
			sendExternalOrder(order);
		}
	}

	private void sendInternalOrder(Order order) {

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

		fill.setParentOrder(order);

		// create the transaction based on the fill
		getTransactionService().createTransaction(fill);
	}

	@Override
	protected void handleCancelOrder(int orderId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleModifyOrder(int orderId, Order order) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handlePropagateOrder(Order order) throws Exception {

		// send the order into the base engine to be correlated with fills
		getRuleService().sendEvent(StrategyImpl.BASE, order);

		// also send the order to the strategy that placed the order
		getRuleService().sendEvent(order.getStrategy().getName(), order);
	}

	@Override
	protected void handlePropagateOrderStatus(OrderStatus orderStatus) throws Exception {

		// send the fill to the strategy that placed the corresponding order
		getRuleService().sendEvent(orderStatus.getParentOrder().getStrategy().getName(), orderStatus);
	}

	public static class PropagateOrderStatusSubscriber {

		public void update(OrderStatus orderStatus) {

			ServiceLocator.serverInstance().getOrderService().propagateOrderStatus(orderStatus);
		}
	}
}
