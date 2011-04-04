
package TradeApp.Data.copy;

import java.util.List;

import TradeApp.Signal.ComputableTask;

public interface Pipe extends HasPipeSourceSink, ComputableTask, Runnable,
		DataSource, DataSink {
	List<? extends TSDataItem> inputElements();
	
	List<? extends TSDataItem> outputElements();
	
	TSDataItem getInputLast();
	
	TSDataItem getInputValue(int i);
	
	TSDataItem getInputValueFromLast(int i);
	
	TSDataItem getOutputLast();
	
	TSDataItem getOutputValue(int i);
	
	TSDataItem getOutputValueFromLast(int i);
	
	int getInputLength();
	
	int getOutputLength();
	
	void dispose();
	
	boolean isDisposed();
}
