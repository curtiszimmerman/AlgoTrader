package TradeApp.Tests.copy;

import java.util.Arrays;
import java.util.Hashtable;

import TradeApp.Algo.Algo;
import TradeApp.CEP.CEPMan;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class TestAlgoReadQuotePriceFromFile implements Algo {
	
	public TestAlgoReadQuotePriceFromFile() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		CEPMan.getCEPMan().start(
			new String[] { "TradeApp.Events", "TradeApp.IB.Events" });
		final Hashtable<String, String> eventNameToSource = new Hashtable<String, String>();
		final Hashtable<String, String> timeStampCol = new Hashtable<String, String>();
		final Hashtable<String, Integer> eventsPerSecond = new Hashtable<String, Integer>();
		
		CEPMan
			.getCEPMan()
			.registerStatementListner(
				new String[] { "select * from SimpleQuote" },
				new StatementAwareUpdateListener[] { new StatementAwareUpdateListener() {
					private int	i	= 1;
					
					@Override
					public void update(final EventBean[] arg0,
						final EventBean[] arg1, final EPStatement arg2,
						final EPServiceProvider arg3) {
						System.out.println("__________________________");
						System.out.println("Count: " + i++);
						System.out.println("EventBean[] arg0: "
							+ (arg0 == null ? "null" : Arrays.asList(arg0)));
						System.out.println("EventBean[] arg1: "
							+ (arg1 == null ? "null" : Arrays.asList(arg1)));
						System.out.println("EPStatement arg2: "
							+ (arg2 == null ? "null" : arg2));
						System.out.println("EPStatement arg3: "
							+ (arg3 == null ? "null" : arg3));
					}
					
				} });
		
		eventNameToSource
			.put(
				"SimpleQuote",
				"file:///C:/Users/sirinath/workspace/TradeApp/TestFiles/TestQuote.csv");
		timeStampCol.put("SimpleQuote", "timestamp");
		
		CEPMan.getCEPMan().addACSVDataSource(eventNameToSource, timeStampCol,
			eventsPerSecond);
		
		CEPMan.getCEPMan().startSimulation();
	}
}
