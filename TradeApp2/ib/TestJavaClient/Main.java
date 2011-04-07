/*
 * Main.java
 */
package TestJavaClient;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
	
	// This method is called to start the application
	public static void main(final String args[]) {
		final SampleFrame sampleFrame = new SampleFrame();
		sampleFrame.setVisible(true);
	}
	
	static public void inform(final Component parent, final String str) {
		if (SwingUtilities.isEventDispatchThread()) {
			Main.showMsg(parent, str, JOptionPane.INFORMATION_MESSAGE);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Main.showMsg(parent, str, JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
	}
	
	static private void showMsg(final Component parent, final String str,
	        final int type) {
		// this function pops up a dlg box displaying a message
		JOptionPane.showMessageDialog(parent, str, "IB Java Test Client", type);
	}
}