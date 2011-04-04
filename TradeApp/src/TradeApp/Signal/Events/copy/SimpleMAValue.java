package TradeApp.Signal.Events.copy;

import java.util.Date;

import TradeApp.Util.BasicUtils;

public class SimpleMAValue extends SignalValue {
	public SimpleMAValue(final String name, final String date,
		final double param,
		final SignalValue... innerValues) {
		super(name, BasicUtils.checkDateTime(date), param, innerValues);
	}
	
	public SimpleMAValue(final String name, final Date date, final double param,
		final SignalValue... innerValues) {
		super(name, date, param, innerValues);
	}
	
	public SimpleMAValue(final String name, final String innerName,
		final String date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, BasicUtils.checkDateTime(date), param,
				innerValues);
	}
	
	public SimpleMAValue(final String name, final String innerName,
		final Date date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, date, param, innerValues);
	}
	
	public SimpleMAValue(final String name, final String innerName,
		final String date, final double param) {
		super(name, innerName, BasicUtils.checkDateTime(date), param);
	}
	
	public SimpleMAValue(final String name, final String innerName,
		final Date date, final double param) {
		super(name, innerName, date, param);
	}
}
