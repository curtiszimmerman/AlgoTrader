package com.algoTrader.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.algoTrader.ServiceLocator;
import com.algoTrader.service.RuleService;

public class DateUtil {
	
	public static Date toDate(final long time) {
		
		return new Date(time);
	}
	
	public static Date getCurrentEPTime() {
		
		final String strategyName = StrategyUtil.getStartedStrategyName();
		final RuleService ruleService = ServiceLocator.commonInstance()
		        .getRuleService();
		if (ruleService.isInitialized(strategyName) &&
		        !ruleService.isInternalClock(strategyName)) {
			return new Date(ruleService.getCurrentTime(strategyName));
		} else {
			return new Date();
		}
	}
	
	public static int toHour(final long time) {
		
		final Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public static Date getNextThirdFriday(final Date input) {
		
		final Calendar cal = new GregorianCalendar();
		cal.setTime(input);
		cal.set(Calendar.WEEK_OF_MONTH, 3);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if (cal.getTimeInMillis() < input.getTime()) {
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.WEEK_OF_MONTH, 3);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		}
		
		return cal.getTime();
	}
	
	public static Date getNextThirdFriday3MonthCycle(final Date input) {
		
		final Calendar cal = new GregorianCalendar();
		cal.setTime(input);
		
		// round to 3-month cycle
		final int month = (cal.get(Calendar.MONTH) + 1) / 3 * 3 - 1;
		
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.WEEK_OF_MONTH, 3);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if (cal.getTimeInMillis() < input.getTime()) {
			cal.add(Calendar.MONTH, 3);
			cal.set(Calendar.WEEK_OF_MONTH, 3);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		}
		
		return cal.getTime();
	}
	
	/**
	 * 
	 * @param first
	 * @param second
	 * @return the value 0 if first is equal to second; a value less than 0 if
	 *         first is before second; and a value greater than 0 if first is
	 *         after second.
	 */
	public static int compareTime(final Date first, final Date second) {
		
		final Calendar firstCal = new GregorianCalendar();
		firstCal.setTime(first);
		firstCal.set(0, 0, 0);
		
		final Calendar secondCal = new GregorianCalendar();
		secondCal.setTime(second);
		secondCal.set(0, 0, 0);
		
		return firstCal.compareTo(secondCal);
	}
	
	/**
	 * 
	 * @param time
	 * @return the value 0 if currentTime is equal to time; a value less than 0
	 *         if currenTime is before time; and a value greater than 0 if
	 *         currenTime is after time.
	 */
	public static int compareToTime(final Date time) {
		
		return DateUtil.compareTime(DateUtil.getCurrentEPTime(), time);
	}
}