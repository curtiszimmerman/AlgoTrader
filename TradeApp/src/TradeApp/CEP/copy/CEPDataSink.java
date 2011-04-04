
package TradeApp.CEP.copy;

import java.util.List;

import TradeApp.Data.DataSink;
import TradeApp.Data.TSDataItem;

public class CEPDataSink implements DataSink {
	
	@Override
	public <T extends TSDataItem> void writeOuputElements(final List<T> items) {
		for (final T i : items)
			CEPMan.getCEPMan().pumpEvent(i);
	}
	
}
