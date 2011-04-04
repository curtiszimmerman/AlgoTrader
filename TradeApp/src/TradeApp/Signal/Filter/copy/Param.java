
package TradeApp.Signal.Filter.copy;

import java.util.List;

import TradeApp.Data.TSDataItem;

public interface Param {
	double getParam(List<TSDataItem> in, List<TSDataItem> out);
}
