/*
 * Config.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
 * @version 2.0
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

    @Override
    boolean equals(Object obj) {
        (obj instanceof Config) ? name == obj.name : false
    }

    @Override
    int hashCode() {
        (name ?: '').hashCode()
    }

    @Override
    String toString() {
        value
    }

    /**
     * Helper method to convert this configuration object to the specified
     * type.  The method exists because some implementations do not call
     * {@code asType} when using the {@code as} keyword or even when directly
     * calling {@code asType}.
     *
     * @param type  the given type
     * @return      the converted object
     * @see         #asType(Class)
     */
    def toType(Class type) {
        switch (type) {
        case Date:
            return value ? Date.parseToStringDate(value) : null
        case Calendar:
            return value ? Date.parseToStringDate(value).toCalendar() : null
        case Boolean:
            return (value == null) ? null : Boolean.valueOf(value)
        default:
            return value?.asType(type)
        }
    }
}
