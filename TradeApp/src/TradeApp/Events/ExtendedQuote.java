
package TradeApp.Events;

import java.util.Map;

import TradeApp.Events.Quote.PriceType;
import TradeApp.Events.Quote.QuoteType;
import TradeApp.Events.Quote.SizeType;
import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class ExtendedQuote extends SimpleQuote {
	private Map<String, ?>	extOpt;
	
	@Deprecated
	public ExtendedQuote() {}
	
	public ExtendedQuote(final double price, final int size,
			final String dateTime, final String quoteType, final String priceType,
			final String sizeType, final String instrument,
			final String tickerType, final String instrumentType,
			final String venue, final Map<String, ?> opt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue);
		
		this.extOpt = opt;
	}
	
	public ExtendedQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Map<String, ?> opt) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue);
		this.extOpt = opt;
	}
	
	public ExtendedQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final PriceType priceType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue,
			final Map<String, ?> opt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue);
		this.extOpt = opt;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}

	public void setExtOpt(Map<String, ?> opt) {
		this.extOpt = opt;
	}

	public Map<String, ?> getExtOpt() {
		return extOpt;
	}
}