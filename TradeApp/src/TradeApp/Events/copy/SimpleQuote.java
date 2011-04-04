
package TradeApp.Events.copy;

import TradeApp.Events.Quote.PriceType;
import TradeApp.Events.Quote.QuoteType;
import TradeApp.Events.Quote.SizeType;
import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class SimpleQuote extends PriceQuote {
	protected int		size;
	protected String	quoteType;
	protected String	priceType;
	protected String	sizeType;
	protected String	tickerType;
	protected String	instrumentType;
	protected String	venue;
	
	@Deprecated
	public SimpleQuote() {}
	
	public SimpleQuote(final double price, final int size,
			final String dateTime, final String quoteType, final String priceType,
			final String sizeType, final String instrument,
			final String tickerType, final String instrumentType,
			final String venue) {
		this(price, size, dateTime, quoteType == null
				|| "".equals(quoteType.trim()) ? QuoteType.CLOSE : QuoteType
				.valueOf(quoteType), priceType == null
				|| "".equals(priceType.trim()) ? PriceType.CURRENCY : PriceType
				.valueOf(priceType),
				sizeType == null || "".equals(sizeType.trim()) ? SizeType.VOLUME
						: SizeType.valueOf(priceType), instrument, tickerType == null
						|| "".equals(tickerType.trim()) ? TickerType.EXCHANGE_TICKER
						: TickerType.valueOf(tickerType), instrumentType, venue);
	}
	
	public SimpleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final SizeType sizeType, final String instrument,
			final TickerType tickerType, final String instrumentType,
			final String venue) {
		this(price, size, dateTime, quoteType, PriceType.CURRENCY, sizeType,
				instrument, tickerType, instrumentType, venue);
	}
	
	public SimpleQuote(final double price, final int size,
			final String dateTime, final QuoteType quoteType,
			final PriceType priceType, final SizeType sizeType,
			final String instrument, final TickerType tickerType,
			final String instrumentType, final String venue) {
		super(price, instrument, dateTime);
		
		final boolean errorFlag = !(sizeType == SizeType.ASK && quoteType == QuoteType.ASK)
				|| !(sizeType == SizeType.BID && quoteType == QuoteType.BID)
				|| !(sizeType == SizeType.LAST && (quoteType == QuoteType.OPEN
						|| quoteType == QuoteType.HIGH || quoteType == QuoteType.LOW || quoteType == QuoteType.CLOSE));
		
		if (errorFlag)
			throw new IllegalArgumentException("QuoteType(" + quoteType
					+ ") and SizeType(" + sizeType + ") are not consistant");
		
		this.size = size;
		this.quoteType = quoteType.toString();
		this.priceType = priceType.toString();
		this.sizeType = sizeType.toString();
		this.tickerType = tickerType.toString();
		this.instrumentType = instrumentType;
		this.venue = venue;
	}
	
	public PriceType priceType() {
		return PriceType.valueOf(this.priceType);
	}
	
	public String getPriceType() {
		return this.priceType;
	}
	
	public QuoteType quoteType() {
		return QuoteType.valueOf(this.quoteType);
	}
	
	public String getQuoteType() {
		return this.quoteType;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void priceType(final PriceType priceType) {
		this.priceType = priceType.toString();
	}
	
	public void setPriceType(final String priceType) {
		this.priceType = PriceType.valueOf(priceType).toString();
	}
	
	public void quoteType(final QuoteType quoteType) {
		this.quoteType = quoteType.toString();
	}
	
	public void setQuoteType(final String quoteType) {
		this.quoteType = QuoteType.valueOf(quoteType).toString();
	}
	
	public void sizeType(final SizeType sizeType) {
		this.sizeType = sizeType.toString();
	}
	
	public SizeType sizeType() {
		return SizeType.valueOf(this.sizeType);
	}
	
	public void setSize(final int size) {
		this.size = size;
	}
	
	public String getVenue() {
		return this.venue;
	}
	
	public void setVenue(final String venue) {
		this.venue = venue;
	}
	
	public String getInstrumentType() {
		return this.instrumentType;
	}
	
	public void setInstrumentType(final String instrumentType) {
		this.instrumentType = instrumentType;
	}
	
	public void fromInstrument(final Instrument i) {
		this.setInstrument(i.getInstrumentID());
		this.tickerType = "" + i.getTickerType();
		this.instrumentType = i.getInstrumentType();
		this.venue = i.instrumentVenueCode();
	}
	
	public Instrument asInstrument() {
		return new Instrument(this.getInstrument(), this.tickerType,
				this.instrumentType, this.venue);
	}
	
	public void fromVenue(final Venue i) {
		this.venue = i.getVenueID();
	}
	
	public Venue asVenue() {
		return new Venue(this.venue);
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	public void tickerType(final TickerType tickerType) {
		this.tickerType = tickerType.toString();
	}
	
	public void setTickerType(final String tickerType) {
		this.tickerType = TickerType.valueOf(tickerType).toString();
	}
	
	public TickerType tickerType() {
		return TickerType.valueOf(this.tickerType);
	}
	
	public String getTickerType() {
		return this.tickerType.toString();
	}
	
	public void setSizeType(final String sizeType) {
		this.sizeType = SizeType.valueOf(sizeType).toString();
	}
	
	public String getSizeType() {
		return this.sizeType;
	}
}