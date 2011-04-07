package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.esper.generic.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class ReceiveFA implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private int	              faDataType;
	private String	          xml;
	
	@Deprecated
	public ReceiveFA() {
	}
	
	public ReceiveFA(final int faDataType, final String xml) {
		this.faDataType = faDataType;
		this.xml = xml;
	}
	
	public int getFaDataType() {
		return faDataType;
	}
	
	public String getXml() {
		return xml;
	}
	
	public void setFaDataType(final int faDataType) {
		this.faDataType = faDataType;
	}
	
	public void setXml(final String xml) {
		this.xml = xml;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
