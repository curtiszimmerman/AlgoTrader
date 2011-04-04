package TradeApp.Signal;

import java.util.List;

public interface ParameterisedSignalComputation extends ComputableSignal {
	<T> void setParams(List<T> params);
}
