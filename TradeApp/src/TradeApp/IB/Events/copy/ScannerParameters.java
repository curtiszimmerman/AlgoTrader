
package TradeApp.IB.Events.copy;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class ScannerParameters implements DataItem {
	private String	xml;
	
	@Deprecated
	public ScannerParameters() {}
	
	public ScannerParameters(final String xml) {
		this.xml = xml;
	}
	
	public String getXml() {
		return this.xml;
	}
	
	public void setXml(final String xml) {
		this.xml = xml;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
