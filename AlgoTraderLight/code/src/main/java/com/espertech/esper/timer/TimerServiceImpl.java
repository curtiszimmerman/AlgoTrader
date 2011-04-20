// line: 122 - 124 check if timertask exists
// line: 160 do not run as daemon-thread
/*
 * *************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved. *
 * http://esper.codehaus.org *
 * http://www.espertech.com *
 * ------------------------------------------------------------------------------
 * ---- *
 * The software in this package is published under the terms of the GPL license
 * *
 * a copy of which has been included with this distribution in the license.txt
 * file. *
 * ******************************************************************************
 * ******
 */
package com.espertech.esper.timer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the internal clocking service interface.
 */
public final class TimerServiceImpl implements TimerService {
	private final String	            engineURI;
	private final long	                msecTimerResolution;
	private TimerCallback	            timerCallback;
	private ScheduledThreadPoolExecutor	timer;
	private EPLTimerTask	            timerTask;
	private static AtomicInteger	    NEXT_ID	= new AtomicInteger(0);
	private final int	                id;
	
	/**
	 * Constructor.
	 * 
	 * @param msecTimerResolution
	 *            is the millisecond resolution or interval the internal timer
	 *            thread
	 *            processes schedules
	 * @param engineURI
	 *            engine URI
	 */
	public TimerServiceImpl(final String engineURI,
	        final long msecTimerResolution) {
		this.engineURI = engineURI;
		this.msecTimerResolution = msecTimerResolution;
		id = TimerServiceImpl.NEXT_ID.getAndIncrement();
	}
	
	/**
	 * Returns the timer resolution.
	 * 
	 * @return the millisecond resolution or interval the internal timer thread
	 *         processes schedules
	 */
	public long getMsecTimerResolution() {
		return msecTimerResolution;
	}
	
	@Override
	public void setCallback(final TimerCallback timerCallback) {
		this.timerCallback = timerCallback;
	}
	
	@Override
	public final void startInternalClock() {
		if (timer != null) {
			TimerServiceImpl.log
			        .warn(".startInternalClock Internal clock is already started, stop first before starting, operation not completed");
			return;
		}
		
		if (TimerServiceImpl.log.isDebugEnabled()) {
			TimerServiceImpl.log
			        .debug(".startInternalClock Starting internal clock daemon thread, resolution=" +
			                msecTimerResolution);
		}
		
		if (timerCallback == null) { throw new IllegalStateException(
		        "Timer callback not set"); }
		
		getScheduledThreadPoolExecutorDaemonThread();
		timerTask = new EPLTimerTask(timerCallback);
		
		// With no delay start every internal
		final ScheduledFuture<?> future = timer.scheduleAtFixedRate(timerTask,
		        0, msecTimerResolution, TimeUnit.MILLISECONDS);
		timerTask.setFuture(future);
	}
	
	@Override
	public final void stopInternalClock(final boolean warnIfNotStarted) {
		if (timer == null) {
			if (warnIfNotStarted) {
				TimerServiceImpl.log
				        .warn(".stopInternalClock Internal clock is already stopped, start first before stopping, operation not completed");
			}
			return;
		}
		
		if (TimerServiceImpl.log.isDebugEnabled()) {
			TimerServiceImpl.log
			        .debug(".stopInternalClock Stopping internal clock daemon thread");
		}
		
		timer.shutdown();
		
		try {
			// Sleep for 100 ms to await the internal timer
			Thread.sleep(100);
		} catch (final InterruptedException e) {
			TimerServiceImpl.log.info("Timer start wait interval interruped");
		}
		
		timer = null;
	}
	
	@Override
	public void enableStats() {
		if (timerTask != null) {
			timerTask._enableStats = true;
		}
	}
	
	@Override
	public void disableStats() {
		if (timerTask != null) {
			timerTask._enableStats = false;
			// now it is safe to reset stats without any synchronization
			timerTask.resetStats();
		}
	}
	
	@Override
	public long getMaxDrift() {
		return timerTask._maxDrift;
	}
	
	@Override
	public long getLastDrift() {
		return timerTask._lastDrift;
	}
	
	@Override
	public long getTotalDrift() {
		return timerTask._totalDrift;
	}
	
	@Override
	public long getInvocationCount() {
		return timerTask._invocationCount;
	}
	
	private void getScheduledThreadPoolExecutorDaemonThread() {
		timer = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			// set new thread as daemon thread and name appropriately
			@Override
			public Thread newThread(final Runnable r) {
				String uri = engineURI;
				if (engineURI == null)
				{
					uri = "default";
				}
				final Thread t = new Thread(r, "com.espertech.esper.Timer-" +
				        uri + "-" + id);
				// t.setDaemon(true);
				return t;
			}
		});
		timer.setMaximumPoolSize(timer.getCorePoolSize());
	}
	
	private static final Log	log	= LogFactory.getLog(TimerServiceImpl.class);
}
