/*
 * CalendarEvent.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
 * @author  Daniel Ellermann
 * @version 2.1
 * @see     Reminder
 */
class CalendarEvent {

    //-- Class fields ---------------------------

    static constraints = {
        subject blank: false
        location nullable: true
        description nullable: true, widget: 'textarea'
        start validator: { start, calendarEvent ->
            ((calendarEvent.recurrence.type == 0i) && !start.before(calendarEvent.end)) ? ['calendarEvent.start.validator.notBefore'] : null
        }
        organization nullable: true
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
    static transients = ['synthetic']


    //-- Fields ---------------------------------

    String subject
    String location
    String description
    Date start
    Date end
    boolean allDay
    RecurrenceData recurrence = new RecurrenceData()
    Date dateCreated
    Date lastUpdated

    /**
     * Marks the calendar event as synthetic, that is, it represents an
     * occurrence of a recurring calendar event.  Synthetic calendar events are
     * not persisted.
     */
    boolean synthetic


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
        synthetic = c.synthetic
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof CalendarEvent && obj.id == id
    }

    /**
     * Creates a calendar event at the given start date with the same duration
     * and data as this calendar event.  The method is used to produce
     * occurrences of recurring calendar events.
     *
     * @param d the given start date
     * @return  the created calendar event
     */
    CalendarEvent eventAtDate(Date d) {
        def res = new CalendarEvent(
            subject: subject, location: location, description: description,
            start: d, end: new Date(d.time + end.time - start.time),
            allDay: allDay, organization: organization, owner: owner,
            dateCreated: dateCreated, lastUpdated: lastUpdated
        )
        res.id = ident()
        res
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        subject
    }
}


/**
 * The class {@code RecurrenceData} represents the data of recurring calendar
 * events.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class RecurrenceData implements MessageSourceResolvable {

    //-- Class variables ------------------------

    static constraints = {
        type inList: [0i, 10i, 30i, 40i, 50i, 60i, 70i]
        until nullable: true
        interval min: 1
        monthDay nullable: true, range: 1i..31i, validator: { monthDay, recurrence ->
            ((recurrence.type == 40i || recurrence.type == 60i) && !monthDay) ? ['default.blank.message'] : null
        }
        weekdays nullable: true, maxSize: 13, validator: { weekdays, recurrence ->
            ((recurrence.type == 30i) && !weekdays) ? ['calendarEvent.recurrence.weekdays.blank'] : null
        }
        weekdayOrd nullable: true, range: -5i..5i, validator: { weekdayOrd, recurrence ->
            ((recurrence.type == 50i || recurrence.type == 70i) && !weekdayOrd) ? ['default.blank.message'] : null
        }
        month nullable: true, range: 1i..12i
    }
    static mapping = {
        type index: 'recurrence_type'
    }
    static transients = [
        'arguments', 'codes', 'defaultMessage', 'monthName', 'weekdaysAsList',
        'weekdayNames', 'weekdayNamesAsList'
    ]


    //-- Instance variables ---------------------

    int type = 0i
    Date until
    int interval = 1i
    Integer monthDay
    String weekdays
    Integer weekdayOrd = 0i
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
    boolean equals(Object obj) {
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
        if (weekdays) {
            res = weekdays.split(',').collect { it as Integer }
        }
        res
    }

    List<String> getWeekdayNamesAsList() {
        List<String> res = []
        if (weekdays) {
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
        res
    }

    String getWeekdayNames() {
        weekdayNamesAsList.join(', ')
    }

    String getMonthName() {
        if (month == null) {
            return null
        } else {
            String [] monthNames = DateFormatSymbols.instance.months
            monthNames[month]
        }
    }

    Object [] getArguments() {
        [
            interval, monthDay, weekdayNames, weekdayOrd, month, monthName
        ] as Object[]
    }

    String [] getCodes() {
        ["calendarEvent.recurrence.pattern.${type}"] as String[]
    }

    String getDefaultMessage() {
        ''
    }

    @Override
    int hashCode() {
        type
    }

    @Override
    String toString() {
        def buf = new StringBuilder()
        buf << type << ': '
        buf << monthDay ?: '*'
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
        buf.toString()
    }
}
