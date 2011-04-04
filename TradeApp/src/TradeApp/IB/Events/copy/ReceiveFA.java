
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class ReceiveFA implements DataItem {
	private int		faDataType;
	private String	xml;
	
	@Deprecated
	public ReceiveFA() {}
	
	public ReceiveFA(final int faDataType, final String xml) {
		this.faDataType = faDataType;
		this.xml = xml;
	}
	
	public int getFaDataType() {
		return this.faDataType;
	}
	
	public String getXml() {
		return this.xml;
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
