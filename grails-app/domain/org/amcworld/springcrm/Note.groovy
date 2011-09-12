package org.amcworld.springcrm

class Note {

    static constraints = {
        number(unique:true)
		title(nullable:false, blank:false, maxSize:200)
		content(nullable:false, blank:true, widget:'textarea')
		organization(nullable:true)
		person(nullable:true)
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		sort 'title'
		content type:'text'
	}
	static searchable = true
	static transients = [ 'fullNumber' ]

	def seqNumberService

	int number
	String title
	String content
	Organization organization
	Person person
	Date dateCreated
	Date lastUpdated

	Note() {}

	Note(Note n) {
		title = n.title
		content = n.content
		organization = n.organization
		person = n.person
	}

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String toString() {
		return title ?: ''
	}
}
