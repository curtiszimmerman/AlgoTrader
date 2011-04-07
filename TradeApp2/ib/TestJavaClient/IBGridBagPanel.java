/*
 * IBGridBagPanel.java
 */

package TestJavaClient;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

class IBGridBagPanel extends JPanel {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	private static final Insets	oneInsets	     = new Insets(1, 1, 1, 1);
	private final GridBagLayout	m_layout	     = new GridBagLayout();
	
	IBGridBagPanel() {
		setLayout(m_layout);
	}
	
	public void setConstraints(final Component comp,
	        final GridBagConstraints constraints) {
		m_layout.setConstraints(comp, constraints);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y) {
		addToPane(c, x, y, 1, 1, 100, 100, IBGridBagPanel.oneInsets);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y,
	        final int w, final int h) {
		addToPane(c, x, y, w, h, 100, 100, IBGridBagPanel.oneInsets);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y,
	        final int w, final int h, final int xGrow, final int yGrow) {
		addToPane(c, x, y, w, h, xGrow, yGrow, IBGridBagPanel.oneInsets);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y,
	        final int w, final int h, final int xGrow, final int yGrow,
	        final int fill) {
		addToPane(c, x, y, w, h, xGrow, yGrow, GridBagConstraints.WEST, fill,
		        IBGridBagPanel.oneInsets);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y,
	        final int w, final int h, final int xGrow, final int yGrow,
	        final int anchor, final int fill) {
		addToPane(c, x, y, w, h, xGrow, yGrow, anchor, fill,
		        IBGridBagPanel.oneInsets);
	}
	
	public void SetObjectPlacement(final Component c, final int x, final int y,
	        final int w, final int h, final int xGrow, final int yGrow,
	        final Insets insets) {
		addToPane(c, x, y, w, h, xGrow, yGrow, insets);
	}
	
	private void addToPane(final Component c, final int x, final int y,
	        final int w, final int h,
	                             final int xGrow, final int yGrow,
	        final Insets insets) {
		addToPane(c, x, y, w, h, xGrow, yGrow, GridBagConstraints.WEST,
		        GridBagConstraints.BOTH, insets);
	}
	
	private void addToPane(final Component c, final int x, final int y,
	        final int w, final int h, final int xGrow,
	        final int yGrow, final int anchor, final int fill,
	        final Insets insets) {
		final GridBagConstraints gbc = new GridBagConstraints();
		
		// the coordinates of the cell in the layout that contains
		// the upper-left corner of the component
		gbc.gridx = x;
		gbc.gridy = y;
		
		// the number of cells that this entry is going to take up
		gbc.gridwidth = w;
		gbc.gridheight = h;
		
		// drive how extra space is distributed among components.
		gbc.weightx = xGrow;
		gbc.weighty = yGrow;
		
		// drive how component is made larger if extra space is available for it
		gbc.fill = fill;
		
		// drive where, within the display area, to place the component when it
		// is larger than its display area.
		gbc.anchor = anchor;
		
		// drive the minimum amount of space between the component and the edges
		// of its display area
		gbc.insets = insets;
		
		add(c, gbc);
	}
}