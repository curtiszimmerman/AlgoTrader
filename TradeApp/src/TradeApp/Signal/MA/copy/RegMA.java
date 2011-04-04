package TradeApp.Signal.MA.copy;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.math.stat.regression.SimpleRegression;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.RegMAValue;
import TradeApp.Signal.Events.SignalValue;

public class RegMA<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	private double									val;
	private final LinkedList<TSDataItem>	tmp	= new LinkedList<TSDataItem>();
	
	public RegMA(final String ticker, final int len, final int inputSize,
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
		
		for (int i = 0; i < 2 * len; i++)
			this.tmp.add(TSItem);
	}
	
	private final long		milliSecPerDay	= 1000 * 60 * 60 * 24;
	final SimpleRegression	sr					= new SimpleRegression();
	
	@Override
	public synchronized void compute() {
		final ArrayList<TSDataItem> out = new ArrayList<TSDataItem>();
		
		for (final TSDataItem dat : this.readInputElements()) {
			this.tmp.add(dat);
			final T deQueued = (T) this.tmp.pollFirst();
			
			final double daysPrevVal = dat.getDateValue().getTime()
					/ this.milliSecPerDay;
			this.sr.addData(dat.getTypicalValue(), daysPrevVal);
			
			final double daysDeQueued = deQueued.getDateValue().getTime()
					/ this.milliSecPerDay;
			this.sr.removeData(deQueued.getTypicalValue(), daysDeQueued);
			
			this.val = this.sr.predict(daysPrevVal);
			
			out.add(new RegMAValue(this.getName(), ((T) dat).getDate(), this.val,
					new SignalValue(this.getName(), "slope", ((T) dat).getDate(),
							this.sr.getSlope()), new SignalValue(this.getName(),
							"RSqr", ((T) dat).getDate(), this.sr.getRSquare())));
		}
		
		this.writeOuputElements(out);
	}
}
