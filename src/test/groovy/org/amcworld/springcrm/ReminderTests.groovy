/*
 * ReminderTests.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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


//@TestFor(Reminder)
class ReminderTests {

    void testGetRule() {
        def r = new Reminder([value:5, unit:'d'])
        assertEquals('5d', r.rule)
        r = new Reminder([value:10, unit:'m'])
        assertEquals('10m', r.rule)
    }

    void testFromRule() {
        def r = Reminder.fromRule('5m')
        assertEquals(5, r.value)
        assertEquals('m', r.unit)
        r = Reminder.fromRule('10d')
        assertEquals(10, r.value)
        assertEquals('d', r.unit)
    }

    void testFromRuleInvalid() {
        shouldFail(IllegalArgumentException) {
            Reminder.fromRule(null)
        }
        shouldFail(IllegalArgumentException) {
            Reminder.fromRule('')
        }
        shouldFail(IllegalArgumentException) {
            Reminder.fromRule('m5')
        }
    }

    void testGetValueAsMilliseconds() {
        def r = new Reminder(value:5, unit:'m')
        assertEquals(300000L, r.valueAsMilliseconds)
        r = new Reminder(value:10, unit:'m')
        assertEquals(600000L, r.valueAsMilliseconds)
        r = new Reminder(value:1, unit:'h')
        assertEquals(3600000L, r.valueAsMilliseconds)
        r = new Reminder(value:2, unit:'h')
        assertEquals(7200000L, r.valueAsMilliseconds)
        r = new Reminder(value:1, unit:'d')
        assertEquals(86400000L, r.valueAsMilliseconds)
        r = new Reminder(value:2, unit:'d')
        assertEquals(172800000L, r.valueAsMilliseconds)
        r = new Reminder(value:1, unit:'w')
        assertEquals(604800000L, r.valueAsMilliseconds)
        r = new Reminder(value:2, unit:'w')
        assertEquals(1209600000L, r.valueAsMilliseconds)
    }
}
