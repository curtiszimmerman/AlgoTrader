/*
 * IBTextPanel.java
 */
package TestJavaClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

class IBTextPanel extends JPanel {
	/**
     * 
     */
	private static final long	serialVersionUID	= 1L;
	public static final Color	textBackgroundColor	= new Color(5, 5, 5);
	public static final Color	textForegroundColor	= new Color(0, 245, 0);
	public static final Font	textComponentFont	= new JList().getFont();
	public static final Color	textCaretColor	    = Color.WHITE;
	public static final String	lineSeparator	    = System.getProperty("line.separator");
	
	private final JTextArea	    m_textArea	        = new JTextArea();
	private final JScrollPane	m_scrollPane	    = new JScrollPane(
	                                                        m_textArea);
	private final static String	CRLF	            = "\r\n";
	private final static String	LF	                = "\n";
	private final static String	TAB	                = "\t";
	private final static String	EIGHT_SPACES	    = "        ";
	private final static String	EMPTY_STRING	    = "";
	
	IBTextPanel() {
		this(null, false);
	}
	
	IBTextPanel(final String title, final boolean editable) {
		super(new BorderLayout());
		if (title != null) {
			final Border border = BorderFactory.createTitledBorder(title);
			setBorder(border);
		}
		m_textArea.setBackground(IBTextPanel.textBackgroundColor);
		m_textArea.setForeground(IBTextPanel.textForegroundColor);
		m_textArea.setFont(IBTextPanel.textComponentFont);
		m_textArea.setCaretColor(IBTextPanel.textCaretColor);
		m_textArea.setEditable(editable);
		add(m_scrollPane);
	}
	
	public void clear() {
		m_textArea.setText(IBTextPanel.EMPTY_STRING);
	}
	
	public void setText(final String text) {
		m_textArea.setText(text);
		if (m_textArea.isEditable()) {
			moveCursorToBeginning();
		} else {
			moveCursorToEnd();
		}
	}
	
	public void setTextDetabbed(final String text) {
		m_textArea.setText(detabbed(text));
	}
	
	public String getText() {
		return m_textArea.getText();
	}
	
	public void add(final String line) {
		m_textArea.append(line + IBTextPanel.lineSeparator);
		moveCursorToEnd();
	}
	
	public void moveCursorToEnd() {
		m_textArea.setCaretPosition(m_textArea.getText().length());
	}
	
	public void moveCursorToBeginning() {
		m_textArea.setCaretPosition(0);
	}
	
	public void add(final Collection lines) {
		for (final Iterator iter = lines.iterator(); iter.hasNext();) {
			add((String) iter.next());
		}
	}
	
	public void addText(final String text) {
		add(IBTextPanel.tokenizedIntoArrayList(detabbed(text), IBTextPanel.LF));
	}
	
	public static ArrayList tokenizedIntoArrayList(final String source,
	        final String delimiter) {
		final ArrayList list = new ArrayList();
		final StringTokenizer st = new StringTokenizer(source, delimiter);
		while (st.hasMoreTokens()) {
			final String temp = st.nextToken();
			list.add(temp);
		}
		return list;
	}
	
	private String detabbed(final String text) {
		return text.replaceAll(IBTextPanel.TAB, IBTextPanel.EIGHT_SPACES);
	}
}