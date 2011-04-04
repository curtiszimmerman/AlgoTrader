package TradeApp.Signal.MA;

import java.util.ArrayList;
import java.util.Date;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.ExpMAValue;
import TradeApp.Signal.Events.SignalValue;

public class ExpMA<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	private double			val;
	private final double	param;
	private TSDataItem	tmp;
	
	public ExpMA(final String ticker, final int len, final int inputSize,
			final int outputSize) {
		super(ticker, len, inputSize, outputSize);
		this.param = 2 / (len + 1);
		
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
		final ArrayList<TSDataItem> out = new ArrayList<TSDataItem>();
		
		for (final TSDataItem dat : this.readInputElements()) {
			this.val = this.param * dat.getTypicalValue() + (1 - this.param)
					* this.tmp.getTypicalValue();
			
			final SignalValue sv = new ExpMAValue(this.getName(),
					((T) dat).getDate(), this.val);
			
			out.add(sv);
			this.tmp = sv;
		}
		
		this.writeOuputElements(out);
	}
}
