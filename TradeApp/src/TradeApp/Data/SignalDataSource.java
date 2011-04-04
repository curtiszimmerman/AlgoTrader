package TradeApp.Data;

import java.util.List;

public interface SignalDataSource {
	<T extends TSDataItem> List<T> readInputElements(String sig);
}
