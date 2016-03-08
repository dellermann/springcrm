package org.amcworld.springcrm

import grails.test.mixin.TestFor


@TestFor(Reminder)
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
