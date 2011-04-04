
package TradeApp.Events;

import java.util.Map;

import TradeApp.Events.Quote.Operation;
import TradeApp.Events.Quote.PriceType;
import TradeApp.Events.Quote.QuoteType;
import TradeApp.Events.Quote.SizeType;
import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class L1 extends ExtendedQuote {
	protected String	opt;
	
	@Deprecated
	public L1() {}
	
	public L1(final double price, final int size, final String dateTime,
			final String quoteType, final String priceType, final String sizeType,
			final String instrument, final String tickerType,
			final String instrumentType, final String venue, final String opt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, null);
		this.setOpt(opt);
	}
	
	public L1(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, null);
		this.opt = opt.toString();
	}
	
	public L1(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final PriceType priceType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, null);
		this.opt = opt.toString();
	}
	
	public L1(final double price, final int size, final String dateTime,
			final String quoteType, final String priceType, final String sizeType,
			final String instrument, final String tickerType,
			final String instrumentType, final String venue, final String opt,
			final Map<String, ?> extOpt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, extOpt);
		
		this.setOpt(opt);
	}
	
	public L1(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt,
			final Map<String, ?> extOpt) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, extOpt);
		this.opt = opt.toString();
	}
	
	public L1(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final PriceType priceType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt, final Map<String, ?> extOpt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, extOpt);
		this.opt = opt.toString();
	}
	
	public Operation opt() {
		return Operation.valueOf(this.opt);
	}
	
	public String getOpt() {
		return this.opt;
	}
	
	public void setOpt(final String opt) {
		this.opt = opt;
	}
	
	public void opt(final Operation opt) {
		this.opt = opt.toString();
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}