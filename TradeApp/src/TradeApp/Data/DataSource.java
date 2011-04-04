
package TradeApp.Data;

import java.util.List;

public interface DataSource {
	<T extends TSDataItem> List<T> readInputElements();
}
