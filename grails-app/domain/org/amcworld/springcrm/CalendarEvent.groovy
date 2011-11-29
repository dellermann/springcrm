package org.amcworld.springcrm

class CalendarEvent {

    static constraints = {
		subject(nullable:false, blank:false)
		location(nullable:true)
		description(nullable:true)
		start(nullable:false)
		end(nullable:false)
		allDay()
		recurType(nullable:false, inList:[0, 10, 30, 40, 50, 60, 70])
		recurUntil(nullable:true)
		recurCount(nullable:true, min:0)
		recurInterval(nullable:false, min:1)
		recurMonthDay(nullable:true, range:1..31)
		recurWeekdays(nullable:true, maxSize:13)
		recurWeekdayOrd(nullable:true, range:-5..5)
		recurMonth(nullable:true, range:1..12)
		organization(nullable:true)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [ organization:Organization ]
	static mapping = {
		sort 'start'
		description type:'text'
	}
	static searchable = true
	static transients = [ 'recurWeekdaysAsList' ]

	String subject
	String location
	String description
	Date start
	Date end
	boolean allDay
	int recurType = 0
	Date recurUntil
	Integer recurCount
	int recurInterval = 1
	Integer recurMonthDay
	String recurWeekdays
	Integer recurWeekdayOrd = 0
	Integer recurMonth
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
		recurType = c.recurType
		recurUntil = c.recurUntil
		recurCount = c.recurCount
		recurInterval = c.recurInterval
		recurMonthDay = c.recurMonthDay
		recurWeekdays = c.recurWeekdays
		recurWeekdayOrd = c.recurWeekdayOrd
		recurMonth = c.recurMonth
	}

	List<Integer> getRecurWeekdaysAsList() {
		List<Integer> res = null
		if (recurWeekdays != null) {
			def wds = recurWeekdays.split(',')
			res = new ArrayList<Integer>(wds.length)
			wds.each { res << (it as Integer) }
		}
		return res
	}

	String toString() {
		return subject
	}
}
