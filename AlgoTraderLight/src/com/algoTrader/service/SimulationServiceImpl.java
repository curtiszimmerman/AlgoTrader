package com.algoTrader.service;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.Transaction;
import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.util.ConfigurationUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.io.CsvTickInputAdapterSpec;
import com.algoTrader.vo.MaxDrawDownVO;
import com.algoTrader.vo.MonthlyPerformanceVO;
import com.algoTrader.vo.PerformanceKeysVO;
import com.algoTrader.vo.SimulationResultVO;
import com.espertech.esperio.csv.CSVInputAdapterSpec;

public class SimulationServiceImpl extends SimulationServiceBase {
	
	private static Logger	     logger	             = MyLogger
	                                                         .getLogger(SimulationServiceImpl.class
	                                                                 .getName());
	private static String	     dataSet	         = ConfigurationUtil
	                                                         .getBaseConfig()
	                                                         .getString(
	                                                                 "dataSource.dataSet");
	private static DecimalFormat	twoDigitFormat	 = new DecimalFormat(
	                                                         "#,##0.00");
	private static DecimalFormat	threeDigitFormat	= new DecimalFormat(
	                                                         "#,##0.000");
	private static DateFormat	 dateFormat	         = new SimpleDateFormat(
	                                                         " MMM-yy ");
	
