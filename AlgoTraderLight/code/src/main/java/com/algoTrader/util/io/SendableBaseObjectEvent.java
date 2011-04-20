package com.algoTrader.util.io;

import com.algoTrader.BaseObject;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esperio.AbstractSendableEvent;
import com.espertech.esperio.AbstractSender;

public class SendableBaseObjectEvent extends AbstractSendableEvent {
	
	private final BaseObject	eventToSend;
	
	public SendableBaseObjectEvent(final BaseObject object,
	        final long timestamp, final ScheduleSlot scheduleSlot) {
		super(timestamp, scheduleSlot);
		
		eventToSend = object;
	}
	
	@Override
	public void send(final AbstractSender sender) {
		sender.sendEvent(this, eventToSend);
	}
	
	@Override
	public String toString() {
		return eventToSend.toString();
	}
	
	public Object getBeanToSend() {
		return eventToSend;
	}
}
