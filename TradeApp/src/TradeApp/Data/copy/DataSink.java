
package TradeApp.Data.copy;

import java.util.List;

public interface DataSink {
	<T extends TSDataItem> void writeOuputElements(final List<T> items);
}
