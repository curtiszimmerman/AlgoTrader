
package TradeApp.Algo.Stratergy.copy;

import TradeApp.Algo.Algo;

public interface Stratergy extends Algo {
	public void init();
	
	public void connected(String broker, String account);
	
	public void disconnected(String broker, String account);
	
	public void suspend();
	
	public void resume();
	
	public void stop();
}
