/*
 * UnderComp.java
 */

package com.ib.client;

public class TagValue {
	
	public String	m_tag;
	public String	m_value;
	
	public TagValue() {
	}
	
	public TagValue(final String p_tag, final String p_value) {
		m_tag = p_tag;
		m_value = p_value;
	}
	
	@Override
	public boolean equals(final Object p_other) {
		
		if (this == p_other) { return true; }
		
		if (p_other == null) { return false; }
		
		final TagValue l_theOther = (TagValue) p_other;
		
		if (Util.StringCompare(m_tag, l_theOther.m_tag) != 0 ||
		        Util.StringCompare(m_value, l_theOther.m_value) != 0) { return false; }
		
		return true;
	}
}
