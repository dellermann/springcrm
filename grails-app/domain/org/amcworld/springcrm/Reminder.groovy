/*
 * Reminder.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import org.springframework.context.MessageSourceResolvable


/**
 * The class {@code Reminder} represents a reminder of a calendar event.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.3
 * @see     CalendarEvent
 */
class Reminder implements MessageSourceResolvable {

    //-- Class fields ---------------------------

    static constraints = {
        value min: 0
        unit inList: ['m', 'h', 'd', 'w']
        user nullable: true
    }
    static belongsTo = [calendarEvent: CalendarEvent, user: User]
    static mapping = {
        nextReminder index: 'next_reminder'
        version false
    }
    static transients = [
        'arguments', 'codes', 'defaultMessage', 'rule', 'valueAsMilliseconds'
    ]


    //-- Fields ---------------------------------

    int value
    String unit
    Date nextReminder


    //-- Properties -----------------------------

    String getRule() {
        value + unit
    }

    long getValueAsMilliseconds() {
        long v = value * 1000       // sec
        switch (unit) {
        case 'm':
            v *= 60
            break
        case 'h':
            v *= 3600
            break
        case 'd':
            v *= 86400
            break
        case 'w':
            v *= 604800
            break
        }
        return v
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Reminder && obj.id == id
    }

    static Reminder fromRule(String rule) {
        def m = rule =~ /^(\d+)([mhdw])$/
        if (!m) {
            throw new IllegalArgumentException("Rule ${rule} is not valid.")
        }

        new Reminder(value: m[0][1] as Integer, unit: m[0][2])
    }

    @Override
    Object [] getArguments() {
        [value] as Object[]
    }

    @Override
    String [] getCodes() {
        ["calendarEvent.reminder.pattern.${unit}"] as String[]
    }

    @Override
    String getDefaultMessage() {
        toString()
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        rule
    }
}
