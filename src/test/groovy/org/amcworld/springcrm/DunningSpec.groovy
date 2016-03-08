/*
 * DunningSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(Dunning)
@Mock([Dunning, InvoicingItem])
class DunningSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty dunning'
        def d = new Dunning()

        then: 'the properties are initialized properly'
        0i == d.number
        'D' == d.type
        null == d.subject
        null == d.organization
        null == d.person
        null != d.docDate
        null == d.carrier
        null == d.shippingDate
        null == d.billingAddr
        null == d.shippingAddr
        null == d.headerText
        null == d.items
        null == d.footerText
        0.0 == d.discountPercent
        0.0 == d.discountAmount
        0.0 == d.shippingCosts
        0.0 == d.shippingTax
        0.0 == d.adjustment
        0.0 == d.total
        null == d.notes
        null == d.createUser
        null == d.dateCreated
        null == d.lastUpdated
        null == d.invoice
        null == d.creditMemos
        null == d.level
        null == d.stage
        null == d.dueDatePayment
        null == d.paymentDate
        0.0 == d.paymentAmount
        null == d.paymentMethod
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty dunning'
        def d1 = new Dunning()

        when: 'I copy the dunning using the constructor'
        def d2 = new Dunning(d1)

        then: 'the properties are set properly'
        0i == d2.number
        'D' == d2.type
        null == d2.subject
        null != d2.docDate
        null == d2.carrier
        null == d2.shippingDate
        null == d2.billingAddr
        null == d2.shippingAddr
        null == d2.headerText
        null == d2.items
        null == d2.footerText
        0.0 == d2.discountPercent
        0.0 == d2.discountAmount
        0.0 == d2.shippingCosts
        0.0 == d2.shippingTax
        0.0 == d2.adjustment
        0.0 == d2.total
        null == d2.notes
        null == d2.createUser
        null == d2.dateCreated
        null == d2.lastUpdated
        null == d2.invoice
        null == d2.creditMemos
        null == d2.level
        null == d2.stage
        null == d2.dueDatePayment
        null == d2.paymentDate
        0.0 == d2.paymentAmount
        null == d2.paymentMethod
    }

    def 'Copy a dunning using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date dueDatePayment = docDate + 14
        Date paymentDate = docDate + 30

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'a dunning with various properties'
        def d1 = new Dunning(
            number: 39999,
            subject: 'Test reminder',
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
            invoice: new Invoice(),
            creditMemos: [new CreditMemo(), new CreditMemo()],
            level: new DunningLevel(),
            stage: new DunningStage(),
            dueDatePayment: dueDatePayment,
            paymentDate: paymentDate,
            paymentAmount: 457.98,
            paymentMethod: new PaymentMethod()
        )

        when: 'I copy the dunning using the constructor'
        def d2 = new Dunning(d1)

        then: 'some properties are the equal'
        d1.type == d2.type
        d1.subject == d2.subject
        !d1.billingAddr.is(d2.billingAddr)
        d1.billingAddr == d2.billingAddr
        !d1.shippingAddr.is(d2.shippingAddr)
        d1.shippingAddr == d2.shippingAddr
        d1.headerText == d2.headerText
        !d1.items.is(d2.items)
        d1.items == d2.items
        d1.footerText == d2.footerText
        d1.discountPercent == d2.discountPercent
        d1.discountAmount == d2.discountAmount
        d1.shippingCosts == d2.shippingCosts
        d1.shippingTax == d2.shippingTax
        d1.adjustment == d2.adjustment
        d1.total == d2.total
        d1.notes == d2.notes
        !d1.termsAndConditions.is(d2.termsAndConditions)
        d1.termsAndConditions == d2.termsAndConditions

        and: 'some instances are the same'
        d1.organization.is d2.organization
        d1.person.is d2.person
        d1.invoice.is d2.invoice

        and: 'some properties are set to new values'
        null != d2.docDate
        d1.docDate != d2.docDate

        and: 'some properties are unset'
        null == d2.id
        0i == d2.number
        null == d2.carrier
        null == d2.shippingDate
        null == d2.createUser
        null == d2.dateCreated
        null == d2.lastUpdated
        null == d2.creditMemos
        null == d2.level
        null == d2.stage
        null == d2.dueDatePayment
        null == d2.paymentDate
        0.0 == d2.paymentAmount
        null == d2.paymentMethod
    }

    def 'Copy an invoice using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date dueDatePayment = docDate + 21
        Date paymentDate = docDate + 22

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'an invoice with various properties'
        def i = new Invoice(
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
            salesOrder: new SalesOrder(),
            creditMemos: [],
            dunnings: [],
            stage: new InvoiceStage(),
            dueDatePayment: dueDatePayment,
            paymentDate: paymentDate,
            paymentAmount: 475.08,
            paymentMethod: new PaymentMethod()
        )

        and: 'some dependent values'
        setCreditMemos i
        setDunnings i

        when: 'I copy the invoice using the constructor'
        def d = new Dunning(i)

        then: 'some properties are the equal'
        i.subject == d.subject
        !i.billingAddr.is(d.billingAddr)
        i.billingAddr == d.billingAddr
        !i.shippingAddr.is(d.shippingAddr)
        i.shippingAddr == d.shippingAddr
        !i.items.is(d.items)
        i.items == d.items
        i.discountPercent == d.discountPercent
        i.discountAmount == d.discountAmount
        i.shippingCosts == d.shippingCosts
        i.shippingTax == d.shippingTax
        i.adjustment == d.adjustment
        i.total == d.total
        i.notes == d.notes
        !i.termsAndConditions.is(d.termsAndConditions)
        i.termsAndConditions == d.termsAndConditions

        and: 'some instances are the same'
        i.organization.is d.organization
        i.person.is d.person
        i.is d.invoice

        and: 'some properties are set to new values'
        'D' == d.type
        null != d.docDate
        i.docDate != d.docDate
        3 == i.dunnings.size()
        i.dunnings.contains(d)

        and: 'some properties are unset'
        null == d.id
        0i == d.number
        null == d.carrier
        null == d.shippingDate
        null == d.createUser
        null == d.dateCreated
        null == d.lastUpdated
        null == d.creditMemos
        null == d.level
        null == d.stage
        null == d.dueDatePayment
        null == d.paymentDate
        0.0 == d.paymentAmount
        null == d.paymentMethod

        and: 'some properties in original object are unmodified'
        2 == i.creditMemos.size()
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty dunning'
        def d = new Dunning()

        when: 'I set the decimal values to null'
        d.paymentAmount = null

        then: 'all decimal values are never null'
        0.0 == d.paymentAmount

        when: 'I create a dunning with null values'
        d = new Dunning(paymentAmount: null)

        then: 'all decimal values are never null'
        0.0 == d.paymentAmount
    }

    def 'Compute balance'() {
        given: 'an empty dunning'
        def d = new Dunning()

        when: 'I set discrete values for total and payment amount'
        d.total = t
        d.paymentAmount = pa

        then: 'I get the correct balance'
        e == d.balance

        where:
        t       | pa        || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0000001
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0000001
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || -0.00001
        0.00001 | 0.0       || -0.00001
        0.00001 | 0.0000001 || -0.0000099
        0.00001 | 0.25      || 0.24999
        0.00001 | 450.47    || 450.46999
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.2499999
        0.25    | 0.25      || 0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.4699999
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Get the balance color'() {
        when: 'I create an empty dunning'
        def d = new Dunning()

        then: 'I get the default color'
        'default' == d.balanceColor

        when: 'I set a zero balance'
        d = new Dunning(paymentAmount: 25.6669, total: 25.666900000)

        then: 'I get the default color'
        'default' == d.balanceColor

        when: 'I set a positive balance'
        d = new Dunning(paymentAmount: 25.7, total: 25.66666669)

        then: 'I get the default color'
        'green' == d.balanceColor

        when: 'I set a negative balance'
        d = new Dunning(paymentAmount: 25.7, total: 25.70000001)

        then: 'I get the default color'
        'red' == d.balanceColor
    }

    def 'Compute (modified) closing balance without credit memos'() {
        when: 'I create an empty dunning'
        def d = new Dunning()

        then: 'the closing balance is zero'
        0.0 == d.closingBalance
        0.0 == d.modifiedClosingBalance

        when: 'I set an empty credit memo list'
        d = new Dunning(creditMemos: [])

        then: 'the closing balance is zero'
        0.0 == d.closingBalance
        0.0 == d.modifiedClosingBalance

        when: 'I add an empty credit memo'
        d.creditMemos << new CreditMemo()

        then: 'the closing balance is zero'
        0.0 == d.closingBalance
        0.0 == d.modifiedClosingBalance
    }

    def 'Compute (modified) closing balance with credit memos'() {
        when: 'I create a non-empty dunning'
        def d = new Dunning(creditMemos: [])
        def c = new CreditMemo(total: t, paymentAmount: pa)
        c.id = 457L
        d.creditMemos << c

        then: 'the closing balance is computed correctly'
        e == d.closingBalance
        e == d.modifiedClosingBalance

        when: 'I add another credit memo'
        c = new CreditMemo(
            total: 2 * (t ?: 0.0), paymentAmount: 2 * (pa ?: 0.0)
        )
        c.id = 458L
        d.creditMemos << c

        then: 'the closing balance is computed correctly'
        e * 3 == d.closingBalance
        e * 3 == d.modifiedClosingBalance

        when: 'I set total and payment amount'
        d.total = 30.2
        d.paymentAmount = 60.9

        then: 'the closing balance is computed correctly'
        e * 3 + 30.7 == d.closingBalance
        e * 3 == d.modifiedClosingBalance

        where:
        pa      | t         || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0000001
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0000001
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || -0.00001
        0.00001 | 0.0       || -0.00001
        0.00001 | 0.0000001 || -0.0000099
        0.00001 | 0.25      || 0.24999
        0.00001 | 450.47    || 450.46999
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.2499999
        0.25    | 0.25      || 0.0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.4699999
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Compute payable without credit memos'() {
        when: 'I create an empty dunning'
        def d = new Dunning()

        then: 'the payable amount is zero'
        0.0 == d.payable

        when: 'I set an empty credit memo list'
        d = new Dunning(creditMemos: [])

        then: 'the payable amount is zero'
        0.0 == d.payable

        when: 'I add an empty credit memo'
        d.creditMemos << new CreditMemo()

        then: 'the payable amount is zero'
        0.0 == d.payable

        when: 'I create an empty invoice'
        d = new Dunning(total: 45.76)

        then: 'the payable amount is zero'
        45.76 == d.payable
    }

    def 'Compute payable amount with credit memos'() {
        when: 'I create a non-empty dunning'
        def d = new Dunning(creditMemos: [])
        def c = new CreditMemo(total: t, paymentAmount: pa)
        c.id = 457L
        d.creditMemos << c

        then: 'the payable amount is computed correctly'
        -e == d.payable

        when: 'I add another credit memo'
        c = new CreditMemo(
            total: 2 * (t ?: 0.0), paymentAmount: 2 * (pa ?: 0.0)
        )
        c.id = 458L
        d.creditMemos << c

        then: 'the payable amount is computed correctly'
        -e * 3 == d.payable

        when: 'I set total'
        d.total = 30.2

        then: 'the payable amount is computed correctly'
        30.2 - e * 3 == d.payable

        where:
        pa      | t         || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0000001
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0000001
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || -0.00001
        0.00001 | 0.0       || -0.00001
        0.00001 | 0.0000001 || -0.0000099
        0.00001 | 0.25      || 0.24999
        0.00001 | 450.47    || 450.46999
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.2499999
        0.25    | 0.25      || 0.0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.4699999
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Get payment state color in special cases'() {
        when: 'I create an empty dunning'
        def d = new Dunning()

        then: 'payment state color is default'
        'default' == d.paymentStateColor

        when: 'I create a invoice with an empty stage'
        d = new Dunning(stage: new DunningStage())

        then: 'payment state color is default'
        'default' == d.paymentStateColor
    }

    def 'Get payment state color for default cases'() {
        when: 'I create a dunning with an empty stage'
        def stage = new DunningStage()
        stage.id = id
        def d = new Dunning(stage: stage)

        then: 'payment state color is default'
        color == d.paymentStateColor

        where:
        id      || color
        null    || 'default'    // unset dunning stage
        0L      || 'default'    // unset dunning stage
        1L      || 'default'    // invalid dunning stage
        2200L   || 'default'    // created
        2201L   || 'default'    // revised
        2204L   || 'blue'       // cashing
        2205L   || 'black'      // booked out
    }

    def 'Get payment state color for balance-dependent cases'() {
        given: 'a dunning with a balance'
        def d = new Dunning(paymentAmount: pa, total: t)

        when: 'I set stage "paid"'
        d.stage = new DunningStage()
        d.stage.id = 2203L
        d.dueDatePayment = null

        then: 'I get the correct color'
        color == d.paymentStateColor
        testPaymentDates d, color

        when: 'I set stage "cancelled"'
        d.stage = new DunningStage()
        d.stage.id = 2206L
        d.dueDatePayment = null

        then: 'I get the correct color'
        color == d.paymentStateColor
        testPaymentDates d, color

        when: 'I set stage "delivered"'
        d.stage = new DunningStage()
        d.stage.id = 2202L
        d.dueDatePayment = null

        then: 'I get the correct color'
        'default' == d.paymentStateColor
        testPaymentDates d, 'default'

        where:
        pa          | t         || color
        null        | null      || 'green'
        null        | 0.0       || 'green'
        null        | 0.0000001 || 'default'
        null        | 5.867     || 'default'
        null        | 4795.492  || 'default'
        0.0         | null      || 'green'
        0.0         | 0.0       || 'green'
        0.0         | 0.0000001 || 'default'
        0.0         | 5.867     || 'default'
        0.0         | 4795.492  || 'default'
        0.0000001   | null      || 'green'
        0.0000001   | 0.0       || 'green'
        0.0000001   | 0.0000001 || 'green'
        0.0000001   | 5.867     || 'default'
        0.0000001   | 4795.492  || 'default'
        5.867       | null      || 'green'
        5.867       | 0.0       || 'green'
        5.867       | 0.0000001 || 'green'
        5.867       | 5.867     || 'green'
        5.867       | 4795.492  || 'default'
        4795.492    | null      || 'green'
        4795.492    | 0.0       || 'green'
        4795.492    | 0.0000001 || 'green'
        4795.492    | 5.867     || 'green'
        4795.492    | 4795.492  || 'green'
    }

    def 'Level must not be null'() {
        given: 'a quite valid dunning'
        def d = new Dunning(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            invoice: new Invoice(),
            stage: new DunningStage(),
            dueDatePayment: new Date()
        )

        when: 'I set the level'
        d.level = new DunningLevel()

        then: 'the instance is valid'
        d.validate()

        when: 'I unset the level'
        d.level = null

        then: 'the instance is not valid'
        !d.validate()
    }

    def 'Stage must not be null'() {
        given: 'a quite valid dunning'
        def d = new Dunning(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            invoice: new Invoice(),
            level: new DunningLevel(),
            dueDatePayment: new Date()
        )

        when: 'I set the stage'
        d.stage = new DunningStage()

        then: 'the instance is valid'
        d.validate()

        when: 'I unset the stage'
        d.stage = null

        then: 'the instance is not valid'
        !d.validate()
    }

    def 'Due date of payment must not be null'() {
        given: 'a quite valid dunning'
        def d = new Dunning(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            invoice: new Invoice(),
            level: new DunningLevel(),
            stage: new DunningStage()
        )

        when: 'I set the date'
        d.dueDatePayment = new Date()

        then: 'the instance is valid'
        d.validate()

        when: 'I unset the date'
        d.dueDatePayment = null

        then: 'the instance is not valid'
        !d.validate()
    }

    def 'Payment amount must be positive or zero'() {
        given: 'a quite valid dunning'
        def d = new Dunning(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            invoice: new Invoice(),
            level: new DunningLevel(),
            stage: new DunningStage(),
            dueDatePayment: new Date()
        )

        when: 'I set the payment amount'
        d.paymentAmount = pa

        then: 'the instance is valid or not'
        v == d.validate()

        where:
        pa      || v
        null    || true
        0.00    || true
        0.00001 || true
        1       || true
        1.27543 || true
        4750.79 || true
        -0.0001 || false
        -1      || false
        -1750.7 || false
    }


    //-- Non-public methods ---------------------

    private void setCreditMemos(Invoice inst) {
        CreditMemo c = new CreditMemo(subject: 'Credit note A')
        c.id = 45L
        inst.creditMemos << c
        c = new CreditMemo(subject: 'Credit note B')
        c.id = 46L
        inst.creditMemos << c
    }

    private void setDunnings(Invoice inst) {
        Dunning d = new Dunning(subject: 'Dunning A')
        d.id = 45L
        inst.dunnings << d
        d = new Dunning(subject: 'Dunning B')
        d.id = 46L
        inst.dunnings << d
    }

    private void testPaymentDates(Dunning d, String color) {

        /*
         * Note: only some dates are tested because the current date obtained in
         * method Invoice.colorIndicatorByDate() is some milliseconds later than
         * date `d`.
         */

        Date date = new Date()
        d.dueDatePayment = date + 4
        assert color == d.paymentStateColor

        d.dueDatePayment = date + 3
        assert (color == 'default' ? 'yellow' : color) == d.paymentStateColor

        d.dueDatePayment = date + 1
        assert (color == 'default' ? 'yellow' : color) == d.paymentStateColor

        d.dueDatePayment = date - 1
        assert (color == 'default' ? 'orange' : color) == d.paymentStateColor

        d.dueDatePayment = date - 2
        assert (color == 'default' ? 'orange' : color) == d.paymentStateColor

        d.dueDatePayment = date - 4
        assert (color == 'default' ? 'red' : color) == d.paymentStateColor
    }
}
