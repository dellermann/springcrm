/*
 * Config.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
 * @author  Daniel Ellermann
 * @version 1.4
 */
class Config {

    //-- Class variables ------------------------

    static constraints = {
        name blank: false
        value nullable: true
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

    @Override
    boolean equals(Object obj) {
        (obj instanceof Config) ? name == obj.name : false
    }

    @Override
    int hashCode() {
        name.hashCode()
    }

    @Override
    String toString() {
        value
    }
}
