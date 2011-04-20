package com.algoTrader.entity;

import java.math.BigDecimal;

import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.util.RoundUtil;

public class TransactionImpl extends Transaction {
	
	private static final long	serialVersionUID	= -1528408715199422753L;
	
	private Double	          value	             = null;	                 // cache
	                                                                         // getValueDouble
	                                                                         // because
	                                                                         // getValue
	                                                                         // get's
	                                                                         // called
	                                                                         // very
	                                                                         // often
	                                                                         
	@Override
	public BigDecimal getValue() {
		
		return RoundUtil.getBigDecimal(getValueDouble());
	}
	
	/**
	 * SELL / CREDIT / INTREST: positive cashflow
	 * BUY / EXPIRATION / DEBIT / FEES: negative cashflow
	 * REBALANCE: positive or negative cashflow (depending on quantity equals 1
	 * or -1)
	 */
	@Override
	public double getValueDouble() {
		
		if (value == null) {
			if (getType().equals(TransactionType.BUY) ||
			        getType().equals(TransactionType.SELL) ||
			        getType().equals(TransactionType.EXPIRATION)) {
				value = -getQuantity() *
				        getSecurity().getSecurityFamily().getContractSize() *
				        getPrice().doubleValue() -
				        getCommission().doubleValue();
			} else if (getType().equals(TransactionType.CREDIT) ||
			        getType().equals(TransactionType.INTREST)) {
				value = getPrice().doubleValue();
			} else if (getType().equals(TransactionType.DEBIT) ||
			        getType().equals(TransactionType.FEES)) {
				value = -getPrice().doubleValue();
			} else if (getType().equals(TransactionType.REBALANCE)) {
				value = getQuantity() * getPrice().doubleValue();;
			} else {
				throw new IllegalArgumentException(
				        "unsupported transactionType: " + getType());
			}
		}
		return value;
	}
}