package TradeApp.Signal.copy;

import TradeApp.Data.DataSink;
import TradeApp.Data.DataSource;

public interface SignalEventFramework {
	void listen(String... signal);
	
	void listen(String name, DataSource source);
	
	void regEventNotify(String name);
	
	void regEventNotify(String name, DataSink sink);
	
	void map(String out, String in);
}
