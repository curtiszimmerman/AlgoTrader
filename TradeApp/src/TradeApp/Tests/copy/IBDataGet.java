
package TradeApp.Tests.copy;

import TradeApp.Algo.Algo;
import TradeApp.IB.IBClient;

import com.ib.client.Contract;

public class IBDataGet implements Algo {
	public IBDataGet() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		final IBClient c = IBClient.getIBClient();
		
		IBClient.connect();
		final Contract contract = new Contract();
		contract.m_secType = "CASH";
		contract.m_currency = "USD";
		contract.m_symbol = "EUR";
		contract.m_exchange = "IDEALPRO";
		
		c.reqMktData(1, contract, null, false);
		
		c.reqCurrentTime();
	}
}
