/*
 * AccountDlg.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import com.ib.client.Contract;
import com.ib.client.Util;

public class AccountDlg extends JDialog {
	/**
     * 
     */
	private static final long	 serialVersionUID	 = 1L;
	private final JTextField	 m_updateTime	     = new JTextField();
	private final JLabel	     m_timeLabel	     = new JLabel(
	                                                         "Update time:");
	private final JButton	     m_close	         = new JButton("Close");
	private final PortfolioTable	m_portfolioModel	= new PortfolioTable();
	private final AcctValueModel	m_acctValueModel	= new AcctValueModel();
	private boolean	             m_rc;
	
	private String	             m_accountName;
	private boolean	             m_complete;
	
	boolean rc() {
		return m_rc;
	}
	
	public AccountDlg(final JFrame parent) {
		super(parent, true);
		
		final JScrollPane acctPane = new JScrollPane(new JTable(
		        m_acctValueModel));
		final JScrollPane portPane = new JScrollPane(new JTable(
		        m_portfolioModel));
		
		acctPane.setBorder(BorderFactory
		        .createTitledBorder("Key, Value, Currency, and Account"));
		portPane.setBorder(BorderFactory
		        .createTitledBorder("Portfolio Entries"));
		
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		        acctPane, portPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(240);
		
		splitPane.setPreferredSize(new Dimension(600, 350));
		
		final JPanel timePanel = new JPanel();
		timePanel.add(m_timeLabel);
		timePanel.add(m_updateTime);
		timePanel.add(m_close);
		
		m_updateTime.setEditable(false);
		m_updateTime.setHorizontalAlignment(SwingConstants.CENTER);
		m_updateTime.setPreferredSize(new Dimension(80, 26));
		m_close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onClose();
			}
		});
		
		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(timePanel, BorderLayout.SOUTH);
		setLocation(20, 20);
		pack();
		reset();
	}
	
	void updateAccountValue(final String key, final String value,
	        final String currency, final String accountName) {
		m_acctValueModel.updateAccountValue(key, value, currency, accountName);
	}
	
	void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		m_portfolioModel.updatePortfolio(contract, position, marketPrice,
		        marketValue,
		        averageCost, unrealizedPNL, realizedPNL, accountName);
	}
	
	void reset() {
		m_acctValueModel.reset();
		m_portfolioModel.reset();
		m_updateTime.setText("");
	}
	
	void onClose() {
		setVisible(false);
	}
	
	void updateAccountTime(final String timeStamp) {
		m_updateTime.setText(timeStamp);
	}
	
	void accountDownloadBegin(final String accountName) {
		m_accountName = accountName;
		m_complete = false;
		
		updateTitle();
	}
	
	void accountDownloadEnd(final String accountName) {
		
		if (!Util.StringIsEmpty(m_accountName) &&
		        !m_accountName.equals(accountName)) { return; }
		
		m_complete = true;
		updateTitle();
	}
	
	private void updateTitle() {
		
		String title = new String();
		
		if (!Util.StringIsEmpty(m_accountName)) {
			title += m_accountName;
		}
		if (m_complete) {
			if (title.length() != 0) {
				title += ' ';
			}
			title += "[complete]";
		}
		
		setTitle(title);
	}
}

class PortfolioTable extends AbstractTableModel {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	Vector	                  m_allData	         = new Vector();
	
	void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	                     final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		final PortfolioTableRow newData =
		        new PortfolioTableRow(contract, position, marketPrice,
		                marketValue, averageCost, unrealizedPNL, realizedPNL,
		                accountName);
		final int size = m_allData.size();
		for (int i = 0; i < size; i++) {
			final PortfolioTableRow test = (PortfolioTableRow) m_allData.get(i);
			if (test.m_contract.equals(newData.m_contract)) {
				if (newData.m_position == 0) {
					m_allData.remove(i);
				} else {
					m_allData.set(i, newData);
				}
				
				fireTableDataChanged();
				return;
			}
		}
		
