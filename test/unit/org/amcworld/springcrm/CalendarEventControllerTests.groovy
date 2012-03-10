/*
 * CalendarEventControllerTests.groovy
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
 * The class {@code CalendarEventControllerTests} contains the unit test cases
 * for {@code CalendarEventController}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(CalendarEventController)
@Mock([CalendarEvent, RecurrenceData, Reminder, Organization, User])
class CalendarEventControllerTests {

    //-- Public methods -------------------------

    void setUp() {
        def crit = [
            list: { Closure cls -> [] }
        ]
        CalendarEvent.metaClass.static.createCriteria = { crit }
        Reminder.metaClass.static.createCriteria = { crit }
        CalendarEvent.metaClass.index = { -> }
        CalendarEvent.metaClass.reindex = { -> }
        session.user = new User(userName: 'dellermann')
    }

    void testIndex() {
        controller.index()
        assert '/calendarEvent/list' == response.redirectedUrl
    }

    void testIndexWithParams() {
        params.max = 30
        params.offset = 60
        controller.index()
        assert '/calendarEvent/list?max=30&offset=60' == response.redirectedUrl
    }

    void testListEmpty() {
        def model = controller.list()
        assert null != model.calendarEventInstanceList
        assert 0 == model.calendarEventInstanceList.size()
        assert 0 == model.calendarEventInstanceTotal
    }

    void testListNonEmpty() {
        def d = new Date()
        makeCalendarEventFixture(d)
        def model = controller.list()
        assert null != model.calendarEventInstanceList
        assert 1 == model.calendarEventInstanceList.size()
        assert 'Test' == model.calendarEventInstanceList[0].subject
        assert d == model.calendarEventInstanceList[0].start
        assert d == model.calendarEventInstanceList[0].end
        assert null != model.calendarEventInstanceList[0].organization
        assert 'AMC World Technologies GmbH' == model.calendarEventInstanceList[0].organization.name
        assert null != model.calendarEventInstanceList[0].recurrence
        assert 0 == model.calendarEventInstanceList[0].recurrence.type
        assert 1 == model.calendarEventInstanceTotal
    }

    void testCalendar() {
        controller.calendar()
    }

    void testListEmbeddedEmptyWithoutOrganization() {
        def model = controller.listEmbedded()
        assert null == model.calendarEventInstanceList
        assert null == model.calendarEventInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithOrganization() {
        params.organization = 1
        def model = controller.listEmbedded()
        assert null == model.calendarEventInstanceList
        assert null == model.calendarEventInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutOrganization() {
        makeCalendarEventFixture()
        params.organization = 2
        def model = controller.listEmbedded()
        assert null == model.calendarEventInstanceList
        assert null == model.calendarEventInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithOrganization() {
        makeCalendarEventFixture()
        params.organization = 1
        def model = controller.listEmbedded()
        assert null != model.calendarEventInstanceList
        assert 1 == model.calendarEventInstanceList.size()
        assert 1 == model.calendarEventInstanceTotal
        assert null != model.linkParams
        assert 1 == model.linkParams.organization
    }

    void testListRange() {
        def d = new Date()
        makeCalendarEventFixture()
        params.start = d.time / 1000
        params.end = d.time / 1000
        controller.listRange()
        assert response.json.isEmpty()
    }

    void testCreate() {
        def model = controller.create()
        assert null != model
        assert null != model.calendarEventInstance
    }

    void testCreateWithParams() {
        params.subject = 'Test calendar entry'
        def model = controller.create()
        assert null != model
        assert null != model.calendarEventInstance
        assert 'Test calendar entry' == model.calendarEventInstance.subject
    }

    void testCopyNonExisting() {
        params.id = 1
        controller.copy()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/show/1' == response.redirectedUrl
    }

    void testCopyExisting() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        controller.copy()
        assert '/calendarEvent/create' == view
        assert null != model.calendarEventInstance
        assert 'Test' == model.calendarEventInstance.subject
        assert d == model.calendarEventInstance.start
        assert d == model.calendarEventInstance.end
        assert null != model.calendarEventInstance.organization
        assert 'AMC World Technologies GmbH' == model.calendarEventInstance.organization.name
        assert null != model.calendarEventInstance.recurrence
        assert 0 == model.calendarEventInstance.recurrence.type
    }

    void testSaveSuccess() {
        makeOrganizationFixture()
        def d = new Date()
        params.subject = 'Test'
        params.start = d
        params.end = d
        params.organization = Organization.get(1)
        params.reminders = ''
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/calendarEvent/show/1' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
    }

    void testSaveError() {
        params.subject = 'Test'
        controller.save()
        assert '/calendarEvent/create' == view
        assert 'Test' == model.calendarEventInstance.subject
    }

    void testSaveWithReturnUrl() {
        makeOrganizationFixture()
        def d = new Date()
        params.subject = 'Test'
        params.start = d
        params.end = d
        params.organization = Organization.get(1)
        params.reminders = ''
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
    }

    void testShowExisting() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        def model = controller.show()
        assert null != model.calendarEventInstance
        assert 'Test' == model.calendarEventInstance.subject
        assert d == model.calendarEventInstance.start
        assert d == model.calendarEventInstance.end
        assert null != model.calendarEventInstance.organization
        assert 'AMC World Technologies GmbH' == model.calendarEventInstance.organization.name
        assert null != model.calendarEventInstance.recurrence
        assert 0 == model.calendarEventInstance.recurrence.type
    }

    void testShowNonExisting() {
        params.id = 1
        controller.show()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
    }

    void testEditExisting() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        def model = controller.edit()
        assert null != model.calendarEventInstance
        assert 'Test' == model.calendarEventInstance.subject
        assert d == model.calendarEventInstance.start
        assert d == model.calendarEventInstance.end
        assert null != model.calendarEventInstance.organization
        assert 'AMC World Technologies GmbH' == model.calendarEventInstance.organization.name
        assert null != model.calendarEventInstance.recurrence
        assert 0 == model.calendarEventInstance.recurrence.type
        assert '' == model.reminderInstanceList
    }

    void testEditNonExisting() {
        params.id = 1
        def model = controller.edit()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
    }

    void testUpdateSuccess() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        params.subject = 'Test 2'
        params.start = d
        params.end = d
        params.reminders = ''
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/calendarEvent/show/1' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
        def calendarEvent = CalendarEvent.get(1)
        assert 'Test 2' == calendarEvent.subject
        assert d == calendarEvent.start
        assert d == calendarEvent.end
    }

    void testUpdateError() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        params.subject = ''
        controller.update()
        assert '/calendarEvent/edit' == view
        assert '' == model.calendarEventInstance.subject
        assert 1 == CalendarEvent.count()
    }

    void testUpdateNonExisting() {
        controller.update()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
    }

    void testUpdateWithReturnUrl() {
        def d = new Date()
        makeCalendarEventFixture(d)
        params.id = 1
        params.subject = 'Test 2'
        params.start = d
        params.end = d
        params.reminders = ''
        params.returnUrl = '/organization/show/5'
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
    }

    void testDeleteExistingConfirmed() {
        makeCalendarEventFixture()
        params.id = 1
        params.confirmed = true
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
        assert 0 == CalendarEvent.count()
    }

    void testDeleteExistingUnconfirmed() {
        makeCalendarEventFixture()
        params.id = 1
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
    }

    void testDeleteNonExisting() {
        params.id = 1
        params.confirmed = true
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/calendarEvent/list' == response.redirectedUrl
    }

    void testDeleteWithReturnUrlConfirmed() {
        makeCalendarEventFixture()
        params.id = 1
        params.confirmed = true
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 0 == CalendarEvent.count()
    }

    void testDeleteWithReturnUrlUnconfirmed() {
        makeCalendarEventFixture()
        params.id = 1
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CalendarEvent.count()
    }

    void testReminders() {
        // TODO implement this method
    }


    //-- Non-public methods ---------------------

    protected void makeCalendarEventFixture(Date d = new Date()) {
        def recurrence = new RecurrenceData()
        def user = new User(userName: 'dellermann')
        makeOrganizationFixture()
        def org = Organization.get(1)
        mockDomain(
            CalendarEvent, [
                [subject: 'Test', start: d, end: d, organization: org, recurrence: recurrence, owner: user]
            ]
        )
    }

    protected void makeOrganizationFixture() {
        mockDomain(
            Organization, [
                [id: 1, number: 10000, recType: 1, name: 'AMC World Technologies GmbH', legalForm: 'GmbH']
            ]
        )
    }
}
