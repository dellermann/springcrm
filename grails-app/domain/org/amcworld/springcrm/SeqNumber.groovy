package org.amcworld.springcrm

class SeqNumber {

    static constraints = {
		controllerName(blank:false, nullable:false)
		prefix(maxSize:5)
		suffix(maxSize:5)
		startValue(min:0)
		endValue(min:0)
    }
	
	String controllerName
	String prefix = ""
	String suffix = ""
	int startValue = 10000
	int endValue = 99999
}
