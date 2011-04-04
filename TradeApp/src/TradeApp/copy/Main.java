
package TradeApp.copy;

import javax.swing.JFrame;

import TradeApp.IB.IBClient;
import TradeApp.Tests.IBDataGet;
import TradeApp.Util.Loggable;

public class Main extends JFrame implements Loggable, Runnable {
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Main m = new Main();
		m.setVisible(true);
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Runtime.getRuntime().addShutdownHook(new Thread(m));
		
		new IBDataGet();
		// new BasicSimpleBarDataFeed();
	}
	
	@Override
	public void run() {
		IBClient.disconnect();
	}
}
