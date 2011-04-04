package TradeApp.Signal.Filter.copy;

import java.util.List;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Events.FracturalOnePoleAdaptiveAutoGainFilterValue;
import TradeApp.Signal.Events.SignalValue;

public class FracturalOnePoleAdaptiveAutoGainFilter<T extends TSDataItem>
		extends OnePoleAdaptiveAutoGainFilter<T> {
	
	public FracturalOnePoleAdaptiveAutoGainFilter(final String ticker,
			final int inputSize, final int outputSize) {
		super(ticker, inputSize, outputSize);
	}
	
	private int startIdx() {
		final List<TSDataItem> in = inputElements();
		
		return 0;
	}
	
	@Override
	protected void doAdj() {
	}
	
	@Override
	protected void genValue(final T dat, final SignalValue sv_rtn,
		final SignalValue sv_sdVal, final SignalValue sv_theMean,
		final SignalValue sv_slope, final SignalValue sv_rSqr,
		final SignalValue sv_sqEr, final SignalValue sv_regRtn,
		final SignalValue sv_len, final SignalValue sv_alpha,
		final SignalValue sv_gain1, final SignalValue sv_gain2,
		final SignalValue sv_regVal, final SignalValue svLnVal,
		final SignalValue svLn_altVal) {
		sv = new FracturalOnePoleAdaptiveAutoGainFilterValue(this.getName(),
			dat.getDate(),
				Math.exp(val), svLnVal, svLn_altVal, svLn_altVal, sv_gain1,
				sv_gain2, sv_regVal, sv_alpha, sv_len, sv_sqEr, sv_regRtn,
				sv_rSqr, sv_slope, sv_theMean, sv_sdVal, sv_rtn);
	}
}
