/*
 * CallControllerTests.groovy
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
 * The class {@code CallControllerTests} contains unit test cases for
 * {@code CallController}.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
@TestFor(CallController)
@Mock([Call, Organization, Person])
class CallControllerTests {

    //-- Public methods -------------------------

    void setUp() {
        Call.metaClass.index = { -> }
		Call.metaClass.reindex = { -> }
	}

    void testIndex() {
		controller.index()
		assert '/call/list' == response.redirectedUrl
    }

    void testIndexWithParams() {
        params.max = 30
        params.offset = 60
        controller.index()
        assert '/call/list?max=30&offset=60' == response.redirectedUrl
    }

    void testListEmpty() {
        def model = controller.list()
        assert null != model.callInstanceList
        assert 0 == model.callInstanceList.size()
        assert 0 == model.callInstanceTotal
    }

    void testListNonEmpty() {
        def d = new Date()
        def callEvent = makeCallFixture(d)
        def model = controller.list()
        assert null != model.callInstanceList
        assert 1 == model.callInstanceList.size()
        assert 'Test' == model.callInstanceList[0].subject
        assert 'Test call' == model.callInstanceList[0].notes
        assert null != model.callInstanceList[0].organization
        assert 'AMC World Technologies GmbH' == model.callInstanceList[0].organization.name
        assert '+49 30 8321475-0' == model.callInstanceList[0].phone
        assert d == model.callInstanceList[0].start
        assert CallType.incoming == model.callInstanceList[0].type
        assert CallStatus.completed == model.callInstanceList[0].status
        assert 1 == model.callInstanceTotal
    }

    void testListWithLetter() {
        def callEvent = makeCallFixture()
        params.letter = 'T'
        def model = controller.list()
        assert null != model.callInstanceList
        assert 1 == model.callInstanceList.size()
        assert 'Test' == model.callInstanceList[0].subject
        assert 1 == model.callInstanceTotal
    }

    void testListEmbeddedEmpty() {
        def model = controller.listEmbedded()
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithOrganization() {
        params.organization = 1
        def model = controller.listEmbedded()
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutOrganization() {
        makeCallFixture()
        params.organization = 2
        def model = controller.listEmbedded()
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithOrganization() {
        makeCallFixture()
        params.organization = 1
        def model = controller.listEmbedded()
        assert null != model.callInstanceList
        assert 1 == model.callInstanceTotal
        assert 1 == model.callInstanceList.size()
        assert null != model.linkParams
        assert 1 == model.linkParams.organization
    }

    void testListEmbeddedEmptyWithPerson() {
        params.person = 1
        def model = controller.listEmbedded()
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutPerson() {
        makeCallFixture()
        params.person = 2
        def model = controller.listEmbedded()
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithPerson() {
        makeCallFixture()
        params.person = 1
        def model = controller.listEmbedded()
        assert null != model.callInstanceList
        assert 1 == model.callInstanceTotal
        assert 1 == model.callInstanceList.size()
        assert null != model.linkParams
        assert 1 == model.linkParams.person
    }

    void testCreate() {
        def model = controller.create()
        assert null != model
        assert null != model.callInstance
    }

    void testCreateWithParams() {
        params.subject = 'Test call'
        def model = controller.create()
        assert null != model
        assert null != model.callInstance
        assert 'Test call' == model.callInstance.subject
    }

    void testCreateWithPerson() {
        makeOrganizationFixture()
        makePersonFixture(Organization.get(1))
        params.person = Person.get(1)
        def model = controller.create()
        assert null != model.callInstance
        assert null == model.callInstance.subject
        assert '123456789' == model.callInstance.phone
    }

    void testCreateWithOrganization() {
        makeOrganizationFixture()
        params.organization = Organization.get(1)
        def model = controller.create()
        assert null != model.callInstance
        assert null == model.callInstance.subject
        assert '987654321' == model.callInstance.phone
    }

    void testCopyNonExisting() {
        params.id = 1
        controller.copy()
        assert 'default.not.found.message' == flash.message
        assert '/call/show/1' == response.redirectedUrl
    }

    void testCopyExisting() {
        def d = new Date()
        makeCallFixture(d)
        params.id = 1
        controller.copy()
        assert '/call/create' == view
        assert null != model.callInstance
        assert 'Test' == model.callInstance.subject
        assert 'Test call' == model.callInstance.notes
        assert null != model.callInstance.organization
        assert 'AMC World Technologies GmbH' == model.callInstance.organization.name
        assert '+49 30 8321475-0' == model.callInstance.phone
        assert d == model.callInstance.start
        assert CallType.incoming == model.callInstance.type
        assert CallStatus.completed == model.callInstance.status
    }

    void testSaveSuccess() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def d = new Date()
        params.subject = 'Test'
        params.notes = 'Test call'
        params.organization = org
        params.person = Person.get(1)
        params.phone = '+49 30 8321475-0'
        params.start = d
        params.type = CallType.outgoing
        params.status = CallStatus.planned
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/call/show/1' == response.redirectedUrl
        assert 1 == Call.count()
    }

    void testSaveError() {
        params.subject = 'Test'
        controller.save()
        assert '/call/create' == view
        assert 'Test' == model.callInstance.subject
    }

    void testSaveWithReturnUrl() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def d = new Date()
        params.subject = 'Test'
        params.notes = 'Test call'
        params.organization = org
        params.person = Person.get(1)
        params.phone = '+49 30 8321475-0'
        params.start = d
        params.type = CallType.outgoing
        params.status = CallStatus.planned
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Call.count()
    }

    void testShowExisting() {
        def d = new Date()
        makeCallFixture(d)
        params.id = 1
        def model = controller.show()
        assert null != model.callInstance
        assert 'Test' == model.callInstance.subject
        assert 'Test call' == model.callInstance.notes
        assert null != model.callInstance.organization
        assert 'AMC World Technologies GmbH' == model.callInstance.organization.name
        assert '+49 30 8321475-0' == model.callInstance.phone
        assert d == model.callInstance.start
        assert CallType.incoming == model.callInstance.type
        assert CallStatus.completed == model.callInstance.status
    }

    void testShowNonExisting() {
        params.id = 1
        controller.show()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
    }

    void testEditExisting() {
        def d = new Date()
        makeCallFixture(d)
        params.id = 1
        def model = controller.edit()
        assert null != model.callInstance
        assert 'Test' == model.callInstance.subject
        assert 'Test call' == model.callInstance.notes
        assert null != model.callInstance.organization
        assert 'AMC World Technologies GmbH' == model.callInstance.organization.name
        assert '+49 30 8321475-0' == model.callInstance.phone
        assert d == model.callInstance.start
        assert CallType.incoming == model.callInstance.type
        assert CallStatus.completed == model.callInstance.status
    }

    void testEditNonExisting() {
        params.id = 1
        def model = controller.edit()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
    }

    void testUpdateSuccess() {
        makeCallFixture()
        params.id = 1
        params.subject = 'Test 2'
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/call/show/1' == response.redirectedUrl
        assert 1 == Call.count()
    }

    void testUpdateError() {
        makeCallFixture()
        params.id = 1
        params.subject = ''
        controller.update()
        assert '/call/edit' == view
        assert '' == model.callInstance.subject
        assert 1 == Call.count()
    }

    void testUpdateNonExisting() {
        controller.update()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
    }

    void testUpdateWithReturnUrl() {
        makeCallFixture()
        params.id = 1
        params.subject = 'Test 2'
        params.returnUrl = '/organization/show/5'
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Call.count()
    }

    void testDeleteExistingConfirmed() {
        makeCallFixture()
        params.id = 1
        params.confirmed = true
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/call/list' == response.redirectedUrl
        assert 0 == Call.count()
    }

    void testDeleteExistingUnconfirmed() {
        makeCallFixture()
        params.id = 1
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
        assert 1 == Call.count()
    }

    void testDeleteNonExisting() {
        params.id = 1
        params.confirmed = true
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
    }

    void testDeleteWithReturnUrlConfirmed() {
        makeCallFixture()
        params.id = 1
        params.confirmed = true
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 0 == Call.count()
    }

    void testDeleteWithReturnUrlUnconfirmed() {
        makeCallFixture()
        params.id = 1
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Call.count()
    }


    //-- Non-public methods ---------------------

    protected void makeCallFixture(Date d = new Date()) {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        mockDomain(
            Call, [
                [subject: 'Test', notes: 'Test call', organization: org, person: person, phone: '+49 30 8321475-0', start: d, type: CallType.incoming, status: CallStatus.completed]
            ]
        )
    }

    protected void makeOrganizationFixture() {
        mockDomain(
            Organization, [
                [id: 1, number: 10000, recType: 1, name: 'AMC World Technologies GmbH', legalForm: 'GmbH', phone: '987654321']
            ]
        )
    }

    protected void makePersonFixture(Organization org) {
        mockDomain(
            Person, [
                [id: 1, number: 10000, organization: org, firstName: 'Daniel', lastName: 'Ellermann', phone: '123456789']
            ]
        )
    }
}
