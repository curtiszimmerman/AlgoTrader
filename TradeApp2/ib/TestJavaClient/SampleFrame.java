/*
 * SampleFrame.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.EWrapperMsgGenerator;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;
import com.ib.client.Util;

class SampleFrame extends JFrame implements EWrapper {
	/**
     * 
     */
	private static final long	serialVersionUID	    = 1L;
	private static final int	NOT_AN_FA_ACCOUNT_ERROR	= 321;
	private final int	      faErrorCodes[]	        = {
	        503, 504, 505, 522, 1100, SampleFrame.NOT_AN_FA_ACCOUNT_ERROR
	                                                    };
	private boolean	          faError;
	
	EClientSocket	          m_client	                = new EClientSocket(
	                                                            this);
	IBTextPanel	              m_tickers	                = new IBTextPanel(
	                                                            "Market and Historical Data",
	                                                            false);
	IBTextPanel	              m_TWS	                    = new IBTextPanel(
	                                                            "TWS Server Responses",
	                                                            false);
	IBTextPanel	              m_errors	                = new IBTextPanel(
	                                                            "Errors and Messages",
	                                                            false);
	OrderDlg	              m_orderDlg	            = new OrderDlg(this);
	ExtOrdDlg	              m_extOrdDlg	            = new ExtOrdDlg(
	                                                            m_orderDlg);
	AccountDlg	              m_acctDlg	                = new AccountDlg(this);
	MktDepthDlg	              m_mktDepthDlg	            = new MktDepthDlg(this);
	NewsBulletinDlg	          m_newsBulletinDlg	        = new NewsBulletinDlg(
	                                                            this);
	ScannerDlg	              m_scannerDlg	            = new ScannerDlg(this);
	
	String	                  faGroupXML;
	String	                  faProfilesXML;
	String	                  faAliasesXML;
	public String	          m_FAAcctCodes;
	public boolean	          m_bIsFAAccount	        = false;
	
	private boolean	          m_disconnectInProgress	= false;
	
	SampleFrame() {
		final JPanel scrollingWindowDisplayPanel = new JPanel(new GridLayout(0,
		        1));
		scrollingWindowDisplayPanel.add(m_tickers);
		scrollingWindowDisplayPanel.add(m_TWS);
		scrollingWindowDisplayPanel.add(m_errors);
		
		final JPanel buttonPanel = createButtonPanel();
		
		getContentPane().add(scrollingWindowDisplayPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.EAST);
		setSize(600, 700);
		setTitle("Sample");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JPanel createButtonPanel() {
		final JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		final JButton butConnect = new JButton("Connect");
		butConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onConnect();
			}
		});
		final JButton butDisconnect = new JButton("Disconnect");
		butDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onDisconnect();
			}
		});
		final JButton butMktData = new JButton("Req Mkt Data");
		butMktData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqMktData();
			}
		});
		final JButton butCancelMktData = new JButton("Cancel Mkt Data");
		butCancelMktData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelMktData();
			}
		});
		final JButton butMktDepth = new JButton("Req Mkt Depth");
		butMktDepth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqMktDepth();
			}
		});
		final JButton butCancelMktDepth = new JButton("Cancel Mkt Depth");
		butCancelMktDepth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelMktDepth();
			}
		});
		final JButton butHistoricalData = new JButton("Historical Data");
		butHistoricalData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onHistoricalData();
			}
		});
		final JButton butCancelHistoricalData = new JButton("Cancel Hist. Data");
		butCancelHistoricalData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelHistoricalData();
			}
		});
		final JButton butRealTimeBars = new JButton("Req Real Time Bars");
		butRealTimeBars.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqRealTimeBars();
			}
		});
		final JButton butCancelRealTimeBars = new JButton(
		        "Cancel Real Time Bars");
		butCancelRealTimeBars.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelRealTimeBars();
			}
		});
		final JButton butCurrentTime = new JButton("Req Current Time");
		butCurrentTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqCurrentTime();
			}
		});
		final JButton butScanner = new JButton("Market Scanner");
		butScanner.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onScanner();
			}
		});
		final JButton butOpenOrders = new JButton("Req Open Orders");
		butOpenOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqOpenOrders();
			}
		});
		final JButton butCalculateImpliedVolatility = new JButton(
		        "Calculate Implied Volatility");
		butCalculateImpliedVolatility.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCalculateImpliedVolatility();
			}
		});
		final JButton butCancelCalculateImpliedVolatility = new JButton(
		        "Cancel Calc Impl Volatility");
		butCancelCalculateImpliedVolatility
		        .addActionListener(new ActionListener() {
			        @Override
			        public void actionPerformed(final ActionEvent e) {
				        onCancelCalculateImpliedVolatility();
			        }
		        });
		final JButton butCalculateOptionPrice = new JButton(
		        "Calculate Option Price");
		butCalculateOptionPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCalculateOptionPrice();
			}
		});
		final JButton butCancelCalculateOptionPrice = new JButton(
		        "Cancel Calc Opt Price");
		butCancelCalculateOptionPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelCalculateOptionPrice();
			}
		});
		final JButton butWhatIfOrder = new JButton("What If");
		butWhatIfOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onWhatIfOrder();
			}
		});
		final JButton butPlaceOrder = new JButton("Place Order");
		butPlaceOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onPlaceOrder();
			}
		});
		final JButton butCancelOrder = new JButton("Cancel Order");
		butCancelOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelOrder();
			}
		});
		final JButton butExerciseOptions = new JButton("Exercise Options");
		butExerciseOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onExerciseOptions();
			}
		});
		final JButton butExtendedOrder = new JButton("Extended");
		butExtendedOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onExtendedOrder();
			}
		});
		final JButton butAcctData = new JButton("Req Acct Data");
		butAcctData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqAcctData();
			}
		});
		final JButton butContractData = new JButton("Req Contract Data");
		butContractData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqContractData();
			}
		});
		final JButton butExecutions = new JButton("Req Executions");
		butExecutions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqExecutions();
			}
		});
		final JButton butNewsBulletins = new JButton("Req News Bulletins");
		butNewsBulletins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqNewsBulletins();
			}
		});
		final JButton butServerLogging = new JButton("Server Logging");
		butServerLogging.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onServerLogging();
			}
		});
		final JButton butAllOpenOrders = new JButton("Req All Open Orders");
		butAllOpenOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqAllOpenOrders();
			}
		});
		final JButton butAutoOpenOrders = new JButton("Req Auto Open Orders");
		butAutoOpenOrders.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqAutoOpenOrders();
			}
		});
		final JButton butManagedAccts = new JButton("Req Accounts");
		butManagedAccts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReqManagedAccts();
			}
		});
		final JButton butFinancialAdvisor = new JButton("Financial Advisor");
		butFinancialAdvisor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onFinancialAdvisor();
			}
		});
		final JButton butClear = new JButton("Clear");
		butClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onClear();
			}
		});
		final JButton butClose = new JButton("Close");
		butClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onClose();
			}
		});
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(butConnect);
		buttonPanel.add(butDisconnect);
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(butMktData);
		buttonPanel.add(butCancelMktData);
		buttonPanel.add(butMktDepth);
		buttonPanel.add(butCancelMktDepth);
		buttonPanel.add(butHistoricalData);
		buttonPanel.add(butCancelHistoricalData);
		buttonPanel.add(butRealTimeBars);
		buttonPanel.add(butCancelRealTimeBars);
		buttonPanel.add(butScanner);
		buttonPanel.add(butCurrentTime);
		buttonPanel.add(butCalculateImpliedVolatility);
		buttonPanel.add(butCancelCalculateImpliedVolatility);
		buttonPanel.add(butCalculateOptionPrice);
		buttonPanel.add(butCancelCalculateOptionPrice);
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(butWhatIfOrder);
		buttonPanel.add(butPlaceOrder);
		buttonPanel.add(butCancelOrder);
		buttonPanel.add(butExerciseOptions);
		buttonPanel.add(butExtendedOrder);
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(butContractData);
		buttonPanel.add(butOpenOrders);
		buttonPanel.add(butAllOpenOrders);
		buttonPanel.add(butAutoOpenOrders);
		buttonPanel.add(butAcctData);
		buttonPanel.add(butExecutions);
		buttonPanel.add(butNewsBulletins);
		buttonPanel.add(butServerLogging);
		buttonPanel.add(butManagedAccts);
		buttonPanel.add(butFinancialAdvisor);
		
		buttonPanel.add(new JPanel());
		buttonPanel.add(butClear);
		buttonPanel.add(butClose);
		
		return buttonPanel;
	}
	
	void onConnect() {
		m_bIsFAAccount = false;
		// get connection parameters
		final ConnectDlg dlg = new ConnectDlg(this);
		dlg.setVisible(true);
		if (!dlg.m_rc) { return; }
		
		// connect to TWS
		m_disconnectInProgress = false;
		
		m_client.eConnect(dlg.m_retIpAddress, dlg.m_retPort, dlg.m_retClientId);
		if (m_client.isConnected()) {
			m_TWS.add("Connected to Tws server version " +
			           m_client.serverVersion() + " at " +
			           m_client.TwsConnectionTime());
		}
	}
	
	void onDisconnect() {
		// disconnect from TWS
		m_disconnectInProgress = true;
		m_client.eDisconnect();
	}
	
	void onReqMktData() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// req mkt data
		m_client.reqMktData(m_orderDlg.m_id, m_orderDlg.m_contract,
		        m_orderDlg.m_genericTicks, m_orderDlg.m_snapshotMktData);
	}
	
	void onReqRealTimeBars() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		// req mkt data
		m_client.reqRealTimeBars(m_orderDlg.m_id, m_orderDlg.m_contract,
		        5 /* TODO: parse and use m_orderDlg.m_barSizeSetting */,
		        m_orderDlg.m_whatToShow, m_orderDlg.m_useRTH > 0);
	}
	
	void onCancelRealTimeBars() {
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		// cancel market data
		m_client.cancelRealTimeBars(m_orderDlg.m_id);
	}
	
	void onScanner() {
		m_scannerDlg.show();
		if (m_scannerDlg.m_userSelection == ScannerDlg.CANCEL_SELECTION) {
			m_client.cancelScannerSubscription(m_scannerDlg.m_id);
		} else if (m_scannerDlg.m_userSelection == ScannerDlg.SUBSCRIBE_SELECTION) {
			m_client.reqScannerSubscription(m_scannerDlg.m_id,
			                                m_scannerDlg.m_subscription);
		} else if (m_scannerDlg.m_userSelection == ScannerDlg.REQUEST_PARAMETERS_SELECTION) {
			m_client.reqScannerParameters();
		}
	}
	
	void onReqCurrentTime() {
		m_client.reqCurrentTime();
	}
	
	void onHistoricalData() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		if (Util.StringCompare(m_orderDlg.m_whatToShow, "estimates") == 0 ||
		        Util.StringCompare(m_orderDlg.m_whatToShow, "finstat") == 0 ||
		        Util.StringCompare(m_orderDlg.m_whatToShow, "snapshot") == 0) {
			
			m_client.reqFundamentalData(m_orderDlg.m_id, m_orderDlg.m_contract,
			        /* reportType */m_orderDlg.m_whatToShow);
			return;
		}
		
		// req historical data
		m_client.reqHistoricalData(m_orderDlg.m_id, m_orderDlg.m_contract,
		                            m_orderDlg.m_backfillEndTime,
		        m_orderDlg.m_backfillDuration,
		                            m_orderDlg.m_barSizeSetting,
		        m_orderDlg.m_whatToShow,
		                            m_orderDlg.m_useRTH,
		        m_orderDlg.m_formatDate);
	}
	
	void onCancelHistoricalData() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		if (Util.StringCompare(m_orderDlg.m_whatToShow, "estimates") == 0 ||
		        Util.StringCompare(m_orderDlg.m_whatToShow, "finstat") == 0 ||
		        Util.StringCompare(m_orderDlg.m_whatToShow, "snapshot") == 0) {
			
			m_client.cancelFundamentalData(m_orderDlg.m_id);
			return;
		}
		
		// cancel historical data
		m_client.cancelHistoricalData(m_orderDlg.m_id);
	}
	
	void onReqContractData() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// req mkt data
		m_client.reqContractDetails(m_orderDlg.m_id, m_orderDlg.m_contract);
	}
	
	void onReqMktDepth() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		m_mktDepthDlg.setParams(m_client, m_orderDlg.m_id);
		
		// req mkt data
		m_client.reqMktDepth(m_orderDlg.m_id, m_orderDlg.m_contract,
		        m_orderDlg.m_marketDepthRows);
		m_mktDepthDlg.show();
	}
	
	void onCancelMktData() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// cancel market data
		m_client.cancelMktData(m_orderDlg.m_id);
	}
	
	void onCancelMktDepth() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// cancel market data
		m_client.cancelMktDepth(m_orderDlg.m_id);
	}
	
	void onReqOpenOrders() {
		m_client.reqOpenOrders();
	}
	
	void onWhatIfOrder() {
		placeOrder(true);
	}
	
	void onPlaceOrder() {
		placeOrder(false);
	}
	
	void placeOrder(final boolean whatIf) {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		final Order order = m_orderDlg.m_order;
		
		// save old and set new value of whatIf attribute
		final boolean savedWhatIf = order.m_whatIf;
		order.m_whatIf = whatIf;
		
		// place order
		m_client.placeOrder(m_orderDlg.m_id, m_orderDlg.m_contract, order);
		
		// restore whatIf attribute
		order.m_whatIf = savedWhatIf;
	}
	
	void onExerciseOptions() {
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// cancel order
		m_client.exerciseOptions(m_orderDlg.m_id, m_orderDlg.m_contract,
		                          m_orderDlg.m_exerciseAction,
		        m_orderDlg.m_exerciseQuantity,
		                          m_orderDlg.m_order.m_account,
		        m_orderDlg.m_override);
	}
	
	void onCancelOrder() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		// cancel order
		m_client.cancelOrder(m_orderDlg.m_id);
	}
	
	void onExtendedOrder() {
		// Show the extended order attributes dialog
		m_extOrdDlg.show();
		if (!m_extOrdDlg.m_rc) { return; }
		
		// Copy over the extended order details
		copyExtendedOrderDetails(m_orderDlg.m_order, m_extOrdDlg.m_order);
	}
	
	void onReqAcctData() {
		final AcctUpdatesDlg dlg = new AcctUpdatesDlg(this);
		
		dlg.show();
		
		if (dlg.m_subscribe) {
			m_acctDlg.accountDownloadBegin(dlg.m_acctCode);
		}
		
		m_client.reqAccountUpdates(dlg.m_subscribe, dlg.m_acctCode);
		
		if (m_client.isConnected() && dlg.m_subscribe) {
			m_acctDlg.reset();
			m_acctDlg.setVisible(true);
		}
	}
	
	void onFinancialAdvisor() {
		faGroupXML = faProfilesXML = faAliasesXML = null;
		faError = false;
		m_client.requestFA(EClientSocket.GROUPS);
		m_client.requestFA(EClientSocket.PROFILES);
		m_client.requestFA(EClientSocket.ALIASES);
	}
	
	void onServerLogging() {
		// get server logging level
		final LogConfigDlg dlg = new LogConfigDlg(this);
		dlg.show();
		if (!dlg.m_rc) { return; }
		
		// connect to TWS
		m_client.setServerLogLevel(dlg.m_serverLogLevel);
	}
	
	void onReqAllOpenOrders() {
		// request list of all open orders
		m_client.reqAllOpenOrders();
	}
	
	void onReqAutoOpenOrders() {
		// request to automatically bind any newly entered TWS orders
		// to this API client. NOTE: TWS orders can only be bound to
		// client's with clientId=0.
		m_client.reqAutoOpenOrders(true);
	}
	
	void onReqManagedAccts() {
		// request the list of managed accounts
		m_client.reqManagedAccts();
	}
	
	void onClear() {
		m_tickers.clear();
		m_TWS.clear();
		m_errors.clear();
	}
	
	void onClose() {
		System.exit(1);
	}
	
	void onReqExecutions() {
		final ExecFilterDlg dlg = new ExecFilterDlg(this);
		
		dlg.show();
		if (dlg.m_rc) {
			// request execution reports based on the supplied filter criteria
			m_client.reqExecutions(dlg.m_reqId, dlg.m_execFilter);
		}
	}
	
	void onReqNewsBulletins() {
		// run m_newsBulletinDlg
		m_newsBulletinDlg.show();
		if (!m_newsBulletinDlg.m_rc) { return; }
		
		if (m_newsBulletinDlg.m_subscribe) {
			m_client.reqNewsBulletins(m_newsBulletinDlg.m_allMsgs);
		} else {
			m_client.cancelNewsBulletins();
		}
	}
	
	void onCalculateImpliedVolatility() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		m_client.calculateImpliedVolatility(m_orderDlg.m_id,
		        m_orderDlg.m_contract,
		        m_orderDlg.m_order.m_lmtPrice, m_orderDlg.m_order.m_auxPrice);
	}
	
	void onCancelCalculateImpliedVolatility() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		m_client.cancelCalculateImpliedVolatility(m_orderDlg.m_id);
	}
	
	void onCalculateOptionPrice() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		m_client.calculateOptionPrice(m_orderDlg.m_id, m_orderDlg.m_contract,
		        m_orderDlg.m_order.m_lmtPrice, m_orderDlg.m_order.m_auxPrice);
	}
	
	void onCancelCalculateOptionPrice() {
		// run m_orderDlg
		m_orderDlg.show();
		if (!m_orderDlg.m_rc) { return; }
		
		m_client.cancelCalculateOptionPrice(m_orderDlg.m_id);
	}
	
	@Override
	public void tickPrice(final int tickerId, final int field,
	        final double price, final int canAutoExecute) {
		// received price tick
		final String msg = EWrapperMsgGenerator.tickPrice(tickerId, field,
		        price, canAutoExecute);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickOptionComputation(final int tickerId, final int field,
	        final double impliedVol, final double delta, final double optPrice,
	        final double pvDividend,
	        final double gamma, final double vega, final double theta,
	        final double undPrice) {
		// received computation tick
		final String msg = EWrapperMsgGenerator.tickOptionComputation(tickerId,
		        field, impliedVol, delta, optPrice, pvDividend,
		        gamma, vega, theta, undPrice);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		// received size tick
		final String msg = EWrapperMsgGenerator.tickSize(tickerId, field, size);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickGeneric(final int tickerId, final int tickType,
	        final double value) {
		// received generic tick
		final String msg = EWrapperMsgGenerator.tickGeneric(tickerId, tickType,
		        value);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickString(final int tickerId, final int tickType,
	        final String value) {
		// received String tick
		final String msg = EWrapperMsgGenerator.tickString(tickerId, tickType,
		        value);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickSnapshotEnd(final int tickerId) {
		final String msg = EWrapperMsgGenerator.tickSnapshotEnd(tickerId);
		m_tickers.add(msg);
	}
	
	@Override
	public void tickEFP(final int tickerId, final int tickType,
	        final double basisPoints, final String formattedBasisPoints,
	                    final double impliedFuture, final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	                    final double dividendsToExpiry) {
		// received EFP tick
		final String msg = EWrapperMsgGenerator.tickEFP(tickerId, tickType,
		        basisPoints, formattedBasisPoints,
		        impliedFuture, holdDays, futureExpiry, dividendImpact,
		        dividendsToExpiry);
		m_tickers.add(msg);
	}
	
	@Override
	public void orderStatus(final int orderId, final String status,
	        final int filled, final int remaining,
	                         final double avgFillPrice, final int permId,
	        final int parentId,
	                         final double lastFillPrice, final int clientId,
	        final String whyHeld) {
		// received order status
		final String msg = EWrapperMsgGenerator.orderStatus(orderId, status,
		        filled, remaining,
		        avgFillPrice, permId, parentId, lastFillPrice, clientId,
		        whyHeld);
		m_TWS.add(msg);
		
		// make sure id for next order is at least orderId+1
		m_orderDlg.setIdAtLeast(orderId + 1);
	}
	
	@Override
	public void openOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		// received open order
		final String msg = EWrapperMsgGenerator.openOrder(orderId, contract,
		        order, orderState);
		m_TWS.add(msg);
	}
	
	@Override
	public void openOrderEnd() {
		// received open order end
		final String msg = EWrapperMsgGenerator.openOrderEnd();
		m_TWS.add(msg);
	}
	
	@Override
	public void contractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final String msg = EWrapperMsgGenerator.contractDetails(reqId,
		        contractDetails);
		m_TWS.add(msg);
	}
	
	@Override
	public void contractDetailsEnd(final int reqId) {
		final String msg = EWrapperMsgGenerator.contractDetailsEnd(reqId);
		m_TWS.add(msg);
	}
	
	@Override
	public void scannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails,
	                        final String distance, final String benchmark,
	        final String projection, final String legsStr) {
		final String msg = EWrapperMsgGenerator.scannerData(reqId, rank,
		        contractDetails, distance,
		        benchmark, projection, legsStr);
		m_tickers.add(msg);
	}
	
	@Override
	public void scannerDataEnd(final int reqId) {
		final String msg = EWrapperMsgGenerator.scannerDataEnd(reqId);
		m_tickers.add(msg);
	}
	
	@Override
	public void bondContractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		final String msg = EWrapperMsgGenerator.bondContractDetails(reqId,
		        contractDetails);
		m_TWS.add(msg);
	}
	
	@Override
	public void execDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		final String msg = EWrapperMsgGenerator.execDetails(reqId, contract,
		        execution);
		m_TWS.add(msg);
	}
	
	@Override
	public void execDetailsEnd(final int reqId) {
		final String msg = EWrapperMsgGenerator.execDetailsEnd(reqId);
		m_TWS.add(msg);
	}
	
	@Override
	public void updateMktDepth(final int tickerId, final int position,
	        final int operation,
	                final int side, final double price, final int size) {
		m_mktDepthDlg.updateMktDepth(tickerId, position, "", operation, side,
		        price, size);
	}
	
	public void updateMktDepthL2(final int tickerId, final int position,
	        final String marketMaker,
	                final int operation, final int side, final double price,
	        final int size) {
		m_mktDepthDlg.updateMktDepth(tickerId, position, marketMaker,
		        operation, side, price, size);
	}
	
	public void nextValidId(final int orderId) {
		// received next valid order id
		final String msg = EWrapperMsgGenerator.nextValidId(orderId);
		m_TWS.add(msg);
		m_orderDlg.setIdAtLeast(orderId);
	}
	
	public void error(final Exception ex) {
		// do not report exceptions if we initiated disconnect
		if (!m_disconnectInProgress) {
			final String msg = EWrapperMsgGenerator.error(ex);
			Main.inform(this, msg);
		}
	}
	
	public void error(final String str) {
		final String msg = EWrapperMsgGenerator.error(str);
		m_errors.add(msg);
	}
	
	public void error(final int id, final int errorCode, final String errorMsg) {
		// received error
		final String msg = EWrapperMsgGenerator.error(id, errorCode, errorMsg);
		m_errors.add(msg);
		for (final int faErrorCode : faErrorCodes) {
			faError |= errorCode == faErrorCode;
		}
		if (errorCode == MktDepthDlg.MKT_DEPTH_DATA_RESET) {
			m_mktDepthDlg.reset();
		}
	}
	
	public void connectionClosed() {
		final String msg = EWrapperMsgGenerator.connectionClosed();
		Main.inform(this, msg);
	}
	
	public void updateAccountValue(final String key, final String value,
	                               final String currency,
	        final String accountName) {
		m_acctDlg.updateAccountValue(key, value, currency, accountName);
	}
	
	public void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice,
	        final double marketValue, final double averageCost,
	        final double unrealizedPNL, final double realizedPNL,
	        final String accountName) {
		m_acctDlg.updatePortfolio(contract, position, marketPrice, marketValue,
		        averageCost, unrealizedPNL, realizedPNL, accountName);
	}
	
	public void updateAccountTime(final String timeStamp) {
		m_acctDlg.updateAccountTime(timeStamp);
	}
	
	public void accountDownloadEnd(final String accountName) {
		m_acctDlg.accountDownloadEnd(accountName);
		
		final String msg = EWrapperMsgGenerator.accountDownloadEnd(accountName);
		m_TWS.add(msg);
	}
	
	public void updateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		final String msg = EWrapperMsgGenerator.updateNewsBulletin(msgId,
		        msgType, message, origExchange);
		JOptionPane.showMessageDialog(this, msg, "IB News Bulletin",
		        JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void managedAccounts(final String accountsList) {
		m_bIsFAAccount = true;
		m_FAAcctCodes = accountsList;
		final String msg = EWrapperMsgGenerator.managedAccounts(accountsList);
		m_TWS.add(msg);
	}
	
	public void historicalData(final int reqId, final String date,
	        final double open, final double high, final double low,
	                           final double close, final int volume,
	        final int count, final double WAP, final boolean hasGaps) {
		final String msg = EWrapperMsgGenerator.historicalData(reqId, date,
		        open, high, low, close, volume, count, WAP, hasGaps);
		m_tickers.add(msg);
	}
	
	public void realtimeBar(final int reqId, final long time,
	        final double open, final double high, final double low,
	        final double close, final long volume, final double wap,
	        final int count) {
		final String msg = EWrapperMsgGenerator.realtimeBar(reqId, time, open,
		        high, low, close, volume, wap, count);
		m_tickers.add(msg);
	}
	
	public void scannerParameters(final String xml) {
		displayXML(EWrapperMsgGenerator.SCANNER_PARAMETERS, xml);
	}
	
	public void currentTime(final long time) {
		final String msg = EWrapperMsgGenerator.currentTime(time);
		m_TWS.add(msg);
	}
	
	public void fundamentalData(final int reqId, final String data) {
		final String msg = EWrapperMsgGenerator.fundamentalData(reqId, data);
		m_tickers.add(msg);
	}
	
	public void deltaNeutralValidation(final int reqId,
	        final UnderComp underComp) {
		final String msg = EWrapperMsgGenerator.deltaNeutralValidation(reqId,
		        underComp);
		m_TWS.add(msg);
	}
	
	void displayXML(final String title, final String xml) {
		m_TWS.add(title);
		m_TWS.addText(xml);
	}
	
	public void receiveFA(final int faDataType, final String xml) {
		displayXML(
		        EWrapperMsgGenerator.FINANCIAL_ADVISOR + " " +
		                EClientSocket.faMsgTypeName(faDataType), xml);
		switch (faDataType) {
			case EClientSocket.GROUPS:
				faGroupXML = xml;
				break;
			case EClientSocket.PROFILES:
				faProfilesXML = xml;
				break;
			case EClientSocket.ALIASES:
				faAliasesXML = xml;
				break;
		}
		
		if (!faError &&
		        !(faGroupXML == null || faProfilesXML == null || faAliasesXML == null)) {
			final FinancialAdvisorDlg dlg = new FinancialAdvisorDlg(this);
			dlg.receiveInitialXML(faGroupXML, faProfilesXML, faAliasesXML);
			dlg.show();
			
			if (!dlg.m_rc) { return; }
			
			m_client.replaceFA(EClientSocket.GROUPS, dlg.groupsXML);
			m_client.replaceFA(EClientSocket.PROFILES, dlg.profilesXML);
			m_client.replaceFA(EClientSocket.ALIASES, dlg.aliasesXML);
			
		}
	}
	
	private void copyExtendedOrderDetails(final Order destOrder,
	        final Order srcOrder) {
		destOrder.m_tif = srcOrder.m_tif;
		destOrder.m_ocaGroup = srcOrder.m_ocaGroup;
		destOrder.m_ocaType = srcOrder.m_ocaType;
		destOrder.m_openClose = srcOrder.m_openClose;
		destOrder.m_origin = srcOrder.m_origin;
		destOrder.m_orderRef = srcOrder.m_orderRef;
		destOrder.m_transmit = srcOrder.m_transmit;
		destOrder.m_parentId = srcOrder.m_parentId;
		destOrder.m_blockOrder = srcOrder.m_blockOrder;
		destOrder.m_sweepToFill = srcOrder.m_sweepToFill;
		destOrder.m_displaySize = srcOrder.m_displaySize;
		destOrder.m_triggerMethod = srcOrder.m_triggerMethod;
		destOrder.m_outsideRth = srcOrder.m_outsideRth;
		destOrder.m_hidden = srcOrder.m_hidden;
		destOrder.m_discretionaryAmt = srcOrder.m_discretionaryAmt;
		destOrder.m_goodAfterTime = srcOrder.m_goodAfterTime;
		destOrder.m_shortSaleSlot = srcOrder.m_shortSaleSlot;
		destOrder.m_designatedLocation = srcOrder.m_designatedLocation;
		destOrder.m_ocaType = srcOrder.m_ocaType;
		destOrder.m_rule80A = srcOrder.m_rule80A;
		destOrder.m_allOrNone = srcOrder.m_allOrNone;
		destOrder.m_minQty = srcOrder.m_minQty;
		destOrder.m_percentOffset = srcOrder.m_percentOffset;
		destOrder.m_eTradeOnly = srcOrder.m_eTradeOnly;
		destOrder.m_firmQuoteOnly = srcOrder.m_firmQuoteOnly;
		destOrder.m_nbboPriceCap = srcOrder.m_nbboPriceCap;
		destOrder.m_auctionStrategy = srcOrder.m_auctionStrategy;
		destOrder.m_startingPrice = srcOrder.m_startingPrice;
		destOrder.m_stockRefPrice = srcOrder.m_stockRefPrice;
		destOrder.m_delta = srcOrder.m_delta;
		destOrder.m_stockRangeLower = srcOrder.m_stockRangeLower;
		destOrder.m_stockRangeUpper = srcOrder.m_stockRangeUpper;
		destOrder.m_overridePercentageConstraints = srcOrder.m_overridePercentageConstraints;
		destOrder.m_volatility = srcOrder.m_volatility;
		destOrder.m_volatilityType = srcOrder.m_volatilityType;
		destOrder.m_deltaNeutralOrderType = srcOrder.m_deltaNeutralOrderType;
		destOrder.m_deltaNeutralAuxPrice = srcOrder.m_deltaNeutralAuxPrice;
		destOrder.m_continuousUpdate = srcOrder.m_continuousUpdate;
		destOrder.m_referencePriceType = srcOrder.m_referencePriceType;
		destOrder.m_trailStopPrice = srcOrder.m_trailStopPrice;
		destOrder.m_scaleInitLevelSize = srcOrder.m_scaleInitLevelSize;
		destOrder.m_scaleSubsLevelSize = srcOrder.m_scaleSubsLevelSize;
		destOrder.m_scalePriceIncrement = srcOrder.m_scalePriceIncrement;
		destOrder.m_account = srcOrder.m_account;
		destOrder.m_settlingFirm = srcOrder.m_settlingFirm;
		destOrder.m_clearingAccount = srcOrder.m_clearingAccount;
		destOrder.m_clearingIntent = srcOrder.m_clearingIntent;
	}
}
