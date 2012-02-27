package org.amcworld.springcrm

import java.text.DateFormatSymbols
import org.springframework.context.MessageSourceResolvable


class CalendarEvent {

    static constraints = {
		subject(nullable: false, blank: false, widget: 'autonumber')
		location(nullable: true)
		description(nullable: true)
		start(nullable: false)
		end(nullable: false)
		allDay()
		recurrence(nullable: false)
		organization(nullable: true)
        owner(nullable: false)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [ organization: Organization, owner: User ]
	static embedded = [ 'recurrence' ]
	static mapping = {
		sort 'start'
		description type: 'text'
        end column: 'end_time'
        start column: 'start_time'
	}
	static searchable = true

	String subject
	String location
	String description
	Date start
	Date end
	boolean allDay
	RecurrenceData recurrence = new RecurrenceData()
	Date dateCreated
	Date lastUpdated

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

	String toString() {
		return subject
	}
}

class RecurrenceData implements MessageSourceResolvable {

    static constraints = {
		type(nullable: false, inList: [0, 10, 30, 40, 50, 60, 70])
		until(nullable: true)
		interval(nullable: false, min: 1)
		monthDay(nullable: true, range: 1..31)
		weekdays(nullable: true, maxSize: 13)
		weekdayOrd(nullable: true, range: -5..5)
		month(nullable: true, range: 1..12)
    }
	static transients = [
		'weekdaysAsList', 'weekdayNamesAsList', 'weekdayNames', 'monthName',
		'arguments', 'codes', 'defaultMessage'
	]

	int type = 0
	Date until
	int interval = 1
	Integer monthDay
	String weekdays
	Integer weekdayOrd = 0
	Integer month

	RecurrenceData() {}

	RecurrenceData(RecurrenceData rd) {
		type = c.type
		until = c.until
		cnt = c.cnt
		interval = c.interval
		monthDay = c.monthDay
		weekdays = c.weekdays
		weekdayOrd = c.weekdayOrd
		month = c.month
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
}
