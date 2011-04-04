package TradeApp.Data;

import java.util.EventListener;

public interface DataListner<T extends DataItem> extends EventListener {
	void dataAvailable(T dataItem);
}
