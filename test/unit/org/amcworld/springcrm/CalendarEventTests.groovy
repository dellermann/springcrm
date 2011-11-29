package org.amcworld.springcrm

import grails.test.*

class CalendarEventTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRecurWeekdaysAsList() {
		def c = new CalendarEvent(subject:'Test', recurWeekdays:'0,4,5,6')
		assertEquals([0, 4, 5, 6], c.recurWeekdaysAsList)
		c = new CalendarEvent(subject:'Test', recurWeekdays:'4')
		assertEquals([4], c.recurWeekdaysAsList)
		c = new CalendarEvent(subject:'Test')
		assertNull(c.recurWeekdaysAsList)
    }
}
