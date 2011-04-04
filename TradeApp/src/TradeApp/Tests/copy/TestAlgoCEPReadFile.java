package TradeApp.Tests.copy;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import TradeApp.Algo.Algo;
import TradeApp.CEP.CEPMan;
import TradeApp.IB.IBAdapter;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.ib.client.Contract;

public class TestAlgoCEPReadFile implements Algo {

	public TestAlgoCEPReadFile() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		final IBAdapter iba = IBAdapter.getAdapter();
		final Contract c = new Contract();
		c.m_symbol = "EUR";
		c.m_secType = "CASH";
		c.m_exchange = "IDEALPRO";
		c.m_currency = "USD";

		iba.getClient().reqMktData(1, c, "EUR", true);
	}
}
