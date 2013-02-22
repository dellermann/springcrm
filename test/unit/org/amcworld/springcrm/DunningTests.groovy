/*
 * DunningTests.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * The class {@code DunningTests} contains the unit test cases for
 * {@code Dunning}.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
@TestFor(Dunning)
@Mock([Dunning, Organization, Person, InvoicingItem, Invoice, InvoiceStage, SeqNumberService])
class DunningTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def dunning = new Dunning()
        assert 'D' == dunning.type
    }

    void testConstraints() {
        mockForConstraintsTests(Dunning)

        def d = new Date()
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        def dunning = new Dunning(number: 20000, subject: 'Test dunning', organization: org, person: person, docDate: d, total: 1789.76)
        assert !dunning.validate()
        assert 'nullable' == dunning.errors['stage']
        assert 'nullable' == dunning.errors['level']
        assert 'nullable' == dunning.errors['dueDatePayment']
        assert 'nullable' == dunning.errors['invoice']

        dunning = new Dunning(number: 20000, subject: 'Test dunning', organization: org, person: person, docDate: d, total: 1789.76, stage: new DunningStage(name: 'created'), level: new DunningLevel(name: '1st dunning'), dueDatePayment: d, invoice: new Invoice())
        assert dunning.validate()
    }


    //-- Non-public methods ---------------------

    protected void makeOrganizationFixture() {
        mockDomain(
            Organization, [
                [id: 1, number: 10000, recType: 1, name: 'AMC World Technologies GmbH', legalForm: 'GmbH', billingAddrStreet: 'Fischerinsel 1', billingAddrLocation: 'Berlin', shippingAddrStreet: 'Fischerinsel 1', shippingAddrLocation: 'Berlin']
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
