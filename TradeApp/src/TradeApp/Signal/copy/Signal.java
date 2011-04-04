package TradeApp.Signal.copy;

import TradeApp.Data.HasPipeSourceSink;
import TradeApp.Data.Pipe;
import TradeApp.Data.SignalDataSink;
import TradeApp.Data.SignalDataSource;

public interface Signal extends Pipe, HasPipeSourceSink, ComputableTask,
        SignalDataSource, SignalDataSink, SignalEventFramework {
}
