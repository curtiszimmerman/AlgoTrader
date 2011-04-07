package com.algoTrader.service.mov;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Strategy;
import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.service.LookupService;
import com.algoTrader.service.PositionService;
import com.algoTrader.service.TransactionService;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.vo.OrderVO;

public class MovServiceImpl {

	private static Logger logger = MyLogger.getLogger(MovServiceImpl.class.getName());

	private PositionService positionService;
	private LookupService lookupService;
	private TransactionService transactionService;
	private static double initialStopLoss = ConfigurationUtil.getStrategyConfig("MOV").getDouble("initialStopLoss");

	public MovServiceImpl(PositionService positionService, LookupService lookupService, TransactionService transactionService) {

		this.positionService = positionService;
		this.lookupService = lookupService;
		this.transactionService = transactionService;
	}

	public void openPosition(String strategyName, int securityId, BigDecimal currentValue) {

		Strategy strategy = this.lookupService.getStrategyByNameFetched(strategyName);

		int qty = (int) (strategy.getAvailableFundsDouble() / currentValue.doubleValue());
		
		if (qty <= 0)
			return;

		OrderVO order = new OrderVO();
		order.setStrategyName(strategyName);
		order.setSecurityId(securityId);
		order.setRequestedQuantity(qty);
		order.setTransactionType(TransactionType.BUY);

		this.transactionService.executeTransaction(strategy.getName(), order);
		
		// if a position was open (or already existed) set margin and exitValue
		Position position = this.lookupService.getPositionBySecurityAndStrategy(securityId, strategyName);
		if (position != null && position.isOpen()) {
			this.positionService.setMargin(position.getId());

			double exitValue = initialStopLoss * currentValue.doubleValue();
			this.positionService.setExitValue(position.getId(), exitValue, true);
		}
	}

	public static class OpenPositionSubscriber {
	
		public void update(String strategyName, int securityId, BigDecimal currentValue) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("openPosition start");
	
			MovServiceImpl movService = ((MovServiceImpl) ServiceLocator.commonInstance().getService("movService"));
			movService.openPosition(strategyName, securityId, currentValue);
	
			logger.debug("openPosition end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
}