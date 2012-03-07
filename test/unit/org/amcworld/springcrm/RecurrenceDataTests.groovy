/*
 * RecurrenceDataTests.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code RecurrenceDataTests} contains test cases for class
 * {@code RecurrenceData}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(RecurrenceData)
@Mock(RecurrenceData)
class RecurrenceDataTests {

    //-- Public methods -------------------------

    void testWeekdaysAsList() {
        def rd = new RecurrenceData(weekdays: '1,4,5,6')
        assert [1, 4, 5, 6] == rd.weekdaysAsList
        rd = new RecurrenceData(weekdays: '4')
        assert [4] == rd.weekdaysAsList
        rd = new RecurrenceData(weekdays: '')
        assert null == rd.weekdaysAsList
        rd = new RecurrenceData()
        assert null == rd.weekdaysAsList
    }

    void testWeekdayNamesAsList() {
        def rd = new RecurrenceData(weekdays: '1,4,5,6')
        Locale.default = Locale.GERMANY
        assert ['Mittwoch', 'Donnerstag', 'Freitag', 'Sonntag'] == rd.weekdayNamesAsList
        Locale.default = Locale.US
        assert ['Sunday', 'Wednesday', 'Thursday', 'Friday'] == rd.weekdayNamesAsList

        rd = new RecurrenceData(weekdays: '1')
        Locale.default = Locale.GERMANY
        assert ['Sonntag'] == rd.weekdayNamesAsList
        Locale.default = Locale.US
        assert ['Sunday'] == rd.weekdayNamesAsList

        rd = new RecurrenceData()
        assert [] == rd.weekdayNamesAsList
    }

    void testWeekdayNames() {
        def rd = new RecurrenceData(weekdays: '1,4,5,6')
        Locale.default = Locale.GERMANY
        assert 'Mittwoch, Donnerstag, Freitag, Sonntag' == rd.weekdayNames
        Locale.default = Locale.US
        assert 'Sunday, Wednesday, Thursday, Friday' == rd.weekdayNames

        rd = new RecurrenceData(weekdays: '1')
        Locale.default = Locale.GERMANY
        assert 'Sonntag' == rd.weekdayNames
        Locale.default = Locale.US
        assert 'Sunday' == rd.weekdayNames

        rd = new RecurrenceData()
        assert '' == rd.weekdayNames
    }

    void testMessageSourceResolvable() {
        def rd = new RecurrenceData(
            type: 30, interval: 3, monthDay: 14, weekdays: '1,4,5,6', weekdayOrd: 2,
            month: Calendar.AUGUST
        )
        Locale.default = Locale.GERMANY
//      println rd.arguments
        assert [3, 14, 'Mittwoch, Donnerstag, Freitag, Sonntag', 2, Calendar.AUGUST, 'August'] == rd.arguments
        assert ['calendarEvent.recurrence.pattern.30'] == rd.codes
    }
}
