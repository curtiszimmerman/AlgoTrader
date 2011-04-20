// line 82: use CustomSender
// line 298: send CurrentTimeEvent for the currentEventTime not the lastEventTime
// line 304: do not send final time processTimeEvent
/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esperio;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.algoTrader.util.io.CustomSender;
import com.espertech.esper.adapter.AdapterState;
import com.espertech.esper.adapter.AdapterStateManager;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.core.EPServiceProviderSPI;
import com.espertech.esper.core.EPStatementHandle;
import com.espertech.esper.core.EPStatementHandleCallback;
import com.espertech.esper.core.ExtensionServicesContext;
import com.espertech.esper.core.StatementFilterVersion;
import com.espertech.esper.core.StatementRWLockImpl;
import com.espertech.esper.epl.metric.StatementMetricHandle;
import com.espertech.esper.schedule.ScheduleHandleCallback;
import com.espertech.esper.schedule.ScheduleSlot;
import com.espertech.esper.schedule.SchedulingService;
import com.espertech.esper.util.ExecutionPathDebugLog;

/**
 * A skeleton implementation for coordinated adapter reading, for adapters that
 * can do timestamp-coordinated input.
 */
public abstract class AbstractCoordinatedAdapter implements CoordinatedAdapter
{
	private static final Log log = LogFactory.getLog(AbstractCoordinatedAdapter.class);

    /**
     * Statement management.
     */
    protected final AdapterStateManager stateManager = new AdapterStateManager();

    /**
     * Sorted events to be sent.
     */
    protected final SortedSet<SendableEvent> eventsToSend = new TreeSet<SendableEvent>(new SendableEventComparator());

    /**
     * Slot for scheduling.
     */
    protected ScheduleSlot scheduleSlot;

    private EPServiceProvider epService;
    private EPRuntime runtime;
	private SchedulingService schedulingService;
	private boolean usingEngineThread, usingExternalTimer;
	private long currentTime = 0;
	private long lastEventTime = 0;
	private long startTime;
	private AbstractSender sender;

	/**
	 * Ctor.
	 * @param epService - the EPServiceProvider for the engine runtime and services
	 * @param usingEngineThread - true if the Adapter should set time by the scheduling service in the engine,
	 *                            false if it should set time externally through the calling thread
	 * @param usingExternalTimer - true to use esper's external timer mechanism instead of internal timing
	 */
	public AbstractCoordinatedAdapter(EPServiceProvider epService, boolean usingEngineThread, boolean usingExternalTimer)
	{
		this.usingEngineThread = usingEngineThread;
		this.usingExternalTimer = usingExternalTimer;

		this.setSender(new CustomSender());
		if(epService == null)
		{
			return;
		}
		if(!(epService instanceof EPServiceProviderSPI))
		{
			throw new IllegalArgumentException("Invalid epService provided");
		}
		this.epService = epService;
		this.runtime = epService.getEPRuntime();
		this.schedulingService = ((EPServiceProviderSPI) epService).getSchedulingService();
	}

	public AdapterState getState()
	{
		return this.stateManager.getState();
	}

	public void start() throws EPException
	{
        if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
        {
            log.debug(".start");
        }
		if (this.runtime == null)
		{
			throw new EPException("Attempting to start an Adapter that hasn't had the epService provided");
		}
		this.startTime = getCurrentTime();
		if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
        {
			log.debug(".start startTime==" + this.startTime);
        }
		this.stateManager.start();
		this.sender.setRuntime(this.runtime);
        continueSendingEvents();
	}

	public void pause() throws EPException
	{
		this.stateManager.pause();
	}

	public void resume() throws EPException
	{
		this.stateManager.resume();
		continueSendingEvents();
	}

	public void destroy() throws EPException
	{
		if (this.sender != null) {
			this.sender.onFinish();
		}
		this.stateManager.destroy();
		close();
	}

	public void stop() throws EPException
	{
		if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
        {
            log.debug(".stop");
        }
		this.stateManager.stop();
		this.eventsToSend.clear();
		this.currentTime = 0;
		reset();
	}

	/* (non-Javadoc)
	 * @see com.espertech.esperio.ReadableAdapter#disallowStateChanges()
	 */
	public void disallowStateTransitions()
	{
		this.stateManager.disallowStateTransitions();
	}

	/* (non-Javadoc)
	 * @see com.espertech.esperio.ReadableAdapter#setUsingEngineThread(boolean)
	 */
	public void setUsingEngineThread(boolean usingEngineThread)
	{
		this.usingEngineThread = usingEngineThread;
	}

    /**
     * Set to true to use esper's external timer mechanism instead of internal timing
     * @param usingExternalTimer true for external timer
     */
    public void setUsingExternalTimer(boolean usingExternalTimer)
	{
		this.usingExternalTimer = usingExternalTimer;
	}

	/* (non-Javadoc)
	 * @see com.espertech.esperio.CoordinatedAdapter#setScheduleSlot(com.espertech.esper.schedule.ScheduleSlot)
	 */
	public void setScheduleSlot(ScheduleSlot scheduleSlot)
	{
		this.scheduleSlot = scheduleSlot;
	}

	/* (non-Javadoc)
	 * @see com.espertech.esperio.CoordinatedAdapter#setEPService(com.espertech.esper.client.EPServiceProvider)
	 */
	public void setEPService(EPServiceProvider epService)
	{
		if(epService == null)
		{
			throw new NullPointerException("epService cannot be null");
		}
		if(!(epService instanceof EPServiceProviderSPI))
		{
			throw new IllegalArgumentException("Invalid type of EPServiceProvider");
		}
		EPServiceProviderSPI spi = (EPServiceProviderSPI)epService;
		this.runtime = spi.getEPRuntime();
		this.schedulingService = spi.getSchedulingService();
		this.sender.setRuntime(this.runtime);
    }