		m_allData.add(newData);
		fireTableDataChanged();
	}
	
	void reset() {
		m_allData.clear();
	}
	
	@Override
	public int getRowCount() {
		return m_allData.size();
	}
	
	@Override
	public int getColumnCount() {
		return 13;
	}
	
	@Override
	public Object getValueAt(final int r, final int c) {
		return ((PortfolioTableRow) m_allData.get(r)).getValue(c);
	}
	
	@Override
	public boolean isCellEditable(final int r, final int c) {
		return false;
	}
	
	@Override
	public String getColumnName(final int c) {
		switch (c) {
			case 0:
				return "Symbol";
			case 1:
				return "SecType";
			case 2:
				return "Expiry";
			case 3:
				return "Strike";
			case 4:
				return "Right";
			case 5:
				return "Multiplier";
			case 6:
				return "Exchange";
			case 7:
				return "Currency";
			case 8:
				return "Position";
			case 9:
				return "Market Price";
			case 10:
				return "Market Value";
			case 11:
				return "Average Cost";
			case 12:
				return "Unrealized P&L";
			case 13:
				return "Realized P&L";
			case 14:
				return "Account Name";
			default:
				return null;
		}
	}
	
	class PortfolioTableRow {
		Contract	m_contract;
		int		 m_position;
		double		m_marketPrice;
		double		m_marketValue;
		double		m_averageCost;
		double		m_unrealizedPNL;
		double		m_realizedPNL;
		String		m_accountName;
		
		PortfolioTableRow(final Contract contract, final int position,
		        final double marketPrice,
		        final double marketValue, final double averageCost,
		        final double unrealizedPNL,
		        final double realizedPNL, final String accountName) {
			m_contract = contract;
			m_position = position;
			m_marketPrice = marketPrice;
			m_marketValue = marketValue;
			m_averageCost = averageCost;
			m_unrealizedPNL = unrealizedPNL;
			m_realizedPNL = realizedPNL;
			m_accountName = accountName;
		}
		
		Object getValue(final int c) {
			switch (c) {
				case 0:
					return m_contract.m_symbol;
				case 1:
					return m_contract.m_secType;
				case 2:
					return m_contract.m_expiry;
				case 3:
					return m_contract.m_expiry == null ? null : "" +
					        m_contract.m_strike;
				case 4:
					return m_contract.m_right != null &&
					        m_contract.m_right.equals("???") ? null
					        : m_contract.m_right;
				case 5:
					return m_contract.m_multiplier;
				case 6:
					return m_contract.m_primaryExch != null ? m_contract.m_primaryExch
					        : "";
				case 7:
					return m_contract.m_currency;
				case 8:
					return "" + m_position;
				case 9:
					return "" + m_marketPrice;
				case 10:
					return "" + m_marketValue;
				case 11:
					return "" + m_averageCost;
				case 12:
					return "" + m_unrealizedPNL;
				case 13:
					return "" + m_realizedPNL;
				case 14:
					return m_accountName;
				default:
					return null;
			}
		}
	}
}

class AcctValueModel extends AbstractTableModel {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	Vector	                  m_allData	         = new Vector();
	
	void updateAccountValue(final String key, final String val,
	        final String currency, final String accountName) {
		final AccountTableRow newData = new AccountTableRow(key, val, currency,
		        accountName);
		final int size = m_allData.size();
		for (int i = 0; i < size; i++) {
			final AccountTableRow test = (AccountTableRow) m_allData.get(i);
			if (test.m_key != null &&
			        test.m_key.equals(newData.m_key) &&
			        test.m_currency != null &&
			        test.m_currency.equals(newData.m_currency)) {
				test.m_value = newData.m_value;
				fireTableDataChanged();
				return;
			}
		}
		
		m_allData.add(newData);
		fireTableDataChanged();
	}
	
	void reset() {
		m_allData.clear();
	}
	
	@Override
	public int getRowCount() {
		return m_allData.size();
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public Object getValueAt(final int r, final int c) {
		return ((AccountTableRow) m_allData.get(r)).getValue(c);
	}
	
	@Override
	public boolean isCellEditable(final int r, final int c) {
		return false;
	}
	
	@Override
	public String getColumnName(final int c) {
		switch (c) {
			case 0:
				return "Key";
			case 1:
				return "Value";
			case 2:
				return "Currency";
			case 3:
				return "Account Name";
				
			default:
				return null;
		}
	}
	
	class AccountTableRow {
		String	m_key;
		String	m_value;
		String	m_currency;
		String	m_accountName;
		
		AccountTableRow(final String key, final String val, final String cur,
		        final String accountName) {
			m_key = key;
			m_value = val;
			m_currency = cur;
			m_accountName = accountName;
		}
		
		Object getValue(final int c) {
			switch (c) {
				case 0:
					return m_key;
				case 1:
					return m_value;
				case 2:
					return m_currency;
				case 3:
					return m_accountName;
					
				default:
					return null;
			}
		}
	}
}