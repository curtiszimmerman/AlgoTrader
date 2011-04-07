package com.algoTrader.subscriber;

import org.apache.log4j.Logger;

import com.algoTrader.entity.Transaction;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.RoundUtil;
import com.algoTrader.vo.PortfolioValueVO;

public class PrintPortfolioValueSubscriber {
	
	private static Logger	logger	      = MyLogger
	                                              .getLogger(PrintPortfolioValueSubscriber.class
	                                                      .getName());
	
	private static long	   initialBalance	= ConfigurationUtil
	                                              .getBaseConfig()
	                                              .getLong(
	                                                      "simulation.initialBalance");
	
	private static boolean	initialized	  = false;
	
	public void
	        update(final long timestamp, final PortfolioValueVO portfolioValue,
	                final Transaction transaction) {
		
		// dont log anything while initialising macd
		if (portfolioValue.getNetLiqValue() != PrintPortfolioValueSubscriber.initialBalance) {
			PrintPortfolioValueSubscriber.initialized = true;
		}
		
		if (PrintPortfolioValueSubscriber.initialized) {
			PrintPortfolioValueSubscriber.logger.info(RoundUtil
			        .getBigDecimal(portfolioValue.getCashBalance()) +
			        "," +
			            RoundUtil.getBigDecimal(portfolioValue
			                    .getSecuritiesCurrentValue()) +
			        "," +
			            RoundUtil.getBigDecimal(portfolioValue
			                    .getMaintenanceMargin()));
		}
	}
}
