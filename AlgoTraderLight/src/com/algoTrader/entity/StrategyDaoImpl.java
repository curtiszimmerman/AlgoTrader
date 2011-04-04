package com.algoTrader.entity;

import java.util.Collection;

import com.algoTrader.util.ConfigurationUtil;

public class StrategyDaoImpl extends StrategyDaoBase {

	private static double initialMarginMarkup = ConfigurationUtil.getBaseConfig().getDouble("initialMarginMarkup");

	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioCashBalanceDouble() throws Exception {

		double cashBalance = 0.0;
		Collection<Transaction> transactions = getTransactionDao().loadAll();
		for (Transaction transaction : transactions) {
			cashBalance += transaction.getValueDouble();
		}
		return cashBalance;
	}

	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioSecuritiesCurrentValueDouble() throws Exception {

		double securitiesValue = 0.0;
		Collection<Position> positions = getPositionDao().findOpenPositions();
		for (Position position : positions) {
			securitiesValue += position.getMarketValueDouble();
		}
		return securitiesValue;
	}

	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioMaintenanceMarginDouble() throws Exception {

		double margin = 0.0;
		Collection<Position> positions = getPositionDao().findOpenPositions();
		for (Position position : positions) {
			margin += position.getMaintenanceMarginDouble();
		}
		return margin;
	}

	protected double handleGetPortfolioInitialMarginDouble() {

		return initialMarginMarkup * getPortfolioMaintenanceMarginDouble();
	}

	protected double handleGetPortfolioNetLiqValueDouble() throws Exception {
	
		return getPortfolioCashBalanceDouble() + getPortfolioSecuritiesCurrentValueDouble();
	}
	
	protected double handleGetPortfolioAvailableFundsDouble() throws Exception {

		return getPortfolioNetLiqValueDouble() - getPortfolioInitialMarginDouble();
	}
}