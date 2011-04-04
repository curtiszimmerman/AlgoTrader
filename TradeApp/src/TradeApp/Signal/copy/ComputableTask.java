package TradeApp.Signal.copy;


public interface ComputableTask extends Computable {
	void startComputing(boolean updateInSeperateThread);
}
