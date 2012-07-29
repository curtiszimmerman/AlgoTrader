package com.algoTrader.service.fix;

import quickfix.field.CumQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.Symbol;

import com.algoTrader.entity.security.Forex;
import com.algoTrader.entity.security.Future;
import com.algoTrader.entity.security.Security;
import com.algoTrader.entity.security.Stock;
import com.algoTrader.entity.security.StockOption;
import com.algoTrader.entity.trade.LimitOrder;
import com.algoTrader.entity.trade.MarketOrder;
import com.algoTrader.entity.trade.Order;
import com.algoTrader.entity.trade.StopLimitOrder;
import com.algoTrader.entity.trade.StopOrder;
import com.algoTrader.enumeration.Side;
import com.algoTrader.enumeration.Status;

public class FixUtil {

	public static Side getSide(quickfix.field.Side side) {

		if (side.getValue() == quickfix.field.Side.BUY) {
			return Side.BUY;
		} else if (side.getValue() == quickfix.field.Side.SELL) {
			return Side.SELL;
		} else {
			throw new IllegalArgumentException("unknow side " + side);
		}
	}

	public static Status getStatus(OrdStatus ordStatus, CumQty cumQty) {

		if (ordStatus.getValue() == OrdStatus.NEW || ordStatus.getValue() == OrdStatus.PENDING_NEW) {
			return Status.SUBMITTED;
		} else if (ordStatus.getValue() == OrdStatus.PARTIALLY_FILLED) {
			return Status.PARTIALLY_EXECUTED;
		} else if (ordStatus.getValue() == OrdStatus.FILLED) {
			return Status.EXECUTED;
		} else if (ordStatus.getValue() == OrdStatus.CANCELED || ordStatus.getValue() == OrdStatus.PENDING_CANCEL || ordStatus.getValue() == OrdStatus.REJECTED) {
			return Status.CANCELED;
		} else if (ordStatus.getValue() == OrdStatus.REPLACED || ordStatus.getValue() == OrdStatus.PENDING_REPLACE) {
			if (cumQty.getValue() == 0) {
				return Status.SUBMITTED;
			} else {
				return Status.PARTIALLY_EXECUTED;
			}
		} else {
			throw new IllegalArgumentException("unknown orderStatus " + ordStatus.getValue());
		}
	}

	public static Symbol getFixSymbol(Security security) {
	
		if (security instanceof StockOption) {
			return new Symbol(security.getUnderlaying().getSymbol());
		} else if (security instanceof Future) {
			return new Symbol(security.getUnderlaying().getSymbol());
		} else if (security instanceof Forex) {
			String[] currencies = security.getSymbol().split("\\.");
			return new Symbol(currencies[0]);
		} else if (security instanceof Stock) {
			return new Symbol(security.getSymbol());
		} else {
			throw new UnsupportedOperationException("unsupported security type " + security.getClass());
		}
	}

	public static quickfix.field.Side getFixSide(Side side) {

		if (side.equals(Side.BUY)) {
			return new quickfix.field.Side(quickfix.field.Side.BUY);
		} else if (side.equals(Side.SELL)) {
			return new quickfix.field.Side(quickfix.field.Side.SELL);
		} else {
			throw new IllegalArgumentException("unknow side " + side);
		}
	}

	public static OrdType getFixOrderType(Order order) {

		if (order instanceof MarketOrder) {
			return new OrdType(OrdType.MARKET);
		} else if (order instanceof LimitOrder) {
			return new OrdType(OrdType.LIMIT);
		} else if (order instanceof StopOrder) {
			return new OrdType(OrdType.STOP);
		} else if (order instanceof StopLimitOrder) {
			return new OrdType(OrdType.STOP_LIMIT);
		} else {
			throw new IllegalArgumentException("unsupported order type " + order.getClass().getName());
		}
	}
}
