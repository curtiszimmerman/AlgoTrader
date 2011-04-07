// line 82: use CustomSender
// line 298: send CurrentTimeEvent for the currentEventTime not the
// lastEventTime
// line 304: do not send final time processTimeEvent
/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved. *
 * http://esper.codehaus.org *
 * http://www.espertech.com *
 * ----------------------------------------------------------------------------
 * ------ *
 * The software in this package is published under the terms of the GPL license
 * *
 * a copy of which has been included with this distribution in the license.txt
 * file. *
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
public abstract class AbstractCoordinatedAdapter implements CoordinatedAdapter {
	private static final Log	             log	         = LogFactory
	                                                                 .getLog(AbstractCoordinatedAdapter.class);
	
	/**
	 * Statement management.
	 */
	protected final AdapterStateManager	     stateManager	 = new AdapterStateManager();
	
	/**
	 * Sorted events to be sent.
	 */
	protected final SortedSet<SendableEvent>	eventsToSend	= new TreeSet<SendableEvent>(
	                                                                 new SendableEventComparator());
	
	/**
	 * Slot for scheduling.
	 */
	protected ScheduleSlot	                 scheduleSlot;
	
	private EPServiceProvider	             epService;
	private EPRuntime	                     runtime;
	private SchedulingService	             schedulingService;
	private boolean	                         usingEngineThread,
	        usingExternalTimer;
	private long	                         currentTime	 = 0;
	private long	                         lastEventTime	 = 0;
	private long	                         startTime;
	private AbstractSender	                 sender;
	
	/**
	 * Ctor.
	 * 
	 * @param epService
	 *            - the EPServiceProvider for the engine runtime and services
	 * @param usingEngineThread
	 *            - true if the Adapter should set time by the scheduling
	 *            service in the engine,
	 *            false if it should set time externally through the calling
	 *            thread
	 * @param usingExternalTimer
	 *            - true to use esper's external timer mechanism instead of
	 *            internal timing
	 */
	public AbstractCoordinatedAdapter(final EPServiceProvider epService,
	        final boolean usingEngineThread, final boolean usingExternalTimer) {
		this.usingEngineThread = usingEngineThread;
		this.usingExternalTimer = usingExternalTimer;
		
		setSender(new CustomSender());
		if (epService == null) { return; }
		if (!(epService instanceof EPServiceProviderSPI)) { throw new IllegalArgumentException(
		        "Invalid epService provided"); }
		this.epService = epService;
		runtime = epService.getEPRuntime();
		schedulingService = ((EPServiceProviderSPI) epService)
		        .getSchedulingService();
	}
	
	@Override
	public AdapterState getState() {
		return stateManager.getState();
	}
	
	@Override
	public void start() throws EPException {
		if (ExecutionPathDebugLog.isDebugEnabled &&
		        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
			AbstractCoordinatedAdapter.log.debug(".start");
		}
		if (runtime == null) { throw new EPException(
		        "Attempting to start an Adapter that hasn't had the epService provided"); }
		startTime = getCurrentTime();
		if (ExecutionPathDebugLog.isDebugEnabled &&
		        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
			AbstractCoordinatedAdapter.log.debug(".start startTime==" +
			        startTime);
		}
		stateManager.start();
		sender.setRuntime(runtime);
		continueSendingEvents();
	}
	
	@Override
	public void pause() throws EPException {
		stateManager.pause();
	}
	
	@Override
	public void resume() throws EPException {
		stateManager.resume();
		continueSendingEvents();
	}
	
	@Override
	public void destroy() throws EPException {
		if (sender != null) {
			sender.onFinish();
		}
		stateManager.destroy();
		close();
	}
	
