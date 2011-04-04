
package TradeApp.Signal.Filter.copy;

import java.util.Collections;
import java.util.Date;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.AbstractSignal;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Signal;
import TradeApp.Signal.Events.SignalValue;

public class GenericAdaptiveFilter<T extends TSDataItem> extends AbstractSignal
		implements Computable, Signal {
	private double				val;
	
	private double				smoothing;
	private double				gain;
	private final Gain		g;
	private final Smoothing	s;
	
	private TSDataItem		tmp;
	
	public GenericAdaptiveFilter(final String ticker, final Gain g,
			final Smoothing s, final int inputSize, final int outputSize) {
		super(ticker, inputSize, outputSize);
		
		this.g = g;
		this.s = s;
		
		this.tmp = new TSDataItem() {
			
			@Override
			public double getTypicalValue() {
				return 0;
			}
			
			@Override
			public double getTypicalValue(final int i) {
				return 0;
			}
			
			@Override
			public void setDate(final String dateTime) {}
			
			@Override
			public String getDate() {
				return null;
			}
			
			@Override
			public Date getDateValue() {
				return null;
			}
			
			@Override
			public int[] getRange() {
				return new int[] { 0, 0 };
			}
			
			@Override
			public double getTypicalValue(final String name) {
				return 0;
			}
			
			@Override
			public String[] getNames() {
				return null;
			}
		};
	}
	
	@Override
	public synchronized void compute() {
		for (final TSDataItem dat : this.readInputElements()) {
			this.smoothing = this.s.getParam(this.inputElements(),
					this.outputElements());
			this.gain = this.g.getParam(this.inputElements(),
					this.outputElements());
			
			final double param = this.smoothing * (1 + this.gain);
			
			this.val = param * dat.getTypicalValue() + (1 - param)
					* this.tmp.getTypicalValue();
			
			final SignalValue sv = new SignalValue(this.getName(),
					((T) dat).getDate(), this.val);
			this.tmp = sv;
			
			this.writeOuputElements(Collections.singletonList(sv));
		}
	}
}
