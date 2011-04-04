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

	private static Logger logger = MyLogger.getLogger(SimulationServiceImpl.class.getName());
	private static String dataSet = ConfigurationUtil.getBaseConfig().getString("dataSource.dataSet");
	private static DecimalFormat twoDigitFormat = new DecimalFormat("#,##0.00");
	private static DecimalFormat threeDigitFormat = new DecimalFormat("#,##0.000");
	private static DateFormat dateFormat = new SimpleDateFormat(" MMM-yy ");

	@SuppressWarnings("unchecked")
	protected void handleResetDB() throws Exception {
	
		// process all strategies
		Collection<Strategy> strategies = getStrategyDao().loadAll();
		for (Strategy strategy : strategies) {

			// delete all transactions except the initial CREDIT
			Collection<Transaction> transactions = strategy.getTransactions();
			Set<Transaction> toRemoveTransactions = new HashSet<Transaction>();
			Set<Transaction> toKeepTransactions = new HashSet<Transaction>();
			for (Transaction transaction : transactions) {
				if (transaction.getType().equals(TransactionType.CREDIT)) {
					toKeepTransactions.add(transaction);
				} else {
					toRemoveTransactions.add(transaction);
				}
			}
			getTransactionDao().remove(toRemoveTransactions);
			strategy.setTransactions(toKeepTransactions);
		
			// delete all positions and references to them
			Collection<Position> positions = strategy.getPositions();
			getPositionDao().remove(positions);
			strategy.setPositions(new HashSet<Position>());

			getStrategyDao().update(strategy);
		}

	}

	@SuppressWarnings("unchecked")
	protected void handleInputCSV() {

		getRuleService().initCoordination(StrategyImpl.BASE);

		List<Security> securities = getSecurityDao().findSecuritiesOnActiveWatchlist();
		for (Security security : securities) {

			if (security.getIsin() == null) {
				logger.warn("no tickdata available for " + security.getSymbol());
				continue;
			}

			File file = new File("results/tickdata/" + dataSet + "/" + security.getIsin() + ".csv");

			if (file == null || !file.exists()) {
				logger.warn("no tickdata available for " + security.getSymbol());
				continue;
			}

			CSVInputAdapterSpec spec = new CsvTickInputAdapterSpec(file);

			getRuleService().coordinate(StrategyImpl.BASE, spec);

			logger.debug("started simulation for security " + security.getSymbol());
		}

		getRuleService().startCoordination(StrategyImpl.BASE);
	}

	@SuppressWarnings("unchecked")
	protected SimulationResultVO handleRunByUnderlayings() {

		long startTime = System.currentTimeMillis();
		
		// must call resetDB through ServiceLocator in order to get a transaction
		ServiceLocator.serverInstance().getSimulationService().resetDB();
		
		// init all activatable strategies
		List<Strategy> strategies = getStrategyDao().findAutoActivateStrategies();
		for (Strategy strategy : strategies) {
			getRuleService().initServiceProvider(strategy.getName());
			getRuleService().deployAllModules(strategy.getName());
		}

		// feed the ticks
		inputCSV();

		// get the results
		SimulationResultVO resultVO = getSimulationResultVO(startTime);
		
		// destroy all service providers
		for (Strategy strategy : strategies) {
			getRuleService().destroyServiceProvider(strategy.getName());
		}
		
		// reset all configuration variables
		ConfigurationUtil.resetConfig();

		// run a garbage collection
		System.gc();

		return resultVO;
	}

	@SuppressWarnings("unchecked")
	protected SimulationResultVO handleGetSimulationResultVO(long startTime) {

		PerformanceKeysVO performanceKeys = (PerformanceKeysVO) getRuleService().getLastEvent(StrategyImpl.BASE, "CREATE_PERFORMANCE_KEYS");
		List<MonthlyPerformanceVO> monthlyPerformances = getRuleService().getAllEvents(StrategyImpl.BASE, "KEEP_MONTHLY_PERFORMANCE");
		MaxDrawDownVO maxDrawDown = (MaxDrawDownVO) getRuleService().getLastEvent(StrategyImpl.BASE, "CREATE_MAX_DRAW_DOWN");

		// assemble the result
		SimulationResultVO resultVO = new SimulationResultVO();
		resultVO.setMins(((double) (System.currentTimeMillis() - startTime)) / 60000);
		resultVO.setDataSet(dataSet);
		resultVO.setNetLiqValue(getStrategyDao().getPortfolioNetLiqValueDouble());
		resultVO.setMonthlyPerformanceVOs(monthlyPerformances);
		resultVO.setPerformanceKeysVO(performanceKeys);
		resultVO.setMaxDrawDownVO(maxDrawDown);
		return resultVO;
	}
	
	protected void handleSimulateWithCurrentParams() throws Exception {
	
		SimulationResultVO resultVO = ServiceLocator.serverInstance().getSimulationService().runByUnderlayings();
		logMultiLineString(convertStatisticsToLongString(resultVO));
	}

	@SuppressWarnings("unchecked")
	private static String convertStatisticsToLongString(SimulationResultVO resultVO) {
	
		StringBuffer buffer = new StringBuffer();
		buffer.append("execution time (min): " + (new DecimalFormat("0.00")).format(resultVO.getMins()) + "\r\n");
		buffer.append("dataSet: " + dataSet + "\r\n");

		double netLiqValue = resultVO.getNetLiqValue();
		buffer.append("netLiqValue=" + twoDigitFormat.format(netLiqValue) + "\r\n");
	
		List<MonthlyPerformanceVO> monthlyPerformanceVOs = resultVO.getMonthlyPerformanceVOs();
		double maxDrawDownM = 0d;
		double bestMonthlyPerformance = Double.NEGATIVE_INFINITY;
		if ((monthlyPerformanceVOs != null)) {
			StringBuffer dateBuffer= new StringBuffer("month-year:         ");
			StringBuffer performanceBuffer = new StringBuffer("MonthlyPerformance: ");
			for (MonthlyPerformanceVO MonthlyPerformanceVO : monthlyPerformanceVOs) {
				maxDrawDownM = Math.min(maxDrawDownM, MonthlyPerformanceVO.getValue());
				bestMonthlyPerformance = Math.max(bestMonthlyPerformance, MonthlyPerformanceVO.getValue());
				dateBuffer.append(dateFormat.format(MonthlyPerformanceVO.getDate()));
				performanceBuffer.append(StringUtils.leftPad(twoDigitFormat.format(MonthlyPerformanceVO.getValue() * 100), 6) + "% ");
			}
			buffer.append(dateBuffer.toString() + "\r\n");
			buffer.append(performanceBuffer.toString() + "\r\n");
		}

		PerformanceKeysVO performanceKeys = resultVO.getPerformanceKeysVO();
		MaxDrawDownVO maxDrawDownVO = resultVO.getMaxDrawDownVO();
		if (performanceKeys != null && maxDrawDownVO != null) {
			buffer.append("n=" + performanceKeys.getN());
			buffer.append(" avgM=" + twoDigitFormat.format(performanceKeys.getAvgM() * 100) + "%");
			buffer.append(" stdM=" + twoDigitFormat.format(performanceKeys.getStdM() * 100) + "%");
			buffer.append(" avgY=" + twoDigitFormat.format(performanceKeys.getAvgY() * 100) + "%");
			buffer.append(" stdY=" + twoDigitFormat.format(performanceKeys.getStdY() * 100) + "% ");
			buffer.append(" sharpRatio=" + threeDigitFormat.format(performanceKeys.getSharpRatio()) + "\r\n");
			
			buffer.append("maxDrawDownM=" + twoDigitFormat.format(-maxDrawDownM * 100) + "%");
			buffer.append(" bestMonthlyPerformance=" + twoDigitFormat.format(bestMonthlyPerformance * 100) + "%");
			buffer.append(" maxDrawDown=" + twoDigitFormat.format(maxDrawDownVO.getAmount() * 100) + "%");
			buffer.append(" maxDrawDownPeriod=" + twoDigitFormat.format(maxDrawDownVO.getPeriod() / 86400000) + "days");
			buffer.append(" colmarRatio=" + twoDigitFormat.format(performanceKeys.getAvgY() / maxDrawDownVO.getAmount()));
		}

		return buffer.toString();
	}

	private static void logMultiLineString(String input) {

		String[] lines = input.split("\r\n");
		for (String line : lines) {
			logger.info(line);
		}
	}
}