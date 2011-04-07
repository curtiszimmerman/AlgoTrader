/*
 * OrderDlg.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.UnderComp;

public class OrderDlg extends JDialog {
	/**
     * 
     */
	private static final long	serialVersionUID	        = 1L;
	final static String	      ALL_GENERIC_TICK_TAGS	        = "100,101,104,105,106,107,165,221,225,233,236,258";
	final static int	      OPERATION_INSERT	            = 0;
	final static int	      OPERATION_UPDATE	            = 1;
	final static int	      OPERATION_DELETE	            = 2;
	
	final static int	      SIDE_ASK	                    = 0;
	final static int	      SIDE_BID	                    = 1;
	
	public boolean	          m_rc;
	public int	              m_id;
	public String	          m_backfillEndTime;
	public String	          m_backfillDuration;
	public String	          m_barSizeSetting;
	public int	              m_useRTH;
	public int	              m_formatDate;
	public int	              m_marketDepthRows;
	public String	          m_whatToShow;
	public Contract	          m_contract	                = new Contract();
	public Order	          m_order	                    = new Order();
	public UnderComp	      m_underComp	                = new UnderComp();
	public int	              m_exerciseAction;
	public int	              m_exerciseQuantity;
	public int	              m_override;
	
	private final JTextField	m_Id	                    = new JTextField(
	                                                                "0");
	private final JTextField	m_BackfillEndTime	        = new JTextField(22);
	private final JTextField	m_BackfillDuration	        = new JTextField(
	                                                                "1 M");
	private final JTextField	m_BarSizeSetting	        = new JTextField(
	                                                                "1 day");
	private final JTextField	m_UseRTH	                = new JTextField(
	                                                                "1");
	private final JTextField	m_FormatDate	            = new JTextField(
	                                                                "1");
	private final JTextField	m_WhatToShow	            = new JTextField(
	                                                                "TRADES");
	private final JTextField	m_conId	                    = new JTextField();
	private final JTextField	m_symbol	                = new JTextField(
	                                                                "QQQQ");
	private final JTextField	m_secType	                = new JTextField(
	                                                                "STK");
	private final JTextField	m_expiry	                = new JTextField();
	private final JTextField	m_strike	                = new JTextField(
	                                                                "0");
	private final JTextField	m_right	                    = new JTextField();
	private final JTextField	m_multiplier	            = new JTextField("");
	private final JTextField	m_exchange	                = new JTextField(
	                                                                "SMART");
	private final JTextField	m_primaryExch	            = new JTextField(
	                                                                "ISLAND");
	private final JTextField	m_currency	                = new JTextField(
	                                                                "USD");
	private final JTextField	m_localSymbol	            = new JTextField();
	private final JTextField	m_includeExpired	        = new JTextField(
	                                                                "0");
	private final JTextField	m_secIdType	                = new JTextField();
	private final JTextField	m_secId	                    = new JTextField();
	private final JTextField	m_action	                = new JTextField(
	                                                                "BUY");
	private final JTextField	m_totalQuantity	            = new JTextField(
	                                                                "10");
	private final JTextField	m_orderType	                = new JTextField(
	                                                                "LMT");
	private final JTextField	m_lmtPrice	                = new JTextField(
	                                                                "40");
	private final JTextField	m_auxPrice	                = new JTextField(
	                                                                "0");
	private final JTextField	m_goodAfterTime	            = new JTextField();
	private final JTextField	m_goodTillDate	            = new JTextField();
	private final JTextField	m_marketDepthRowTextField	= new JTextField(
	                                                                "20");
	private final JTextField	m_genericTicksTextField	    = new JTextField(
	                                                                OrderDlg.ALL_GENERIC_TICK_TAGS);
	private final JCheckBox	  m_snapshotMktDataTextField	= new JCheckBox(
	                                                                "Snapshot",
	                                                                false);
	private final JTextField	m_exerciseActionTextField	= new JTextField(
	                                                                "1");
	private final JTextField	m_exerciseQuantityTextField	= new JTextField(
	                                                                "1");
	private final JTextField	m_overrideTextField	        = new JTextField(
	                                                                "0");
	
	private final JButton	  m_sharesAlloc	                = new JButton(
	                                                                "FA Allocation Info...");
	private final JButton	  m_comboLegs	                = new JButton(
	                                                                "Combo Legs");
	private final JButton	  m_btnUnderComp	            = new JButton(
	                                                                "Delta Neutral");
	private final JButton	  m_btnAlgoParams	            = new JButton(
	                                                                "Algo Params");
	
	private final JButton	  m_ok	                        = new JButton("OK");
	private final JButton	  m_cancel	                    = new JButton(
	                                                                "Cancel");
	private final SampleFrame	m_parent;
	
	private String	          m_faGroup;
	private String	          m_faProfile;
	private String	          m_faMethod;
	private String	          m_faPercentage;
	public String	          m_genericTicks;
	public boolean	          m_snapshotMktData;
	
	private static final int	COL1_WIDTH	                = 30;
	private static final int	COL2_WIDTH	                = 100 - OrderDlg.COL1_WIDTH;
	
	public void faGroup(final String s) {
		m_faGroup = s;
	}
	
	public void faProfile(final String s) {
		m_faProfile = s;
	}
	
	public void faMethod(final String s) {
		m_faMethod = s;
	}
	
	public void faPercentage(final String s) {
		m_faPercentage = s;
	}
	
	private static void addGBComponent(final IBGridBagPanel panel,
	        final Component comp,
	                                   final GridBagConstraints gbc,
	        final int weightx, final int gridwidth) {
		gbc.weightx = weightx;
		gbc.gridwidth = gridwidth;
		panel.setConstraints(comp, gbc);
		panel.add(comp, gbc);
	}
	
	public OrderDlg(final SampleFrame owner) {
		super(owner, true);
		
		m_parent = owner;
		setTitle("Sample");
		
		final java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weighty = 100;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		// create id panel
		final IBGridBagPanel pId = new IBGridBagPanel();
		pId.setBorder(BorderFactory.createTitledBorder("Message Id"));
		
		OrderDlg.addGBComponent(pId, new JLabel("Id"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pId, m_Id, gbc, OrderDlg.COL2_WIDTH,
		        GridBagConstraints.REMAINDER);
		
		// create contract panel
		final IBGridBagPanel pContractDetails = new IBGridBagPanel();
		pContractDetails.setBorder(BorderFactory
		        .createTitledBorder("Contract Info"));
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Contract Id"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_conId, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Symbol"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_symbol, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Security Type"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_secType, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Expiry"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_expiry, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Strike"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_strike, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Put/Call"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_right, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel(
		        "Option Multiplier"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_multiplier, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Exchange"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_exchange, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails,
		        new JLabel("Primary Exchange"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_primaryExch, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Currency"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_currency, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Local Symbol"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_localSymbol, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails,
		        new JLabel("Include Expired"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_includeExpired, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Sec Id Type"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_secIdType, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pContractDetails, new JLabel("Sec Id"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pContractDetails, m_secId, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create order panel
		final IBGridBagPanel pOrderDetails = new IBGridBagPanel();
		pOrderDetails.setBorder(BorderFactory.createTitledBorder("Order Info"));
		OrderDlg.addGBComponent(pOrderDetails, new JLabel("Action"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_action, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel("Total Order Size"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_totalQuantity, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel("Order Type"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_orderType, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel(
		        "Lmt Price / Option Price / Volatility"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_lmtPrice, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel(
		        "Aux Price / Underlying Price"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_auxPrice, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel("Good After Time"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_goodAfterTime, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOrderDetails, new JLabel("Good Till Date"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOrderDetails, m_goodTillDate, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create marketDepth panel
		final IBGridBagPanel pMarketDepth = new IBGridBagPanel();
		pMarketDepth
		        .setBorder(BorderFactory.createTitledBorder("Market Depth"));
		OrderDlg.addGBComponent(pMarketDepth, new JLabel("Number of Rows"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pMarketDepth, m_marketDepthRowTextField, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create marketData panel
		final IBGridBagPanel pMarketData = new IBGridBagPanel();
		pMarketData.setBorder(BorderFactory.createTitledBorder("Market Data"));
		OrderDlg.addGBComponent(pMarketData, new JLabel("Generic Tick Tags"),
		        gbc, OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pMarketData, m_genericTicksTextField, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pMarketData, m_snapshotMktDataTextField, gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		
		// create options exercise panel
		final IBGridBagPanel pOptionsExercise = new IBGridBagPanel();
		pOptionsExercise.setBorder(BorderFactory
		        .createTitledBorder("Options Exercise"));
		OrderDlg.addGBComponent(pOptionsExercise,
		        new JLabel("Action (1 or 2)"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOptionsExercise, m_exerciseActionTextField,
		        gbc, OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOptionsExercise, new JLabel(
		        "Number of Contracts"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOptionsExercise, m_exerciseQuantityTextField,
		        gbc, OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pOptionsExercise, new JLabel(
		        "Override (0 or 1)"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pOptionsExercise, m_overrideTextField, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create historical data panel
		final IBGridBagPanel pBackfill = new IBGridBagPanel();
		pBackfill.setBorder(BorderFactory
		        .createTitledBorder("Historical Data Query"));
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(TimeZone.getTimeZone("GMT"));
		final String dateTime = "" +
		        gc.get(Calendar.YEAR) +
		        OrderDlg.pad(gc.get(Calendar.MONTH) + 1) +
		        OrderDlg.pad(gc.get(Calendar.DAY_OF_MONTH)) + " " +
		        OrderDlg.pad(gc.get(Calendar.HOUR_OF_DAY)) + ":" +
		        OrderDlg.pad(gc.get(Calendar.MINUTE)) + ":" +
		        OrderDlg.pad(gc.get(Calendar.SECOND)) + " " +
		        gc.getTimeZone().getDisplayName(false, TimeZone.SHORT);
		
		m_BackfillEndTime.setText(dateTime);
		OrderDlg.addGBComponent(pBackfill, new JLabel("End Date/Time"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_BackfillEndTime, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pBackfill, new JLabel("Duration"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_BackfillDuration, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pBackfill, new JLabel(
		        "Bar Size Setting (1 to 11)"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_BarSizeSetting, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pBackfill, new JLabel("What to Show"), gbc,
		        OrderDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_WhatToShow, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pBackfill, new JLabel(
		        "Regular Trading Hours (1 or 0)"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_UseRTH, gbc, OrderDlg.COL2_WIDTH,
		        GridBagConstraints.REMAINDER);
		OrderDlg.addGBComponent(pBackfill, new JLabel(
		        "Date Format Style (1 or 2)"), gbc, OrderDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		OrderDlg.addGBComponent(pBackfill, m_FormatDate, gbc,
		        OrderDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create mid Panel
		final JPanel pMidPanel = new JPanel();
		pMidPanel.setLayout(new BoxLayout(pMidPanel, BoxLayout.Y_AXIS));
		pMidPanel.add(pContractDetails, BorderLayout.CENTER);
		pMidPanel.add(pOrderDetails, BorderLayout.CENTER);
		pMidPanel.add(pMarketDepth, BorderLayout.CENTER);
		pMidPanel.add(pMarketData, BorderLayout.CENTER);
		pMidPanel.add(pOptionsExercise, BorderLayout.CENTER);
		pMidPanel.add(pBackfill, BorderLayout.CENTER);
		
		// create order button panel
		final JPanel pOrderButtonPanel = new JPanel();
		pOrderButtonPanel.add(m_sharesAlloc);
		pOrderButtonPanel.add(m_comboLegs);
		pOrderButtonPanel.add(m_btnUnderComp);
		pOrderButtonPanel.add(m_btnAlgoParams);
		
		pMidPanel.add(pOrderButtonPanel, BorderLayout.CENTER);
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_ok);
		buttonPanel.add(m_cancel);
		
		// create action listeners
		m_sharesAlloc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onSharesAlloc();
			}
		});
		
		m_comboLegs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onAddComboLegs();
			}
		});
		m_btnUnderComp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onBtnUnderComp();
			}
		});
		m_btnAlgoParams.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onBtnAlgoParams();
			}
		});
		m_ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onOk();
			}
		});
		m_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancel();
			}
		});
		
		// create top panel
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(pId);
		topPanel.add(pMidPanel);
		
		// create dlg box
		getContentPane().add(topPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
	}
	
	private static String pad(final int val) {
		return val < 10 ? "0" + val : "" + val;
	}
	
	void onSharesAlloc() {
		if (!m_parent.m_bIsFAAccount) { return; }
		
		final FAAllocationInfoDlg dlg = new FAAllocationInfoDlg(this);
		
		// show the combo leg dialog
		dlg.setVisible(true);
	}
	
	void onAddComboLegs() {
		
		final ComboLegDlg comboLegDlg = new ComboLegDlg(
		        m_contract.m_comboLegs, m_exchange.getText(), this);
		
		// show the combo leg dialog
		comboLegDlg.setVisible(true);
	}
	
	void onBtnUnderComp() {
		
		final UnderCompDlg underCompDlg = new UnderCompDlg(m_underComp, this);
		
		// show delta neutral dialog
		underCompDlg.setVisible(true);
		if (underCompDlg.ok()) {
			m_contract.m_underComp = m_underComp;
		} else if (underCompDlg.reset()) {
			m_contract.m_underComp = null;
		}
	}
	
	void onBtnAlgoParams() {
		
		final AlgoParamsDlg algoParamsDlg = new AlgoParamsDlg(m_order, this);
		
		// show delta neutral dialog
		algoParamsDlg.setVisible(true);
	}
	
	void onOk() {
		m_rc = false;
		
		try {
			// set id
			m_id = Integer.parseInt(m_Id.getText());
			
			// set contract fields
			m_contract.m_conId = OrderDlg.ParseInt(m_conId.getText(), 0);
			m_contract.m_symbol = m_symbol.getText();
			m_contract.m_secType = m_secType.getText();
			m_contract.m_expiry = m_expiry.getText();
			m_contract.m_strike = OrderDlg.ParseDouble(m_strike.getText(), 0.0);
			m_contract.m_right = m_right.getText();
			m_contract.m_multiplier = m_multiplier.getText();
			m_contract.m_exchange = m_exchange.getText();
			m_contract.m_primaryExch = m_primaryExch.getText();
			m_contract.m_currency = m_currency.getText();
			m_contract.m_localSymbol = m_localSymbol.getText();
			try {
				final int includeExpired = Integer.parseInt(m_includeExpired
				        .getText());
				m_contract.m_includeExpired = includeExpired == 1;
			} catch (final NumberFormatException ex) {
				m_contract.m_includeExpired = false;
			}
			m_contract.m_secIdType = m_secIdType.getText();
			m_contract.m_secId = m_secId.getText();
			
			// set order fields
			m_order.m_action = m_action.getText();
			m_order.m_totalQuantity = Integer.parseInt(m_totalQuantity
			        .getText());
			m_order.m_orderType = m_orderType.getText();
			m_order.m_lmtPrice = Double.parseDouble(m_lmtPrice.getText());
			m_order.m_auxPrice = Double.parseDouble(m_auxPrice.getText());
			m_order.m_goodAfterTime = m_goodAfterTime.getText();
			m_order.m_goodTillDate = m_goodTillDate.getText();
			
			m_order.m_faGroup = m_faGroup;
			m_order.m_faProfile = m_faProfile;
			m_order.m_faMethod = m_faMethod;
			m_order.m_faPercentage = m_faPercentage;
			
			// set historical data fields
			m_backfillEndTime = m_BackfillEndTime.getText();
			m_backfillDuration = m_BackfillDuration.getText();
			m_barSizeSetting = m_BarSizeSetting.getText();
			m_useRTH = Integer.parseInt(m_UseRTH.getText());
			m_whatToShow = m_WhatToShow.getText();
			m_formatDate = Integer.parseInt(m_FormatDate.getText());
			m_exerciseAction = Integer.parseInt(m_exerciseActionTextField
			        .getText());
			m_exerciseQuantity = Integer.parseInt(m_exerciseQuantityTextField
			        .getText());
			m_override = Integer.parseInt(m_overrideTextField.getText());;
			
			// set market depth rows
			m_marketDepthRows = Integer.parseInt(m_marketDepthRowTextField
			        .getText());
			m_genericTicks = m_genericTicksTextField.getText();
			m_snapshotMktData = m_snapshotMktDataTextField.isSelected();
		} catch (final Exception e) {
			Main.inform(this, "Error - " + e);
			return;
		}
		
		m_rc = true;
		setVisible(false);
	}
	
	void onCancel() {
		m_rc = false;
		setVisible(false);
	}
	
	@Override
	public void show() {
		m_rc = false;
		super.show();
	}
	
	void setIdAtLeast(final int id) {
		try {
			// set id field to at least id
			final int curId = Integer.parseInt(m_Id.getText());
			if (curId < id) {
				m_Id.setText(String.valueOf(id));
			}
		} catch (final Exception e) {
			Main.inform(this, "Error - " + e);
		}
	}
	
	private static int ParseInt(final String text, final int defValue) {
		try {
			return Integer.parseInt(text);
		} catch (final NumberFormatException e) {
			return defValue;
		}
	}
	
	private static double ParseDouble(final String text, final double defValue) {
		try {
			return Double.parseDouble(text);
		} catch (final NumberFormatException e) {
			return defValue;
		}
	}
}
