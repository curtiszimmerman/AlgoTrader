
package TradeApp.Signal.copy;

public class GenericSignal extends AbstractSignal {
	private final ComputablePipe	cs;
	
	public GenericSignal(final String ticker, final ComputablePipe cs,
			final int inputSize, final int outputSize) {
		super(ticker, inputSize, outputSize);
		this.cs = cs;
	}
	
	@Override
	public void compute() {
		this.writeOuputElements(this.cs.compute(this.readInputElements()));
	}
	
}
