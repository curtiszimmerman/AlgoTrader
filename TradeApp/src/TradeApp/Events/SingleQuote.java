
package TradeApp.Events;

import TradeApp.Events.Quote.Operation;
import TradeApp.Events.Quote.PriceType;
import TradeApp.Events.Quote.QuoteType;
import TradeApp.Events.Quote.SizeType;
import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class SingleQuote extends L2 {
	private int	nFromBestAtArrival	= -1;
	
	@Deprecated
	public SingleQuote() {}
	
	public SingleQuote(final double price, final int size,
			final String dateTime, final String quoteType, final String priceType,
			final String sizeType, final String instrument,
			final String tickerType, final String instrumentType,
			final String venue, final String opt, final String marketMaker,
			final int nFromBest) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt, marketMaker);
		this.nFromBestAtArrival = nFromBest;
	}
	
	public SingleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt, final String marketMaker) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, opt, marketMaker);
	}
	
	public SingleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt, final String marketMaker,
			final int nFromBest) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, opt, marketMaker);
		this.nFromBestAtArrival = nFromBest;
	}
	
	public SingleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final PriceType priceType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt,
			final String marketMaker) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt, marketMaker);
	}
	
	public SingleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final PriceType priceType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt,
			final String marketMaker, final int nFromBest) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt, marketMaker);
		this.nFromBestAtArrival = nFromBest;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	public void setnFromBestAtArrival(final int nFromBest) {
		this.nFromBestAtArrival = nFromBest;
	}
	
	public int getnFromBestAtArrival() {
		return this.nFromBestAtArrival;
	}
}