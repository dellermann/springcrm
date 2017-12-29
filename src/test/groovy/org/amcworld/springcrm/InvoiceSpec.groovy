/*
 * InvoiceSpec.groovy
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


class InvoiceSpec extends Specification implements DomainUnitTest<Invoice> {

    //-- Feature Methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'the properties are initialized properly'
        0i == i.number
        'I' == i.type
        null == i.subject
        null == i.organization
        null == i.person
        null != i.docDate
        null == i.carrier
        null == i.shippingDate
        null == i.billingAddr
        null == i.shippingAddr
        null == i.headerText
        null == i.items
        null == i.footerText
        0.0 == i.discountPercent
        0.0 == i.discountAmount
        0.0 == i.shippingCosts
        0.0 == i.shippingTax
        0.0 == i.adjustment
        0.0 == i.total
        null == i.notes
        null == i.createUser
        null == i.dateCreated
        null == i.lastUpdated
        null == i.quote
        null == i.salesOrder
        null == i.creditMemos
        null == i.dunnings
        null == i.stage
        null == i.dueDatePayment
        null == i.paymentDate
        0.0 == i.paymentAmount
        null == i.paymentMethod
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty invoice'
        def i1 = new Invoice()

        when: 'I copy the invoice using the constructor'
        def i2 = new Invoice(i1)

        then: 'the properties are set properly'
        0i == i2.number
        'I' == i2.type
        null == i2.subject
        null != i2.docDate
        null == i2.carrier
        null == i2.shippingDate
        null == i2.billingAddr
        null == i2.shippingAddr
        null == i2.headerText
        null == i2.items
        null == i2.footerText
        0.0 == i2.discountPercent
        0.0 == i2.discountAmount
        0.0 == i2.shippingCosts
        0.0 == i2.shippingTax
        0.0 == i2.adjustment
        0.0 == i2.total
        null == i2.notes
        null == i2.createUser
        null == i2.dateCreated
        null == i2.lastUpdated
        null == i2.quote
        null == i2.salesOrder
        null == i2.creditMemos
        null == i2.dunnings
        null == i2.stage
        null == i2.dueDatePayment
        null == i2.paymentDate
        0.0 == i2.paymentAmount
        null == i2.paymentMethod
    }

    def 'Copy an invoice using constructor'() {
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

        and: 'an invoice with various properties'
        def i1 = new Invoice(
            number: 39999,
            subject: 'Test invoice',
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
                new TermsAndConditions(name: 'works')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated,
            quote: new Quote(),
            salesOrder: new SalesOrder(),
            creditMemos: [new CreditMemo(), new CreditMemo()],
            dunnings: [new Dunning(), new Dunning()],
            stage: new InvoiceStage(),
            dueDatePayment: dueDatePayment,
            paymentDate: paymentDate,
            paymentAmount: 457.98,
            paymentMethod: new PaymentMethod()
        )

        when: 'I copy the invoice using the constructor'
        def i2 = new Invoice(i1)

        then: 'some properties are the equal'
        i1.type == i2.type
        i1.subject == i2.subject
        !i1.billingAddr.is(i2.billingAddr)
        i1.billingAddr == i2.billingAddr
        !i1.shippingAddr.is(i2.shippingAddr)
        i1.shippingAddr == i2.shippingAddr
        i1.headerText == i2.headerText
        !i1.items.is(i2.items)
        i1.items == i2.items
        i1.footerText == i2.footerText
        i1.discountPercent == i2.discountPercent
        i1.discountAmount == i2.discountAmount
        i1.shippingCosts == i2.shippingCosts
        i1.shippingTax == i2.shippingTax
        i1.adjustment == i2.adjustment
        i1.total == i2.total
        i1.notes == i2.notes
        !i1.termsAndConditions.is(i2.termsAndConditions)
        i1.termsAndConditions == i2.termsAndConditions

        and: 'some instances are the same'
        i1.organization.is i2.organization
        i1.person.is i2.person
        i1.quote.is i2.quote
        i1.salesOrder.is i2.salesOrder

        and: 'some properties are set to new values'
        null != i2.docDate
        i1.docDate != i2.docDate

        and: 'some properties are unset'
        null == i2.id
        0i == i2.number
        null == i2.carrier
        null == i2.shippingDate
        null == i2.createUser
        null == i2.dateCreated
        null == i2.lastUpdated
        null == i2.creditMemos
        null == i2.dunnings
        null == i2.stage
        null == i2.dueDatePayment
        null == i2.paymentDate
        0.0 == i2.paymentAmount
        null == i2.paymentMethod
    }

    def 'Copy a quote using constructor'() {
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
        def q = new Quote(
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
                new TermsAndConditions(name: 'works')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated,
            salesOrders: [],
            invoices: [],
            stage: new QuoteStage(),
            validUntil: validUntilDate
        )

        and: 'some dependent values'
        setInvoices q
        setSalesOrders q

        when: 'I copy the quote using the constructor'
        def i = new Invoice(q)

        then: 'some properties are the equal'
        q.subject == i.subject
        !q.billingAddr.is(i.billingAddr)
        q.billingAddr == i.billingAddr
        !q.shippingAddr.is(i.shippingAddr)
        q.shippingAddr == i.shippingAddr
        !q.items.is(i.items)
        q.items == i.items
        q.discountPercent == i.discountPercent
        q.discountAmount == i.discountAmount
        q.shippingCosts == i.shippingCosts
        q.shippingTax == i.shippingTax
        q.adjustment == i.adjustment
        q.total == i.total
        q.notes == i.notes
        !q.termsAndConditions.is(i.termsAndConditions)
        q.termsAndConditions == i.termsAndConditions

        and: 'some instances are the same'
        q.organization.is i.organization
        q.person.is i.person
        q.is i.quote

        and: 'some properties are set to new values'
        'I' == i.type
        null != i.docDate
        q.docDate != i.docDate
        3 == q.invoices.size()
        q.invoices.contains(i)

        and: 'some properties are unset'
        null == i.id
        0i == i.number
        null == i.carrier
        null == i.shippingDate
        null == i.createUser
        null == i.dateCreated
        null == i.lastUpdated
        null == i.salesOrder
        null == i.stage
        null == i.dueDatePayment
        null == i.paymentDate
        0.0 == i.paymentAmount
        null == i.paymentMethod

        and: 'some original values are unmodified'
        2 == q.salesOrders.size()
    }

    def 'Copy a sales order using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date dueDate = docDate + 60
        Date deliveryDate = docDate + 120

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'a sales order with various properties'
        def so = new SalesOrder(
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
                new TermsAndConditions(name: 'works')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated,
            quote: new Quote(),
            invoices: [],
            stage: new SalesOrderStage(),
            dueDate: dueDate,
            deliveryDate: deliveryDate
        )

        and: 'some dependent values'
        setInvoices so

        when: 'I copy the sales order using the constructor'
        def i = new Invoice(so)

        then: 'some properties are the equal'
        so.subject == i.subject
        !so.billingAddr.is(i.billingAddr)
        so.billingAddr == i.billingAddr
        !so.shippingAddr.is(i.shippingAddr)
        so.shippingAddr == i.shippingAddr
        !so.items.is(i.items)
        so.items == i.items
        so.discountPercent == i.discountPercent
        so.discountAmount == i.discountAmount
        so.shippingCosts == i.shippingCosts
        so.shippingTax == i.shippingTax
        so.adjustment == i.adjustment
        so.total == i.total
        so.notes == i.notes
        !so.termsAndConditions.is(i.termsAndConditions)
        so.termsAndConditions == i.termsAndConditions

        and: 'some instances are the same'
        so.organization.is i.organization
        so.person.is i.person
        so.is i.salesOrder

        and: 'some properties are set to new values'
        'I' == i.type
        null != i.docDate
        so.docDate != i.docDate
        3 == so.invoices.size()
        so.invoices.contains(i)

        and: 'some properties are unset'
        null == i.id
        0i == i.number
        null == i.carrier
        null == i.shippingDate
        null == i.createUser
        null == i.dateCreated
        null == i.lastUpdated
        null == i.quote
        null == i.stage
        null == i.dueDatePayment
        null == i.paymentDate
        0.0 == i.paymentAmount
        null == i.paymentMethod
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty invoice'
        def i = new Invoice()

        when: 'I set the decimal values to null'
        i.paymentAmount = null

        then: 'all decimal values are never null'
        0.0 == i.paymentAmount

        when: 'I create an invoice with null values'
        i = new Invoice(paymentAmount: null)

        then: 'all decimal values are never null'
        0.0 == i.paymentAmount
    }

    def 'Compute balance'() {
        given: 'an empty invoice'
        def i = new Invoice()

        and: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2
        i.userService = userService

        when: 'I set discrete values for total and payment amount'
        i.total = t
        i.paymentAmount = pa

        then: 'I get the correct balance'
        e == i.balance

        where:
        t       | pa        || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || -0.0
        0.00001 | 0.0       || -0.0
        0.00001 | 0.0000001 || -0.0
        0.00001 | 0.25      || 0.25
        0.00001 | 450.47    || 450.47
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.25
        0.25    | 0.25      || 0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.47
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Get the balance color'() {
        given: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        when: 'I create an empty invoice'
        def i = new Invoice()
        i.userService = userService

        then: 'I get the default color'
        'default' == i.balanceColor

        when: 'I set a zero balance'
        i = new Invoice(paymentAmount: 25.6669, total: 25.666900000)
        i.userService = userService

        then: 'I get the default color'
        'default' == i.balanceColor

        when: 'I set a positive balance'
        i = new Invoice(paymentAmount: 25.7, total: 25.66666669)
        i.userService = userService

        then: 'I get the default color'
        'green' == i.balanceColor

        when: 'I set a negative balance'
        i = new Invoice(paymentAmount: 25.7, total: 25.70000001)
        i.userService = userService

        then: 'I get the default color'
        'default' == i.balanceColor
    }

    def 'Compute (modified) closing balance without credit memos'() {
        given: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        and: 'a mocked credit memo'
        def c = new CreditMemo()
        c.userService = userService

        when: 'I create an empty invoice'
        def i = new Invoice()
        i.userService = userService

        then: 'the closing balance is zero'
        0.0 == i.closingBalance
        0.0 == i.modifiedClosingBalance

        when: 'I set an empty credit memo list'
        i = new Invoice(creditMemos: [])
        i.userService = userService

        then: 'the closing balance is zero'
        0.0 == i.closingBalance
        0.0 == i.modifiedClosingBalance

        when: 'I add an empty credit memo'
        i.creditMemos << c

        then: 'the closing balance is zero'
        0.0 == i.closingBalance
        0.0 == i.modifiedClosingBalance
    }

    def 'Compute (modified) closing balance with credit memos'() {
        when: 'I create a non-empty invoice'
        def i = new Invoice(creditMemos: [])
        def c = new CreditMemo(total: t, paymentAmount: pa)
        c.id = 457L
        i.creditMemos << c

        and: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2
        i.userService = userService
        c.userService = userService

        then: 'the closing balance is computed correctly'
        e == i.closingBalance
        e == i.modifiedClosingBalance

        when: 'I add another credit memo'
        c = new CreditMemo(
            total: 2 * (t ?: 0.0), paymentAmount: 2 * (pa ?: 0.0)
        )
        c.userService = userService
        c.id = 458L
        i.creditMemos << c

        then: 'the closing balance is computed correctly'
        e * 3 == i.closingBalance
        e * 3 == i.modifiedClosingBalance

        when: 'I set total and payment amount'
        i.total = 30.2
        i.paymentAmount = 60.9

        then: 'the closing balance is computed correctly'
        e * 3 + 30.7 == i.closingBalance
        e * 3 == i.modifiedClosingBalance

        where:
        pa      | t         || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || 0.0
        0.00001 | 0.0       || 0.0
        0.00001 | 0.0000001 || 0.0
        0.00001 | 0.25      || 0.25
        0.00001 | 450.47    || 450.47
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.25
        0.25    | 0.25      || 0.0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.47
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Compute payable amount without credit memos'() {
        given: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        and: 'a credit memo'
        def c = new CreditMemo()
        c.userService = userService

        when: 'I create an empty invoice'
        def i = new Invoice()
        i.userService = userService

        then: 'the payable amount is zero'
        0.0 == i.payable

        when: 'I set an empty credit memo list'
        i = new Invoice(creditMemos: [])
        i.userService = userService

        then: 'the payable amount is zero'
        0.0 == i.payable

        when: 'I add an empty credit memo'
        i.creditMemos << c

        then: 'the payable amount is zero'
        0.0 == i.payable

        when: 'I create an empty invoice'
        i = new Invoice(total: 45.76)
        i.userService = userService

        then: 'the payable amount is zero'
        45.76 == i.payable
    }

    def 'Compute payable amount with credit memos'() {
        given: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        when: 'I create a non-empty invoice'
        def i = new Invoice(creditMemos: [])
        i.userService = userService
        def c = new CreditMemo(total: t, paymentAmount: pa)
        c.userService = userService
        c.id = 457L
        i.creditMemos << c

        then: 'the payable amount is computed correctly'
        -e == i.payable

        when: 'I add another credit memo'
        c = new CreditMemo(
            total: 2 * (t ?: 0.0), paymentAmount: 2 * (pa ?: 0.0)
        )
        c.userService = userService
        c.id = 458L
        i.creditMemos << c

        then: 'the payable amount is computed correctly'
        -e * 3 == i.payable

        when: 'I set total'
        i.total = 30.2

        then: 'the payable amount is computed correctly'
        30.2 - e * 3 == i.payable

        where:
        pa      | t         || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0
        null    | 0.25      || 0.25
        null    | 450.47    || 450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0
        0.0     | 0.25      || 0.25
        0.0     | 450.47    || 450.47
        0.00001 | null      || 0.0
        0.00001 | 0.0       || 0.0
        0.00001 | 0.0000001 || 0.0
        0.00001 | 0.25      || 0.25
        0.00001 | 450.47    || 450.47
        0.25    | null      || -0.25
        0.25    | 0.0       || -0.25
        0.25    | 0.0000001 || -0.25
        0.25    | 0.25      || 0.0
        0.25    | 450.47    || 450.22
        450.47  | null      || -450.47
        450.47  | 0.0       || -450.47
        450.47  | 0.0000001 || -450.47
        450.47  | 0.25      || -450.22
        450.47  | 450.47    || 0.0
    }

    def 'Get payment state color in special cases'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'payment state color is default'
        'default' == i.paymentStateColor

        when: 'I create a invoice with an empty stage'
        i = new Invoice(stage: new InvoiceStage())

        then: 'payment state color is default'
        'default' == i.paymentStateColor
    }

    def 'Get payment state color for default cases'() {
        when: 'I create an invoice with an empty stage'
        def stage = new InvoiceStage()
        stage.id = id
        def i = new Invoice(stage: stage)

        then: 'payment state color is default'
        color == i.paymentStateColor

        where:
        id      || color
        null    || 'default'    // unset invoice stage
        0L      || 'default'    // unset invoice stage
        1L      || 'default'    // invalid invoice stage
        900L    || 'default'    // created
        901L    || 'default'    // revised
        904L    || 'purple'     // reminded
        905L    || 'blue'       // cashing
        906L    || 'black'      // booked out
    }

    def 'Get payment state color for balance-dependent cases'() {
        given: 'an invoice with a balance'
        def i = new Invoice(paymentAmount: pa, total: t)

        and: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2
        i.userService = userService

        when: 'I set stage "paid"'
        i.stage = new InvoiceStage()
        i.stage.id = 903L
        i.dueDatePayment = null

        then: 'I get the correct color'
        color == i.paymentStateColor
        testPaymentDates i, color

        when: 'I set stage "cancelled"'
        i.stage = new InvoiceStage()
        i.stage.id = 907L
        i.dueDatePayment = null

        then: 'I get the correct color'
        color == i.paymentStateColor
        testPaymentDates i, color

        when: 'I set stage "delivered"'
        i.stage = new InvoiceStage()
        i.stage.id = 902L
        i.dueDatePayment = null

        then: 'I get the correct color'
        'default' == i.paymentStateColor
        testPaymentDates i, 'default'

        where:
        pa          | t         || color
        null        | null      || 'green'
        null        | 0.0       || 'green'
        null        | 0.0000001 || 'green'
        null        | 5.867     || 'default'
        null        | 4795.492  || 'default'
        0.0         | null      || 'green'
        0.0         | 0.0       || 'green'
        0.0         | 0.0000001 || 'green'
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

    def 'Compute turnover'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'I get the correct turnover'
        0.0 == i.turnover

        when: 'I create an invoice with an empty list'
        i = new Invoice(items: [])

        then: 'I get the correct turnover'
        0.0 == i.turnover

        when: 'I create an invoice with a list of other items'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnover

        when: 'I create an invoice with a list of works'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnover

        when: 'I create an invoice with a list of some products'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnover

        when: 'I create an invoice with a list of products'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnover
    }

    def 'Compute turnover of products'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'I get the correct turnover'
        0.0 == i.turnoverProducts

        when: 'I create an invoice with an empty list'
        i = new Invoice(items: [])

        then: 'I get the correct turnover'
        0.0 == i.turnoverProducts

        when: 'I create an invoice with a list of other items'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        0.0 == i.turnoverProducts

        when: 'I create an invoice with a list of works'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        0.0 == i.turnoverProducts

        when: 'I create an invoice with a list of some products'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        28.45 == i.turnoverProducts

        when: 'I create an invoice with a list of products'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnoverProducts
    }

    def 'Compute turnover of works'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'I get the correct turnover'
        0.0 == i.turnoverWorks

        when: 'I create an invoice with an empty list'
        i = new Invoice(items: [])

        then: 'I get the correct turnover'
        0.0 == i.turnoverWorks

        when: 'I create an invoice with a list of other items'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        0.0 == i.turnoverWorks

        when: 'I create an invoice with a list of products'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        0.0 == i.turnoverWorks

        when: 'I create an invoice with a list of some works'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        28.45 == i.turnoverWorks

        when: 'I create an invoice with a list of works'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnoverWorks
    }

    def 'Compute turnover of other items'() {
        when: 'I create an empty invoice'
        def i = new Invoice()

        then: 'I get the correct turnover'
        0.0 == i.turnoverOtherSalesItems

        when: 'I create an invoice with an empty list'
        i = new Invoice(items: [])

        then: 'I get the correct turnover'
        0.0 == i.turnoverOtherSalesItems

        when: 'I create an invoice with a list of some products and works'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        0.0 == i.turnoverOtherSalesItems

        when: 'I create an invoice with a list of some other items'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        28.45 == i.turnoverOtherSalesItems

        when: 'I create an invoice with a list of other items'
        i = new Invoice(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        29.95 == i.turnoverOtherSalesItems
    }

    def 'Stage must not be null'() {
        given: 'a quite valid invoice'
        def i = new Invoice(
            number: 39999,
            type: 'X',
            subject: 'Works',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            dueDatePayment: new Date()
        )

        when: 'I set the stage'
        i.stage = new InvoiceStage()

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the stage'
        i.stage = null

        then: 'the instance is not valid'
        !i.validate()
    }

    def 'Due date of payment must not be null'() {
        given: 'a quite valid invoice'
        def i = new Invoice(
            number: 39999,
            type: 'X',
            subject: 'Works',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            stage: new InvoiceStage()
        )

        when: 'I set the date'
        i.dueDatePayment = new Date()

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the date'
        i.dueDatePayment = null

        then: 'the instance is not valid'
        !i.validate()
    }

    def 'Payment amount must be positive or zero'() {
        given: 'a quite valid invoice'
        def i = new Invoice(
            number: 39999,
            type: 'X',
            subject: 'Works',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            stage: new InvoiceStage(),
            dueDatePayment: new Date()
        )

        when: 'I set the payment amount'
        i.paymentAmount = pa

        then: 'the instance is valid or not'
        v == i.validate()

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

    private void setInvoices(def inst) {
        Invoice iv = new Invoice(subject: 'Invoice A')
        iv.id = 45L
        inst.invoices << iv
        iv = new Invoice(subject: 'Invoice B')
        iv.id = 46L
        inst.invoices << iv
    }

    private void setSalesOrders(def inst) {
        SalesOrder so = new SalesOrder(subject: 'Sales order A')
        so.id = 740L
        inst.salesOrders << so
        so = new SalesOrder(subject: 'Sales order B')
        so.id = 741L
        inst.salesOrders << so
    }

    private void testPaymentDates(Invoice i, String color) {

        /*
         * Note: only some dates are tested because the current date obtained in
         * method Invoice.colorIndicatorByDate() is some milliseconds later than
         * date `d`.
         */

        Date d = new Date()
        i.dueDatePayment = d + 4
        assert color == i.paymentStateColor

        i.dueDatePayment = d + 3
        assert (color == 'default' ? 'yellow' : color) == i.paymentStateColor

        i.dueDatePayment = d + 1
        assert (color == 'default' ? 'yellow' : color) == i.paymentStateColor

        i.dueDatePayment = d - 1
        assert (color == 'default' ? 'orange' : color) == i.paymentStateColor

        i.dueDatePayment = d - 2
        assert (color == 'default' ? 'orange' : color) == i.paymentStateColor

        i.dueDatePayment = d - 4
        assert (color == 'default' ? 'red' : color) == i.paymentStateColor
    }
}
