/*
 * QuoteSpec.groovy
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

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class QuoteSpec extends Specification implements DomainUnitTest<Quote> {

    //-- Feature methods ------------------------

    void 'Creating an empty instance initializes the properties'() {
        when: 'an empty quote is created'
        def q = new Quote()

        then: 'the properties are initialized properly'
        0i == q.number
        'Q' == q.type
        null == q.subject
        null == q.organization
        null == q.person
        null != q.docDate
        null == q.carrier
        null == q.shippingDate
        null == q.billingAddr
        null == q.shippingAddr
        null == q.headerText
        null == q.items
        null == q.footerText
        0.0 == q.discountPercent
        0.0 == q.discountAmount
        0.0 == q.shippingCosts
        0.0 == q.shippingTax
        0.0 == q.adjustment
        0.0 == q.total
        null == q.notes
        null == q.createUser
        null == q.dateCreated
        null == q.lastUpdated
        null == q.salesOrders
        null == q.invoices
        null == q.stage
        null == q.validUntil
    }

    void 'Copy an empty instance using constructor'() {
        given: 'an empty quote'
        def q1 = new Quote()

        when: 'the quote is copied using the constructor'
        def q2 = new Quote(q1)

        then: 'the properties are set properly'
        0i == q2.number
        'Q' == q2.type
        null == q2.subject
        null != q2.docDate
        null == q2.carrier
        null == q2.shippingDate
        null == q2.billingAddr
        null == q2.shippingAddr
        null == q2.headerText
        null == q2.items
        null == q2.footerText
        0.0 == q2.discountPercent
        0.0 == q2.discountAmount
        0.0 == q2.shippingCosts
        0.0 == q2.shippingTax
        0.0 == q2.adjustment
        0.0 == q2.total
        null == q2.notes
        null == q2.createUser
        null == q2.dateCreated
        null == q2.lastUpdated
        null == q2.salesOrders
        null == q2.invoices
        null == q2.stage
        null == q2.validUntil
    }

    void 'Copy a quote using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date validUntilDate = docDate + 30

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'a quote with various properties'
        def q1 = new Quote(
            number: 39999,
            subject: 'Test quote',
            organization: organization,
            person: person,
            docDate: docDate,
            carrier: new Carrier(),
            shippingDate: shippingDate,
            billingAddr: new Address(street: '34 Park Ave.'),
            shippingAddr: new Address(street: '78 Main rd.'),
            headerText: 'my header text',
            items: [
                new InvoicingItem(
                    number: 'P-10000', quantity: 4, unit: 'pcs.',
                    name: 'books', unitPrice: 44.99, tax: 19
                ),
                new InvoicingItem(
                    number: 'P-20000', quantity: 10.5, unit: 'm',
                    name: 'tape', unitPrice: 0.89, tax: 7
                ),
                new InvoicingItem(
                    number: 'S-10100', quantity: 4.25, unit: 'h',
                    name: 'repairing', unitPrice: 39, tax: 19
                )
            ],
            footerText: 'my footer text',
            discountPercent: 2,
            discountAmount: 5.89,
            shippingCosts: 4.5,
            shippingTax: 7,
            adjustment: 0.54,
            total: 4505.31,
            notes: 'my notes',
            termsAndConditions: [
                new TermsAndConditions(name: 'wares'),
                new TermsAndConditions(name: 'services')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated,
            salesOrders: [new SalesOrder(), new SalesOrder()],
            invoices: [new Invoice(), new Invoice()],
            stage: new QuoteStage(),
            validUntil: validUntilDate
        )

        when: 'the quote is copied using the constructor'
        def q2 = new Quote(q1)

        then: 'some properties are the equal'
        q1.type == q2.type
        q1.subject == q2.subject
        !q1.billingAddr.is(q2.billingAddr)
        q1.billingAddr == q2.billingAddr
        !q1.shippingAddr.is(q2.shippingAddr)
        q1.shippingAddr == q2.shippingAddr
        q1.headerText == q2.headerText
        !q1.items.is(q2.items)
        q1.items == q2.items
        q1.footerText == q2.footerText
        q1.discountPercent == q2.discountPercent
        q1.discountAmount == q2.discountAmount
        q1.shippingCosts == q2.shippingCosts
        q1.shippingTax == q2.shippingTax
        q1.adjustment == q2.adjustment
        q1.total == q2.total
        q1.notes == q2.notes
        !q1.termsAndConditions.is(q2.termsAndConditions)
        q1.termsAndConditions == q2.termsAndConditions

        and: 'some instances are the same'
        q1.organization.is q2.organization
        q1.person.is q2.person

        and: 'some properties are set to new values'
        null != q2.docDate
        q1.docDate != q2.docDate

        and: 'some properties are unset'
        null == q2.id
        0i == q2.number
        null == q2.carrier
        null == q2.shippingDate
        null == q2.createUser
        null == q2.dateCreated
        null == q2.lastUpdated
        null == q2.salesOrders
        null == q2.invoices
        null == q2.validUntil
    }

    void 'Stage must not be null'() {
        given: 'a quite valid quote'
        def q = new Quote(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')]
        )

        when: 'the stage is set'
        q.stage = new QuoteStage()

        then: 'the instance is valid'
        q.validate()

        when: 'the stage is unset'
        q.stage = null

        then: 'the instance is not valid'
        !q.validate()
    }
}
