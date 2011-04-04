package TradeApp.Signal.MA;

import java.util.*;

import TradeApp.Data.TSDataItem;
import TradeApp.Signal.Computable;
import TradeApp.Signal.Events.FrAMAValue;

public class FrAMA<T extends TSDataItem> extends AbstractMA implements
		Computable, MA {
	private static double	minAlpha	= 0.01;
	private double				val;
	private final double		param;
	
	public FrAMA(final String ticker, final int len, final double param,
			final int inputSize, final int outputSize) {
		super(ticker, len, inputSize, outputSize);
		this.param = param;
		
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
	
	private final LinkedList<TSDataItem>	tmp	= new LinkedList<TSDataItem>();
	
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
	public synchronized void compute() {
		final ArrayList<TSDataItem> out = new ArrayList<TSDataItem>();
		
		for (final TSDataItem dat : this.readInputElements()) {
			this.tmp.add(dat);
			this.tmp.pollFirst();
			
			final List<TSDataItem> L1 = this.tmp.subList(0,
					(int) (getLengthParam() - 1));
			final List<TSDataItem> L2 = this.tmp.subList(
					(int) getLengthParam(),
					(int) (getLengthParam() * 2 - 1));
			
			final double L1Max = Collections.max(L1, this.comp).getTypicalValue();
			final double L1Min = Collections.min(L1, this.comp).getTypicalValue();
			
			final double L2Max = Collections.max(L2, this.comp).getTypicalValue();
			final double L2Min = Collections.min(L2, this.comp).getTypicalValue();
			
			final double max = Collections.max(this.tmp, this.comp)
					.getTypicalValue();
			final double min = Collections.min(this.tmp, this.comp)
					.getTypicalValue();
			
			final double N1 = (L1Max - L1Min) / getLengthParam();
			final double N2 = (L2Max - L2Min) / getLengthParam();
			final double N3 = (max - min) / (2 * getLengthParam());
			
			final double d = (Math.log(N1 + N2) - Math.log(N3)) / Math.log(2);
			final double alpha = Math.max(
					Math.min(Math.exp(this.param * (d - 1)), 1), FrAMA.minAlpha);
			this.val = alpha * dat.getTypicalValue() + (1 - alpha)
					* this.tmp.get(this.tmp.size() - 2).getTypicalValue();
			
			out.add(new FrAMAValue(this.getName(), ((T) dat).getDate(), this.val));
		}
		
		this.writeOuputElements(out);
	}
}
