package org.amcworld.springcrm

class LruEntry {

    static constraints = {
		user()
		controller(blank:false, nullable:false)
		itemId(unique:['user', 'controller'])
		pos()
		name(blank:true, nullable:true)
    }
	static mapping = {
		version false
	}

	User user
	String controller
	long itemId
	long pos
	String name
}
