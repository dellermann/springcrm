/*
 * Reminder.groovy
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
 * The class {@code Reminder} represents a reminder of a calendar event.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 * @see     CalendarEvent
 */
class Reminder {

    //-- Class variables ------------------------

    static constraints = {
        value(nullable: false, min: 0)
        unit(nullable: false, inList: ['m', 'h', 'd', 'w'])
        nextReminder(nullable: false)
        calendarEvent(nullable: false)
        user(nullable: true)
    }
    static belongsTo = [calendarEvent: CalendarEvent, user: User]
    static mapping = {
        version false
    }
    static transients = ['rule', 'valueAsMilliseconds']


    //-- Instance variables ---------------------

    int value
    String unit
    Date nextReminder


    //-- Public methods -------------------------

    String getRule() {
        return "${value}${unit}"
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

    static Reminder fromRule(String rule) {
        def m = (rule =~ /^(\d+)([mhdw])$/)
        if (!m) {
            throw new IllegalArgumentException("Rule ${rule} is not valid.")
        }
        return new Reminder([value: m[0][1] as Integer, unit: m[0][2]])
    }
}