	/**
	 * Perform any actions specific to this Adapter that should
	 * be completed before the Adapter is stopped.
	 */
	protected abstract void close();

	/**
	 * Remove the first member of eventsToSend and insert
	 * another event chosen in some fashion specific to this
	 * Adapter.
	 */
	protected abstract void replaceFirstEventToSend();

	/**
	 * Reset all the changeable state of this Adapter, as if it were just created.
	 */
	protected abstract void reset();

	private void continueSendingEvents()
	{
		boolean keepLooping = true;
		while (this.stateManager.getState() == AdapterState.STARTED && keepLooping)
		{
			this.currentTime = getCurrentTime();
            if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
            {
				log.debug(".continueSendingEvents currentTime==" + this.currentTime);
            }
            fillEventsToSend();
			sendSoonestEvents();
			keepLooping = waitToSendEvents();
		}
	}

	private boolean waitToSendEvents()
	{
		if (this.usingExternalTimer)
		{
			return false;
		}
 else if (this.usingEngineThread)
		{
			scheduleNextCallback();
			return false;
		}
		else
		{
			long sleepTime = 0;
			if (this.eventsToSend.isEmpty())
			{
				sleepTime = 100;
			}
			else
			{
				sleepTime = this.eventsToSend.first().getSendTime() - (this.currentTime - this.startTime);
			}

			try
			{
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException ex)
			{
				throw new EPException(ex);
			}
			return true;
		}
	}

	private long getCurrentTime()
	{
		return this.usingEngineThread ? this.schedulingService.getTime() : System.currentTimeMillis();
	}

	private void fillEventsToSend()
	{
		if (this.eventsToSend.isEmpty())
		{
			SendableEvent event = read();
			if(event != null)
			{
				this.eventsToSend.add(event);
			}
		}
	}

	private void sendSoonestEvents()
	{
		if (this.usingExternalTimer)
		{
			// send all events in order and when time clicks over send time event for previous time
			while (!this.eventsToSend.isEmpty())
			{
				long currentEventTime = this.eventsToSend.first().getSendTime();
				// check whether time has increased. Cannot go backwards due to checks elsewhere
				if (currentEventTime > this.lastEventTime)
				{
					this.sender.sendEvent(null, new CurrentTimeEvent(currentEventTime));
					this.lastEventTime = currentEventTime;
				}
				sendFirstEvent();
			}
			// send final time processTimeEvent
			// this.sender.sendEvent(null, new CurrentTimeEvent(lastEventTime));
		}
		else
		{
			// watch time and send events to catch up
			while (!this.eventsToSend.isEmpty() && this.eventsToSend.first().getSendTime() <= this.currentTime - this.startTime)
			{
	            sendFirstEvent();
			}
		}
	}

	private void sendFirstEvent()
	{
		if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
		{
			log.debug(".sendFirstEvent currentTime==" + this.currentTime);
			log.debug(".sendFirstEvent sending event " + this.eventsToSend.first() + ", its sendTime==" + this.eventsToSend.first().getSendTime());
		}
		this.sender.setRuntime(this.runtime);
		this.eventsToSend.first().send(this.sender);
		replaceFirstEventToSend();
	}

	private void scheduleNextCallback()
	{
		ScheduleHandleCallback nextScheduleCallback = new ScheduleHandleCallback() { public void scheduledTrigger(ExtensionServicesContext extensionServicesContext) { continueSendingEvents(); } };
		EPServiceProviderSPI spi = (EPServiceProviderSPI) this.epService;
        StatementMetricHandle metricsHandle = spi.getMetricReportingService().getStatementHandle("AbstractCoordinatedAdapter", "AbstractCoordinatedAdapter");
		EPStatementHandleCallback scheduleCSVHandle = new EPStatementHandleCallback(new EPStatementHandle("AbstractCoordinatedAdapter", "AbstractCoordinatedAdapter", null, new StatementRWLockImpl("CSV", false), "AbstractCoordinatedAdapter", false, metricsHandle, 0, false, new StatementFilterVersion()), nextScheduleCallback);
        ScheduleSlot nextScheduleSlot;

		if (this.eventsToSend.isEmpty())
		{
            if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
            {
			    log.debug(".scheduleNextCallback no events to send, scheduling callback in 100 ms");
            }
            nextScheduleSlot = new ScheduleSlot(0,0);
			this.schedulingService.add(100, scheduleCSVHandle, nextScheduleSlot);
		}
		else
		{
            // Offset is not a function of the currentTime alone.
			long baseMsec = this.currentTime - this.startTime;
			long afterMsec = this.eventsToSend.first().getSendTime() - baseMsec;

			nextScheduleSlot = this.eventsToSend.first().getScheduleSlot();
            if ((ExecutionPathDebugLog.isDebugEnabled) && (log.isDebugEnabled()))
            {
			    log.debug(".scheduleNextCallback schedulingCallback in " + afterMsec + " milliseconds");
            }
			this.schedulingService.add(afterMsec, scheduleCSVHandle, nextScheduleSlot);
		}
	}

    /**
     * Returns the runtime.
     * @return runtime
     */
    public EPRuntime getRuntime() {
		return this.runtime;
	}

    /**
     * Sets a new sender to use.
     * @param sender for sending
     */
    public void setSender(AbstractSender sender) {
		this.sender = sender;
		this.sender.setRuntime(this.runtime);
	}
}