	@Override
	public void stop() throws EPException {
		if (ExecutionPathDebugLog.isDebugEnabled &&
		        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
			AbstractCoordinatedAdapter.log.debug(".stop");
		}
		stateManager.stop();
		eventsToSend.clear();
		currentTime = 0;
		reset();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.espertech.esperio.ReadableAdapter#disallowStateChanges()
	 */
	@Override
	public void disallowStateTransitions() {
		stateManager.disallowStateTransitions();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.espertech.esperio.ReadableAdapter#setUsingEngineThread(boolean)
	 */
	@Override
	public void setUsingEngineThread(final boolean usingEngineThread) {
		this.usingEngineThread = usingEngineThread;
	}
	
	/**
	 * Set to true to use esper's external timer mechanism instead of internal
	 * timing
	 * 
	 * @param usingExternalTimer
	 *            true for external timer
	 */
	@Override
	public void setUsingExternalTimer(final boolean usingExternalTimer) {
		this.usingExternalTimer = usingExternalTimer;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.espertech.esperio.CoordinatedAdapter#setScheduleSlot(com.espertech
	 * .esper.schedule.ScheduleSlot)
	 */
	@Override
	public void setScheduleSlot(final ScheduleSlot scheduleSlot) {
		this.scheduleSlot = scheduleSlot;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.espertech.esperio.CoordinatedAdapter#setEPService(com.espertech.esper
	 * .client.EPServiceProvider)
	 */
	@Override
	public void setEPService(final EPServiceProvider epService) {
		if (epService == null) { throw new NullPointerException(
		        "epService cannot be null"); }
		if (!(epService instanceof EPServiceProviderSPI)) { throw new IllegalArgumentException(
		        "Invalid type of EPServiceProvider"); }
		final EPServiceProviderSPI spi = (EPServiceProviderSPI) epService;
		runtime = spi.getEPRuntime();
		schedulingService = spi.getSchedulingService();
		sender.setRuntime(runtime);
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
	 * Reset all the changeable state of this Adapter, as if it were just
	 * created.
	 */
	protected abstract void reset();
	
	private void continueSendingEvents() {
		boolean keepLooping = true;
		while (stateManager.getState() == AdapterState.STARTED && keepLooping) {
			currentTime = getCurrentTime();
			if (ExecutionPathDebugLog.isDebugEnabled &&
			        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
				AbstractCoordinatedAdapter.log
				        .debug(".continueSendingEvents currentTime==" +
				                currentTime);
			}
			fillEventsToSend();
			sendSoonestEvents();
			keepLooping = waitToSendEvents();
		}
	}
	
	private boolean waitToSendEvents() {
		if (usingExternalTimer) {
			return false;
		} else if (usingEngineThread) {
			scheduleNextCallback();
			return false;
		} else {
			long sleepTime = 0;
			if (eventsToSend.isEmpty()) {
				sleepTime = 100;
			} else {
				sleepTime = eventsToSend.first().getSendTime() -
				        (currentTime - startTime);
			}
			
			try {
				Thread.sleep(sleepTime);
			} catch (final InterruptedException ex) {
				throw new EPException(ex);
			}
			return true;
		}
	}
	
	private long getCurrentTime() {
		return usingEngineThread ? schedulingService.getTime() : System
		        .currentTimeMillis();
	}
	
	private void fillEventsToSend() {
		if (eventsToSend.isEmpty()) {
			final SendableEvent event = read();
			if (event != null) {
				eventsToSend.add(event);
			}
		}
	}
	
	private void sendSoonestEvents() {
		if (usingExternalTimer) {
			// send all events in order and when time clicks over send time
			// event for previous time
			while (!eventsToSend.isEmpty()) {
				final long currentEventTime = eventsToSend.first()
				        .getSendTime();
				// check whether time has increased. Cannot go backwards due to
				// checks elsewhere
				if (currentEventTime > lastEventTime) {
					sender.sendEvent(null, new CurrentTimeEvent(
					        currentEventTime));
					lastEventTime = currentEventTime;
				}
				sendFirstEvent();
			}
			// send final time processTimeEvent
			// this.sender.sendEvent(null, new CurrentTimeEvent(lastEventTime));
		} else {
			// watch time and send events to catch up
			while (!eventsToSend.isEmpty() &&
			        eventsToSend.first().getSendTime() <= currentTime -
			                startTime) {
				sendFirstEvent();
			}
		}
	}
	
	private void sendFirstEvent() {
		if (ExecutionPathDebugLog.isDebugEnabled &&
		        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
			AbstractCoordinatedAdapter.log
			        .debug(".sendFirstEvent currentTime==" + currentTime);
			AbstractCoordinatedAdapter.log
			        .debug(".sendFirstEvent sending event " +
			                eventsToSend.first() + ", its sendTime==" +
			                eventsToSend.first().getSendTime());
		}
		sender.setRuntime(runtime);
		eventsToSend.first().send(sender);
		replaceFirstEventToSend();
	}
	
	private void scheduleNextCallback() {
		final ScheduleHandleCallback nextScheduleCallback = new ScheduleHandleCallback() {
			@Override
			public void scheduledTrigger(
			        final ExtensionServicesContext extensionServicesContext) {
				continueSendingEvents();
			}
		};
		final EPServiceProviderSPI spi = (EPServiceProviderSPI) epService;
		final StatementMetricHandle metricsHandle = spi
		        .getMetricReportingService().getStatementHandle(
		                "AbstractCoordinatedAdapter",
		                "AbstractCoordinatedAdapter");
		final EPStatementHandleCallback scheduleCSVHandle = new EPStatementHandleCallback(
		        new EPStatementHandle("AbstractCoordinatedAdapter",
		                "AbstractCoordinatedAdapter", null,
		                new StatementRWLockImpl("CSV", false),
		                "AbstractCoordinatedAdapter", false, metricsHandle, 0,
		                false, new StatementFilterVersion()),
		        nextScheduleCallback);
		ScheduleSlot nextScheduleSlot;
		
		if (eventsToSend.isEmpty()) {
			if (ExecutionPathDebugLog.isDebugEnabled &&
			        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
				AbstractCoordinatedAdapter.log
				        .debug(".scheduleNextCallback no events to send, scheduling callback in 100 ms");
			}
			nextScheduleSlot = new ScheduleSlot(0, 0);
			schedulingService.add(100, scheduleCSVHandle, nextScheduleSlot);
		} else {
			// Offset is not a function of the currentTime alone.
			final long baseMsec = currentTime - startTime;
			final long afterMsec = eventsToSend.first().getSendTime() -
			        baseMsec;
			
			nextScheduleSlot = eventsToSend.first().getScheduleSlot();
			if (ExecutionPathDebugLog.isDebugEnabled &&
			        AbstractCoordinatedAdapter.log.isDebugEnabled()) {
				AbstractCoordinatedAdapter.log
				        .debug(".scheduleNextCallback schedulingCallback in " +
				                afterMsec + " milliseconds");
			}
			schedulingService.add(afterMsec, scheduleCSVHandle,
			        nextScheduleSlot);
		}
	}
	
	/**
	 * Returns the runtime.
	 * 
	 * @return runtime
	 */
	public EPRuntime getRuntime() {
		return runtime;
	}
	
	/**
	 * Sets a new sender to use.
	 * 
	 * @param sender
	 *            for sending
	 */
	public void setSender(final AbstractSender sender) {
		this.sender = sender;
		this.sender.setRuntime(runtime);
	}
}
