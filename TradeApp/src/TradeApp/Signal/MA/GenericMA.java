
package TradeApp.Signal.MA;

import TradeApp.Signal.ComputablePipe;

public class GenericMA extends AbstractMA {
	private final ComputablePipe	cs;
	
	public GenericMA(final String ticker, final MACompSig cs,
			final int inputSize, final int outputSize) {
		super(ticker, cs.getLengthParam(), inputSize, outputSize);
		this.cs = cs;
	}
	
	@Override
	public void compute() {
		this.writeOuputElements(this.cs.compute(this.readInputElements()));
	}
}
