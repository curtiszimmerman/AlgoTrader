package com.algoTrader.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.log4j.Logger;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Position;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.trade.MarketOrderImpl;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.enumeration.Side;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.RoundUtil;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class PositionServiceImpl extends PositionServiceBase {

	private static Logger logger = MyLogger.getLogger(PositionServiceImpl.class.getName());

	protected void handleClosePosition(int positionId) throws Exception {
	
		Position position = getPositionDao().load(positionId);

		reducePosition(positionId, Math.abs(position.getQuantity()));
	}

	protected void handleReducePosition(int positionId, long quantity) throws Exception {

		Position position = getPositionDao().load(positionId);
		Security security = position.getSecurity();

		Order order = new MarketOrderImpl();
		order.setSecurity(security);
		order.setQuantity(Math.abs(quantity));
		order.setSide((position.getQuantity() > 0) ? Side.SELL : Side.BUY);

		getOrderService().sendOrder(position.getStrategy().getName(), order);

		// only remove the security from the watchlist, if the position is closed
		if (!position.isOpen()) {
			ServiceLocator.commonInstance().getMarketDataService().removeFromWatchlist(position.getStrategy(), security);
		}
	}

	protected void handleSetExitValue(int positionId, double exitValue, boolean force) throws MathException {
	
		Position position = getPositionDao().load(positionId);
	
		// there needs to be a position
		if (position == null) {
			throw new PositionServiceException("position does not exist: " + positionId);
		}
	
		// in generall there should have been set a exitValue on creation of the position
		if (!force && position.getExitValue() == null) {
			logger.warn("no exitValue was set for position: " + positionId);
			return;
		}

		// we don't want to set the exitValue to (almost)Zero
		if (exitValue <= 0.05) {
			logger.warn("setting of exitValue below 0.05 is prohibited: " + exitValue);
			return;
		}

		// in generall, exit value should not be set higher than existing exitValue
		if (!force) {
			if (position.isShort() && exitValue > position.getExitValueDouble()) {
				logger.warn("exit value " + exitValue + " is higher than existing exit value " + position.getExitValue() + " of short position " + positionId);
				return;
			} else if (position.isLong() && exitValue < position.getExitValueDouble()) {
				logger.warn("exit value " + exitValue + " is lower than existing exit value " + position.getExitValue() + " of long position " + positionId);
				return;
			}
		}
	
		// exitValue cannot be lower than currentValue
		double currentValue = position.getSecurity().getLastTick().getCurrentValueDouble();
		if (position.isShort() && exitValue < currentValue) {
			throw new PositionServiceException("ExitValue (" + exitValue + ") for short-position " + position.getId() + " is lower than currentValue: " + currentValue);
		} else if (position.isLong() && exitValue > currentValue) {
			throw new PositionServiceException("ExitValue (" + exitValue + ") for long-position " + position.getId() + " is higher than currentValue: " + currentValue);
		}
	
		position.setExitValue(exitValue);
		getPositionDao().update(position);
	
		logger.info("set exit value " + position.getSecurity().getSymbol() + " to " + exitValue);
	}

	protected void handleSetMargin(int positionId) throws Exception {
	
		Position position = getPositionDao().load(positionId);
		setMargin(position);
	}

	protected void handleSetMargin(Position position) throws Exception {
	
		Security security = position.getSecurity();
		double marginPerContract = security.getMargin();
	
		if (marginPerContract != 0) {
	
			long numberOfContracts = Math.abs(position.getQuantity());
			BigDecimal totalMargin = RoundUtil.getBigDecimal(marginPerContract * numberOfContracts);
			position.setMaintenanceMargin(totalMargin);
	
			getPositionDao().update(position);
	
			Strategy strategy = position.getStrategy();
	
			int percent = (int) (strategy.getAvailableFundsDouble() / strategy.getNetLiqValueDouble() * 100.0);
			if (strategy.getAvailableFundsDouble() >= 0) {
				logger.info("set margin for " + security.getSymbol() + " to " + RoundUtil.getBigDecimal(marginPerContract) + " total margin: " + RoundUtil.getBigDecimal(strategy.getMaintenanceMarginDouble())
						+ " availableFunds: " + RoundUtil.getBigDecimal(strategy.getAvailableFundsDouble()) + " (" + percent + "% of balance)");
			} else {
				logger.warn("set margin for " + security.getSymbol() + " to " + RoundUtil.getBigDecimal(marginPerContract) + " total margin: " + RoundUtil.getBigDecimal(strategy.getMaintenanceMarginDouble())
						+ " availableFunds: " + RoundUtil.getBigDecimal(strategy.getAvailableFundsDouble()) + " (" + percent + "% of balance)");
			}
		}
	}

	protected void handleSetMargins() throws Exception {
	
		List<Position> positions = getPositionDao().findOpenPositions();
	
		for (Position position : positions) {
			setMargin(position);
		}
	}

	public static class ClosePositionSubscriber {

		public void update(int positionId) {

			long startTime = System.currentTimeMillis();
			logger.debug("closePosition start");

			ServiceLocator.serverInstance().getPositionService().closePosition(positionId);

			logger.debug("closePosition end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}

	public static class SetExitValueSubscriber {

		public void update(int positionId, double exitValue) {

			long startTime = System.currentTimeMillis();
			logger.debug("setExitValue start");

			ServiceLocator.commonInstance().getPositionService().setExitValue(positionId, exitValue, false);

			logger.debug("setExitValue end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}

	public static class SetMarginsListener implements UpdateListener {
	
		public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	
			long startTime = System.currentTimeMillis();
			logger.debug("setMargins start");
	
			ServiceLocator.serverInstance().getPositionService().setMargins();
	
			logger.debug("setMargins end (" + (System.currentTimeMillis() - startTime) + "ms execution)");
		}
	}
}