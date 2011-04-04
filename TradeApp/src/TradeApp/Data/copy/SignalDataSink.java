package TradeApp.Data.copy;

import java.util.List;

public interface SignalDataSink {
	<T extends TSDataItem> void writeOuputElements(
	        String sig,
	        final List<T> items);
}
