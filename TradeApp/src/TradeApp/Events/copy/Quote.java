package TradeApp.Events.copy;

import TradeApp.Util.BasicUtils;

public class Quote {
	public static enum Operation {
		DELETE, INSERT, UPDATE, NA
	}
	
	public static enum PriceType {
		CURRENCY, MODEL
	}
	
	public static enum QuoteType {
		BID, ASK, LAST, OPEN, HIGH, LOW, CLOSE,
	}
	
	public static enum SizeType {
		BID, ASK, LAST, VOLUME, NULL
	}
	
	public static enum TickerType {
		CUSIP, EXCHANGE_TICKER, ISIN, RIC, SEDOL
	}
	
	private GenericQuote	value;
	
	@Deprecated
	public Quote() {
	}
	
	public Quote(final GenericQuote value) {
		this.value = value;
	}
	
	public void setValue(final GenericQuote value) {
		this.value = value;
	}
	
	public GenericQuote getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
