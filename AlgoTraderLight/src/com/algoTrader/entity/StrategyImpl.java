package com.algoTrader.entity;

import java.util.Collection;

import com.algoTrader.ServiceLocator;
import com.algoTrader.util.ConfigurationUtil;

public class StrategyImpl extends Strategy {
	
	private static final long	serialVersionUID	= -2271735085273721632L;
	
	private static double	   initialMarginMarkup	= ConfigurationUtil
	                                                       .getBaseConfig()
	                                                       .getDouble(
	                                                               "initialMarginMarkup");
	
	public final static String	BASE	           = "BASE";
	
	@Override
	@SuppressWarnings("unchecked")
	public double getCashBalanceDouble() {
		
		// sum of all transactions that belongs to this strategy
		double balance = 0.0;
		final Collection<Transaction> transactions = getTransactions();
		for (final Transaction transaction : transactions) {
			balance += transaction.getValueDouble();
		}
		
		// plus part of all cashFlows
		double cashFlows = 0.0;
		final Transaction[] cashFlowTransactions = ServiceLocator
		        .commonInstance().getLookupService().getAllCashFlows();
		for (final Transaction transaction : cashFlowTransactions) {
			cashFlows += transaction.getValueDouble();
		}
		balance += cashFlows * getAllocation();
		
		return balance;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public double getMaintenanceMarginDouble() {
		
		double margin = 0.0;
		final Collection<Position> positions = getPositions();
		for (final Position position : positions) {
			margin += position.getMaintenanceMarginDouble();
		}
		return margin;
	}
	
	@Override
	public double getInitialMarginDouble() {
		
		return StrategyImpl.initialMarginMarkup * getMaintenanceMarginDouble();
	}
	
	@Override
	public double getAvailableFundsDouble() {
		
		return getNetLiqValueDouble() - getInitialMarginDouble();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public double getSecuritiesCurrentValueDouble() {
		
		double securitiesValue = 0.0;
		final Collection<Position> positions = getPositions();
		for (final Position position : positions) {
			securitiesValue += position.getMarketValueDouble();
		}
		return securitiesValue;
	}
	
	@Override
	public double getNetLiqValueDouble() {
		
		return getCashBalanceDouble() + getSecuritiesCurrentValueDouble();
	}
	
	@Override
	public boolean isBase() {
		return StrategyImpl.BASE.equals(getName());
	}
}