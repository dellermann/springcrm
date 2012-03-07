/*
 * CallTests.groovy
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

import java.util.Date
import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code CallTests} contains the unit test cases for {@code Call}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(Call)
@Mock([Call, Organization, Person])
class CallTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def call = new Call()
        assert null != call
        assert null != call.start
    }

    void testCopyConstructor() {
        def d = new Date()
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        def call = new Call(
            subject: 'Test', notes: 'Test call', organization: org,
            person: person, phone: '123456789', start: d
        )
        def anotherCall = new Call(call)
        assert null != anotherCall
        assert 'Test' == anotherCall.subject
        assert 'Test call' == anotherCall.notes
        assert null != anotherCall.organization
        assert 'AMC World Technologies GmbH' == anotherCall.organization.name
        assert null != anotherCall.person
        assert 'Daniel' == anotherCall.person.firstName
        assert 'Ellermann' == anotherCall.person.lastName
        assert '123456789' == anotherCall.phone
        assert d == anotherCall.start
    }

    void testConstraints() {
        mockForConstraintsTests(Call)

        def call = new Call()
        assert !call.validate()
        assert 'nullable' == call.errors['subject']
        assert 'nullable' == call.errors['type']
        assert 'nullable' == call.errors['status']

        call = new Call(subject: '')
        assert !call.validate()
        assert 'blank' == call.errors['subject']

        call = new Call(phone: '1234567890' * 5)
        assert !call.validate()
        assert 'maxSize' == call.errors['phone']

        def d = new Date()
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        call = new Call(
            subject: 'Test', notes: 'Test call', organization: org,
            person: person, phone: '123456789', start: d,
            type: CallType.incoming, status: CallStatus.completed
        )
        assert call.validate()
    }

    void testToString() {
        def d = new Date()
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        def call = new Call(
            subject: 'Test', notes: 'Test call', organization: org,
            person: person, phone: '123456789', start: d
        )
        assert 'Test' == call.toString()
    }


    //-- Non-public methods ---------------------

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
