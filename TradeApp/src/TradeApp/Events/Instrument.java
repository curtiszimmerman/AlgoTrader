
package TradeApp.Events;

import TradeApp.Events.Quote.TickerType;
import TradeApp.Util.BasicUtils;

public class Instrument {
	private String	instrumentID;
	private Venue	instrumentVenue;
	private String tickerType;
	private String	instrumentType;
	private String	instrumentName;
	
	@Deprecated
	public Instrument() {}
	
	public Instrument(final String instrumentID, final TickerType tickerType,
	      final String instrumentType) {
		this.instrumentID = instrumentID;
		this.tickerType = tickerType.toString();
		this.instrumentType = instrumentType;
	}
	
	public Instrument(final String instrumentID, final TickerType tickerType,
	      final String instumentType,
	      final String instrumentVenueCode) {
		this.instrumentID = instrumentID;
		this.tickerType = tickerType.toString();
		this.instrumentType = instumentType;
		instrumentVenue = new Venue(instrumentVenueCode);
		this.instrumentName = instrumentID;
	}
	
	public Instrument(final String instrumentID, final TickerType tickerType,
	      final String instrumentType,
	      final Venue instrumentVenue) {
		this.instrumentID = instrumentID;
		this.tickerType = tickerType.toString();
		this.instrumentType = instrumentType;
		this.instrumentVenue = instrumentVenue;
		this.instrumentName = instrumentID;
	}
	
	public Instrument(final String instrumentID, final TickerType tickerType,
	      final String instumentType,
	      final String instrumentVenueCode, 
	      final String instrumentName) {
		this.instrumentID = instrumentID;
		this.tickerType = tickerType.toString();
		this.instrumentType = instumentType;
		instrumentVenue = new Venue(instrumentVenueCode);
		this.instrumentName = instrumentName;
	}
	
	public Instrument(final String instrumentID, final TickerType tickerType,
	      final String instumentType,
	      final Venue instrumentVenue,
	      final String instrumentName) {
		this.instrumentID = instrumentID;
		this.tickerType = tickerType.toString();
		this.instrumentType = instumentType;
		this.instrumentVenue = instrumentVenue;
		this.instrumentName = instrumentName;
	}	
	
	public Instrument(final String instrumentID, final String tickerType,
		final String instrumentType, final String instrumentVenue,
      final String instrumentName) {
		this(instrumentID, 
			TickerType.valueOf(tickerType),
			instrumentType,
	      instrumentVenue,
	      instrumentName);
	}

	public Instrument(final String instrumentID, final String tickerType,
		final String instrumentType, final String instrumentVenue) {
		this(instrumentID, 
			TickerType.valueOf(tickerType),
			instrumentType,
	      instrumentVenue);		
	}

	public String getInstrumentID() {
		return instrumentID;
	}
	
	public Venue instrumentVenue() {
		return instrumentVenue;
	}
	
	public String instrumentVenueCode() {
		return instrumentVenue == null ? null : instrumentVenue.getVenueID();
	}
	
	public String getInstrumentType() {
		return instrumentType;
	}
	
	public void setInstrumentID(final String instrument) {
		this.instrumentID = instrument;
	}
	
	public void fromInstrumentVenue(final Venue instrumentVenue) {
		this.instrumentVenue = instrumentVenue;
	}
	
	public void setInstrumentVenueCode(final String instrumentVenue) {
		this.instrumentVenue = new Venue(instrumentVenue);
	}
	
	public void setInstrumentType(final String instumentType) {
		this.instrumentType = instumentType;
	}
	
	public void fromVenueAsString(final String i) {
		this.instrumentVenue = new Venue(i);
	}
	
	public String venueAsString() {
		return instrumentVenue.getVenueID();
	}
	
	@Override
	public int hashCode() {
		return instrumentID.hashCode();
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}

	public void tickerType(TickerType tickerType) {
		this.tickerType = tickerType.toString();
	}

	public void setTickerType(String tickerType) {
		this.tickerType = TickerType.valueOf(tickerType).toString();
	}
	
	public TickerType tickerType() {
		return TickerType.valueOf(tickerType);
	}
	
	public String getTickerType() {
		return tickerType;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getInstrumentName() {
		return instrumentName;
	}
}
