/*
 * CalendarEvent.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import java.text.DateFormatSymbols
import org.springframework.context.MessageSourceResolvable


/**
 * The class {@code CalendarEvent} represents an entry in the calendar, that
 * is, an appointment.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @see     Reminder
 */
class CalendarEvent {

    //-- Class variables ------------------------

    static constraints = {
		subject(blank: false, widget: 'autonumber')
		location(nullable: true)
		description(nullable: true)
		start()
		end()
		allDay()
		recurrence()
		organization(nullable: true)
        owner()
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [organization: Organization, owner: User]
	static embedded = ['recurrence']
	static mapping = {
		sort 'start'
		description type: 'text'
        end column: 'end_time', index: 'end_time'
        start column: 'start_time', index: 'start_time'
        subject index: 'subject'
	}
	static searchable = true


    //-- Instance variables ---------------------

	String subject
	String location
	String description
	Date start
	Date end
	boolean allDay
	RecurrenceData recurrence = new RecurrenceData()
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	CalendarEvent() {}

	CalendarEvent(CalendarEvent c) {
		subject = c.subject
		location = c.location
		description = c.description
		start = c.start
		end = c.end
		allDay = c.allDay
		recurrence = new RecurrenceData(c.recurrence)
        organization = c.organization
        owner = c.owner
	}


    //-- Public methods -------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CalendarEvent) {
            return obj.id == id
        } else {
            return false
        }
    }

    CalendarEvent eventAtDate(Date d) {
        def res = new CalendarEvent([
            subject: subject, location: location, description: description,
            start: d, end: new Date(d.time + end.time - start.time),
            allDay: allDay, organization: organization, owner: owner,
            dateCreated: dateCreated, lastUpdated: lastUpdated
        ])
        res.setId(ident())
        return res
    }

	@Override
    public int hashCode() {
        return id as int
    }

    @Override
    String toString() {
		return subject
	}
}


/**
 * The class {@code RecurrenceData} represents the data of recurring calendar
 * events.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class RecurrenceData implements MessageSourceResolvable {

    //-- Class variables ------------------------

    static constraints = {
		type(inList: [0, 10, 30, 40, 50, 60, 70])
		until(nullable: true)
		interval(min: 1)
		monthDay(nullable: true, range: 1..31)
		weekdays(nullable: true, maxSize: 13)
		weekdayOrd(nullable: true, range: -5..5)
		month(nullable: true, range: 1..12)
    }
    static mapping = {
        type index: 'recurrence_type'
    }
	static transients = [
		'weekdaysAsList', 'weekdayNamesAsList', 'weekdayNames', 'monthName',
		'arguments', 'codes', 'defaultMessage'
	]


    //-- Instance variables ---------------------

	int type = 0
	Date until
	int interval = 1
	Integer monthDay
	String weekdays
	Integer weekdayOrd = 0
	Integer month


    //-- Constructors ---------------------------

	RecurrenceData() {}

	RecurrenceData(RecurrenceData rd) {
		type = rd.type
		until = rd.until
		interval = rd.interval
		monthDay = rd.monthDay
		weekdays = rd.weekdays
		weekdayOrd = rd.weekdayOrd
		month = rd.month
	}


    //-- Public methods -------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RecurrenceData) {
            return obj.type == type && obj.until == until &&
                obj.interval == interval && obj.monthDay == monthDay &&
                obj.weekdays == weekdays && obj.weekdayOrd == weekdayOrd &&
                obj.month == month
        } else {
            return false
        }
    }

	List<Integer> getWeekdaysAsList() {
		List<Integer> res = null
		if (weekdays != null && weekdays.length() > 0) {
			res = weekdays.split(',').collect { it as Integer }
		}
		return res
	}

	List<String> getWeekdayNamesAsList() {
		List<String> res = []
		if (weekdays != null) {
			List<Integer> wds = weekdaysAsList
			String [] wdNames = DateFormatSymbols.instance.weekdays
			Calendar cal = Calendar.instance
			int offset = cal.firstDayOfWeek - Calendar.SUNDAY
			int n = cal.getMaximum(Calendar.DAY_OF_WEEK)
			for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
				int j = (i - Calendar.SUNDAY + offset) % n + Calendar.SUNDAY
				if (wds.contains(j)) {
					res << wdNames[j]
				}
			}
		}
		return res
	}

	String getWeekdayNames() {
		return weekdayNamesAsList.join(', ')
	}

	String getMonthName() {
		if (month == null) {
			return null
		} else {
			String [] monthNames = DateFormatSymbols.instance.months
			return monthNames[month]
		}
	}

    Object [] getArguments() {
        return [
			interval, monthDay, weekdayNames, weekdayOrd, month, monthName
		] as Object[]
    }

    String [] getCodes() {
        return [ "calendarEvent.recurrence.pattern.${type}" ] as String[]
    }

    String getDefaultMessage() {
        return ''
    }

    @Override
    public int hashCode() {
        return type
    }

    @Override
    String toString() {
        def buf = new StringBuilder()
        buf << type
        buf << ': '
        buf << (monthDay ?: '*')
        buf << ' '
        buf << month ?: '*'
        buf << ' '
        buf << weekdays ?: '*'
        if (weekdayOrd) {
            buf << ':' << weekdayOrd
        }
        if (interval > 1) {
            buf << ' /' << interval
        }
        return buf.toString()
    }
}
