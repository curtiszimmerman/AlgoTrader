package com.algoTrader.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.algoTrader.ServiceLocator;
import com.algoTrader.service.RuleService;

public class DateUtil {

	public static Date toDate(long time) {

		return new Date(time);
	}

	public static Date getCurrentEPTime() {

		String strategyName = StrategyUtil.getStartedStrategyName();
		RuleService ruleService = ServiceLocator.commonInstance().getRuleService();
		if (ruleService.isInitialized(strategyName) && !ruleService.isInternalClock(strategyName)) {
			return new Date(ruleService.getCurrentTime(strategyName));
		} else {
			return new Date();
		}
	}

	public static int toHour(long time) {

		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static Date getNextThirdFriday(Date input) {

		Calendar cal = new GregorianCalendar();
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

	public static Date getNextThirdFriday3MonthCycle(Date input) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(input);

		// round to 3-month cycle
		int month = (cal.get(Calendar.MONTH) + 1) / 3 * 3 - 1;

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
	public static int compareTime(Date first, Date second) {

		Calendar firstCal = new GregorianCalendar();
		firstCal.setTime(first);
		firstCal.set(0, 0, 0);

		Calendar secondCal = new GregorianCalendar();
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
	public static int compareToTime(Date time) {

		return compareTime(getCurrentEPTime(), time);
	}
}