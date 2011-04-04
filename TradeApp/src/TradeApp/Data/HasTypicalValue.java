
package TradeApp.Data;

public interface HasTypicalValue extends DataItem {
	double getTypicalValue();
	
	double getTypicalValue(int i);
	
	int[] getRange();
	
	double getTypicalValue(String name);
	
	String[] getNames();
}
