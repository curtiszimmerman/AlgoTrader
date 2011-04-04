
package TradeApp.Events.copy;

import java.util.Map;

import TradeApp.Util.BasicUtils;

public class ExtendedBar extends Bar {
	private Map<String, ?>	extOpt;
	
	@Deprecated
	public ExtendedBar() {}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close) {
		super(ticker, secs, date, open, high, low, close);
	}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final boolean hasGaps) {
		this(ticker, secs, date, open, high, low, close);
	}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume) {
		super(ticker, secs, date, open, high, low, close, volume);
	}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume, final boolean hasGaps) {
		super(ticker, secs, date, open, high, low, close, volume, hasGaps);
	}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume, final long count,
			final double VWAP, final boolean hasGaps) {
		super(ticker, secs, date, open, high, low, close, volume, count, VWAP,
				hasGaps);
	}
	
	public ExtendedBar(final String ticker, final long secs, final String date,
			final double open, final double high, final double low,
			final double close, final long volume, final long count,
			final double VWAP, final boolean hasGaps, final Map<String, ?> extOpt) {
		super(ticker, secs, date, open, high, low, close, volume, count, VWAP,
				hasGaps);
		this.extOpt = extOpt;
	}
	
	public void setExtOpt(final Map<String, ?> extOpt) {
		this.extOpt = extOpt;
	}
	
	public Map<String, ?> getExtOpt() {
		return this.extOpt;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
