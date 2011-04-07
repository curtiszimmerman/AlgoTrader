/*
 * 
 * AlgoDlg.java
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

import com.ib.client.Order;
import com.ib.client.TagValue;

public class AlgoParamsDlg extends JDialog {
	
	/**
     * 
     */
	private static final long	 serialVersionUID	= 1L;
	
	private final Order	         m_order;
	
	private final JTextField	 m_algoStrategy	  = new JTextField("");
	
	private Vector	             m_algoParams;
	
	private final JTextField	 m_tag	          = new JTextField("");
	private final JTextField	 m_value	      = new JTextField("");
	
	private final JButton	     m_addParam	      = new JButton("Add");
	private final JButton	     m_removeParam	  = new JButton("Remove");
	
	private final JButton	     m_ok	          = new JButton("OK");
	private final JButton	     m_cancel	      = new JButton("Cancel");
	
	private final AlgoParamModel	m_paramModel	= new AlgoParamModel();
	private final JTable	     m_paramTable	  = new JTable(m_paramModel);
	private final JScrollPane	 m_paramPane	  = new JScrollPane(
	                                                      m_paramTable);
	
	public AlgoParamModel paramModel() {
		return m_paramModel;
	}
	
	public AlgoParamsDlg(final Order order, final JDialog owner) {
		super(owner, true);
		
		m_order = order;
		
		setTitle("Algo Order Parameters");
		
		final JPanel pAlgoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		pAlgoPanel.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		pAlgoPanel.add(new JLabel("Strategy:"));
		m_algoStrategy.setText(m_order.m_algoStrategy);
		pAlgoPanel.add(m_algoStrategy);
		
		// create algo params panel
		final JPanel pParamList = new JPanel(new GridLayout(0, 1, 10, 10));
		pParamList.setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		final Vector algoParams = m_order.m_algoParams;
		if (algoParams != null) {
			m_paramModel.algoParams().addAll(algoParams);
		}
		pParamList.add(m_paramPane);
		
		// create combo details panel
		final JPanel pParamListControl = new JPanel(
		        new GridLayout(0, 2, 10, 10));
		pParamListControl.setBorder(BorderFactory
		        .createTitledBorder("Add / Remove"));
		pParamListControl.add(new JLabel("Param:"));
		pParamListControl.add(m_tag);
		pParamListControl.add(new JLabel("Value:"));
		pParamListControl.add(m_value);
		pParamListControl.add(m_addParam);
		pParamListControl.add(m_removeParam);
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_ok);
		buttonPanel.add(m_cancel);
		
		// create wrapper panel
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(pAlgoPanel);
		topPanel.add(pParamList);
		topPanel.add(pParamListControl);
		
		// create dlg box
		getContentPane().add(topPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		// create action listeners
		m_addParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onAddParam();
			}
		});
		m_removeParam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onRemoveParam();
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
	
	public void onAddParam() {
		try {
			final String tag = m_tag.getText();
			final String value = m_value.getText();
			
			m_paramModel.addParam(new TagValue(tag, value));
		} catch (final Exception e) {
			reportError("Error - ", e);
			return;
		}
	}
	
	public void onRemoveParam() {
		try {
			if (m_paramTable.getSelectedRowCount() != 0) {
				final int[] rows = m_paramTable.getSelectedRows();
				for (int i = rows.length - 1; i >= 0; i--) {
					m_paramModel.removeParam(rows[i]);
				}
			}
		} catch (final Exception e) {
			reportError("Error - ", e);
			return;
		}
	}
	
	void onOk() {
		
		m_order.m_algoStrategy = m_algoStrategy.getText();
		
		final Vector algoParams = m_paramModel.algoParams();
		m_order.m_algoParams = algoParams.isEmpty() ? null : algoParams;
		
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

class AlgoParamModel extends AbstractTableModel {
	
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	private final Vector	  m_allData	         = new Vector();
	
	synchronized public void addParam(final TagValue tagValue) {
		m_allData.add(tagValue);
		fireTableDataChanged();
	}
	
	synchronized public void removeParam(final int index) {
		m_allData.remove(index);
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
		return 2;
	}
	
	@Override
	synchronized public Object getValueAt(final int r, final int c) {
		final TagValue tagValue = (TagValue) m_allData.get(r);
		
		switch (c) {
			case 0:
				return tagValue.m_tag;
			case 1:
				return tagValue.m_value;
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
				return "Param";
			case 1:
				return "Value";
			default:
				return null;
		}
	}
	
	public Vector algoParams() {
		return m_allData;
	}
}
