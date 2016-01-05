/*
 * SalesOrderSpec.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SalesOrder)
class SalesOrderSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty sales order'
        def so = new SalesOrder()

        then: 'the properties are initialized properly'
        0i == so.number
        'O' == so.type
        null == so.subject
        null == so.organization
        null == so.person
        null != so.docDate
        null == so.carrier
        null == so.shippingDate
        null == so.billingAddr
        null == so.shippingAddr
        null == so.headerText
        null == so.items
        null == so.footerText
        0.0 == so.discountPercent
        0.0 == so.discountAmount
        0.0 == so.shippingCosts
        0.0 == so.shippingTax
        0.0 == so.adjustment
        0.0 == so.total
        null == so.notes
        null == so.createUser
        null == so.dateCreated
        null == so.lastUpdated
        null == so.quote
        null == so.invoices
        null == so.stage
        null == so.dueDate
        null == so.deliveryDate
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty sales order'
        def so1 = new SalesOrder()

        when: 'I copy the sales order using the constructor'
        def so2 = new SalesOrder(so1)

        then: 'the properties are set properly'
        0i == so2.number
        'O' == so2.type
        null == so2.subject
        null != so2.docDate
        null == so2.carrier
        null == so2.shippingDate
        null == so2.billingAddr
        null == so2.shippingAddr
        null == so2.headerText
        null == so2.items
        null == so2.footerText
        0.0 == so2.discountPercent
        0.0 == so2.discountAmount
        0.0 == so2.shippingCosts
        0.0 == so2.shippingTax
        0.0 == so2.adjustment
        0.0 == so2.total
        null == so2.notes
        null == so2.createUser
        null == so2.dateCreated
        null == so2.lastUpdated
        null == so2.quote
        null == so2.invoices
        null == so2.stage
        null == so2.dueDate
        null == so2.deliveryDate
    }

    def 'Copy a sales order using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date dueDate = docDate + 30
        Date deliveryDate = docDate + 60

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'a sales order with various properties'
        def so1 = new SalesOrder(
            number: 39999,
            subject: 'Test sales order',
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
            quote: new Quote(),
            invoices: [new Invoice(), new Invoice()],
            stage: new SalesOrderStage(),
            dueDate: dueDate,
            deliveryDate: deliveryDate
        )

        when: 'I copy the sales order using the constructor'
        def so2 = new SalesOrder(so1)

        then: 'some properties are the equal'
        so1.type == so2.type
        so1.subject == so2.subject
        !so1.billingAddr.is(so2.billingAddr)
        so1.billingAddr == so2.billingAddr
        !so1.shippingAddr.is(so2.shippingAddr)
        so1.shippingAddr == so2.shippingAddr
        so1.headerText == so2.headerText
        !so1.items.is(so2.items)
        so1.items == so2.items
        so1.footerText == so2.footerText
        so1.discountPercent == so2.discountPercent
        so1.discountAmount == so2.discountAmount
        so1.shippingCosts == so2.shippingCosts
        so1.shippingTax == so2.shippingTax
        so1.adjustment == so2.adjustment
        so1.total == so2.total
        so1.notes == so2.notes
        !so1.termsAndConditions.is(so2.termsAndConditions)
        so1.termsAndConditions == so2.termsAndConditions

        and: 'some instances are the same'
        so1.organization.is so2.organization
        so1.person.is so2.person
        so1.quote.is so2.quote

        and: 'some properties are set to new values'
        null != so2.docDate
        so1.docDate != so2.docDate

        and: 'some properties are unset'
        null == so2.id
        0i == so2.number
        null == so2.carrier
        null == so2.shippingDate
        null == so2.createUser
        null == so2.dateCreated
        null == so2.lastUpdated
        null == so2.invoices
        null == so2.dueDate
        null == so2.deliveryDate
    }

    def 'Stage must not be null'() {
        given: 'a quite valid sales order'
        def so = new SalesOrder(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem()]
        )

        when: 'I set the stage'
        so.stage = new SalesOrderStage()

        then: 'the instance is valid'
        so.validate()

        when: 'I unset the stage'
        so.stage = null

        then: 'the instance is not valid'
        !so.validate()
    }
}
