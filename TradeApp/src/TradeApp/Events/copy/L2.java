
package TradeApp.Events.copy;

import java.util.Map;

import TradeApp.Events.Quote.Operation;
import TradeApp.Events.Quote.PriceType;
import TradeApp.Events.Quote.QuoteType;
import TradeApp.Events.Quote.SizeType;
import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class L2 extends L1 {
	protected String	marketMaker;
	
	@Deprecated
	public L2() {}
	
	public L2(final double price, final int size, final String dateTime,
			final String quoteType, final String priceType, final String sizeType,
			final String instrument, final String tickerType,
			final String instrumentType, final String venue, final String opt,
			final String marketMaker) {
		
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt);
		
		this.setMarketMaker(marketMaker);
	}
	
	public L2(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt,
			final String marketMaker) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, opt);
		this.marketMaker = marketMaker;
	}
	
	public L2(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final PriceType priceType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt, final String marketMaker) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt);
		this.marketMaker = marketMaker;
	}
	
	public L2(final double price, final int size, final String dateTime,
			final String quoteType, final String priceType, final String sizeType,
			final String instrument, final String tickerType,
			final String instrumentType, final String venue, final String opt,
			final String marketMaker, final Map<String, ?> extOpt) {
		
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt, extOpt);
		
		this.setMarketMaker(marketMaker);
	}
	
	public L2(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue, final Operation opt,
			final String marketMaker, final Map<String, ?> extOpt) {
		super(price, size, dateTime, quoteType, sizeType, instrument, tickerType,
				instrumentType, venue, opt, extOpt);
		this.marketMaker = marketMaker;
	}
	
	public L2(final double price, final int size, final String dateTime,
			final QuoteType quoteType, final PriceType priceType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue, final Operation opt, final String marketMaker,
			final Map<String, ?> extOpt) {
		super(price, size, dateTime, quoteType, priceType, sizeType, instrument,
				tickerType, instrumentType, venue, opt, extOpt);
		this.marketMaker = marketMaker;
	}
	
	public String getMarketMaker() {
		return this.marketMaker;
	}
	
	public void setMarketMaker(final String marketMaker) {
		this.marketMaker = marketMaker;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}