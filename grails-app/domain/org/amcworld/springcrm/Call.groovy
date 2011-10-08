package org.amcworld.springcrm

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
    static belongsTo = [ organization:Organization, person:Person ]
	static mapping = {
		sort start:'desc'
		table 'phone_call'
		notes type:'text'
    }
	static searchable = true
	
	String subject
	String notes
	String phone
	Date start = new Date()
	String type
	String status
	Date dateCreated
	Date lastUpdated

	Call() {}

	Call(Call call) {
		subject = call.subject
		notes = call.notes
		organization = call.organization
		person = call.person
		phone = call.phone
		start = call.start
		type = call.type
		status = call.status
	}

	String toString() {
		return subject
	}
}
