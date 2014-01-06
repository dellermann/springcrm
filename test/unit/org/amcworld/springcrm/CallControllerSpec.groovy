/*
 * CallControllerSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification


@TestFor(CallController)
@TestMixin(DomainClassUnitTestMixin)
@Mock([Call, Organization, Person])
class CallControllerSpec extends Specification {

    //-- Feature methods --------------------------

    def 'Index action without parameters'() {
        when:
        controller.index()

        then:
        '/call/list' == response.redirectedUrl
    }

    def 'Index action with parameters'() {
        when:
        params.max = 30
        params.offset = 60
        controller.index()

        then:
        '/call/list?max=30&offset=60' == response.redirectedUrl
    }

    def 'List action with empty content'() {
        when:
        def model = controller.list()

        then:
        matchEmptyList model
    }

    def 'List action with content'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the list action'
        def model = controller.list()

        then: 'I get a list of phone calls with one correct entry'
        matchCallInList model, d

        and: 'the organization and person is set'
    }

    def 'List action with a letter'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the list action with a letter of an existing subject'
        params.letter = 'T'
        def model = controller.list()

        then: 'I get one correct entry in the list'
        matchCallInList model, d

        when: 'I call the list action with another letter'
        params.letter = 'S'
        model = controller.list()

        then: 'I get also one correct entry in the list'
        matchCallInList model, d

        when: 'I call the list action with just another letter'
        params.letter = 'U'
        model = controller.list()

        then: 'I get also one correct entry in the list'
        matchCallInList model, d
    }

    def 'List action with search term'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the list action with an existing search term'
        params.search = term
        def model = controller.list()

        then: 'I get or not one correct entry in the list'
        if (matches) matchCallInList model, d
        else matchEmptyList model

        where:
        term            | matches
        'Test'          | true
        'Tes'           | true
        'Te'            | true
        'T'             | true
        'est'           | true
        'es'            | true
        'e'             | true
        'st'            | true
        's'             | true
        't'             | true
        ''              | true
        'e'             | true
        'TEST'          | false     // XXX should actually be true (case insensitive!)
        'x'             | false
        'tseT'          | false
    }

    def 'ListEmbedded action with empty content'() {
        when:
        def model = controller.listEmbedded()

        then:
        matchNullList model
    }

    def 'ListEmbedded action without parameters'() {
        given: 'a phone call'
        makeCallFixture()

        when: 'I call the listEmbedded action'
        def model = controller.listEmbedded()

        then: 'I get an empty list'
        matchNullList model
    }

    def 'ListEmbedded action with a non-existing organization'() {
        given: 'a phone call'
        makeCallFixture()

        when: 'I call the listEmbedded action'
        params.organization = 2
        def model = controller.listEmbedded()

        then: 'I get an empty list'
        matchNullList model
    }

    def 'ListEmbedded action with an existing organization'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the listEmbedded action'
        params.organization = 1
        def model = controller.listEmbedded()

        then: 'I get one correct entry in the list'
        matchCallInList model, d
    }

    def 'ListEmbedded action with a non-existing person'() {
        given: 'a phone call'
        makeCallFixture()

        when: 'I call the listEmbedded action'
        params.person = 2
        def model = controller.listEmbedded()

        then: 'I get an empty list'
        matchNullList model
    }

    def 'ListEmbedded action with an existing person'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the listEmbedded action'
        params.person = 1
        def model = controller.listEmbedded()

        then: 'I get one correct entry in the list'
        matchCallInList model, d
    }

    def 'Create action without parameters'() {
        when:
        def model = controller.create()

        then:
        Call c = model.callInstance
        null != c
        null != c.start
    }

    def 'Create action with parameters'() {
        when: 'I call the create action with Call properties'
        params.subject = 'foo'
        params.notes = 'bar'
        def model = controller.create()

        then: 'I get a Call object using these properties'
        Call c = model.callInstance
        null != c
        'foo' == c.subject
        'bar' == c.notes
        null != c.start
    }

    def 'Create action with existing organization'() {
        given: 'an organization'
        makeOrganizationFixture()

        when: 'I call the create action with an organization ID'
        params.organization = 1
        def model = controller.create()

        then: 'I get a Call object using this organization and its phone number'
        Call c = model.callInstance
        null != c
        null != c.organization
        'AMC World Technologies GmbH' == c.organization.name
        '987654321' == c.phone
        null != c.start
    }

    def 'Create action with non-existing organization'() {
        when: 'I call the create action with an invalid organization ID'
        params.organization = 5
        def model = controller.create()

        then: 'I get a Call object without organization'
        Call c = model.callInstance
        null != c
        null == c.organization
        null == c.phone
        null != c.start
    }

    def 'Create action with existing person'() {
        given: 'an organization and a person'
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture org

        when: 'I call the create action with a person ID'
        params.person = 1
        def model = controller.create()

        then: 'I get a Call object using this person, its organization, and its phone number'
        Call c = model.callInstance
        null != c
        null != c.organization
        'AMC World Technologies GmbH' == c.organization.name
        null != c.person
        'Ellermann' == c.person.lastName
        'Daniel' == c.person.firstName
        '123456789' == c.phone
        null != c.start
    }

    def 'Create action with non-existing person'() {
        when: 'I call the create action with an invalid person ID'
        params.person = 1
        def model = controller.create()

        then: 'I get a Call object without person'
        Call c = model.callInstance
        null != c
        null == c.organization
        null == c.person
        null == c.phone
        null != c.start
    }

    def 'Copy action with non-existing phone call'() {
        when: 'I call the copy action with an invalid call ID'
        params.id = 1
        controller.copy()

        then: 'I are redirected to an error page'
        '/call/show/1' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Copy action with existing phone call'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the copy action with a valid call ID'
        params.id = 1
        controller.copy()

        then: 'I get the create view'
        '/call/create' == view

        and: 'a copy of that phone call'
        matchCall model.callInstance, d
    }

    def 'Save action successful'() {
        given: 'an organization and a person'
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture org
        def person = Person.get(1)

        when: 'I send a form to the save action'
        def d = new Date()
        params.subject = 'Test'
        params.notes = 'Test call'
        params.organization = org
        params.person = person
        params.phone = '+49 30 8321475-0'
        params.start = d
        params.type = CallType.incoming
        params.status = CallStatus.completed
        controller.save()

        then: 'I am redirected to the show view'
        '/call/show/1' == response.redirectedUrl
        'default.created.message' == flash.message

        and: 'a phone call has been created'
        1 == Call.count()
        def c = Call.first()
        matchCall c, d
        null != c.dateCreated
        null != c.lastUpdated
    }

    def 'Save action unsuccessful'() {
        given: 'an organization and a person'
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture org
        def person = Person.get(1)

        when: 'I send an quite empty form to the save action'
        params.subject = 'Test'
        controller.save()

        then: 'I get the create form again'
        '/call/create' == view

        and: 'the entered values are set again'
        'Test' == model.callInstance.subject
    }

    def 'Save action successful with returnUrl'() {
        given: 'an organization and a person'
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture org
        def person = Person.get(1)

        when: 'I send a form to the save action'
        def d = new Date()
        params.subject = 'Test'
        params.notes = 'Test call'
        params.organization = org
        params.person = person
        params.phone = '+49 30 8321475-0'
        params.start = d
        params.type = CallType.incoming
        params.status = CallStatus.completed
        params.returnUrl = '/organization/show/5'
        controller.save()

        then: 'I am redirected to the requested URL'
        '/organization/show/5' == response.redirectedUrl
        'default.created.message' == flash.message

        and: 'a phone call has been created'
        1 == Call.count()
        def c = Call.first()
        matchCall c, d
        null != c.dateCreated
        null != c.lastUpdated
    }

    def 'Show action with non-existing phone call'() {
        when: 'I call the show action with an invalid ID'
        params.id = 1
        controller.show()

        then: 'I am redirected to the list view'
        '/call/list' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Show action with existing phone call'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the show action with a valid ID'
        params.id = 1
        def model = controller.show()

        then: 'I get the show view with the phone call'
        1 == model.callInstance.id
        matchCall model.callInstance, d
    }

    def 'Edit action with non-existing phone call'() {
        when: 'I call the edit action with an invalid ID'
        params.id = 1
        controller.edit()

        then: 'I am redirected to the list view'
        '/call/list' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Edit action with existing phone call'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I call the edit action with a valid ID'
        params.id = 1
        def model = controller.edit()

        then: 'I get the edit view with the phone call'
        1 == model.callInstance.id
        matchCall model.callInstance, d
    }

    def 'Update action with non-existing phone call'() {
        when: 'I call the update action with an invalid ID'
        params.id = 1
        controller.edit()

        then: 'I am redirected to the list view'
        '/call/list' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Update action with existing phone call successful'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I send a form to the update action'
        params.id = 1
        params.subject = 'Another test'
        controller.update()

        then: 'I am redirected to the show view'
        '/call/show/1' == response.redirectedUrl
        'default.updated.message' == flash.message

        and: 'there is still one phone call'
        1 == Call.count()

        and: 'it has been updated'
        def c = Call.get(1)
        null != c
        'Another test' == c.subject
    }

    def 'Update action with existing phone call unsuccessful'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I send a form with invalid content to the update action'
        params.id = 1
        params.subject = ''
        controller.update()

        then: 'I am redirected to the edit view'
        '/call/edit' == view

        and: 'there is still one phone call'
        1 == Call.count()

        and: 'the entered values are set again'
        def c = model.callInstance
        !c.subject
        'Test call' == c.notes
    }

    def 'Update action with existing phone call successful with returnUrl'() {
        given: 'a phone call'
        def d = new Date()
        makeCallFixture d

        when: 'I send a form to the update action'
        params.id = 1
        params.subject = 'Another test'
        params.returnUrl = '/organization/show/5'
        controller.update()

        then: 'I am redirected to the requested URL'
        '/organization/show/5' == response.redirectedUrl
        'default.updated.message' == flash.message

        and: 'there is still one phone call'
        1 == Call.count()

        and: 'it has been updated'
        def c = Call.get(1)
        null != c
        'Another test' == c.subject
    }

    def 'Delete action with non-existing phone call'() {
        when: 'I call the delete action with an invalid ID'
        params.id = 1
        controller.delete()

        then: 'I am redirected to the list view'
        '/call/list' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Delete action with existing phone call'() {
        given: 'a phone call'
        makeCallFixture()

        when: 'I call the delete action with a valid ID'
        params.id = 1
        controller.delete()

        then: 'I am redirected to the list view'
        '/call/list' == response.redirectedUrl
        'default.deleted.message' == flash.message

        and: 'the phone call has been deleted'
        0 == Call.count()
    }

    def 'Delete action with existing phone call and returnUrl'() {
        given: 'a phone call'
        makeCallFixture()

        when: 'I call the delete action with a valid ID'
        params.id = 1
        params.returnUrl = '/organization/show/5'
        controller.delete()

        then: 'I am redirected to the requested URL'
        '/organization/show/5' == response.redirectedUrl
        'default.deleted.message' == flash.message

        and: 'the phone call has been deleted'
        0 == Call.count()
    }


    //-- Non-public methods ---------------------

    protected void makeCallFixture(Date d = new Date()) {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture org
        def person = Person.get(1)
        mockDomain Call, [
            [
                subject: 'Test', notes: 'Test call', organization: org,
                person: person, phone: '+49 30 8321475-0', start: d,
                type: CallType.incoming, status: CallStatus.completed
            ]
        ]
    }

    protected Organization makeOrganizationFixture() {
        mockDomain Organization, [
            [
                id: 1, number: 10000, recType: 1,
                name: 'AMC World Technologies GmbH', legalForm: 'GmbH',
                billingAddr: new Address(), shippingAddr: new Address(),
                phone: '987654321'
            ]
        ]
    }

    protected void makePersonFixture(Organization org) {
        mockDomain Person, [
            [
                id: 1, number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann',
                mailingAddr: new Address(), otherAddr: new Address(),
                phone: '123456789'
            ]
        ]
    }

    protected void matchCall(Call c, Date d = null) {
        assert 'Test' == c.subject
        assert 'Test call' == c.notes
        assert '+49 30 8321475-0' == c.phone
        if (d) {
            assert d == c.start
        }
        assert CallType.incoming == c.type
        assert CallStatus.completed == c.status

        assert null != c.organization
        assert 'AMC World Technologies GmbH' == c.organization.name
        assert null != c.person
        assert 'Daniel' == c.person.firstName
        assert 'Ellermann' == c.person.lastName
    }

    protected void matchCallInList(Map model, Date d = null) {
        assert null != model.callInstanceList
        assert 1 == model.callInstanceList.size()
        assert 'Test' == model.callInstanceList[0].subject
        assert 1 == model.callInstanceTotal

        matchCall model.callInstanceList[0], d
    }

    protected void matchEmptyList(Map model) {
        assert null != model.callInstanceList
        assert 0 == model.callInstanceList.size()
        assert 0 == model.callInstanceTotal
    }

    protected void matchNullList(Map model) {
        assert null == model.callInstanceList
        assert null == model.callInstanceTotal
    }
}
