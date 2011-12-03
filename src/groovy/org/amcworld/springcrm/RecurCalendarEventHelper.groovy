package org.amcworld.springcrm

import static java.util.Calendar.*

class RecurCalendarEventHelper {

	//-- Instance variables ---------------------

	private RecurrenceData data


	//-- Constructors ---------------------------

	RecurCalendarEventHelper(RecurrenceData data) {
		this.data = data
	}


	//-- Public methods -------------------------

	Date calibrateStart(Date start) {
		Calendar cal = Calendar.instance
		cal.time = start
		switch (data.type) {
		case 30:
			if (needCalibration30(cal)) {
				calibrateStart30(cal)
			}
			break
		case 40:
			if (needCalibration40(cal)) {
				calibrateStart40(cal)
			}
			break
		case 50:
			if (needCalibration50(cal)) {
				calibrateStart50(cal)
			}
			break
		case 60:
			if (needCalibration60(cal)) {
				calibrateStart60(cal)
			}
			break
		case 70:
			if (needCalibration70(cal)) {
				calibrateStart70(cal)
			}
			break
		}
		return cal.time
	}

	public static int delin(int x) {
		return (x - SUNDAY) % numDaysInWeek + SUNDAY
	}

	public static int lin(int wd) {
		return wd + unitStep(wd)
	}


	//-- Non-public methods ---------------------

	/**
	 * Computes an auxiliary date using month and year from the given calendar
	 * and day of week and week number in month from the recurrence data. The
	 * time values are all set to zero.
	 * 
	 * @param cal	the given date
	 * @param month	the month to set; if {@code null} the month is taken from
	 * 				the given date
	 * @return		the auxiliary date
	 */
	private Calendar auxDate(Calendar cal, Integer month = null) {
		def c = Calendar.instance
		c.set(MONTH, month ?: cal.get(MONTH))
		c.set(YEAR, cal.get(YEAR))
		c.set(DAY_OF_WEEK, data.weekdaysAsList[0])
		c.set(DAY_OF_WEEK_IN_MONTH, data.weekdayOrd)
		c.set(HOUR_OF_DAY, 0)
		c.set(MINUTE, 0)
		c.set(SECOND, 0)
		c.set(MILLISECOND, 0)
		return c
	}

	private void calibrateStart30(Calendar cal) {
		int calwd = lin(cal.get(DAY_OF_WEEK))
		List<Integer> wds = data.weekdaysAsList
		List<Integer> linWds = wds.collect({ lin(it) }).sort()
		int wd = linWds.find { it > calwd }
		if (wd) {
			cal.set(DAY_OF_WEEK, delin(wd))
		} else {
			wd = linWds.min()
			cal.set(DAY_OF_WEEK, delin(wd))
			cal.add(WEEK_OF_YEAR, data.interval)
		}
	}

	private void calibrateStart40(Calendar cal) {
		if (cal.get(DAY_OF_MONTH) > data.monthDay) {
			cal.add(MONTH, data.interval)
		}
		cal.set(DAY_OF_MONTH, data.monthDay)
	}

	private void calibrateStart50(Calendar cal) {
		def c = auxDate(cal)
		if (cal > c) {
			c.add(MONTH, data.interval)
			c.set(DAY_OF_WEEK, data.weekdaysAsList[0])
			c.set(DAY_OF_WEEK_IN_MONTH, data.weekdayOrd)
		}
		cal.time = c.time
	}

	private void calibrateStart60(Calendar cal) {
		Calendar c = cal.clone()
		c.set(DAY_OF_MONTH, data.monthDay)
		c.set(MONTH, data.month)
		if (cal > c) {
			c.add(YEAR, 1)
		}
		cal.time = c.time
	}

	private void calibrateStart70(Calendar cal) {
		def c = auxDate(cal, data.month)
		if (cal > c) {
			c.add(YEAR, 1)
			c.set(DAY_OF_WEEK, data.weekdaysAsList[0])
			c.set(DAY_OF_WEEK_IN_MONTH, data.weekdayOrd)
		}
		cal.time = c.time
	}

	protected static int getFirstDayOfWeek() {
		return Calendar.instance.firstDayOfWeek
	}

	protected static int getNumDaysInWeek() {
		return Calendar.instance.getMaximum(DAY_OF_WEEK)
	}

	private boolean needCalibration30(Calendar cal) {
		return !(cal.get(DAY_OF_WEEK) in data.weekdaysAsList)
	}

	private boolean needCalibration40(Calendar cal) {
		return !(cal.get(DAY_OF_MONTH) == data.monthDay)
	}

	private boolean needCalibration50(Calendar cal) {
		boolean b
		if (data.weekdayOrd < 0) {
			def c = auxDate(cal)
			b = (cal.get(DAY_OF_MONTH) == c.get(DAY_OF_MONTH)) &&
				(cal.get(MONTH) == c.get(MONTH))
		} else {
			b = cal.get(DAY_OF_WEEK_IN_MONTH) != data.weekdayOrd
		}
		return !((cal.get(DAY_OF_WEEK) in data.weekdaysAsList) && b)
	}

	private boolean needCalibration60(Calendar cal) {
		return !((cal.get(DAY_OF_MONTH) == data.monthDay) &&
			(cal.get(MONTH) == data.month))
	}

	private boolean needCalibration70(Calendar cal) {
		boolean b
		if (data.weekdayOrd < 0) {
			def c = auxDate(cal)
			b = cal.get(DAY_OF_MONTH) == c.get(DAY_OF_MONTH)
		} else {
			b = cal.get(DAY_OF_WEEK_IN_MONTH) != data.weekdayOrd
		}
		return !((cal.get(DAY_OF_WEEK) in data.weekdaysAsList) && 
			(cal.get(MONTH) == data.month) && b)
	}

	protected static int unitStep(int x) {
		return (x < firstDayOfWeek) ? numDaysInWeek : 0
	}
}
