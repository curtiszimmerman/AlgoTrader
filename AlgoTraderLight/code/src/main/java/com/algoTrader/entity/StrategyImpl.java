package com.algoTrader.entity;

import java.util.Collection;

import com.algoTrader.ServiceLocator;
import com.algoTrader.util.ConfigurationUtil;

public class StrategyImpl extends Strategy {

	private static final long serialVersionUID = -2271735085273721632L;

	private static double initialMarginMarkup = ConfigurationUtil.getBaseConfig().getDouble("initialMarginMarkup");

	public final static String BASE = "BASE";

	public double getCashBalanceDouble() {

		// sum of all transactions that belongs to this strategy
		double balance = 0.0;
		Collection<Transaction> transactions = getTransactions();
		for (Transaction transaction : transactions) {
			balance += transaction.getValueDouble();
		}

		// plus part of all cashFlows
		double cashFlows = 0.0;
		Transaction[] cashFlowTransactions = ServiceLocator.commonInstance().getLookupService().getAllCashFlows();
		for (Transaction transaction : cashFlowTransactions) {
			cashFlows += transaction.getValueDouble();
		}
		balance += (cashFlows * getAllocation());

		return balance;
	}

	public double getMaintenanceMarginDouble() {

		double margin = 0.0;
		Collection<Position> positions = getPositions();
		for (Position position : positions) {
			margin += position.getMaintenanceMarginDouble();
		}
		return margin;
	}

	@Override
	public double getInitialMarginDouble() {
		
		return initialMarginMarkup * getMaintenanceMarginDouble();
	}

	public double getAvailableFundsDouble() {

		return getNetLiqValueDouble() - getInitialMarginDouble();
	}

	public double getSecuritiesCurrentValueDouble() {

		double securitiesValue = 0.0;
		Collection<Position> positions = getPositions();
		for (Position position : positions) {
			securitiesValue += position.getMarketValueDouble();
		}
		return securitiesValue;
	}

	public double getNetLiqValueDouble() {

		return getCashBalanceDouble() + getSecuritiesCurrentValueDouble();
	}

	public boolean isBase() {
		return (BASE.equals(getName()));
	}
}