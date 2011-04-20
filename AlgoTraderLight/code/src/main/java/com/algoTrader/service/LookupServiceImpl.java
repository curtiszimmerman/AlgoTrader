package com.algoTrader.service;

import java.util.List;

import com.algoTrader.entity.Position;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.SecurityFamily;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.Tick;
import com.algoTrader.entity.Transaction;
import com.algoTrader.util.DateUtil;
import com.algoTrader.vo.PortfolioValueVO;

@SuppressWarnings("unchecked")
public class LookupServiceImpl extends LookupServiceBase {
	
	@Override
	protected Security handleGetSecurity(final int id)
	        throws java.lang.Exception {
		
		return getSecurityDao().load(id);
	}
	
	@Override
	protected Security handleGetSecurityByIsin(final String isin)
	        throws Exception {
		
		return getSecurityDao().findByIsin(isin);
	}
	
	@Override
	protected Security handleGetSecurityFetched(final int securityId)
	        throws Exception {
		
		return getSecurityDao().findByIdFetched(securityId);
	}
	
	@Override
	protected Strategy handleGetStrategy(final int id)
	        throws java.lang.Exception {
		
		return getStrategyDao().load(id);
	}
	
	@Override
	protected Strategy handleGetStrategyByName(final String name)
	        throws Exception {
		
		return getStrategyDao().findByName(name);
	}
	
	@Override
	protected Strategy handleGetStrategyByNameFetched(final String name)
	        throws Exception {
		
		return getStrategyDao().findByNameFetched(name);
	}
	
	@Override
	protected SecurityFamily handleGetSecurityFamily(final int id)
	        throws Exception {
		
		return getSecurityFamilyDao().load(id);
	}
	
	@Override
	protected Position handleGetPosition(final int id)
	        throws java.lang.Exception {
		
		return getPositionDao().load(id);
	}
	
	@Override
	protected Position handleGetPositionFetched(final int id) throws Exception {
		
		return getPositionDao().findByIdFetched(id);
	}
	
	@Override
	protected Position handleGetPositionBySecurityAndStrategy(
	        final int securityId, final String strategyName) throws Exception {
		
		return getPositionDao().findBySecurityAndStrategy(securityId,
		        strategyName);
	}
	
	@Override
	protected Transaction handleGetTransaction(final int id)
	        throws java.lang.Exception {
		
		return getTransactionDao().load(id);
	}
	
	@Override
	protected Security[] handleGetAllSecurities() throws Exception {
		
		return (Security[]) getSecurityDao().loadAll().toArray(new Security[0]);
	}
	
	@Override
	protected Strategy[] handleGetAllStrategies() throws Exception {
		
		return (Strategy[]) getStrategyDao().loadAll().toArray(new Strategy[0]);
	}
	
	@Override
	protected Position[] handleGetAllPositions() throws Exception {
		
		return (Position[]) getPositionDao().loadAll().toArray(new Position[0]);
	}
	
	@Override
	protected Transaction[] handleGetAllTransactions() throws Exception {
		
		return (Transaction[]) getTransactionDao().loadAll().toArray(
		        new Transaction[0]);
	}
	
	@Override
	protected Transaction[] handleGetAllTrades() throws Exception {
		
		return (Transaction[]) getTransactionDao().findAllTrades().toArray(
		        new Transaction[0]);
	}
	
	@Override
	protected Transaction[] handleGetAllCashFlows() throws Exception {
		
		return (Transaction[]) getTransactionDao().findAllCashflows().toArray(
		        new Transaction[0]);
	}
	
	@Override
	protected Security[] handleGetAllSecuritiesInPortfolio() throws Exception {
		
		return (Security[]) getSecurityDao().findSecuritiesInPortfolio()
		        .toArray(new Security[0]);
	}
	
	@Override
	protected Position[] handleGetOpenPositions() throws Exception {
		
		return (Position[]) getPositionDao().findOpenPositions().toArray(
		        new Position[0]);
	}
	
	@Override
	protected Position[] handleGetOpenPositionsByStrategy(
	        final String strategyName) throws Exception {
		
		return (Position[]) getPositionDao().findOpenPositionsByStrategy(
		        strategyName).toArray(new Position[0]);
	}
	
	@Override
	protected PortfolioValueVO handleGetPortfolioValue() throws Exception {
		
		final PortfolioValueVO portfolioValueVO = new PortfolioValueVO();
		portfolioValueVO.setSecuritiesCurrentValue(getStrategyDao()
		        .getPortfolioSecuritiesCurrentValueDouble());
		portfolioValueVO.setCashBalance(getStrategyDao()
		        .getPortfolioCashBalanceDouble());
		portfolioValueVO.setMaintenanceMargin(getStrategyDao()
		        .getPortfolioMaintenanceMarginDouble());
		portfolioValueVO.setNetLiqValue(getStrategyDao()
		        .getPortfolioNetLiqValueDouble());
		
		return portfolioValueVO;
	}
	
	@Override
	protected Tick handleGetLastTick(final int securityId) throws Exception {
		
		return getTickDao().findLastTickForSecurityAndMaxDate(securityId,
		        DateUtil.getCurrentEPTime());
	}
	
	@Override
	protected List<Strategy> handleGetAutoActivateStrategies() throws Exception {
		
		return getStrategyDao().findAutoActivateStrategies();
	}
}