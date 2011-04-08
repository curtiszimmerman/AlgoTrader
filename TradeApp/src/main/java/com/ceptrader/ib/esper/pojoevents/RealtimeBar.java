package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class RealtimeBar implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private double	          close;
	private int	              count;
	private double	          high;
	private double	          low;
	private double	          open;
	private int	              reqId;
	private long	          time;
	private long	          volume;
	private double	          WAP;
	
	@Deprecated
	public RealtimeBar() {
	}
	
	public RealtimeBar(final int reqId, final long time, final double open,
	        final double high, final double low, final double close,
	        final long volume, final double wap, final int count) {
		this.reqId = reqId;
		this.time = time;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
	
	public double getHigh() {
		return high;
	}
	
	public double getLow() {
		return low;
	}
	
	public double getOpen() {
		return open;
	}
	
	public int getReqId() {
		return reqId;
	}
	
	public long getTime() {
		return time;
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
	
	public void setTime(final long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	public void setClose(final double close) {
		this.close = close;
	}
	
	public double getClose() {
		return close;
	}
	
	public void setCount(final int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setVolume(final long volume) {
		this.volume = volume;
	}
	
	public long getVolume() {
		return volume;
	}
	
	public void setWAP(final double wap) {
		WAP = wap;
	}
	
	public double getWAP() {
		return WAP;
	}
}
