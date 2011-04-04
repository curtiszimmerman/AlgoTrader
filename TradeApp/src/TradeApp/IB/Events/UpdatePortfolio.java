
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

import com.ib.client.Contract;

public class UpdatePortfolio implements DataItem {
	private String		accountName;
	private double		averageCost;
	private Contract	contract;
	private double		marketPrice;
	private double		marketValue;
	private int			position;
	private double		realizedPNL;
	private double		unrealizedPNL;
	
	@Deprecated
	public UpdatePortfolio() {}
	
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
		return this.accountName;
	}
	
	public double getAverageCost() {
		return this.averageCost;
	}
	
	public Contract getContract() {
		return this.contract;
	}
	
	public double getMarketPrice() {
		return this.marketPrice;
	}
	
	public double getMarketValue() {
		return this.marketValue;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public double getRealizedPNL() {
		return this.realizedPNL;
	}
	
	public double getUnrealizedPNL() {
		return this.unrealizedPNL;
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
