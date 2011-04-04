
package TradeApp.Signal.Filter;

import java.util.List;

import TradeApp.Data.TSDataItem;

public interface Param {
	double getParam(List<TSDataItem> in, List<TSDataItem> out);
}
