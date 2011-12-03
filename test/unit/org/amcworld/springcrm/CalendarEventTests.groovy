package org.amcworld.springcrm

import grails.test.*

class CalendarEventTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
}

class RecurrenceDataTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testWeekdaysAsList() {
		def rd = new RecurrenceData(weekdays:'1,4,5,6')
		assertEquals([1, 4, 5, 6], rd.weekdaysAsList)
		rd = new RecurrenceData(weekdays:'4')
		assertEquals([4], rd.weekdaysAsList)
		rd = new RecurrenceData(weekdays:'')
		assertNull(rd.weekdaysAsList)
		rd = new RecurrenceData()
		assertNull(rd.weekdaysAsList)
    }

	void testWeekdayNamesAsList() {
		def rd = new RecurrenceData(weekdays:'1,4,5,6')
		Locale.default = Locale.GERMANY
		assertEquals(['Mittwoch', 'Donnerstag', 'Freitag', 'Sonntag'], rd.weekdayNamesAsList)
		Locale.default = Locale.US
		assertEquals(['Sunday', 'Wednesday', 'Thursday', 'Friday'], rd.weekdayNamesAsList)

		rd = new RecurrenceData(weekdays:'1')
		Locale.default = Locale.GERMANY
		assertEquals(['Sonntag'], rd.weekdayNamesAsList)
		Locale.default = Locale.US
		assertEquals(['Sunday'], rd.weekdayNamesAsList)

		rd = new RecurrenceData()
		assertEquals([], rd.weekdayNamesAsList)
	}

	void testWeekdayNames() {
		def rd = new RecurrenceData(weekdays:'1,4,5,6')
		Locale.default = Locale.GERMANY
		assertEquals('Mittwoch, Donnerstag, Freitag, Sonntag', rd.weekdayNames)
		Locale.default = Locale.US
		assertEquals('Sunday, Wednesday, Thursday, Friday', rd.weekdayNames)

		rd = new RecurrenceData(weekdays:'1')
		Locale.default = Locale.GERMANY
		assertEquals('Sonntag', rd.weekdayNames)
		Locale.default = Locale.US
		assertEquals('Sunday', rd.weekdayNames)

		rd = new RecurrenceData()
		assertEquals('', rd.weekdayNames)
	}

	void testMessageSourceResolvable() {
		def rd = new RecurrenceData(
			type:30, interval:3, monthDay:14, weekdays:'1,4,5,6', weekdayOrd:2,
			month:Calendar.AUGUST
		)
		Locale.default = Locale.GERMANY
		println rd.arguments
		assertEquals(
			[ 3, 14, 'Mittwoch, Donnerstag, Freitag, Sonntag', 2, Calendar.AUGUST, 'August' ],
			rd.arguments
		)
		assertEquals(['calendarEvent.recurrence.pattern.30'], rd.codes)
	}
}