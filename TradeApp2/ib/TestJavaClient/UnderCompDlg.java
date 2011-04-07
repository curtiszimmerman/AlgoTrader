/*
 * UnderCompDlg.java
 */

package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ib.client.UnderComp;

public class UnderCompDlg extends JDialog {
	
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	
	private final UnderComp	  m_underComp;
	
	private final JTextField	m_txtConId	     = new JTextField();
	private final JTextField	m_txtDelta	     = new JTextField();
	private final JTextField	m_txtPrice	     = new JTextField();
	
	private final JButton	  m_btnOk	         = new JButton("OK");
	private final JButton	  m_btnReset	     = new JButton("Reset");
	private final JButton	  m_btnCancel	     = new JButton("Cancel");
	
	private boolean	          m_ok	             = false;
	private boolean	          m_reset	         = false;
	
	private static final int	COL1_WIDTH	     = 30;
	private static final int	COL2_WIDTH	     = 100 - UnderCompDlg.COL1_WIDTH;
	
	public UnderCompDlg(final UnderComp underComp, final JDialog owner) {
		super(owner, true);
		
		m_underComp = underComp;
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_btnOk);
		buttonPanel.add(m_btnReset);
		buttonPanel.add(m_btnCancel);
		
		// create action listeners
		m_btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onOk();
			}
		});
		m_btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onReset();
			}
		});
		m_btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onCancel();
			}
		});
		
		final java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weighty = 100;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		
		// create mid panel
		final IBGridBagPanel midPanel = new IBGridBagPanel();
		midPanel.setBorder(BorderFactory.createTitledBorder("Under Comp"));
		UnderCompDlg.addGBComponent(midPanel, new JLabel("Contract Id"), gbc,
		        UnderCompDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		UnderCompDlg.addGBComponent(midPanel, m_txtConId, gbc,
		        UnderCompDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		UnderCompDlg.addGBComponent(midPanel, new JLabel("Delta"), gbc,
		        UnderCompDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		UnderCompDlg.addGBComponent(midPanel, m_txtDelta, gbc,
		        UnderCompDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		UnderCompDlg.addGBComponent(midPanel, new JLabel("Price"), gbc,
		        UnderCompDlg.COL1_WIDTH, GridBagConstraints.RELATIVE);
		UnderCompDlg.addGBComponent(midPanel, m_txtPrice, gbc,
		        UnderCompDlg.COL2_WIDTH, GridBagConstraints.REMAINDER);
		
		m_txtConId.setText(Integer.toString(m_underComp.m_conId));
		m_txtDelta.setText(Double.toString(m_underComp.m_delta));
		m_txtPrice.setText(Double.toString(m_underComp.m_price));
		
		// create dlg box
		getContentPane().add(midPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setTitle("Delta Neutral");
		pack();
	}
	
	private void onOk() {
		
		try {
			final int conId = Integer.parseInt(m_txtConId.getText());
			final double delta = Double.parseDouble(m_txtDelta.getText());
			final double price = Double.parseDouble(m_txtPrice.getText());
			
			m_underComp.m_conId = conId;
			m_underComp.m_delta = delta;
			m_underComp.m_price = price;
			m_ok = true;
			setVisible(false);
		} catch (final Exception e) {
			Main.inform(this, "Error - " + e);
		}
	}
	
	private void onReset() {
		m_underComp.m_conId = 0;
		m_underComp.m_delta = 0;
		m_underComp.m_price = 0;
		m_reset = true;
		setVisible(false);
	}
	
	private void onCancel() {
		setVisible(false);
	}
	
	public boolean ok() {
		return m_ok;
	}
	
	public boolean reset() {
		return m_reset;
	}
	
	private static void
	        addGBComponent(final IBGridBagPanel panel, final Component comp,
	                final GridBagConstraints gbc, final int weightx,
	                final int gridwidth) {
		gbc.weightx = weightx;
		gbc.gridwidth = gridwidth;
		panel.setConstraints(comp, gbc);
		panel.add(comp, gbc);
	}
	
}
