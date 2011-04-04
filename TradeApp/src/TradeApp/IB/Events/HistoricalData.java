
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class HistoricalData implements DataItem {
	private double		close;
	private int			count;
	private String		date;
	private boolean	hasGaps;
	private double		high;
	private double		low;
	private double		open;
	private int			reqId;
	private int			volume;
	private double		WAP;
	
	@Deprecated
	public HistoricalData() {}
	
	public HistoricalData(final int reqId, final String date, final double open,
			final double high, final double low, final double close,
			final int volume, final int count, final double WAP,
			final boolean hasGaps) {
		this.reqId = reqId;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.count = count;
		this.WAP = WAP;
		this.hasGaps = hasGaps;
	}
	
	public double getClose() {
		return this.close;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public double getHigh() {
		return this.high;
	}
	
	public double getLow() {
		return this.low;
	}
	
	public double getOpen() {
		return this.open;
	}
	
	public int getReqId() {
		return this.reqId;
	}
	
	public int getVolume() {
		return this.volume;
	}
	
	public double getWAP() {
		return this.WAP;
	}
	
	public boolean isHasGaps() {
		return this.hasGaps;
	}
	
	public void setClose(final double close) {
		this.close = close;
	}
	
	public void setCount(final int count) {
		this.count = count;
	}
	
	public void setDate(final String date) {
		this.date = date;
	}
	
	public void setHasGaps(final boolean hasGaps) {
		this.hasGaps = hasGaps;
	}
	
	public void setHigh(final double high) {
		this.high = high;
	}
	
	public void setLow(final double low) {
		this.low = low;
	}
	
	public void setOpen(final double open) {
		this.open = open;
	}
	
	public void setReqId(final int reqId) {
		this.reqId = reqId;
	}
	
	public void setVolume(final int volume) {
		this.volume = volume;
	}
	
	public void setWAP(final double wAP) {
		this.WAP = wAP;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
