/*
 * ScannerDlg.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ib.client.ScannerSubscription;

public class ScannerDlg extends JDialog {
	/**
     * 
     */
	private static final long	serialVersionUID	        = 1L;
	public static final int	   NO_SELECTION	                = 0;
	public static final int	   SUBSCRIBE_SELECTION	        = 1;
	public static final int	   CANCEL_SELECTION	            = 2;
	public static final int	   REQUEST_PARAMETERS_SELECTION	= 3;
	
	public int	               m_userSelection	            = ScannerDlg.NO_SELECTION;
	public int	               m_id;
	public ScannerSubscription	m_subscription	            = new ScannerSubscription();
	
	private final JTextField	m_Id	                    = new JTextField(
	                                                                "0");
	private final JTextField	m_numberOfRows	            = new JTextField(
	                                                                "10");
	private final JTextField	m_instrument	            = new JTextField(
	                                                                "STK");
	private final JTextField	m_locationCode	            = new JTextField(
	                                                                "STK.US");
	private final JTextField	m_scanCode	                = new JTextField(
	                                                                "HIGH_OPT_VOLUME_PUT_CALL_RATIO");
	private final JTextField	m_abovePrice	            = new JTextField(
	                                                                "3");
	private final JTextField	m_belowPrice	            = new JTextField();
	private final JTextField	m_aboveVolume	            = new JTextField(
	                                                                "0");
	private final JTextField	m_averageOptionVolumeAbove	= new JTextField(
	                                                                "0");
	private final JTextField	m_marketCapAbove	        = new JTextField(
	                                                                "100000000");
	private final JTextField	m_marketCapBelow	        = new JTextField();
	private final JTextField	m_moodyRatingAbove	        = new JTextField();
	private final JTextField	m_moodyRatingBelow	        = new JTextField();
	private final JTextField	m_spRatingAbove	            = new JTextField();
	private final JTextField	m_spRatingBelow	            = new JTextField();
	private final JTextField	m_maturityDateAbove	        = new JTextField();
	private final JTextField	m_maturityDateBelow	        = new JTextField();
	private final JTextField	m_couponRateAbove	        = new JTextField();
	private final JTextField	m_couponRateBelow	        = new JTextField();
	private final JTextField	m_excludeConvertible	    = new JTextField(
	                                                                "0");
	private final JTextField	m_scannerSettingPairs	    = new JTextField(
	                                                                "Annual,true");
	private final JTextField	m_stockTypeFilter	        = new JTextField(
	                                                                "ALL");
	
	private final JButton	   m_requestParameters	        = new JButton(
	                                                                "Request Parameters");
	private final JButton	   m_subscribe	                = new JButton(
	                                                                "Subscribe");
	private final JButton	   m_cancel	                    = new JButton(
	                                                                "Cancel Subscription");
	private final SampleFrame	m_parent;
	
	private static final int	COL1_WIDTH	                = 30;
	private static final int	COL2_WIDTH	                = 100 - ScannerDlg.COL1_WIDTH;
	
	private static void addGBComponent(final IBGridBagPanel panel,
	        final Component comp,
	                                   final GridBagConstraints gbc,
	        final int weightx, final int gridwidth) {
		gbc.weightx = weightx;
		gbc.gridwidth = gridwidth;
		panel.setConstraints(comp, gbc);
		panel.add(comp, gbc);
	}
	
	public ScannerDlg(final SampleFrame owner) {
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
		
		ScannerDlg.addGBComponent(pId, new JLabel("Id"), gbc,
		        ScannerDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pId, m_Id, gbc, ScannerDlg.COL2_WIDTH,
		        GridBagConstraints.REMAINDER);
		
		// create contract panel
		final IBGridBagPanel pSubscriptionDetails = new IBGridBagPanel();
		pSubscriptionDetails.setBorder(BorderFactory
		        .createTitledBorder("Subscription Info"));
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Number of Rows"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_numberOfRows, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails,
		        new JLabel("Instrument"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_instrument, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Location Code"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_locationCode, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails,
		        new JLabel("Scan Code"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_scanCode, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Above Price"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_abovePrice, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Below Price"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_belowPrice, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Above Volume"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_aboveVolume, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Avg Option Volume Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails,
		        m_averageOptionVolumeAbove, gbc, ScannerDlg.COL2_WIDTH,
		        GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Market Cap Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_marketCapAbove, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Market Cap Below"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_marketCapBelow, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Moody Rating Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_moodyRatingAbove,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Moody Rating Below"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_moodyRatingBelow,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "S & P Rating Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_spRatingAbove, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "S & P Rating Below"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_spRatingBelow, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Maturity Date Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_maturityDateAbove,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Maturity Date Below"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_maturityDateBelow,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Coupon Rate Above"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_couponRateAbove, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Coupon Rate Below"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_couponRateBelow, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Exclude Convertible"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_excludeConvertible,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Scanner Setting Pairs"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_scannerSettingPairs,
		        gbc, ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		ScannerDlg.addGBComponent(pSubscriptionDetails, new JLabel(
		        "Stock Type Filter"), gbc, ScannerDlg.COL1_WIDTH,
		        GridBagConstraints.RELATIVE);
		ScannerDlg.addGBComponent(pSubscriptionDetails, m_stockTypeFilter, gbc,
		        ScannerDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_requestParameters);
		buttonPanel.add(m_subscribe);
		buttonPanel.add(m_cancel);
		
		m_requestParameters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onRequestParameters();
			}
		});
		m_subscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onSubscribe();
			}
		});
		m_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancelSubscription();
			}
		});
		
		// create top panel
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(pId);
		topPanel.add(pSubscriptionDetails);
		
		// create dlg box
		getContentPane().add(topPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		pack();
	}
	
	private static String pad(final int val) {
		return val < 10 ? "0" + val : "" + val;
	}
	
	private double parseDouble(final JTextField textfield) {
		try {
			return Double.parseDouble(textfield.getText().trim());
		} catch (final Exception ex) {
			return Double.MAX_VALUE;
		}
	}
	
	private int parseInt(final JTextField textfield) {
		try {
			return Integer.parseInt(textfield.getText().trim());
		} catch (final Exception ex) {
			return Integer.MAX_VALUE;
		}
	}
	
	void onSubscribe() {
		m_userSelection = ScannerDlg.NO_SELECTION;
		
		try {
			// set id
			m_id = Integer.parseInt(m_Id.getText().trim());
			m_subscription.numberOfRows(parseInt(m_numberOfRows));
			m_subscription.instrument(m_instrument.getText().trim());
			m_subscription.locationCode(m_locationCode.getText().trim());
			m_subscription.scanCode(m_scanCode.getText().trim());
			m_subscription.abovePrice(parseDouble(m_abovePrice));
			m_subscription.belowPrice(parseDouble(m_belowPrice));
			m_subscription.aboveVolume(parseInt(m_aboveVolume));
			final int avgOptVolume = parseInt(m_averageOptionVolumeAbove);
			// with Integer.MAX_VALUE creates filter in TWS
			m_subscription
			        .averageOptionVolumeAbove(avgOptVolume != Integer.MAX_VALUE ? avgOptVolume
			                : Integer.MIN_VALUE);
			m_subscription.marketCapAbove(parseDouble(m_marketCapAbove));
			m_subscription.marketCapBelow(parseDouble(m_marketCapBelow));
			m_subscription
			        .moodyRatingAbove(m_moodyRatingAbove.getText().trim());
			m_subscription
			        .moodyRatingBelow(m_moodyRatingBelow.getText().trim());
			m_subscription.spRatingAbove(m_spRatingAbove.getText().trim());
			m_subscription.spRatingBelow(m_spRatingBelow.getText().trim());
			m_subscription.maturityDateAbove(m_maturityDateAbove.getText()
			        .trim());
			m_subscription.maturityDateBelow(m_maturityDateBelow.getText()
			        .trim());
			m_subscription.couponRateAbove(parseDouble(m_couponRateAbove));
			m_subscription.couponRateBelow(parseDouble(m_couponRateBelow));
			m_subscription.excludeConvertible(m_excludeConvertible.getText()
			        .trim());
			m_subscription.scannerSettingPairs(m_scannerSettingPairs.getText()
			        .trim());
			// m_subscription.stockTypeFilter(m_stockTypeFilter.getText().trim());
			// Peter ???
		} catch (final Exception e) {
			Main.inform(this, "Error - " + e);
			return;
		}
		
		m_userSelection = ScannerDlg.SUBSCRIBE_SELECTION;
		setVisible(false);
	}
	
	void onRequestParameters() {
		m_userSelection = ScannerDlg.REQUEST_PARAMETERS_SELECTION;
		setVisible(false);
	}
	
	void onCancelSubscription() {
		m_userSelection = ScannerDlg.CANCEL_SELECTION;
		m_id = Integer.parseInt(m_Id.getText().trim());
		setVisible(false);
	}
	
	@Override
	public void show() {
		m_userSelection = ScannerDlg.NO_SELECTION;
		super.show();
	}
}