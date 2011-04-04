package TradeApp.Signal.Events;

import java.util.Date;

import TradeApp.Util.BasicUtils;

public class FrAMAValue extends SignalValue {
	public FrAMAValue(final String name, final String date,
		final double param,
		final SignalValue... innerValues) {
		super(name, BasicUtils.checkDateTime(date), param, innerValues);
	}
	
	public FrAMAValue(final String name, final Date date, final double param,
		final SignalValue... innerValues) {
		super(name, date, param, innerValues);
	}
	
	public FrAMAValue(final String name, final String innerName,
		final String date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, BasicUtils.checkDateTime(date), param,
				innerValues);
	}
	
	public FrAMAValue(final String name, final String innerName,
		final Date date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, date, param, innerValues);
	}
	
	public FrAMAValue(final String name, final String innerName,
		final String date, final double param) {
		super(name, innerName, BasicUtils.checkDateTime(date), param);
	}
	
	public FrAMAValue(final String name, final String innerName,
		final Date date, final double param) {
		super(name, innerName, date, param);
	}
}
