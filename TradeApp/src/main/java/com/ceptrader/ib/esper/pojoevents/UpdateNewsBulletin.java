package com.ceptrader.ib.esper.pojoevents;

import com.ceptrader.generic.esper.pojoevents.DataItem;
import com.ceptrader.util.BasicUtils;

public class UpdateNewsBulletin implements DataItem {
	private static final long	serialVersionUID	= 1L;
	private String	          message;
	private int	              msgId;
	private int	              msgType;
	private String	          origExchange;
	
	@Deprecated
	public UpdateNewsBulletin() {
	}
	
	public UpdateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		this.msgId = msgId;
		this.msgType = msgType;
		this.message = message;
		this.origExchange = origExchange;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getMsgId() {
		return msgId;
	}
	
	public int getMsgType() {
		return msgType;
	}
	
	public String getOrigExchange() {
		return origExchange;
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
