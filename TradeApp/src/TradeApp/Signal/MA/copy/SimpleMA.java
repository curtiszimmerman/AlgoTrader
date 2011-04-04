package TradeApp.Signal.MA.copy;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import TradeApp.Data.DataItem;
import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.SignalValue;
import TradeApp.Signal.Events.SimpleMAValue;

public class SimpleMA<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	
	private double									val;
	private final LinkedList<TSDataItem>	tmp	= new LinkedList<TSDataItem>();
	
	public SimpleMA(final String ticker, final int len, final int inputSize,
			final int outputSize) {
		super(ticker, len, inputSize, outputSize);
		
		final TSDataItem TSItem = new TSDataItem() {
			
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
		
		for (int i = 0; i < len; i++)
			this.tmp.add(TSItem);
	}
	
	@Override
	public synchronized void compute() {
		final ArrayList<TSDataItem> out = new ArrayList<TSDataItem>();
		
		for (final DataItem dat : this.readInputElements()) {
			final T prevVal = (T) this.tmp.pollFirst();
			
			this.val += (((T) dat).getTypicalValue() - prevVal.getTypicalValue())
					/ getLengthParam();
			
			final SignalValue sv = new SimpleMAValue(this.getName(),
					((T) dat).getDate(), this.val);
			out.add(sv);
			this.tmp.add(sv);
		}
		
		this.writeOuputElements(out);
	}
}
