/*
 * AcctUpdatesDlg.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AcctUpdatesDlg extends JDialog {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	JTextField	              m_txtAcctCode	     = new JTextField(10);
	JButton	                  m_btnSubscribe	 = new JButton("Subscribe");
	JButton	                  m_btnUnSubscribe	 = new JButton("UnSubscribe");
	JButton	                  m_btnClose	     = new JButton("Close");
	SampleFrame	              m_parent;
	String	                  m_acctCode;
	boolean	                  m_subscribe	     = false;
	boolean	                  m_rc;
	
	public AcctUpdatesDlg(final SampleFrame owner) {
		super(owner, true);
		
		m_parent = owner;
		
		setTitle("Account Updates (FA Customers only)");
		setSize(200, 300);
		
		m_txtAcctCode.setMaximumSize(m_txtAcctCode.getPreferredSize());
		
		final Box row1 = Box.createHorizontalBox();
		row1.add(new JLabel(
		        " Enter the account code for the FA managed \n account you wish to receive updates for :"));
		
		final Box row2 = Box.createHorizontalBox();
		row2.add(new JLabel("Account Code :"));
		row2.add(Box.createHorizontalStrut(10));
		row2.add(m_txtAcctCode);
		
		final Box row3 = Box.createHorizontalBox();
		row3.add(m_btnSubscribe);
		row3.add(Box.createHorizontalStrut(10));
		row3.add(m_btnUnSubscribe);
		
		final Box vbox = Box.createVerticalBox();
		vbox.add(Box.createVerticalStrut(10));
		vbox.add(row1);
		vbox.add(Box.createVerticalStrut(10));
		vbox.add(row2);
		vbox.add(Box.createVerticalStrut(10));
		vbox.add(row3);
		vbox.add(Box.createVerticalStrut(10));
		
		// create accoun chooser panel
		final JPanel acctChooserPanel = new JPanel();
		acctChooserPanel.setBorder(BorderFactory.createTitledBorder(""));
		acctChooserPanel.add(vbox);
		
		// create button panel
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(m_btnClose);
		
		// create action listeners
		m_btnSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onSubscribe();
			}
		});
		m_btnUnSubscribe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onUnSubscribe();
			}
		});
		m_btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				onClose();
			}
		});
		
		// create dlg box
		getContentPane().add(acctChooserPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
	}
	
	void onSubscribe() {
		m_subscribe = true;
		m_acctCode = m_txtAcctCode.getText();
		
		m_rc = true;
		setVisible(false);
	}
	
	void onUnSubscribe() {
		m_subscribe = false;
		m_acctCode = m_txtAcctCode.getText();
		
		m_rc = true;
		setVisible(false);
	}
	
	void onClose() {
		m_acctCode = "";
		m_rc = false;
		setVisible(false);
	}
}