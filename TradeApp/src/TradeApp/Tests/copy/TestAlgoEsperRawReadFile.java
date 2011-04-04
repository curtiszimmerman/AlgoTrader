package TradeApp.Tests.copy;

import java.io.File;
import java.net.URL;
import java.util.Hashtable;

import TradeApp.Algo.Algo;
import TradeApp.CEP.CEPMan;
import TradeApp.Events.SimpleQuote;
import TradeApp.IB.IBAdapter;

import com.espertech.esper.client.*;
import com.espertech.esperio.AdapterCoordinator;
import com.espertech.esperio.AdapterCoordinatorImpl;
import com.espertech.esperio.AdapterInputSource;
import com.espertech.esperio.csv.CSVInputAdapter;
import com.espertech.esperio.csv.CSVInputAdapterSpec;
import com.ib.client.Contract;

public class TestAlgoEsperRawReadFile implements Algo {

	public TestAlgoEsperRawReadFile() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		Configuration configuration = new Configuration();
		configuration.addEventType("SimpleQuote", SimpleQuote.class);
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
		EPStatement stmt = epService.getEPAdministrator().createEPL(
	   "select * from SimpleQuote");
		
		AdapterInputSource source = new AdapterInputSource(new File("d:/Data/TradeApp/TestQuote.csv"));
		CSVInputAdapterSpec spec = new CSVInputAdapterSpec(source, "SimpleQuote");
		spec.setTimestampColumn("timestamp");
		CSVInputAdapter csv = new CSVInputAdapter(spec);
		
		AdapterCoordinator coordinator = new AdapterCoordinatorImpl(epService, true);
		coordinator.coordinate(csv);
		coordinator.start();
	}
}
