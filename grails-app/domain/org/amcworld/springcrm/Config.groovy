package org.amcworld.springcrm

class Config {

    static constraints = {
		name(blank:false, nullable:false)
		intValue(nullable:true)
		stringValue(nullable:true)
		booleanValue(nullable:true)
		dateValue(nullable:true)
    }
	static transients = ['value']

	String name
	BigInteger intValue
	String stringValue
	Boolean booleanValue
	Date dateValue
	
	Object getValue() {
		if (intValue != null) {
			return intValue
		} else if (stringValue != null) {
			return stringValue
		} else if (booleanValue != null) {
			return booleanValue
		} else {
			return dateValue
		}
	}

	void setValue(Object value) {
		if (value instanceof Number) {
			intValue = value
		} else if (value instanceof CharSequence) {
			stringValue = value.toString()
		} else if (value instanceof Boolean) {
			booleanValue = value
		} else if (value instanceof Date) {
			dateValue = value
		} else if (value instanceof Calendar) {
			dateValue = value.time
		}
	}
}
