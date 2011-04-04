package TradeApp.Signal;

import java.util.List;

import TradeApp.Data.HasTypicalValue;

public interface ComputableSignal {
	<T extends HasTypicalValue, K extends HasTypicalValue> List<T> compute(
		List<K> input);
}
