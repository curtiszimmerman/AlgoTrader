package TradeApp.Signal.Events.copy;

import java.util.Date;

public class FracturalOnePoleAdaptiveAutoGainFilterValue extends SignalValue {
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final String date,
		final double param, final SignalValue... innerValues) {
		super(name, date, param, innerValues);
	}
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final Date date,
		final double param, final SignalValue... innerValues) {
		super(name, date, param, innerValues);
	}
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final String innerName, final String date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, date, param, innerValues);
	}
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final String innerName, final Date date, final double param,
		final SignalValue... innerValues) {
		super(name, innerName, date, param, innerValues);
	}
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final String innerName, final String date, final double param) {
		super(name, innerName, date, param);
	}
	
	public FracturalOnePoleAdaptiveAutoGainFilterValue(final String name,
		final String innerName, final Date date, final double param) {
		super(name, innerName, date, param);
	}
	
}
