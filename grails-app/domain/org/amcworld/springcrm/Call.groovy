package org.amcworld.springcrm

import java.util.Date;

class Call {

    static constraints = {
		subject(blank:false)
		notes(widget:'textarea')
		organization(nullable:true)
		person(nullable:true)
		phone(maxSize:40, nullable:true)
		start()
		type(inList:['incoming', 'outgoing'])
		status(inList:['planned', 'completed', 'acknowledged', 'cancelled'])
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		table 'phone_call'
    }
	static searchable = true
	
	String subject
	String notes
	Organization organization
	Person person
	String phone
	Date start
	String type
	String status
	Date dateCreated
	Date lastUpdated

	String toString() {
		return subject
	}
}
