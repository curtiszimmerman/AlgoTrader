/*
 * 
 * ComboLegDlg.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.ib.client.ComboLeg;

public class ComboLegDlg extends JDialog {
	
	// private static String BUY = "BUY";
	// private static String SELL = "SELL";
	// private static String SSHORT = "SSHORT";
	
	/**
     * 
     */
	private static final long	serialVersionUID	 = 1L;
	
	private final Vector	    m_comboLegs;
	
	private final JTextField	m_conId	             = new JTextField("0");
	private final JTextField	m_ratio	             = new JTextField("0");
	private final JTextField	m_action	         = new JTextField("BUY");
	private final JTextField	m_exchange	         = new JTextField("");
	private final JTextField	m_openClose	         = new JTextField("0");
	private final JTextField	m_shortSaleSlot	     = new JTextField("0");
	private final JTextField	m_designatedLocation	= new JTextField("");
	
	private final JButton	    m_addLeg	         = new JButton("Add");
	private final JButton	    m_removeLeg	         = new JButton("Remove");
	private final JButton	    m_ok	             = new JButton("OK");
	private final JButton	    m_cancel	         = new JButton("Cancel");
	
	private final ComboLegModel	m_comboLegsModel	 = new ComboLegModel();
	private final JTable	    m_comboTable	     = new JTable(
	                                                         m_comboLegsModel);
	private final JScrollPane	m_comboLegsPane	     = new JScrollPane(
	                                                         m_comboTable);
	
	public ComboLegModel comboLegModel() {
		return m_comboLegsModel;
	}
	
	public ComboLegDlg(final Vector comboLegs, final String orderExchange,
	        final JDialog owner) {
		super(owner, true);
		
		m_comboLegs = comboLegs;
		
		setTitle("Combination Legs");
		
		// create combos list panel
		final JPanel pLegList = new JPanel(new GridLayout(0, 1, 10, 10));
		pLegList.setBorder(BorderFactory
		        .createTitledBorder("Combination Order legs:"));
		m_comboLegsModel.comboLegModel().addAll(comboLegs);
		pLegList.add(m_comboLegsPane);
		
		if (orderExchange != null && orderExchange.length() > 0) {
			m_exchange.setText(orderExchange);
		}
		
		// create combo details panel
		final JPanel pComboDetails = new JPanel(new GridLayout(0, 2, 10, 10));
		pComboDetails.setBorder(BorderFactory
		        .createTitledBorder("Combo Leg Details:"));
		pComboDetails.add(new JLabel("ConId:"));
		pComboDetails.add(m_conId);
		pComboDetails.add(new JLabel("Ratio:"));
		pComboDetails.add(m_ratio);
		pComboDetails.add(new JLabel("Side:"));
		pComboDetails.add(m_action);
		pComboDetails.add(new JLabel("Exchange:"));
		pComboDetails.add(m_exchange);
		pComboDetails.add(new JLabel("Open/Close:"));
		pComboDetails.add(m_openClose);
		pComboDetails.add(new JLabel("Short Sale Slot:"));
		pComboDetails.add(m_shortSaleSlot);
		pComboDetails.add(new JLabel("Designated Location:"));
		pComboDetails.add(m_designatedLocation);
		pComboDetails.add(m_addLeg);
		pComboDetails.add(m_removeLeg);
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_ok);
		buttonPanel.add(m_cancel);
		
		// create wrapper panel
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(pLegList);
		topPanel.add(pComboDetails);
		
		// create dlg box
		getContentPane().add(topPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		// create action listeners
		m_addLeg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onAddLeg();
			}
		});
		m_removeLeg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onRemoveLeg();
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
		
		setSize(250, 600);
		centerOnOwner(this);
	}
	
	public void onAddLeg() {
		try {
			final int conId = Integer.parseInt(m_conId.getText());
			final int ratio = Integer.parseInt(m_ratio.getText());
			final int openClose = Integer.parseInt(m_openClose.getText());
			final int shortSaleSlot = Integer.parseInt(m_shortSaleSlot
			        .getText());
			m_comboLegsModel.addComboLeg(new ComboLeg(conId, ratio,
			                m_action.getText(), m_exchange.getText(),
			        openClose,
			                shortSaleSlot, m_designatedLocation.getText()));
		} catch (final Exception e) {
			reportError("Error - ", e);
			return;
		}
	}
	
	public void onRemoveLeg() {
		try {
			if (m_comboTable.getSelectedRowCount() != 0) {
				final int[] rows = m_comboTable.getSelectedRows();
				for (int i = rows.length - 1; i >= 0; i--) {
					m_comboLegsModel.removeComboLeg(rows[i]);
				}
			}
		} catch (final Exception e) {
			reportError("Error - ", e);
			return;
		}
	}
	
	void onOk() {
		m_comboLegs.clear();
		m_comboLegs.addAll(m_comboLegsModel.comboLegModel());
		setVisible(false);
	}
	
	void onCancel() {
		setVisible(false);
	}
	
	void reportError(final String msg, final Exception e) {
		Main.inform(this, msg + " --" + e);
	}
	
	private void centerOnOwner(final Window window) {
		final Window owner = window.getOwner();
		if (owner == null) { return; }
		int x = owner.getX() + (owner.getWidth() - window.getWidth()) / 2;
		int y = owner.getY() + (owner.getHeight() - window.getHeight()) / 2;
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		window.setLocation(x, y);
	}
}

class ComboLegModel extends AbstractTableModel {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	private final Vector	  m_allData	         = new Vector();
	
	synchronized public void addComboLeg(final ComboLeg leg) {
		m_allData.add(leg);
		fireTableDataChanged();
	}
	
	synchronized public void removeComboLeg(final int index) {
		m_allData.remove(index);
		fireTableDataChanged();
	}
	
	synchronized public void removeComboLeg(final ComboLeg leg) {
		for (int i = 0; i < m_allData.size(); i++) {
			if (leg.equals(m_allData.get(i))) {
				m_allData.remove(i);
				break;
			}
		}
		fireTableDataChanged();
	}
	
	synchronized public void reset() {
		m_allData.removeAllElements();
		fireTableDataChanged();
	}
	
	@Override
	synchronized public int getRowCount() {
		return m_allData.size();
	}
	
	@Override
	synchronized public int getColumnCount() {
		return 7;
	}
	
	@Override
	synchronized public Object getValueAt(final int r, final int c) {
		final ComboLeg leg = (ComboLeg) m_allData.get(r);
		
		switch (c) {
			case 0:
				return Integer.toString(leg.m_conId);
			case 1:
				return Integer.toString(leg.m_ratio);
			case 2:
				return leg.m_action;
			case 3:
				return leg.m_exchange;
			case 4:
				return Integer.toString(leg.m_openClose);
			case 5:
				return Integer.toString(leg.m_shortSaleSlot);
			case 6:
				return leg.m_designatedLocation;
			default:
				return "";
		}
		
	}
	
	@Override
	public boolean isCellEditable(final int r, final int c) {
		return false;
	}
	
	@Override
	public String getColumnName(final int c) {
		switch (c) {
			case 0:
				return "ConId";
			case 1:
				return "Ratio";
			case 2:
				return "Side";
			case 3:
				return "Exchange";
			case 4:
				return "Open/Close";
			case 5:
				return "Short Sale Slot";
			case 6:
				return "Designated Location";
			default:
				return null;
		}
	}
	
	public Vector comboLegModel() {
		return m_allData;
	}
}
