package com.algoTrader.service;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Order;
import com.algoTrader.entity.OrderImpl;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.PositionImpl;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.Tick;
import com.algoTrader.entity.Transaction;
import com.algoTrader.entity.TransactionImpl;
import com.algoTrader.enumeration.OrderStatus;
import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.RoundUtil;
import com.algoTrader.vo.OrderVO;

public class TransactionServiceImpl extends TransactionServiceBase {

	private static Logger logger = MyLogger.getLogger(TransactionServiceImpl.class.getName());

	@SuppressWarnings("unchecked")
	protected Order handleExecuteTransaction(String strategyName, OrderVO orderVO) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);

		// construct a order-entity from the orderVO
		Order order = orderVOToEntity(orderVO);

		Security security = order.getSecurity();
		TransactionType transactionType = order.getTransactionType();
		long requestedQuantity = order.getRequestedQuantity();

		if (requestedQuantity <= 0) {
			throw new IllegalArgumentException("quantity must be greater than 0");
		}

		executeInternalTransaction(order);

		Collection<Transaction> transactions = order.getTransactions();
		long totalQuantity = 0;
		double totalPrice = 0.0;
		double totalCommission = 0.0;
		
		for (Transaction transaction : transactions) {

			transaction.setType(transactionType);
			transaction.setSecurity(security);
			transaction.setCurrency(security.getSecurityFamily().getCurrency());
			
			// Strategy
			transaction.setStrategy(strategy);
			strategy.getTransactions().add(transaction);

			// Position
			Position position = getPositionDao().findBySecurityAndStrategy(security.getId(), strategyName);
			if (position == null) {

				position = new PositionImpl();
				position.setQuantity(transaction.getQuantity());

				position.setExitValue(null);
				position.setMaintenanceMargin(null);

				position.setSecurity(security);
				security.getPositions().add(position);

				position.getTransactions().add(transaction);
				transaction.setPosition(position);

				position.setStrategy(strategy);
				strategy.getPositions().add(position);

				getPositionDao().create(position);

			} else {
				
				position.setQuantity(position.getQuantity() + transaction.getQuantity());

				if (!position.isOpen()) {
					position.setExitValue(null);
					position.setMaintenanceMargin(null);
				}

				position.getTransactions().add(transaction);
				transaction.setPosition(position);

				getPositionDao().update(position);
			}

			getTransactionDao().create(transaction);
			getStrategyDao().update(strategy);
			getSecurityDao().update(security);
			
			totalQuantity += transaction.getQuantity();
			totalPrice += transaction.getPrice().doubleValue() * transaction.getQuantity();
			totalCommission += transaction.getCommission().doubleValue();
			
			String logMessage = "executed transaction type: " + transactionType + " quantity: " + transaction.getQuantity() +
					" of " + security.getSymbol() + " price: " + transaction.getPrice() + " commission: " + transaction.getCommission();
			
			logger.info(logMessage);
		}

		return order;
	}

	@SuppressWarnings("unchecked")
	private void executeInternalTransaction(Order order) {

		Transaction transaction = new TransactionImpl();
		transaction.setDateTime(DateUtil.getCurrentEPTime());

		Security security = order.getSecurity();
		Tick tick = security.getLastTick();

		if (TransactionType.SELL.equals(order.getTransactionType())) {

			double bid = tick.getBid().doubleValue();

			transaction.setPrice(RoundUtil.getBigDecimal(bid));
			transaction.setQuantity(-Math.abs(order.getRequestedQuantity()));

		} else if (TransactionType.BUY.equals(order.getTransactionType())) {

			double ask = tick.getAsk().doubleValue();

			transaction.setPrice(RoundUtil.getBigDecimal(ask));
			transaction.setQuantity(Math.abs(order.getRequestedQuantity()));

		}

		if (TransactionType.SELL.equals(order.getTransactionType()) || TransactionType.BUY.equals(order.getTransactionType())) {
			
			if(security.getSecurityFamily().getCommission() == null) {
				throw new RuntimeException("commission is undefined for " + security.getSymbol());
			}
			
			double commission = Math.abs(order.getRequestedQuantity() * security.getSecurityFamily().getCommission().doubleValue());
			transaction.setCommission(RoundUtil.getBigDecimal(commission));
		} else {
			transaction.setCommission(new BigDecimal(0));
		}

		order.setStatus(OrderStatus.AUTOMATIC);
		order.getTransactions().add(transaction);
	}

	/**
	 * implemented here because Order is nonPersistent
	 */
	private Order orderVOToEntity(OrderVO orderVO) {
	
		Order order = new OrderImpl();
		order.setStrategy(getStrategyDao().findByName(orderVO.getStrategyName()));
		order.setSecurity(getSecurityDao().load(orderVO.getSecurityId()));
		order.setRequestedQuantity(orderVO.getRequestedQuantity());
		order.setTransactionType(orderVO.getTransactionType());
	
		return order;
	}
}