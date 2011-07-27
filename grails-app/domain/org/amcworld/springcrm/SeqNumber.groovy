package org.amcworld.springcrm

class SeqNumber {

    static constraints = {
		controllerName(blank:false, nullable:false)
		startNumber(min:0)
		prefix(maxSize:5)
		suffix(maxSize:5)
		nextNumber(min:0)
    }
	
	String controllerName
	int startNumber
	String prefix = ""
	String suffix = ""
	int nextNumber
}
