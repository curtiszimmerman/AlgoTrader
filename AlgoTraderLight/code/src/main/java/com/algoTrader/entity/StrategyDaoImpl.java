package com.algoTrader.entity;

import java.util.Collection;

import com.algoTrader.util.ConfigurationUtil;

public class StrategyDaoImpl extends StrategyDaoBase {
	
	private static double	initialMarginMarkup	= ConfigurationUtil
	                                                    .getBaseConfig()
	                                                    .getDouble(
	                                                            "initialMarginMarkup");
	
	@Override
	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioCashBalanceDouble() throws Exception {
		
		double cashBalance = 0.0;
		final Collection<Transaction> transactions = getTransactionDao()
		        .loadAll();
		for (final Transaction transaction : transactions) {
			cashBalance += transaction.getValueDouble();
		}
		return cashBalance;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioSecuritiesCurrentValueDouble()
	        throws Exception {
		
		double securitiesValue = 0.0;
		final Collection<Position> positions = getPositionDao()
		        .findOpenPositions();
		for (final Position position : positions) {
			securitiesValue += position.getMarketValueDouble();
		}
		return securitiesValue;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected double handleGetPortfolioMaintenanceMarginDouble()
	        throws Exception {
		
		double margin = 0.0;
		final Collection<Position> positions = getPositionDao()
		        .findOpenPositions();
		for (final Position position : positions) {
			margin += position.getMaintenanceMarginDouble();
		}
		return margin;
	}
	
	@Override
	protected double handleGetPortfolioInitialMarginDouble() {
		
		return StrategyDaoImpl.initialMarginMarkup *
		        getPortfolioMaintenanceMarginDouble();
	}
	
	@Override
	protected double handleGetPortfolioNetLiqValueDouble() throws Exception {
		
		return getPortfolioCashBalanceDouble() +
		        getPortfolioSecuritiesCurrentValueDouble();
	}
	
	@Override
	protected double handleGetPortfolioAvailableFundsDouble() throws Exception {
		
		return getPortfolioNetLiqValueDouble() -
		        getPortfolioInitialMarginDouble();
	}
}