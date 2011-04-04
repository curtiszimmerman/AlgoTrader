
package TradeApp.Events.copy;

import java.util.Date;

import TradeApp.Data.TSDataItem;
import TradeApp.Util.BasicUtils;

public class SimpleBar implements TSDataItem, Comparable<TSDataItem> {
	protected String	ticker;
	protected Date		date;
	private long		secs;
	protected double	open		= -1;
	protected double	high		= -1;
	protected double	low		= -1;
	protected double	close		= -1;
	protected long		volume	= -1;
	
	@Deprecated
	public SimpleBar() {}
	
	public SimpleBar(final String ticker, final long secs, final Date date,
			final double open, final double high, final double low,
			final double close) {
		this.date = date;
		this.secs = secs;
		this.ticker = ticker;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
	
	public SimpleBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close) {
		this(ticker, secs, BasicUtils.checkDateTime(date), open, high, low, close);
	}
	
	public SimpleBar(final String ticker, final long secs, final Date date,
			final double open, final double high, final double low,
			final double close, final long volume) {
		this(ticker, secs, date, open, high, low, close);
		this.volume = volume;
	}
	
	public SimpleBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume) {
		this(ticker, secs, BasicUtils.checkDateTime(date), open, high, low,
				close, volume);
	}
	
	@Override
	public void setDate(final String dateTime) {
		this.date = BasicUtils.checkDateTime(dateTime);
	}
	
	@Override
	public String getDate() {
		return this.date.toString();
	}
	
	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return this.ticker;
	}
	
	public void setOpen(final double open) {
		this.open = open;
	}
	
	public double getOpen() {
		return this.open;
	}
	
	public void setHigh(final double high) {
		this.high = high;
	}
	
	public double getHigh() {
		return this.high;
	}
	
	public void setLow(final double low) {
		this.low = low;
	}
	
	public double getLow() {
		return this.low;
	}
	
	public void setClose(final double close) {
		this.close = close;
	}
	
	public double getClose() {
		return this.close;
	}
	
	public void setVolume(final long volume) {
		this.volume = volume;
	}
	
	public double getVolume() {
		return this.volume;
	}
	
	@Override
	public double getTypicalValue() {
		return (this.open + 2.0 * (this.high + this.low) + this.close) / 6.0;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	@Override
	public int compareTo(final TSDataItem o) {
		return (int) Math.signum(o.getTypicalValue() - this.getTypicalValue());
	}
	
	@Override
	public Date getDateValue() {
		return this.date;
	}
	
	@Override
	public double getTypicalValue(final int i) {
		switch (i) {
			case 0:
				return this.open;
			case 1:
				return this.high;
			case 2:
				return this.low;
			case 3:
				return this.close;
			case 4:
				return this.volume;
			default:
				throw new IllegalArgumentException("Unknow index: " + i);
		}
	}
	
	private final int[]	rng	= new int[] { 0, 4 };
	
	@Override
	public int[] getRange() {
		return this.rng;
	}
	
	public void setSecs(final long secs) {
		this.secs = secs;
	}
	
	public long getSecs() {
		return this.secs;
	}
	
	@Override
	public double getTypicalValue(final String name) {
		if (name.equals(this.names[0])) return this.open;
		if (name.equals(this.names[1])) return this.high;
		if (name.equals(this.names[2])) return this.low;
		if (name.equals(this.names[3])) return this.close;
		if (name.equals(this.names[4])) return this.volume;
		
		throw new IllegalArgumentException("Unknow index: " + name);
	}
	
	private final String[]	names	= { "open", "high", "low", "close", "volume" };
	
	@Override
	public String[] getNames() {
		return this.names;
	}
}