	@Override
	@SuppressWarnings("unchecked")
	protected void handleResetDB() throws Exception {
		
		// process all strategies
		final Collection<Strategy> strategies = getStrategyDao().loadAll();
		for (final Strategy strategy : strategies) {
			
			// delete all transactions except the initial CREDIT
			final Collection<Transaction> transactions = strategy
			        .getTransactions();
			final Set<Transaction> toRemoveTransactions = new HashSet<Transaction>();
			final Set<Transaction> toKeepTransactions = new HashSet<Transaction>();
			for (final Transaction transaction : transactions) {
				if (transaction.getType().equals(TransactionType.CREDIT)) {
					toKeepTransactions.add(transaction);
				} else {
					toRemoveTransactions.add(transaction);
				}
			}
			getTransactionDao().remove(toRemoveTransactions);
			strategy.setTransactions(toKeepTransactions);
			
			// delete all positions and references to them
			final Collection<Position> positions = strategy.getPositions();
			getPositionDao().remove(positions);
			strategy.setPositions(new HashSet<Position>());
			
			getStrategyDao().update(strategy);
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void handleInputCSV() {
		
		getRuleService().initCoordination(StrategyImpl.BASE);
		
		final List<Security> securities = getSecurityDao()
		        .findSecuritiesOnActiveWatchlist();
		for (final Security security : securities) {
			
			if (security.getIsin() == null) {
				SimulationServiceImpl.logger.warn("no tickdata available for " +
				        security.getSymbol());
				continue;
			}
			
			final File file = new File("results/tickdata/" +
			        SimulationServiceImpl.dataSet + "/" + security.getIsin() +
			        ".csv");
			
			if (file == null || !file.exists()) {
				SimulationServiceImpl.logger.warn("no tickdata available for " +
				        security.getSymbol());
				continue;
			}
			
			final CSVInputAdapterSpec spec = new CsvTickInputAdapterSpec(file);
			
			getRuleService().coordinate(StrategyImpl.BASE, spec);
			
			SimulationServiceImpl.logger
			        .debug("started simulation for security " +
			                security.getSymbol());
		}
		
		getRuleService().startCoordination(StrategyImpl.BASE);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected SimulationResultVO handleRunByUnderlayings() {
		
		final long startTime = System.currentTimeMillis();
		
		// must call resetDB through ServiceLocator in order to get a
		// transaction
		ServiceLocator.serverInstance().getSimulationService().resetDB();
		
		// init all activatable strategies
		final List<Strategy> strategies = getStrategyDao()
		        .findAutoActivateStrategies();
		for (final Strategy strategy : strategies) {
			getRuleService().initServiceProvider(strategy.getName());
			getRuleService().deployAllModules(strategy.getName());
		}
		
		// feed the ticks
		inputCSV();
		
		// get the results
		final SimulationResultVO resultVO = getSimulationResultVO(startTime);
		
		// destroy all service providers
		for (final Strategy strategy : strategies) {
			getRuleService().destroyServiceProvider(strategy.getName());
		}
		
		// reset all configuration variables
		ConfigurationUtil.resetConfig();
		
		// run a garbage collection
		System.gc();
		
		return resultVO;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected SimulationResultVO handleGetSimulationResultVO(
	        final long startTime) {
		
		final PerformanceKeysVO performanceKeys = (PerformanceKeysVO) getRuleService()
		        .getLastEvent(StrategyImpl.BASE, "CREATE_PERFORMANCE_KEYS");
		final List<MonthlyPerformanceVO> monthlyPerformances = getRuleService()
		        .getAllEvents(StrategyImpl.BASE, "KEEP_MONTHLY_PERFORMANCE");
		final MaxDrawDownVO maxDrawDown = (MaxDrawDownVO) getRuleService()
		        .getLastEvent(StrategyImpl.BASE, "CREATE_MAX_DRAW_DOWN");
		
		// assemble the result
		final SimulationResultVO resultVO = new SimulationResultVO();
		resultVO.setMins((double) (System.currentTimeMillis() - startTime) / 60000);
		resultVO.setDataSet(SimulationServiceImpl.dataSet);
		resultVO.setNetLiqValue(getStrategyDao()
		        .getPortfolioNetLiqValueDouble());
		resultVO.setMonthlyPerformanceVOs(monthlyPerformances);
		resultVO.setPerformanceKeysVO(performanceKeys);
		resultVO.setMaxDrawDownVO(maxDrawDown);
		return resultVO;
	}
	
	@Override
	protected void handleSimulateWithCurrentParams() throws Exception {
		
		final SimulationResultVO resultVO = ServiceLocator.serverInstance()
		        .getSimulationService().runByUnderlayings();
		SimulationServiceImpl.logMultiLineString(SimulationServiceImpl
		        .convertStatisticsToLongString(resultVO));
	}
	
	@SuppressWarnings("unchecked")
	private static String convertStatisticsToLongString(
	        final SimulationResultVO resultVO) {
		
		final StringBuffer buffer = new StringBuffer();
		buffer.append("execution time (min): " +
		        new DecimalFormat("0.00").format(resultVO.getMins()) + "\r\n");
		buffer.append("dataSet: " + SimulationServiceImpl.dataSet + "\r\n");
		
		final double netLiqValue = resultVO.getNetLiqValue();
		buffer.append("netLiqValue=" +
		        SimulationServiceImpl.twoDigitFormat.format(netLiqValue) +
		        "\r\n");
		
		final List<MonthlyPerformanceVO> monthlyPerformanceVOs = resultVO
		        .getMonthlyPerformanceVOs();
		double maxDrawDownM = 0d;
		double bestMonthlyPerformance = Double.NEGATIVE_INFINITY;
		if (monthlyPerformanceVOs != null) {
			final StringBuffer dateBuffer = new StringBuffer(
			        "month-year:         ");
			final StringBuffer performanceBuffer = new StringBuffer(
			        "MonthlyPerformance: ");
			for (final MonthlyPerformanceVO MonthlyPerformanceVO : monthlyPerformanceVOs) {
				maxDrawDownM = Math.min(maxDrawDownM,
				        MonthlyPerformanceVO.getValue());
				bestMonthlyPerformance = Math.max(bestMonthlyPerformance,
				        MonthlyPerformanceVO.getValue());
				dateBuffer.append(SimulationServiceImpl.dateFormat
				        .format(MonthlyPerformanceVO.getDate()));
				performanceBuffer.append(StringUtils.leftPad(
				        SimulationServiceImpl.twoDigitFormat
				                .format(MonthlyPerformanceVO.getValue() * 100),
				        6) +
				        "% ");
			}
			buffer.append(dateBuffer.toString() + "\r\n");
			buffer.append(performanceBuffer.toString() + "\r\n");
		}
		
		final PerformanceKeysVO performanceKeys = resultVO
		        .getPerformanceKeysVO();
		final MaxDrawDownVO maxDrawDownVO = resultVO.getMaxDrawDownVO();
		if (performanceKeys != null && maxDrawDownVO != null) {
			buffer.append("n=" + performanceKeys.getN());
			buffer.append(" avgM=" +
			        SimulationServiceImpl.twoDigitFormat.format(performanceKeys
			                .getAvgM() * 100) + "%");
			buffer.append(" stdM=" +
			        SimulationServiceImpl.twoDigitFormat.format(performanceKeys
			                .getStdM() * 100) + "%");
			buffer.append(" avgY=" +
			        SimulationServiceImpl.twoDigitFormat.format(performanceKeys
			                .getAvgY() * 100) + "%");
			buffer.append(" stdY=" +
			        SimulationServiceImpl.twoDigitFormat.format(performanceKeys
			                .getStdY() * 100) + "% ");
			buffer.append(" sharpRatio=" +
			        SimulationServiceImpl.threeDigitFormat
			                .format(performanceKeys.getSharpRatio()) + "\r\n");
			
			buffer.append("maxDrawDownM=" +
			        SimulationServiceImpl.twoDigitFormat
			                .format(-maxDrawDownM * 100) + "%");
			buffer.append(" bestMonthlyPerformance=" +
			        SimulationServiceImpl.twoDigitFormat
			                .format(bestMonthlyPerformance * 100) + "%");
			buffer.append(" maxDrawDown=" +
			        SimulationServiceImpl.twoDigitFormat.format(maxDrawDownVO
			                .getAmount() * 100) + "%");
			buffer.append(" maxDrawDownPeriod=" +
			        SimulationServiceImpl.twoDigitFormat.format(maxDrawDownVO
			                .getPeriod() / 86400000) + "days");
			buffer.append(" colmarRatio=" +
			        SimulationServiceImpl.twoDigitFormat.format(performanceKeys
			                .getAvgY() / maxDrawDownVO.getAmount()));
		}
		
		return buffer.toString();
	}
	
	private static void logMultiLineString(final String input) {
		
		final String[] lines = input.split("\r\n");
		for (final String line : lines) {
			SimulationServiceImpl.logger.info(line);
		}
	}
}