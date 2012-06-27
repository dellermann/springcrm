/*
 * Config.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm


/**
 * The class {@code Config} represents a system configuration information.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class Config {

    //-- Class variables ------------------------

    static constraints = {
		name(nullable: false, blank: false)
		value(nullable: true)
    }
    static mapping = {
        name index: 'name'
    }


    //-- Instance variables ---------------------

	String name
	String value


    //-- Public methods -------------------------

    Object asType(Class type) {
        switch (type) {
        case Date:
            return Date.parseToStringDate(value)
        case Calendar:
            return Date.parseToStringDate(value).toCalendar()
        case Boolean:
            return (value == null) ? null : Boolean.valueOf(value)
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

    String toString() {
        return name
    }
}
