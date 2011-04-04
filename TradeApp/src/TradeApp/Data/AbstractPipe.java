package TradeApp.Data;

import java.util.LinkedList;
import java.util.List;

import TradeApp.Util.BasicUtils;

public abstract class AbstractPipe extends HasPipeSourceSinkImp implements
        Pipe, HasSource, HasSink {
	private final LinkedList<TSDataItem>	inputQueue	=
	                                                            new LinkedList<TSDataItem>();
	private final LinkedList<TSDataItem>	outputQueue	=
	                                                            new LinkedList<TSDataItem>();
	
	private int	                         inputSize;
	private int	                         outputSize;
	
	public AbstractPipe(final int inputSize, final int outputSize) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;
	}
	
	public AbstractPipe(final int inputSize, final int outputSize,
	        final DataSource source, final DataSink sink) {
		super(source, sink);
		
		this.inputSize = inputSize;
		this.outputSize = outputSize;
	}
	
	private boolean	isDisposed	= false;
	
	@Override
	final public void run() {
		for (; !isDisposed;) {
			compute();
		}
	}
	
	@Override
	public void startComputing(final boolean updateInSeperateThread) {
		if (updateInSeperateThread) {
			BasicUtils.getThreadPool().submit(this);
		} else {
			run();
		}
	}
	
	@Override
	public synchronized void dispose() {
		isDisposed = true;
	}
	
	@Override
	public boolean isDisposed() {
		return isDisposed;
	}
	
	@Override
	public abstract void compute();
	
	@Override
	final public <T extends TSDataItem> void writeOuputElements(
	        final List<T> items) {
		final DataSink s = this.getSink();
		
		synchronized (s) {
			synchronized (items) {
				synchronized (outputQueue) {
					s.writeOuputElements(items);
					outputQueue.addAll(items);
					
					for (int i = 0; i < items.size(); i++) {
						outputQueue.remove();
					}
				}
			}
		}
	}
	
	@Override
	final synchronized public <T extends TSDataItem> List<T>
	        readInputElements() {
		final DataSource s = this.getSource();
		synchronized (s) {
			synchronized (inputQueue) {
				final List<T> in = s.readInputElements();
				inputQueue.addAll(in);
				
				for (int i = 0; i < in.size(); i++) {
					inputQueue.remove();
				}
				
				return in;
			}
		}
	}
	
	@Override
	synchronized public List<TSDataItem> inputElements() {
		return inputQueue;
	}
	
	@Override
	synchronized public List<TSDataItem> outputElements() {
		return outputQueue;
	}
	
	@Override
	synchronized public int getInputLength() {
		return inputQueue.size();
	}
	
	@Override
	synchronized public int getOutputLength() {
		return outputQueue.size();
	}
	
	@Override
	synchronized public TSDataItem getInputLast() {
		return inputQueue.peekLast();
	}
	
	@Override
	synchronized public TSDataItem getInputValue(final int i) {
		return inputQueue.get(i);
	}
	
	@Override
	synchronized public TSDataItem getOutputValue(final int i) {
		return outputQueue.get(i);
	}
	
	@Override
	synchronized public TSDataItem getOutputLast() {
		return outputQueue.peekLast();
	}
	
	synchronized public void setInputSize(final int inputSize) {
		this.inputSize = inputSize;
	}
	
	public int getInputSize() {
		return inputSize;
	}
	
	synchronized public void setOutputSize(final int outputSize) {
		this.outputSize = outputSize;
	}
	
	public int getOutputSize() {
		return outputSize;
	}
	
	@Override
	public TSDataItem getInputValueFromLast(final int i) {
		synchronized (inputQueue) {
			return inputQueue.get(inputQueue.size() - 1 - i);
		}
	}
	
	@Override
	public TSDataItem getOutputValueFromLast(final int i) {
		synchronized (outputQueue) {
			return outputQueue.get(outputQueue.size() - 1 - i);
		}
	}
}
