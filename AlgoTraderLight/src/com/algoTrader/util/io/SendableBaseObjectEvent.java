package com.algoTrader.util.io;

import com.algoTrader.BaseObject;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esperio.AbstractSendableEvent;
import com.espertech.esperio.AbstractSender;

public class SendableBaseObjectEvent extends AbstractSendableEvent {

	private final BaseObject eventToSend;

	public SendableBaseObjectEvent(BaseObject object, long timestamp, ScheduleSlot scheduleSlot) {
		super(timestamp, scheduleSlot);

		this.eventToSend = object;
	}

	public void send(AbstractSender sender) {
		sender.sendEvent(this, this.eventToSend);
	}

	public String toString() {
		return this.eventToSend.toString();
	}

	public Object getBeanToSend() {
		return this.eventToSend;
	}
}
