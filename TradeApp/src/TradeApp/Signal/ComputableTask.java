package TradeApp.Signal;


public interface ComputableTask extends Computable {
	void startComputing(boolean updateInSeperateThread);
}
