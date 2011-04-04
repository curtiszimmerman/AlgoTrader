
package TradeApp.Data.copy;

import java.util.Date;

public interface TSDataItem extends DataItem, HasTypicalValue {
	public void setDate(final String dateTime);
	
	public String getDate();
	
	public Date getDateValue();
}
