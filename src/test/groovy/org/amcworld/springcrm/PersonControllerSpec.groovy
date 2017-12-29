/*
 * PersonControllerSpec.groovy
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

import grails.testing.web.controllers.ControllerUnitTest
import javax.naming.AuthenticationException
import javax.naming.CommunicationException
import javax.naming.NameNotFoundException
import spock.lang.Specification


class PersonControllerSpec extends Specification
    implements ControllerUnitTest<PersonController>
{

    //-- Feature methods ------------------------

    def 'Index action with empty content'() {
        when:
        def model = controller.index()

        then:
        matchEmptyList model
    }

    def 'Index action with content'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the list action'
        def model = controller.index()

        then: 'I get a list of persons with one correct entry'
        matchPersonInList model
    }

    def 'Index action with a letter'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the list action with a letter of an existing subject'
        params.letter = 'E'
        def model = controller.index()

        then: 'I get one correct entry in the list'
        matchPersonInList model

        when: 'I call the list action with another letter'
        params.letter = 'C'
        model = controller.index()

        then: 'I get also one correct entry in the list'
        matchPersonInList model

        when: 'I call the list action with just another letter'
        params.letter = 'U'
        model = controller.index()

        then: 'I get also one correct entry in the list'
        matchPersonInList model
    }

    def 'ListEmbedded action with empty content'() {
        when:
        def model = controller.listEmbedded()

        then:
        matchNullList model
    }

    def 'ListEmbedded action without parameters'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the listEmbedded action'
        def model = controller.listEmbedded()

        then: 'I get an empty list'
        matchNullList model
    }

    def 'ListEmbedded action with a non-existing organization'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the listEmbedded action'
        params.organization = 2
        def model = controller.listEmbedded()

        then: 'I get an empty list'
        matchNullList model
    }

    def 'ListEmbedded action with an existing organization'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the listEmbedded action'
        params.organization = 1
        def model = controller.listEmbedded()

        then: 'I get one correct entry in the list'
        matchPersonInList model
    }

    def 'Create action without parameters'() {
        when:
        def model = controller.create()

        then:
        Person p = model.personInstance
        null != p
    }

    def 'Create action with parameters'() {
        when: 'I call the create action with Person properties'
        params.firstName = 'Daniel'
        params.lastName = 'Ellermann'
        def model = controller.create()

        then: 'I get a Person object using these properties'
        Person p = model.personInstance
        null != p
        'Daniel' == p.firstName
        'Ellermann' == p.lastName
    }

    def 'Create action with existing organization'() {
        given: 'an organization'
        makeOrganizationFixture()

        when: 'I call the create action with an organization ID'
        params.organization = 1
        def model = controller.create()

        then: 'I get a Person object using this organization'
        Person p = model.personInstance
        null != p
        null != p.organization
        'AMC World Technologies GmbH' == p.organization.name
    }

    def 'Create action with non-existing organization'() {
        when: 'I call the create action with an invalid organization ID'
        params.organization = 5
        def model = controller.create()

        then: 'I get a Person object without organization'
        Person p = model.personInstance
        null != p
        null == p.organization
    }

    def 'Copy action with non-existing person'() {
        when: 'I call the copy action with an invalid person ID'
        params.id = 1
        controller.copy()

        then: 'I are redirected to the list'
        '/person/index' == response.redirectedUrl
        'default.not.found.message' == flash.message
    }

    def 'Copy action with existing person'() {
        given: 'a person'
        makePersonFixture()

        when: 'I call the copy action with a valid person ID'
        params.id = 1
        controller.copy()

        then: 'I get the create view'
        '/person/create' == view

        and: 'a copy of that person'
        matchPerson model.personInstance
    }

    def 'Save action successful'() {
        given: 'an organization'
        makeOrganizationFixture()
        def org = Organization.get(1)

        when: 'I send a form to the save action'
        params.number = 30000
        params.firstName = 'Daniel'
        params.lastName = 'Ellermann'
        params.organization = org
        params.phone = '123456789'
        params.mailingAddr = new Address(
            street: 'Fischerinsel 1', postalCode: '10179', location: 'Berlin',
            state: 'Berlin', country: 'Deutschland'
        )
        params.otherAddr = new Address()
        params.email1 = 'daniel@example.com'
        params.email2 = 'info@example.com'
        request.method = 'POST'
        controller.save()

        then: 'I am redirected to the show view'
        '/person/show/1' == response.redirectedUrl
        'default.created.message' == flash.message

        and: 'a person has been created'
        1 == Person.count()
        def p = Person.first()
        matchPerson p
        null != p.dateCreated
        null != p.lastUpdated
    }

    def 'Save action unsuccessful'() {
        given: 'an organization'
        makeOrganizationFixture()
        def org = Organization.get(1)

        when: 'I send an quite empty form to the save action'
        params.lastName = 'Ellermann'
        request.method = 'POST'
        controller.save()

        then: 'I get the create form again'
        '/person/create' == view

        and: 'the entered values are set again'
        'Ellermann' == model.personInstance.lastName
    }

    def 'Save action successful with returnUrl'() {
        given: 'an organization'
        makeOrganizationFixture()
        def org = Organization.get(1)

        when: 'I send a form to the save action'
        params.number = 10000   // prevents use of SeqNumberService
        params.firstName = 'Daniel'
        params.lastName = 'Ellermann'
        params.organization = org
        params.phone = '123456789'
        params.mailingAddr = new Address(
            street: 'Fischerinsel 1', postalCode: '10179', location: 'Berlin',
            state: 'Berlin', country: 'Deutschland'
        )
        params.otherAddr = new Address()
        params.email1 = 'daniel@example.com'
        params.email2 = 'info@example.com'
        params.returnUrl = '/organization/show/5'
        request.method = 'POST'
        controller.save()

        then: 'I am redirected to the requested URL'
        '/organization/show/5' == response.redirectedUrl
        'default.created.message' == flash.message

        and: 'a person has been created'
        1 == Person.count()
        def p = Person.first()
        matchPerson p
        null != p.dateCreated
        null != p.lastUpdated
    }

    // TODO test other actions

    def 'Update action with existing person successful'() {
        given: 'a person'
        makePersonFixture()

        when: 'I send a form to the update action'
        params.id = 1
        params.firstName = 'John'
        params.lastName = 'Smith'
        request.method = 'POST'
        controller.update()

        then: 'I am redirected to the show view'
        '/person/show/1' == response.redirectedUrl
        'default.updated.message' == flash.message

        and: 'there is still one person'
        1 == Person.count()

        and: 'it has been updated'
        def p = Person.get(1)
        null != p
        'John' == p.firstName
        'Smith' == p.lastName
    }

    def 'Handle authentication exception'() {
        given: 'a person'
        makePersonFixture()

        and: 'a mocked credential'
        makeCredential()

        and: 'a mock LDAP service that throws an exception'
        def srv = Mock(LdapService)
        srv.save(_) >> { throw new AuthenticationException() }
        controller.ldapService = srv

        when: 'I send a form to the update action'
        params.id = 1
        params.firstName = 'John'
        params.lastName = 'Smith'
        request.method = 'POST'
        controller.update()

        then: 'I am redirected to the show view'
        '/error/ldapPerson?type=authentication&origAction=&personId=1' == response.redirectedUrl   // the origAction is not available in tests

        and: 'there is still one person'
        1 == Person.count()

        and: 'but it has been updated nevertheless'
        def p = Person.get(1)
        null != p
        'John' == p.firstName
        'Smith' == p.lastName
    }

    def 'Handle communication exception'() {
        given: 'a person'
        makePersonFixture()

        and: 'a mocked credential'
        makeCredential()

        and: 'a mock LDAP service that throws an exception'
        def srv = Mock(LdapService)
        srv.save(_) >> { throw new CommunicationException() }
        controller.ldapService = srv

        when: 'I send a form to the update action'
        params.id = 1
        params.firstName = 'John'
        params.lastName = 'Smith'
        request.method = 'POST'
        controller.update()

        then: 'I am redirected to the show view'
        '/error/ldapPerson?type=communication&origAction=&personId=1' == response.redirectedUrl   // the origAction is not available in tests

        and: 'there is still one person'
        1 == Person.count()

        and: 'but it has been updated nevertheless'
        def p = Person.get(1)
        null != p
        'John' == p.firstName
        'Smith' == p.lastName
    }

    def 'Handle name not found exception'() {
        given: 'a person'
        makePersonFixture()

        and: 'a mocked credential'
        makeCredential()

        and: 'a mock LDAP service that throws an exception'
        def srv = Mock(LdapService)
        srv.save(_) >> { throw new NameNotFoundException() }
        controller.ldapService = srv

        when: 'I send a form to the update action'
        params.id = 1
        params.firstName = 'John'
        params.lastName = 'Smith'
        request.method = 'POST'
        controller.update()

        then: 'I am redirected to the show view'
        '/error/ldapPerson?type=nameNotFound&origAction=&personId=1' == response.redirectedUrl   // the origAction is not available in tests

        and: 'there is still one person'
        1 == Person.count()

        and: 'but it has been updated nevertheless'
        def p = Person.get(1)
        null != p
        'John' == p.firstName
        'Smith' == p.lastName
    }


    //-- Non-public methods ---------------------

    protected void makeOrganizationFixture() {
        mockDomain Organization, [
            [
                id: 1, number: 10000, recType: 1,
                name: 'AMC World Technologies GmbH', legalForm: 'GmbH',
                billingAddr: new Address(
                    street: '45, Tulip rd.',
                    postalCode: '39339',
                    location: 'Mt Helena',
                    state: 'NY',
                    country: 'USA'
                ), shippingAddr: new Address(),
                phone: '987654321',
                email1: 'info@example.com',
                website: 'http://www.example.com'
            ]
        ]
    }

    protected void makePersonFixture(Organization org = null) {
        if (!org) {
            makeOrganizationFixture()
            org = Organization.first()
        }

        mockDomain Person, [
            [
                id: 1, number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann',
                mailingAddr: new Address(
                    street: 'Fischerinsel 1',
                    postalCode: '10179',
                    location: 'Berlin',
                    state: 'Berlin',
                    country: 'Deutschland'
                ),
                number: 30000,
                otherAddr: new Address(), phone: '123456789',
                email1: 'daniel@example.com',
                email2: 'info@example.com'

            ]
        ]
    }

    private makeCredential() {
        UserSettings us = Mock()
        us.getAt(_) >> null

        User u = Mock()
        u.getId() >> 5L
        u.getSettings() >> us

        session.credential = new Credential(u)
    }

    protected void matchEmptyList(Map model) {
        assert null != model.personInstanceList
        assert 0 == model.personInstanceList.size()
        assert 0 == model.personInstanceTotal
    }

    protected void matchNullList(Map model) {
        assert null == model.personInstanceList
        assert 0 == model.personInstanceTotal
    }

    protected void matchPerson(Person p) {
        assert 'Daniel' == p.firstName
        assert 'Ellermann' == p.lastName

        assert null != p.organization
        assert 'AMC World Technologies GmbH' == p.organization.name

        Address addr = p.mailingAddr
        assert 'Fischerinsel 1' == addr.street
        assert '10179' == addr.postalCode
        assert 'Berlin' == addr.location
        assert 'Berlin' == addr.state
        assert 'Deutschland' == addr.country

        assert '123456789' == p.phone
        assert 'daniel@example.com' == p.email1
        assert 'info@example.com' == p.email2
    }

    protected void matchPersonInList(Map model) {
        assert null != model.personInstanceList
        assert 1 == model.personInstanceList.size()
        assert 1 == model.personInstanceTotal

        matchPerson model.personInstanceList[0]
    }
}
