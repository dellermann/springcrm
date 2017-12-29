/*
 * CreditMemoSpec.groovy
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


class CreditMemoSpec extends Specification
    implements DomainUnitTest<CreditMemo>
{

    //-- Feature Methods -----------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        then: 'the properties are initialized properly'
        0i == c.number
        'C' == c.type
        null == c.subject
        null == c.organization
        null == c.person
        null != c.docDate
        null == c.carrier
        null == c.shippingDate
        null == c.billingAddr
        null == c.shippingAddr
        null == c.headerText
        null == c.items
        null == c.footerText
        0.0 == c.discountPercent
        0.0 == c.discountAmount
        0.0 == c.shippingCosts
        0.0 == c.shippingTax
        0.0 == c.adjustment
        0.0 == c.total
        null == c.notes
        null == c.createUser
        null == c.dateCreated
        null == c.lastUpdated
        null == c.invoice
        null == c.dunning
        null == c.stage
        null == c.paymentDate
        0.0 == c.paymentAmount
        null == c.paymentMethod
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty credit memo'
        def c1 = new CreditMemo()

        when: 'I copy the credit memo using the constructor'
        def c2 = new CreditMemo(c1)

        then: 'the properties are set properly'
        0i == c2.number
        'C' == c2.type
        null == c2.subject
        null != c2.docDate
        null == c2.carrier
        null == c2.shippingDate
        null == c2.billingAddr
        null == c2.shippingAddr
        null == c2.headerText
        null == c2.items
        null == c2.footerText
        0.0 == c2.discountPercent
        0.0 == c2.discountAmount
        0.0 == c2.shippingCosts
        0.0 == c2.shippingTax
        0.0 == c2.adjustment
        0.0 == c2.total
        null == c2.notes
        null == c2.createUser
        null == c2.dateCreated
        null == c2.lastUpdated
        null == c2.invoice
        null == c2.dunning
        null == c2.stage
        null == c2.paymentDate
        0.0 == c2.paymentAmount
        null == c2.paymentMethod
    }

    def 'Copy a credit memo using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1
        Date paymentDate = docDate + 30

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'a credit memo with various properties'
        def c1 = new CreditMemo(
            number: 39999,
            subject: 'Test credit memo',
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
            dunning: new Dunning(),
            stage: new CreditMemoStage(),
            paymentDate: paymentDate,
            paymentAmount: 457.98,
            paymentMethod: new PaymentMethod()
        )

        when: 'I copy the credit memo using the constructor'
        def c2 = new CreditMemo(c1)

        then: 'some properties are the equal'
        c1.type == c2.type
        c1.subject == c2.subject
        !c1.billingAddr.is(c2.billingAddr)
        c1.billingAddr == c2.billingAddr
        !c1.shippingAddr.is(c2.shippingAddr)
        c1.shippingAddr == c2.shippingAddr
        c1.headerText == c2.headerText
        !c1.items.is(c2.items)
        c1.items == c2.items
        c1.footerText == c2.footerText
        c1.discountPercent == c2.discountPercent
        c1.discountAmount == c2.discountAmount
        c1.shippingCosts == c2.shippingCosts
        c1.shippingTax == c2.shippingTax
        c1.adjustment == c2.adjustment
        c1.total == c2.total
        c1.notes == c2.notes
        !c1.termsAndConditions.is(c2.termsAndConditions)
        c1.termsAndConditions == c2.termsAndConditions

        and: 'some instances are the same'
        c1.organization.is c2.organization
        c1.person.is c2.person
        c1.invoice.is c2.invoice
        c1.dunning.is c2.dunning

        and: 'some properties are set to new values'
        null != c2.docDate
        c1.docDate != c2.docDate

        and: 'some properties are unset'
        null == c2.id
        0i == c2.number
        null == c2.carrier
        null == c2.shippingDate
        null == c2.createUser
        null == c2.dateCreated
        null == c2.lastUpdated
        null == c2.stage
        null == c2.paymentDate
        0.0 == c2.paymentAmount
        null == c2.paymentMethod
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

        and: 'a credit memo with various properties'
        def i = new Invoice(
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
                new TermsAndConditions(name: 'services')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated,
            quote: new Quote(),
            salesOrder: new SalesOrder(),
            invoices: [],
            dunnings: [],
            stage: new InvoiceStage(),
            dueDatePayment: dueDatePayment,
            paymentDate: paymentDate,
            paymentAmount: 457.98,
            paymentMethod: new PaymentMethod()
        )

        when: 'I copy the invoice using the constructor'
        def c = new CreditMemo(i)

        then: 'some properties are the equal'
        i.subject == c.subject
        !i.billingAddr.is(c.billingAddr)
        i.billingAddr == c.billingAddr
        !i.shippingAddr.is(c.shippingAddr)
        i.shippingAddr == c.shippingAddr
        !i.items.is(c.items)
        i.items == c.items
        i.discountPercent == c.discountPercent
        i.discountAmount == c.discountAmount
        i.shippingCosts == c.shippingCosts
        i.shippingTax == c.shippingTax
        i.adjustment == c.adjustment
        i.total == c.total
        i.notes == c.notes
        !i.termsAndConditions.is(c.termsAndConditions)
        i.termsAndConditions == c.termsAndConditions

        and: 'some instances are the same'
        i.organization.is c.organization
        i.person.is c.person
        i.is c.invoice

        and: 'some properties are set to new values'
        'C' == c.type
        null != c.docDate
        i.docDate != c.docDate
        '' == c.headerText
        '' == c.footerText
        1 == i.creditMemos.size()
        c.is i.creditMemos[0]

        and: 'some properties are unset'
        null == c.id
        0i == c.number
        null == c.carrier
        null == c.shippingDate
        null == c.createUser
        null == c.dateCreated
        null == c.lastUpdated
        null == c.dunning
        null == c.stage
        null == c.paymentDate
        0.0 == c.paymentAmount
        null == c.paymentMethod
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

        and: 'a credit memo with various properties'
        def d = new Dunning(
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
            creditMemos: [],
            level: new DunningLevel(),
            stage: new DunningStage(),
            dueDatePayment: dueDatePayment,
            paymentDate: paymentDate,
            paymentAmount: 457.98,
            paymentMethod: new PaymentMethod()
        )

        when: 'I copy the dunning using the constructor'
        def c = new CreditMemo(d)

        then: 'some properties are the equal'
        d.subject == c.subject
        !d.billingAddr.is(c.billingAddr)
        d.billingAddr == c.billingAddr
        !d.shippingAddr.is(c.shippingAddr)
        d.shippingAddr == c.shippingAddr
        !d.items.is(c.items)
        d.items == c.items
        d.discountPercent == c.discountPercent
        d.discountAmount == c.discountAmount
        d.shippingCosts == c.shippingCosts
        d.shippingTax == c.shippingTax
        d.adjustment == c.adjustment
        d.total == c.total
        d.notes == c.notes
        !d.termsAndConditions.is(c.termsAndConditions)
        d.termsAndConditions == c.termsAndConditions

        and: 'some instances are the same'
        d.organization.is c.organization
        d.person.is c.person
        d.is c.dunning

        and: 'some properties are set to new values'
        'C' == c.type
        null != c.docDate
        d.docDate != c.docDate
        '' == c.headerText
        '' == c.footerText
        1 == d.creditMemos.size()
        c.is d.creditMemos[0]

        and: 'some properties are unset'
        null == c.id
        0i == c.number
        null == c.carrier
        null == c.shippingDate
        null == c.createUser
        null == c.dateCreated
        null == c.lastUpdated
        null == c.invoice
        null == c.stage
        null == c.paymentDate
        0.0 == c.paymentAmount
        null == c.paymentMethod
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty credit memo'
        def c = new CreditMemo()

        when: 'I set the decimal values to null'
        c.paymentAmount = null

        then: 'all decimal values are never null'
        0.0 == c.paymentAmount

        when: 'I create a credit memo with null values'
        c = new CreditMemo(paymentAmount: null)

        then: 'all decimal values are never null'
        0.0 == c.paymentAmount
    }

    def 'Compute balance'() {
        given: 'an empty credit memo'
        def c = new CreditMemo()

        and: 'a mocked user service'
        UserService userService = Mock()
        1 * userService.numFractionDigitsExt >> 2
        c.userService = userService

        when: 'I set discrete values for total and payment amount'
        c.total = t
        c.paymentAmount = pa

        then: 'I get the correct balance'
        e == c.balance

        where:
        t       | pa        || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0
        null    | 0.25      || -0.25
        null    | 450.47    || -450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0
        0.0     | 0.25      || -0.25
        0.0     | 450.47    || -450.47
        0.00001 | null      || 0.0
        0.00001 | 0.0       || 0.0
        0.00001 | 0.0000001 || 0.0
        0.00001 | 0.25      || -0.25
        0.00001 | 450.47    || -450.47
        0.25    | null      || 0.25
        0.25    | 0.0       || 0.25
        0.25    | 0.0000001 || 0.25
        0.25    | 0.25      || 0
        0.25    | 450.47    || -450.22
        450.47  | null      || 450.47
        450.47  | 0.0       || 450.47
        450.47  | 0.0000001 || 450.47
        450.47  | 0.25      || 450.22
        450.47  | 450.47    || 0.0
    }

    def 'Get closing balance color'() {
        given: 'a credit memo with a mocked invoice'
        def c = new CreditMemo(invoice: Mock(Invoice))
        c.invoice.getClosingBalance() >>> cb

        expect:
        color == c.balanceColor

        where:
        cb      || color
        0       || 'default'
        -0.0001 || 'green'
        -25.8   || 'green'
        0.00001 || 'red'
        4574.27 || 'red'
    }

    def 'Compute closing balance'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        and: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2
        c.userService = userService

        and: 'a mocked invoice'
        Invoice invoice = Mock()
        invoice.userService = userService

        and: 'a mocked dunning'
        Dunning dunning = Mock()
        dunning.userService = userService

        then: 'the closing balance is zero'
        0.0 == c.closingBalance

        when: 'I set an empty invoice'
        c = new CreditMemo(invoice: invoice)

        then: 'the closing balance is zero'
        0.0 == c.closingBalance

        when: 'I set a non-empty invoice'
        c = new CreditMemo(invoice: invoice)
        3 * c.invoice.getClosingBalance() >>> [-30.7, 45.8, 0.2584]

        then: 'the closing balance is the same as in the invoice'
        -30.7 == c.closingBalance
        45.8 == c.closingBalance
        0.2584 == c.closingBalance

        when: 'I set an empty dunning'
        c = new CreditMemo(dunning: dunning)

        then: 'the closing balance is zero'
        0.0 == c.closingBalance

        when: 'I set a non-empty dunning'
        c = new CreditMemo(dunning: dunning)
        3 * c.dunning.getClosingBalance() >>> [-30.7, 45.8, 0.2584]

        then: 'the closing balance is the same as in the invoice'
        -30.7 == c.closingBalance
        45.8 == c.closingBalance
        0.2584 == c.closingBalance
    }

    def 'Compute modified closing balance'() {
        given: 'a mocked user service'
        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        and: 'a mocked invoice'
        Invoice invoice = Mock()
        invoice.userService = userService

        and: 'a mocked dunning'
        Dunning dunning = Mock()
        dunning.userService = userService

        when: 'I create a credit memo'
        def c = new CreditMemo(total: t, paymentAmount: pa)
        c.userService = userService

        then: 'I get the correct modified closing balance'
        e == c.modifiedClosingBalance

        when: 'I create a credit memo with an empty invoice'
        c = new CreditMemo(total: t, paymentAmount: pa, invoice: invoice)
        c.userService = userService

        then: 'I get the correct modified closing balance'
        e == c.modifiedClosingBalance

        when: 'I create a credit memo with a non-empty invoice'
        c = new CreditMemo(total: t, paymentAmount: pa, invoice: invoice)
        c.userService = userService
        3 * c.invoice.getClosingBalance() >>> [-30.7, 45.8, 0.2584]

        then: 'I get the correct modified closing balance'
        e + 30.7 == c.modifiedClosingBalance
        e - 45.8 == c.modifiedClosingBalance
        e - 0.2584 == c.modifiedClosingBalance

        when: 'I create a credit memo with an empty dunning'
        c = new CreditMemo(total: t, paymentAmount: pa, dunning: dunning)
        c.userService = userService

        then: 'I get the correct modified closing balance'
        e == c.modifiedClosingBalance

        when: 'I create a credit memo with a non-empty dunning'
        c = new CreditMemo(total: t, paymentAmount: pa, dunning: dunning)
        c.userService = userService
        3 * c.dunning.getClosingBalance() >>> [-30.7, 45.8, 0.2584]

        then: 'I get the correct modified closing balance'
        e + 30.7 == c.modifiedClosingBalance
        e - 45.8 == c.modifiedClosingBalance
        e - 0.2584 == c.modifiedClosingBalance

        where:
        t       | pa        || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.0000001 || 0.0
        null    | 0.25      || -0.25
        null    | 450.47    || -450.47
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.0000001 || 0.0
        0.0     | 0.25      || -0.25
        0.0     | 450.47    || -450.47
        0.00001 | null      || 0.0
        0.00001 | 0.0       || 0.0
        0.00001 | 0.0000001 || 0.0
        0.00001 | 0.25      || -0.25
        0.00001 | 450.47    || -450.47
        0.25    | null      || 0.25
        0.25    | 0.0       || 0.25
        0.25    | 0.0000001 || 0.25
        0.25    | 0.25      || 0
        0.25    | 450.47    || -450.22
        450.47  | null      || 450.47
        450.47  | 0.0       || 450.47
        450.47  | 0.0000001 || 450.47
        450.47  | 0.25      || 450.22
        450.47  | 450.47    || 0.0
    }

    def 'Compute payable'() {
        when: 'I create a credit memo with a discrete total'
        def c = new CreditMemo(total: t)

        then: 'I get the correct payable value'
        e == c.payable

        where:
        t           || e
        null        || 0.0
        0.0         || 0.0
        0.000000001 || 0.000000001
        0.257       || 0.257
        15.4704     || 15.4704
        1047584.743 || 1047584.743
    }

    def 'Get payment state color in special cases'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        then: 'payment state color is default'
        'default' == c.paymentStateColor

        when: 'I create a credit memo with an empty stage'
        c = new CreditMemo(stage: new CreditMemoStage())

        then: 'payment state color is default'
        'default' == c.paymentStateColor
    }

    def 'Get payment state color for default cases'() {
        when: 'I create a credit memo with an empty stage'
        def stage = new CreditMemoStage()
        stage.id = id
        def c = new CreditMemo(stage: stage)

        then: 'payment state color is default'
        'default' == c.paymentStateColor

        where:
        id      || _
        null    || _            // unset credit memo stage
        0L      || _            // unset credit memo stage
        1L      || _            // invalid credit memo stage
        2500L   || _            // created
        2501L   || _            // revised
    }

    def 'Get payment state color for balance-dependent cases'() {
        given: 'a credit memo with a mocked invoice'
        def c = new CreditMemo(invoice: Mock(Invoice))
        3 * c.invoice.getClosingBalance() >> cb

        when: 'I set stage "delivered"'
        c.stage = new CreditMemoStage()
        c.stage.id = 2502L

        then: 'I get the correct color'
        color == c.paymentStateColor

        when: 'I set stage "paid"'
        c.stage = new CreditMemoStage()
        c.stage.id = 2503L

        then: 'I get the correct color'
        color == c.paymentStateColor

        when: 'I set stage "cancelled"'
        c.stage = new CreditMemoStage()
        c.stage.id = 2504L

        then: 'I get the correct color'
        color == c.paymentStateColor

        where:
        cb          || color
        0.0         || 'green'
        0.000000001 || 'green'
        0.275       || 'green'
        45794.47438 || 'green'
        -0.00000001 || 'red'
        -0.4753     || 'red'
        -476502.829 || 'red'
    }

    def 'Compute turnover of products'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        then: 'I get the correct turnover'
        0.0 == c.turnoverProducts

        when: 'I create a credit memo with an empty list'
        c = new CreditMemo(items: [])

        then: 'I get the correct turnover'
        0.0 == c.turnoverProducts

        when: 'I create a credit memo with a list of other items'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        0.0 == c.turnoverProducts

        when: 'I create a credit memo with a list of works'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        0.0 == c.turnoverProducts

        when: 'I create a credit memo with a list of some products'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        28.45 == c.turnoverProducts

        when: 'I create a credit memo with a list of products'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        29.95 == c.turnoverProducts
    }

    def 'Compute turnover of services'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        then: 'I get the correct turnover'
        0.0 == c.turnoverWorks

        when: 'I create a credit memo with an empty list'
        c = new CreditMemo(items: [])

        then: 'I get the correct turnover'
        0.0 == c.turnoverWorks

        when: 'I create a credit memo with a list of other items'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        0.0 == c.turnoverWorks

        when: 'I create a credit memo with a list of products'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Product(type: 'P'))
        ])

        then: 'I get the correct turnover'
        0.0 == c.turnoverWorks

        when: 'I create a credit memo with a list of some works'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        28.45 == c.turnoverWorks

        when: 'I create a credit memo with a list of works'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        29.95 == c.turnoverWorks
    }

    def 'Compute turnover of other items'() {
        when: 'I create an empty credit memo'
        def c = new CreditMemo()

        then: 'I get the correct turnover'
        0.0 == c.turnoverOtherSalesItems

        when: 'I create a credit memo with an empty list'
        c = new CreditMemo(items: [])

        then: 'I get the correct turnover'
        0.0 == c.turnoverOtherSalesItems

        when: 'I create a credit memo with a list of some products and works'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1, salesItem: new Work(type: 'S')),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1, salesItem: new Work(type: 'S'))
        ])

        then: 'I get the correct turnover'
        0.0 == c.turnoverOtherSalesItems

        when: 'I create a credit memo with a list of some other items'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75, salesItem: new Product(type: 'P')),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        28.45 == c.turnoverOtherSalesItems

        when: 'I create a credit memo with a list of other items'
        c = new CreditMemo(items: [
            new InvoicingItem(quantity: 0.5, unitPrice: 40.1),
            new InvoicingItem(quantity: 2, unitPrice: 0.75),
            new InvoicingItem(quantity: 4, unitPrice: 2.1)
        ])

        then: 'I get the correct turnover'
        29.95 == c.turnoverOtherSalesItems
    }

    def 'Stage must not be null'() {
        given: 'a quite valid credit memo'
        def c = new CreditMemo(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')]
        )

        when: 'I set the stage'
        c.stage = new CreditMemoStage()

        then: 'the instance is valid'
        c.validate()

        when: 'I unset the stage'
        c.stage = null

        then: 'the instance is not valid'
        !c.validate()
    }

    def 'Payment amount must be positive or zero'() {
        given: 'a quite valid credit memo'
        def c = new CreditMemo(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'm', name: 'cable')],
            stage: new CreditMemoStage()
        )

        when: 'I set the payment amount'
        c.paymentAmount = pa

        then: 'the instance is valid or not'
        v == c.validate()

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
}
