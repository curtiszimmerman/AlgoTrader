package TradeApp.Signal.Events;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import TradeApp.Data.TSDataItem;
import TradeApp.Util.BasicUtils;

public class SignalValue implements TSDataItem, Comparable<TSDataItem> {
	private static HashMap<String, Long>	   order	       = new HashMap<String, Long>();
	private final long	                       ord;
	private final String	                   name;
	private final double	                   param;
	private Date	                           date;
	private final SignalValue[]	               innerValues;
	private final HashMap<String, SignalValue>	innerValuesMap	= new HashMap<String, SignalValue>();
	
	public SignalValue(final String name, final String date,
	        final double param,
	        final SignalValue... innerValues) {
		this(name, BasicUtils.checkDateTime(date), param, innerValues);
	}
	
	public SignalValue(final String name, final Date date, final double param,
	        final SignalValue... innerValues) {
		this.name = name;
		this.date = date;
		this.param = param;
		this.innerValues = innerValues;
		
		synchronized (SignalValue.order) {
			if (!SignalValue.order.containsKey(name)) {
				SignalValue.order.put(name, 0L);
			}
			
			ord = SignalValue.order.get(name) + 1L;
		}
		
		innerValuesMap.put(name, this);
		
		for (final SignalValue sv : innerValues) {
			innerValuesMap.put(sv.getName(), sv);
		}
	}
	
	public SignalValue(final String name, final String innerName,
	        final String date, final double param,
	        final SignalValue... innerValues) {
		this(name, innerName, BasicUtils.checkDateTime(date), param,
		        innerValues);
	}
	
	public SignalValue(final String name, final String innerName,
	        final Date date, final double param,
	        final SignalValue... innerValues) {
		this.name = SignalValue.catName(name, innerName);
		this.date = date;
		this.param = param;
		this.innerValues = innerValues;
		
		synchronized (SignalValue.order) {
			if (!SignalValue.order.containsKey(name)) {
				SignalValue.order.put(name, 0L);
			}
			
			ord = SignalValue.order.get(name) + 1L;
		}
		
		innerValuesMap.put(name, this);
		
		for (final SignalValue sv : innerValues) {
			innerValuesMap.put(sv.getName(), sv);
		}
	}
	
	public SignalValue(final String name, final String innerName,
	        final String date, final double param) {
		this(name, innerName, BasicUtils.checkDateTime(date), param);
	}
	
	public SignalValue(final String name, final String innerName,
	        final Date date, final double param) {
		this.name = SignalValue.catName(name, innerName);
		this.date = date;
		this.param = param;
		innerValues = null;
		
		synchronized (SignalValue.order) {
			if (!SignalValue.order.containsKey(name)) {
				SignalValue.order.put(name, 0L);
			}
			
			ord = SignalValue.order.get(name) + 1L;
		}
		
		innerValuesMap.put(name, this);
		
		for (final SignalValue sv : innerValues) {
			innerValuesMap.put(sv.getName(), sv);
		}
	}
	
	public double getValue() {
		return param;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
	
	@Override
	public void setDate(final String date) {
		this.date = this.date = BasicUtils.checkDateTime(date);;
	}
	
	@Override
	public String getDate() {
		return date.toString();
	}
	
	@Override
	public double getTypicalValue() {
		return getValue();
	}
	
	@Override
	public int compareTo(final TSDataItem o) {
		return (int) Math.signum(o.getTypicalValue() - this.getTypicalValue());
	}
	
	@Override
	public Date getDateValue() {
		return date;
	}
	
	public static String catName(final String name, final String innerName) {
		return new StringBuilder(name).append("; Inner Name: ")
		        .append(innerName)
		        .toString();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public double getTypicalValue(final int i) {
		final Map.Entry<String, SignalValue> entry = (Entry<String, SignalValue>) innerValuesMap
		        .entrySet().toArray()[i];
		return entry.getValue().getTypicalValue();
	}
	
	@Override
	public int[] getRange() {
		return new int[] {
		        0, innerValuesMap.size()
		};
	}
	
	@Override
	public double getTypicalValue(final String name) {
		return innerValuesMap.get(name).getTypicalValue();
	}
	
	@Override
	public String[] getNames() {
		return innerValuesMap.keySet().toArray(new String[] {});
	}
	
	public SignalValue[] getInnerValues() {
		return innerValues;
	}
	
	public SignalValue getInnerValues(final String name) {
		return innerValuesMap.get(name);
	}
	
	public long getOrd() {
		return ord;
	}
	
}
