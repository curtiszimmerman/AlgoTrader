package com.algoTrader.service;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.PositionImpl;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.Transaction;
import com.algoTrader.entity.TransactionImpl;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.trade.Fill;
import com.algoTrader.enumeration.Side;
import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.util.MyLogger;

public class TransactionServiceImpl extends TransactionServiceBase {

	private static Logger logger = MyLogger.getLogger(TransactionServiceImpl.class.getName());

	protected void handleCreateTransaction(Fill fill) throws Exception {

		Strategy strategy = fill.getParentOrder().getStrategy();
		Security security = fill.getParentOrder().getSecurity();

		// lock and initialize the security & strategy
		Session session = this.getSessionFactory().getCurrentSession();
		session.buildLockRequest(LockOptions.NONE).lock(security);
		session.buildLockRequest(LockOptions.NONE).lock(strategy);
		Hibernate.initialize(security);
		Hibernate.initialize(strategy);

		TransactionType transactionType = Side.BUY.equals(fill.getSide()) ? TransactionType.BUY : TransactionType.SELL;
		long quantity = Side.BUY.equals(fill.getSide()) ? fill.getQuantity() : -fill.getQuantity();

		Transaction transaction = new TransactionImpl();
		transaction.setDateTime(fill.getDateTime());
		transaction.setQuantity(quantity);
		transaction.setPrice(fill.getPrice());
		transaction.setType(transactionType);
		transaction.setSecurity(security);
		transaction.setStrategy(strategy);
		transaction.setCurrency(security.getSecurityFamily().getCurrency());
		transaction.setCommission(fill.getCommission());

		// Strategy
		strategy.getTransactions().add(transaction);

		// Position
		Position position = getPositionDao().findBySecurityAndStrategy(security.getId(), strategy.getName());
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

		// progapate the order to all corresponding esper engines
		propagateTransaction(transaction);

		String logMessage = "executed transaction type: " + transactionType + " quantity: " + transaction.getQuantity() + " of " + security.getSymbol()
				+ " price: " + transaction.getPrice() + " commission: " + transaction.getCommission();

		logger.info(logMessage);
	}

	protected void handlePropagateTransaction(Transaction transaction) {

		// also send the transaction to the corresponding strategy
		getRuleService().sendEvent(transaction.getStrategy().getName(), transaction);
	}

	@Override
	protected void handlePropagateFill(Fill fill) throws Exception {
	
		// send the fill to the strategy that placed the corresponding order
		getRuleService().sendEvent(fill.getParentOrder().getStrategy().getName(), fill);
	}

	public static class CreateTransactionSubscriber {

		public void update(Fill fill) {

			long startTime = System.currentTimeMillis();
			logger.info("createTransaction start");

			ServiceLocator.commonInstance().getTransactionService().createTransaction(fill);

			logger.info("createTransaction end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}

	}

	public static class PropagateFillSubscriber {

		public void update(Fill fill) {

			ServiceLocator.serverInstance().getTransactionService().propagateFill(fill);
		}
	}
}
