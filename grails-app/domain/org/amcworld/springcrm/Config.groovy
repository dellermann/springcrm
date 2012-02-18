package org.amcworld.springcrm

class Config {

    static constraints = {
		name(blank: false, nullable: false)
		value(nullable: true)
    }

	String name
	String value

    Object asType(Class type) {
        switch (type) {
        case Date:
            return Date.parseToStringDate(value)
        case Calendar:
            return Date.parseToStringDate(value).toCalendar()
        default:
            return value?.asType(type)
        }
    }

    boolean equals(Object o) {
        if (o instanceof Config) {
            Config c = (Config) o
            return name == c.name
        } else {
            return false
        }
    }

    int hashCode() {
        return name.hashCode()
    }
}
