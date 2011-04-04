
package TradeApp.Events;

import TradeApp.Util.BasicUtils;

public class Bar extends SimpleBar {
	private long		count		= -1;
	private double		VWAP		= -1;
	private Boolean	hasGaps	= null;
	
	@Deprecated
	public Bar() {}
	
	public Bar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close) {
		super(ticker, secs, date, open, high, low, close);
	}
	
	public Bar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final boolean hasGaps) {
		this(ticker, secs, date, open, high, low, close);
		this.hasGaps = hasGaps;
	}
	
	public Bar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume) {
		super(ticker, secs, date, open, high, low, close, volume);
	}
	
	public Bar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume, final boolean hasGaps) {
		this(ticker, secs, date, open, high, low, close, volume);
		this.hasGaps = hasGaps;
	}
	
	public Bar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume, final long count,
			final double VWAP, final boolean hasGaps) {
		this(ticker, secs, date, open, high, low, close, volume, hasGaps);
		this.count = count;
		this.VWAP = VWAP;
	}
	
	public void setHasGaps(final boolean hasGaps) {
		this.hasGaps = hasGaps;
	}
	
	public Boolean getHasGaps() {
		return this.hasGaps;
	}
	
	public Boolean isHasGaps() {
		return this.hasGaps;
	}
	
	public void setVWAP(final double vWAP) {
		this.VWAP = vWAP;
	}
	
	public double getVWAP() {
		return this.VWAP;
	}
	
	public void setCount(final long count) {
		this.count = count;
	}
	
	public long getCount() {
		return this.count;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
