package TradeApp.Signal.Filter.copy;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.SignalValue;
import TradeApp.Signal.Events.SwissArmyKnifeValue;
import TradeApp.Signal.MA.AbstractMA;
import TradeApp.Signal.MA.MA;

public class SwissArmyKnife<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	private double			val;
	
	private final Param	a[];
	private final Param	b[];
	private final Param	c[];
	
	private TSDataItem	tmp;
	
	public SwissArmyKnife(final String ticker, final Param[] a, final Param[] b,
			final Param[] c, final int N, final int inputSize, final int outputSize) {
		super(ticker, N, inputSize, outputSize);
		
		this.a = a;
		this.b = b;
		this.c = c;
		
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
			public void setDate(final String dateTime) {
			}
			
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
			
			final List<TSDataItem> in = inputElements();
			final List<TSDataItem> out = outputElements();
			
			final double a0 = this.a[0].getParam(in, out);
			final double a1 = this.a[1].getParam(in, out);
			final double b0 = this.b[0].getParam(in, out);
			final double b1 = this.b[1].getParam(in, out);
			final double b2 = this.b[2].getParam(in, out);
			final double c0 = this.c[0].getParam(in, out);
			final double c1 = this.c[1].getParam(in, out);
			
			this.val = c0
					* (b0 * dat.getTypicalValue() + b1
							* getInputLast().getTypicalValue() + b2
							* getInputValueFromLast(1).getTypicalValue())
					+ a0
					* getOutputLast().getTypicalValue()
					+ a1
					* getOutputValueFromLast(1).getTypicalValue()
					- c1
					* getInputValueFromLast((int) getLengthParam())
							.getTypicalValue();
			
			final SignalValue sv = new SwissArmyKnifeValue(this.getName(),
					((T) dat).getDate(), this.val, new SignalValue(this.getName(),
							"a0", ((T) dat).getDate(), a0), new SignalValue(
							this.getName(), "a1", ((T) dat).getDate(), a1),
					new SignalValue(this.getName(), "b0", ((T) dat).getDate(), b0),
					new SignalValue(this.getName(), "b1", ((T) dat).getDate(), b1),
					new SignalValue(this.getName(), "c0", ((T) dat).getDate(), c0),
					new SignalValue(this.getName(), "c1", ((T) dat).getDate(), c1));
			this.tmp = sv;
			
			this.writeOuputElements(Collections.singletonList(sv));
		}
	}
}
