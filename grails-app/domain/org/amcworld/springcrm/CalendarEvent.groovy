package org.amcworld.springcrm

class CalendarEvent {

    static constraints = {
		subject(nullable:false, blank:false)
		location(nullable:true)
		description(nullable:true)
		start(nullable:false)
		end(nullable:false)
		allDay()
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

	String subject
	String location
	String description
	Date start
	Date end
	boolean allDay
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
	}

	String toString() {
		return subject
	}
}
