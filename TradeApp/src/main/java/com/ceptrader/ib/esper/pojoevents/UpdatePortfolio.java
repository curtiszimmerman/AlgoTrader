package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;
import com.ib.client.Contract;

public class UpdatePortfolio implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          accountName;
	private double	          averageCost;
	private Contract	      contract;
	private double	          marketPrice;
	private double	          marketValue;
	private int	              position;
	private double	          realizedPNL;
	private double	          unrealizedPNL;
	
	@Deprecated
	public UpdatePortfolio() {
	}
	
	public UpdatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		this.contract = contract;
		this.position = position;
		this.marketPrice = marketPrice;
		this.marketValue = marketValue;
		this.averageCost = averageCost;
		this.unrealizedPNL = unrealizedPNL;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public double getAverageCost() {
		return averageCost;
	}
	
	public Contract getContract() {
		return contract;
	}
	
	public double getMarketPrice() {
		return marketPrice;
	}
	
	public double getMarketValue() {
		return marketValue;
	}
	
	public int getPosition() {
		return position;
	}
	
	public double getRealizedPNL() {
		return realizedPNL;
	}
	
	public double getUnrealizedPNL() {
		return unrealizedPNL;
	}
	
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}
	
	public void setAverageCost(final double averageCost) {
		this.averageCost = averageCost;
	}
	
	public void setContract(final Contract contract) {
		this.contract = contract;
	}
	
	public void setMarketPrice(final double marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	public void setMarketValue(final double marketValue) {
		this.marketValue = marketValue;
	}
	
	public void setPosition(final int position) {
		this.position = position;
	}
	
	public void setRealizedPNL(final double realizedPNL) {
		this.realizedPNL = realizedPNL;
	}
	
	public void setUnrealizedPNL(final double unrealizedPNL) {
		this.unrealizedPNL = unrealizedPNL;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
