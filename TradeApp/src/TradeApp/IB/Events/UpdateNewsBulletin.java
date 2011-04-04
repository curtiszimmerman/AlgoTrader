
package TradeApp.IB.Events;

import TradeApp.Data.DataItem;
import TradeApp.Util.BasicUtils;

public class UpdateNewsBulletin implements DataItem {
	private String	message;
	private int		msgId;
	private int		msgType;
	private String	origExchange;
	
	@Deprecated
	public UpdateNewsBulletin() {}
	
	public UpdateNewsBulletin(final int msgId, final int msgType,
			final String message, final String origExchange) {
		this.msgId = msgId;
		this.msgType = msgType;
		this.message = message;
		this.origExchange = origExchange;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getMsgId() {
		return this.msgId;
	}
	
	public int getMsgType() {
		return this.msgType;
	}
	
	public String getOrigExchange() {
		return this.origExchange;
	}
	
	public void setMessage(final String message) {
		this.message = message;
	}
	
	public void setMsgId(final int msgId) {
		this.msgId = msgId;
	}
	
	public void setMsgType(final int msgType) {
		this.msgType = msgType;
	}
	
	public void setOrigExchange(final String origExchange) {
		this.origExchange = origExchange;
	}
	
	@Override
	public String toString() {
		return BasicUtils.toString(this);
	}
}
