import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.esper.epl.EPLDebug;
import com.ceptrader.esper.epl.scripts.EsperEPLUtils;
import com.ceptrader.tradeapp.ib.esper.adapters.IBClient;
import com.ceptrader.tradeapp.ib.util.IBUtils;
import com.ceptrader.tradeapp.util.BasicUtils;
import com.ceptrader.tradeapp.util.Loggable;
import com.espertech.esper.client.deploy.DeploymentResult;

public class Main extends JFrame implements
        Loggable, Runnable {
	private static final long	serialVersionUID	= 1L;
	private final JButton	  exit	             = new JButton("Exit");
	private final JButton	  start	             = new JButton("Start");
	private final JPanel	  pane	             = new JPanel();
	
	public Main() {
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				BasicUtils.getThreadPool().execute(new Runnable() {
					
					@Override
					public void run() {
						IBUtils.reqQuotes("CASH", "USD", "EUR", "IDEALPRO", 0);
					}
				});
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		pane.add(start);
		pane.add(exit);
		
		getContentPane().add(pane);
		setSize(100, 200);
		
		validateTree();
		setVisible(true);
	}
	
	private static void initCEP() {
		if (!CEPMan.getCEPMan().isStarted()) {
			CEPMan.getCEPMan().start();
		}
		
		final URL esperMain = Main.class.getResource("EsperMain.epl");
		final URL ibInit = EsperEPLUtils.class.getResource("IBInit.epl");
		final URL sampleIndicators = EsperEPLUtils.class
		        .getResource("SampleIndicators.epl");
		final URL ibTradeRouting = EsperEPLUtils.class
		        .getResource("IBTradeRouting.epl");
		
		Logger.log((DeploymentResult) CEPMan.getCEPMan().deploy(esperMain));
		Logger.log((DeploymentResult) CEPMan.getCEPMan().deploy(ibInit));
		Logger.log((DeploymentResult) CEPMan.getCEPMan().deploy(
		        sampleIndicators));
		Logger.log((DeploymentResult) CEPMan.getCEPMan().deploy(ibTradeRouting));
		
		final EPLDebug db = new EPLDebug();
	}
	
	public static void main(final String[] args) {
		Main.initCEP();
		
		final Main m = new Main();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Runtime.getRuntime().addShutdownHook(new Thread(m));
	}
	
	@Override
	public void run() {
		IBClient.disconnect();
	}
}
