/*
 * RecurCalendarEventHelper.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm

import static java.util.Calendar.*


/**
 * The class {@code RecurCalendarEventHelper} represents an auxiliary class
 * for working with recurring calendar events.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class RecurCalendarEventHelper {

    //-- Instance variables ---------------------

    private RecurrenceData data


    //-- Constructors ---------------------------

    RecurCalendarEventHelper(RecurrenceData data) {
        this.data = data
    }


    //-- Public methods -------------------------

    /**
     * Finds the calendar event of this recurrence which is greater than or
     * equal to the given date.
     *
     * @param start the start of the recurrence; the start date must be part of
     *              the recurrence
     * @param d     the given date
     * @return      the next possible calendar event
     */
    Date approximate(Date start, Date d = new Date()) {
        Calendar calStart = start.toCalendar()
        Calendar cal = d.toCalendar()
        cal.clearTime()
        if (cal <= calStart) {
            return start
        }

        switch (data.type) {
        case 10:
            approximate10 calStart, cal
            break
        case 30:
            approximate30 calStart, cal
            break
        case 40:
            approximate40 calStart, cal
            break
        case 50:
            approximate50 calStart, cal
            break
        case 60:
            approximate60 calStart, cal
            break
        case 70:
            approximate70 calStart, cal
            break
        }
        calStart.time
    }

    /**
     * Computes a date after or equal to the given date which can act as the
     * start of the recurrence. Thus the returned date is part of the
     * recurrence.
     *
     * @param start   the given date
     * @return        the date which is part of the recurrence and therefore may
     *                be used as start of the recurrence
     */
    Date calibrateStart(Date start) {
        Calendar cal = start.toCalendar()
        cal.clearTime()
        switch (data.type) {
        case 30:
            if (needCalibration30(cal)) {
                calibrateStart30 cal
            }
            break
        case 40:
            if (needCalibration40(cal)) {
                calibrateStart40 cal
            }
            break
        case 50:
            if (needCalibration50(cal)) {
                calibrateStart50 cal
            }
            break
        case 60:
            if (needCalibration60(cal)) {
                calibrateStart60 cal
            }
            break
        case 70:
            if (needCalibration70(cal)) {
                calibrateStart70 cal
            }
            break
        }
        cal.time
    }

    /**
     * Computes the date of the recurring calendar event at the n-th
     * recurrence. The first recurrence is the start of the recurrence, that
     * is, the given start date.
     *
     * @param start  the start of the recurrence; the start date must be part of
     *         the recurrence
     * @param n    the n-th recurrence; must be greater than zero
     * @return    the date of the n-th recurrence
     */
    Date computeNthEvent(Date start, int n) {
        if (n < 1) {
            throw new IllegalArgumentException(
                "n must be greater than zero but is ${n}."
            )
        }

        Calendar cal = start.toCalendar()
        switch (data.type) {
        case 10:
            cal.add DAY_OF_MONTH, (n - 1) * data.interval
            break
        case 30:
            computeNthEvent30 cal, n
            break
        case 40:
            cal.add MONTH, (n - 1) * data.interval
            break
        case 50:
            cal.add MONTH, (n - 1) * data.interval
            cal[DAY_OF_WEEK] = data.weekdaysAsList[0]
            cal[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
            break
        case 60:
            cal.add YEAR, n - 1
            break
        case 70:
            cal.add YEAR, n - 1
            cal[DAY_OF_WEEK] = data.weekdaysAsList[0]
            cal[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
            break
        }
        cal.time
    }

    static int delin(int x) {
        int i = Calendar.instance.getMinimum(DAY_OF_WEEK)
        (x - i) % numDaysInWeek + i
    }

    static int lin(int wd) {
        wd + unitStep(wd)
    }


    //-- Non-public methods ---------------------

    private void approximate10(Calendar calStart, Calendar cal) {
        int n = data.interval
        int q = (int) ((cal - calStart - 1) / n + 1)
        calStart.add DAY_OF_MONTH, n * q
    }

    private void approximate30(Calendar calStart, Calendar cal) {
        int t = numDaysInWeek
        int n = data.interval
        int q = (int) ((cal - calStart) / t)
        if (q % n == 0) {
            List<Integer> linWds = theta
            int delta = n * t
            int k = linWds.size()
            int w = lin(cal[DAY_OF_WEEK])
            if (k > 1 && w >= linWds.first() && w <= linWds.last()) {
                delta = linWds.find({ it >= w }) - linWds.first()
            } else if (k == 1 && w == linWds.first()) {
                delta = 0
            }
            calStart.add DAY_OF_MONTH, q * t + delta
        } else {
            calStart.add DAY_OF_MONTH, n * t * (((int) (q / n)) + 1)
        }
    }

    private void approximate40(Calendar calStart, Calendar cal) {
        int delta = numMonths *
            (cal[YEAR] - calStart[YEAR]) + cal[MONTH] - calStart[MONTH]
        calStart[DAY_OF_MONTH] = data.monthDay
        calStart.add MONTH, delta
        if (delta % data.interval == 0) {
            if (cal > calStart) {
                calStart.add MONTH, data.interval
            }
        } else {
            calStart.add MONTH, data.interval - delta
        }
    }

    private void approximate50(Calendar calStart, Calendar cal) {
        int delta = numMonths *
            (cal[YEAR] - calStart[YEAR]) + cal[MONTH] - calStart[MONTH]
        calStart.add MONTH, delta
        calStart[DAY_OF_WEEK] = data.weekdaysAsList.first()
        calStart[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        if (delta % data.interval == 0) {
            if (cal > calStart) {
                calStart.add MONTH, data.interval
                calStart[DAY_OF_WEEK] = data.weekdaysAsList.first()
                calStart[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
            }
        } else {
            calStart.add MONTH, data.interval - delta
            calStart[DAY_OF_WEEK] = data.weekdaysAsList.first()
            calStart[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        }
    }

    private void approximate60(Calendar calStart, Calendar cal) {
        calStart[DAY_OF_MONTH] = data.monthDay
        calStart[MONTH] = data.month
        calStart[YEAR] = cal[YEAR]
        if (cal > calStart) {
            calStart.add YEAR, 1
        }
    }

    private void approximate70(Calendar calStart, Calendar cal) {
        calStart[MONTH] = data.month
        calStart[YEAR] = cal[YEAR]
        calStart[DAY_OF_WEEK] = data.weekdaysAsList.first()
        calStart[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        if (cal > calStart) {
            calStart.add YEAR, 1
            calStart[DAY_OF_WEEK] = data.weekdaysAsList.first()
            calStart[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        }
    }

    /**
     * Computes an auxiliary date using month and year from the given calendar
     * and day of week and week number in month from the recurrence data. The
     * time values are all set to zero.
     *
     * @param cal   the given date
     * @param month the month to set; if {@code null} the month is taken from
     *              the given date
     * @return      the auxiliary date
     */
    private Calendar auxDate(Calendar cal, Integer month = null) {
        def c = Calendar.instance
        c[MONTH] = month ?: cal[MONTH]
        c[YEAR] = cal[YEAR]
        c[DAY_OF_WEEK] = data.weekdaysAsList.first()
        c[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        c[HOUR_OF_DAY] = 0
        c[MINUTE] = 0
        c[SECOND] = 0
        c[MILLISECOND] = 0
        c
    }

    private void calibrateStart30(Calendar cal) {
        int calwd = lin(cal[DAY_OF_WEEK])
        List<Integer> linWds = theta
        int wd = linWds.find { it > calwd }
        if (wd) {
            cal[DAY_OF_WEEK] = delin(wd)
        } else {
            wd = linWds.first()
            cal[DAY_OF_WEEK] = delin(wd)
            cal.add WEEK_OF_YEAR, data.interval
        }
    }

    private void calibrateStart40(Calendar cal) {
        if (cal[DAY_OF_MONTH] > data.monthDay) {
            cal.add MONTH, data.interval
        }
        cal[DAY_OF_MONTH] = data.monthDay
    }

    private void calibrateStart50(Calendar cal) {
        def c = auxDate(cal)
        if (cal > c) {
            c.add MONTH, data.interval
            c[DAY_OF_WEEK] = data.weekdaysAsList.first()
            c[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        }
        cal.time = c.time
    }

    private void calibrateStart60(Calendar cal) {
        Calendar c = cal.clone()
        c[DAY_OF_MONTH] = data.monthDay
        c[MONTH] = data.month
        if (cal > c) {
            c.add YEAR, 1
        }
        cal.time = c.time
    }

    private void calibrateStart70(Calendar cal) {
        def c = auxDate(cal, data.month)
        if (cal > c) {
            c.add YEAR, 1
            c[DAY_OF_WEEK] = data.weekdaysAsList.first()
            c[DAY_OF_WEEK_IN_MONTH] = data.weekdayOrd
        }
        cal.time = c.time
    }

    protected void computeNthEvent30(Calendar cal, int n) {
        List<Integer> linWds = theta
        int k = theta.size()
        int m = (int) ((n - 1) / k)
        cal.add DAY_OF_MONTH, data.interval * numDaysInWeek * m
        for (int i = k * m + 2; i <= n; i++) {
            int ws = lin(cal[DAY_OF_WEEK])
            cal.add DAY_OF_MONTH, delta(linWds, linWds.indexOf(ws))
        }
    }

    private int delta(List<Integer> linWds, int i) {
        int k = linWds.size()
        if (i < k - 1) {
            return linWds[i + 1] - linWds[i]
        }

        data.interval * numDaysInWeek + linWds.first() - linWds.last()
    }

    protected static int getFirstDayOfWeek() {
        Calendar.instance.firstDayOfWeek
    }

    protected List<Integer> getTheta() {
        data.weekdaysAsList.collect({ lin(it) }).sort()
    }

    protected static int getNumDaysInWeek() {
        def c = Calendar.instance
        c.getMaximum(DAY_OF_WEEK) - c.getMinimum(DAY_OF_WEEK) + 1
    }

    protected static int getNumMonths() {
        def c = Calendar.instance
        c.getMaximum(MONTH) - c.getMinimum(MONTH) + 1
    }

    private boolean needCalibration30(Calendar cal) {
        !(cal[DAY_OF_WEEK] in data.weekdaysAsList)
    }

    private boolean needCalibration40(Calendar cal) {
        !(cal[DAY_OF_MONTH] == data.monthDay)
    }

    private boolean needCalibration50(Calendar cal) {
        boolean b
        if (data.weekdayOrd < 0) {
            def c = auxDate(cal)
            b = (cal[DAY_OF_MONTH] == c[DAY_OF_MONTH]) &&
                (cal[MONTH] == c[MONTH])
        } else {
              b = cal[DAY_OF_WEEK_IN_MONTH] != data.weekdayOrd
        }
        !((cal[DAY_OF_WEEK] in data.weekdaysAsList) && b)
    }

    private boolean needCalibration60(Calendar cal) {
        !((cal[DAY_OF_MONTH] == data.monthDay) &&
            (cal[MONTH] == data.month))
    }

    private boolean needCalibration70(Calendar cal) {
        boolean b
        if (data.weekdayOrd < 0) {
            def c = auxDate(cal)
            b = cal[DAY_OF_MONTH] == c[DAY_OF_MONTH]
        } else {
            b = cal[DAY_OF_WEEK_IN_MONTH] != data.weekdayOrd
        }
        !((cal[DAY_OF_WEEK] in data.weekdaysAsList) &&
            (cal[MONTH] == data.month) && b)
    }

    protected static int unitStep(int x) {
        (x < firstDayOfWeek) ? numDaysInWeek : 0
    }
}
