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

public class BasicSimpleBarDataFeed implements Algo {
	public BasicSimpleBarDataFeed() {
		final Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		CEPMan.getCEPMan().start(
		        new String[] {
		                "TradeApp.Events", "TradeApp.IB.Events"
		        });
		final Hashtable<String, List<String>> eventNameToSource = new Hashtable<String, List<String>>();
		
		CEPMan.getCEPMan()
		        .registerStatementListner(
		                new String[] {
			                "select * from SimpleBar"
		                },
		                new StatementAwareUpdateListener[] {
			                new StatementAwareUpdateListener() {
				                private int	i	= 1;
				                
				                @Override
				                public synchronized
				                        void
				                        update(
				                                final EventBean[] newEvents,
				                                final EventBean[] oldEvents,
				                                final EPStatement statement,
				                                final EPServiceProvider epServiceProvider) {
					                dumpData(newEvents, oldEvents, statement,
					                        epServiceProvider);
				                }
				                
				                private
				                        void
				                        dumpData(
				                                final EventBean[] newEvents,
				                                final EventBean[] oldEvents,
				                                final EPStatement statement,
				                                final EPServiceProvider epServiceProvider) {
					                System.out
					                        .println("__________________________");
					                System.out.println("Count: " + i++);
					                System.out.println("EventBean[] arg0: "
					                        +
					                        (newEvents == null ? "null"
					                                : Arrays
					                                        .asList(newEvents)));
					                System.out.println("EventBean[] arg1: "
					                        +
					                        (oldEvents == null ? "null"
					                                : Arrays
					                                        .asList(oldEvents)));
					                System.out.println("EPStatement arg2: "
					                        +
					                        (statement == null ? "null"
					                                : statement));
					                System.out.println("EPStatement arg3: "
					                        +
					                        (epServiceProvider == null ? "null"
					                                : epServiceProvider));
				                }
				                
			                }
		                });
		
		final String base = "file:///C:/WorkSpace/Code/TestFiles/";
		final String ext = ".csv";
		
		final LinkedList<String> theFiles = new LinkedList<String>();
		
		theFiles.add(base + "ADBE" + ext);
		theFiles.add(base + "ADSK" + ext);
		theFiles.add(base + "MSFT" + ext);
		theFiles.add(base + "ORCL" + ext);
		theFiles.add(base + "IBM" + ext);
		theFiles.add(base + "INTC" + ext);
		theFiles.add(base + "AMD" + ext);
		
		theFiles.add(base + "FM" + ext);
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
