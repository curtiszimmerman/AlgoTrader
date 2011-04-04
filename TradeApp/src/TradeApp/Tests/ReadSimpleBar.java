package TradeApp.Tests;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import TradeApp.Algo.Algo;
import TradeApp.CEP.CEPMan;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

public class ReadSimpleBar implements Algo {
	public ReadSimpleBar() {
		final Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		CEPMan.getCEPMan().start(
			new String[] { "TradeApp.Events", "TradeApp.IB.Events" });
		final Hashtable<String, List<String>> eventNameToSource = new Hashtable<String, List<String>>();
		
		CEPMan
			.getCEPMan()
			.registerStatementListner(
				new String[] { "select * from SimpleBar" },
				new StatementAwareUpdateListener[] { new StatementAwareUpdateListener() {
					private int	i	= 1;
					
					@Override
					public synchronized void update(final EventBean[] arg0,
						final EventBean[] arg1, final EPStatement arg2,
						final EPServiceProvider arg3) {
						System.out.println("__________________________");
						System.out.println("Count: " + i++);
						System.out.println("EventBean[] arg0: "
							+ (arg0 == null ? "null" : Arrays.asList(arg0) + "; Len "
								+ arg0.length));
						System.out.println("EventBean[] arg1: "
							+ (arg1 == null ? "null" : Arrays.asList(arg1) + "; Len"
								+ arg1.length));
						System.out.println("EPStatement arg2: "
							+ (arg2 == null ? "null" : arg2));
						System.out.println("EPStatement arg3: "
							+ (arg3 == null ? "null" : arg3));
					}
					
				} });
		
		final String base = "file:///D:/Documents and Settings/bsf6951/workspace/TradeApp/TestFiles/";
		final String ext = ".csv";
		
		final LinkedList<String> theFiles = new LinkedList<String>();
		
		theFiles.add(base + "ADBE" + ext);
		theFiles.add(base + "ADSK" + ext);
		theFiles.add(base + "MSFT" + ext);
		theFiles.add(base + "ORCL" + ext);
		theFiles.add(base + "IBM" + ext);
		theFiles.add(base + "INTC" + ext);
		theFiles.add(base + "AMD" + ext);
		
		theFiles.add(base + "F" + ext);
		theFiles.add(base + "HOG" + ext);
		
		theFiles.add(base + "BA" + ext);
		theFiles.add(base + "GD" + ext);
		theFiles.add(base + "LMT" + ext);
		theFiles.add(base + "NOC" + ext);
		
		eventNameToSource.put("SimpleBar", theFiles);
		
		CEPMan.getCEPMan().addCSVDataSource(eventNameToSource, null, null);
		
		CEPMan.getCEPMan().startSimulation();
	}
}
