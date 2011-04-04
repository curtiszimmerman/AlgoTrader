
package TradeApp.Signal.copy;

import java.util.List;

import TradeApp.Data.TSDataItem;

public interface ComputablePipe {
	<T extends TSDataItem, K extends TSDataItem> List<T> compute(List<K> in);
}
