package TradeApp.Signal.Filter;

import java.util.*;

import TradeApp.Data.DataItem;
import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.FisherValue;
import TradeApp.Signal.Events.SignalValue;
import TradeApp.Signal.MA.AbstractMA;
import TradeApp.Signal.MA.MA;

public class Fisher<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	
	private double									tmpVal;
	double											fisherTransform;
	
	private final LinkedList<TSDataItem>	tmpRng	= new LinkedList<TSDataItem>();
	
	public Fisher(final String ticker, final double len, final int inputSize,
			final int outputSize) {
		super(ticker, len, inputSize, outputSize);
		
		for (int i = 0; i < len; i++) {
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
			
			this.tmpRng.add(TSItem);
		}
	}
	
	private final Comparator<TSDataItem>	comp	= new Comparator<TSDataItem>() {
																	
																	@Override
																	public int compare(
																			final TSDataItem o1,
																			final TSDataItem o2) {
																		return (int) Math
																				.signum(o1
																						.getTypicalValue()
																						- o2
																							.getTypicalValue());
																	}
																};
	
	@Override
	public void compute() {
		final ArrayList<TSDataItem> out = new ArrayList<TSDataItem>();
		
		for (final DataItem dat : this.readInputElements()) {
			this.tmpRng.add((T) dat);
			this.tmpRng.pollFirst();
			
			final double max = Collections.max(this.tmpRng, this.comp)
					.getTypicalValue();
			final double min = Collections.min(this.tmpRng, this.comp)
					.getTypicalValue();
			
			this.tmpVal = 1 / 3 * 2
					* ((((T) dat).getTypicalValue() - min) / (max - min) - 0.5) + 2
					/ 3 * this.tmpVal;
			
			this.fisherTransform = 0.5
					* Math.log((1 + this.tmpVal) / (1 - this.tmpVal)) + 0.5
					* this.fisherTransform;
			
			final SignalValue sv = new FisherValue(this.getName(),
					((T) dat).getDate(), this.fisherTransform, new SignalValue(
							this.getName(), "oscillator", ((T) dat).getDate(),
							this.tmpVal));
			
			out.add(sv);
		}
		
		this.writeOuputElements(out);
	}
}
