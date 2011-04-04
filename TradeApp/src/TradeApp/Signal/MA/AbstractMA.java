
package TradeApp.Signal.MA;

import TradeApp.Signal.AbstractSignal;

public abstract class AbstractMA extends AbstractSignal implements MA {
	private final double	len;
	
	private final String	name;
	
	public AbstractMA(final String TSName, final double len,
			final int inputSize, final int outputSize) {
		super(TSName, inputSize, outputSize);
		
		this.len = len;
		this.name = AbstractMA.getName(this.getName(), this, len);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public static String getName(final String TSName, final Object ma,
			final double len) {
		return new StringBuilder(AbstractSignal.getName(TSName, ma))
				.append("; Length = ").append(len).toString();
	}
	
	@Override
	public abstract void compute();
	
	@Override
	public double getLengthParam() {
		return this.len;
	}
}
