
package TradeApp.Events.copy;

import java.util.Date;

import TradeApp.Data.TSDataItem;
import TradeApp.Util.BasicUtils;

public class PriceQuote implements GenericQuote, TSDataItem,
		Comparable<TSDataItem> {
	protected Date		date;
	protected double	price;
	protected String	instrument;
	
	@Deprecated
	public PriceQuote() {}
	
	public PriceQuote(final double price, final String instrument,
			final String dateTime) {
		this.date = BasicUtils.checkDateTime(dateTime);;
		this.price = price;
		this.instrument = instrument;
		BasicUtils.checkDateTime(dateTime);
	}
	
	@Override
	public void setDate(final String dateTime) {
		this.date = BasicUtils.checkDateTime(dateTime);
	}
	
	@Override
	public String getDate() {
		return this.date.toString();
	}
	
	public void setPrice(final double price) {
		this.price = price;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void setInstrument(final String instrument) {
		this.instrument = instrument;
	}
	
	public String getInstrument() {
		return this.instrument;
	}
	
	@Override
	public double getTypicalValue() {
		return this.price;
	}
	
	@Override
	public double getTypicalValue(final int i) {
		if (i != 0) throw new IllegalArgumentException("Unknow index: " + i);
		return this.price;
	}
	
	@Override
	public int compareTo(final TSDataItem o) {
		return (int) Math.signum(o.getTypicalValue() - this.getTypicalValue());
	}
	
	@Override
	public Date getDateValue() {
		return this.date;
	}
	
	private final int[]	rng	= new int[] { 0, 0 };
	
	@Override
	public int[] getRange() {
		return this.rng;
	}
	
	@Override
	public double getTypicalValue(final String name) {
		if (name != null || name.length() != 0)
			throw new IllegalArgumentException("Unknow index: " + name);
		
		return this.price;
	}
	
	@Override
	public String[] getNames() {
		return null;
	}
}
