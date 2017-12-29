/*
 * CalendarEventTests.groovy
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


//@TestFor(CalendarEvent)
//@Mock([CalendarEvent, RecurrenceData, Reminder, Organization, User])
class CalendarEventTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def calendarEvent = new CalendarEvent()
        assert null != calendarEvent.recurrence
    }

    void testCopyConstructor() {
        def d = new Date()
        makeOrganizationFixture()
        def calendarEvent = new CalendarEvent(
            subject: 'Test', location: 'Berlin',
            description: 'Test calendar event', start: d, end: d,
            allDay: false, organization: Organization.get(1),
            owner: new User(username: 'dellermann')
        )
        def anotherCalendarEvent = new CalendarEvent(calendarEvent)
        assert 'Test' == anotherCalendarEvent.subject
        assert 'Berlin' == anotherCalendarEvent.location
        assert 'Test calendar event' == anotherCalendarEvent.description
        assert d == anotherCalendarEvent.start
        assert d == anotherCalendarEvent.end
        assert !anotherCalendarEvent.allDay
        assert null != anotherCalendarEvent.recurrence
        assert 0 == anotherCalendarEvent.recurrence.type
        assert null != anotherCalendarEvent.organization
        assert 'AMC World Technologies GmbH' == anotherCalendarEvent.organization.name
        assert null != anotherCalendarEvent.owner
        assert 'dellermann' == anotherCalendarEvent.owner.username
    }

    void testConstraints() {
        mockForConstraintsTests(CalendarEvent)

        def calendarEvent = new CalendarEvent()
        assert !calendarEvent.validate()
        assert 'nullable' == calendarEvent.errors['subject']
        assert 'nullable' == calendarEvent.errors['start']
        assert 'nullable' == calendarEvent.errors['end']
        assert 'nullable' == calendarEvent.errors['owner']

        calendarEvent = new CalendarEvent(subject: '')
        assert !calendarEvent.validate()
        assert 'blank' == calendarEvent.errors['subject']

        def d = new Date()
        calendarEvent = new CalendarEvent(
            subject: 'Test', start: d, end: d, owner: new User()
        )
        assert calendarEvent.validate()
    }

    void testEventAtDate() {
        def d1 = new Date()
        def calendarEvent = new CalendarEvent(
            subject: 'Test', start: d1, end: d1, location: 'Berlin',
            allDay: false
        )
        def d2 = d1 + 2
        def newCalendarEvent = calendarEvent.eventAtDate(d2)
        assert null != newCalendarEvent
        assert 'Test' == newCalendarEvent.subject
        assert d2 == newCalendarEvent.start
        assert d2 == newCalendarEvent.end
        assert 'Berlin' == newCalendarEvent.location
        assert !newCalendarEvent.allDay
    }

    void testToString() {
        def d = new Date()
        def calendarEvent = new CalendarEvent(
            subject: 'Test', start: d, end: d, location: 'Berlin',
            allDay: false
        )
        assert 'Test' == calendarEvent.toString()
    }


    //-- Non-public methods ---------------------

    protected void makeOrganizationFixture() {
        mockDomain(
            Organization, [
                [id: 1, number: 10000, recType: 1, name: 'AMC World Technologies GmbH', legalForm: 'GmbH']
            ]
        )
    }
}
