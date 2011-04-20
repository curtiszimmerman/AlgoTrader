// line 46 - 51: commented out pre-creation
// line 75 - 77: export the beanToSend (so that it can be modified by the TickCSVInputAdapter
/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esperio;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.espertech.esper.client.EPException;
import com.espertech.esper.schedule.ScheduleSlot;

/**
 * An implementation of SendableEvent that wraps a Map event for
 * sending into the runtime.
 * 
 */
public class SendableBeanEvent extends AbstractSendableEvent
{
	private final Object beanToSend;

	/**
	 * Converts mapToSend to an instance of beanClass
	 * @param mapToSend - the map containing data to send into the runtime
	 * @param beanClass  - type of the bean to create from mapToSend
	 * @param eventTypeName - the event type name for the map event
	 * @param timestamp - the timestamp for this event
	 * @param scheduleSlot - the schedule slot for the entity that created this event
	 */
	public SendableBeanEvent(Map<String, Object> mapToSend, Class beanClass, String eventTypeName, long timestamp, ScheduleSlot scheduleSlot)
	{
		super(timestamp, scheduleSlot);

		try {
			this.beanToSend = beanClass.newInstance();
			// pre-create nested properties if any, as BeanUtils does not otherwise populate 'null' objects from their respective properties

			/*
			 * PropertyDescriptor[] pds =
			 * ReflectUtils.getBeanSetters(beanClass); for (PropertyDescriptor
			 * pd : pds) { if (!pd.getPropertyType().isPrimitive() &&
			 * !pd.getPropertyType().getName().startsWith("java")) {
			 * BeanUtils.setProperty(beanToSend, pd.getName(),
			 * pd.getPropertyType().newInstance()); } }
			 */
			
			// this method silently ignores read only properties on the dest bean but we should
			// have caught them in CSVInputAdapter.constructPropertyTypes.
			BeanUtils.copyProperties(this.beanToSend, mapToSend);
		} catch (Exception e) {
			throw new EPException("Cannot populate bean instance", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.espertech.esperio.SendableEvent#send(com.espertech.esper.client.EPRuntime)
	 */
	public void send(AbstractSender sender)
	{
		sender.sendEvent(this, this.beanToSend);
	}

	public String toString()
	{
		return this.beanToSend.toString();
	}
	
	public Object getBeanToSend() {
		return this.beanToSend;
	}
}
