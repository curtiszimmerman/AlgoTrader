package com.algoTrader.service;

import org.apache.log4j.Logger;

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

	protected void handleCreateTransaction(String strategyName, Fill fill) throws Exception {

		Strategy strategy = getStrategyDao().findByName(strategyName);
		Security security = fill.getOrder().getSecurity();

		TransactionType transactionType = Side.BUY.equals(fill.getSide()) ? TransactionType.BUY : TransactionType.SELL;
		long quantity = Side.BUY.equals(fill.getSide()) ? fill.getQuantity() : -fill.getQuantity();

		Transaction transaction = new TransactionImpl();
		transaction.setDateTime(fill.getDateTime());
		transaction.setQuantity(quantity);
		transaction.setPrice(fill.getPrice());
		transaction.setType(transactionType);
		transaction.setSecurity(security);
		transaction.setCurrency(security.getSecurityFamily().getCurrency());
		transaction.setCommission(fill.getCommission());

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

		String logMessage = "executed transaction type: " + transactionType + " quantity: " + transaction.getQuantity() + " of " + security.getSymbol()
				+ " price: " + transaction.getPrice() + " commission: " + transaction.getCommission();

		logger.info(logMessage);
	}

	public static class CreateTransactionSubscriber {

		public void update(String strategyName, Fill fill) {

			long startTime = System.currentTimeMillis();
			logger.info("createTransaction start");

			ServiceLocator.commonInstance().getTransactionService().createTransaction(strategyName, fill);

			logger.info("createTransaction end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}

	}
}
