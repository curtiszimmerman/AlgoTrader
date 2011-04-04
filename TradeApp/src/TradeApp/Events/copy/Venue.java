
package TradeApp.Events.copy;

import TradeApp.Util.BasicUtils;

public class Venue {
	private String	venueID;
	private String	name;
	private String type;
	
	@Deprecated
	public Venue() {}
	
	public Venue(final String venueID) {
		this.venueID = venueID;
		this.name = venueID;
	}
	
	public Venue(final String venueID, final String name) {
		this.venueID = venueID;
		this.name = name;
	}	
	
	public Venue(final String venueID, final String name, String type) {
		this.venueID = venueID;
		this.name = name;
		this.type = type;
	}
	
	public String getVenueID() {
		return venueID;
	}
	
	@Override
	public int hashCode() {
		return venueID.hashCode();
	}
	
	public void setVenueID(final String venue) {
		this.venueID = venue;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
