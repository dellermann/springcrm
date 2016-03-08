/*
 * PurchaseInvoiceSpec.groovy
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


@TestFor(PurchaseInvoice)
class PurchaseInvoiceSpec extends Specification {

	//-- Feature Methods --------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty purchase invoice'
        def pi = new PurchaseInvoice()

        then: 'the properties are initialized properly'
        null == pi.number
        null == pi.subject
        null == pi.vendorName
        null == pi.vendor
        null != pi.docDate
        null == pi.dueDate
        null == pi.stage
        null == pi.items
        0.0 == pi.discountPercent
        0.0 == pi.discountAmount
        0.0 == pi.shippingCosts
        0.0 == pi.shippingTax
        0.0 == pi.adjustment
        0.0 == pi.total
        null == pi.notes
        null == pi.documentFile
        null == pi.paymentDate
        0.0 == pi.paymentAmount
        null == pi.paymentMethod
        null == pi.dateCreated
        null == pi.lastUpdated
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty purchase invoice'
        def pi1 = new PurchaseInvoice()

        when: 'I copy the purchase invoice using the constructor'
        def pi2 = new PurchaseInvoice(pi1)

        then: 'the properties are set properly'
        null == pi2.number
        null == pi2.subject
        null == pi2.vendorName
        null == pi2.vendor
        null != pi2.docDate
        null == pi2.dueDate
        null == pi2.stage
        null == pi2.items
        0.0 == pi2.discountPercent
        0.0 == pi2.discountAmount
        0.0 == pi2.shippingCosts
        0.0 == pi2.shippingTax
        0.0 == pi2.adjustment
        0.0 == pi2.total
        null == pi2.notes
        null == pi2.documentFile
        null == pi2.paymentDate
        0.0 == pi2.paymentAmount
        null == pi2.paymentMethod
        null == pi2.dateCreated
        null == pi2.lastUpdated
    }

	def 'Copy a purchase invoice using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date dueDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1

        and: 'an organization'
        def organization = new Organization(name: 'Plumbing inc.')

        and: 'a purchase invoice with various properties'
        PurchaseInvoice p1 = new PurchaseInvoice(
            number: 225,
            subject: 'International delivery',
            vendor: organization,
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: docDate,
            dueDate: dueDate,
            stage: new PurchaseInvoiceStage(),
            items: [
                new PurchaseInvoiceItem(
                    quantity: 5, unit: 'kg', name: 'salt',
                    description: 'Fair trade salt from Egypt',
                    unitPrice: 2, tax: 17
                ),
                new PurchaseInvoiceItem(
                    quantity: 3, unit: 'kg', name: 'sugar',
                    description: 'Fair trade sugar from Uganda',
                    unitPrice: 3, tax: 17
                ),
                new PurchaseInvoiceItem(
                    quantity: 1, unit: 'piece', name: 'bicycle',
                    description: 'Bicycle from the Netherlands',
                    unitPrice: 633, tax: 17
                )
            ],
            discountPercent: 0,
            discountAmount: 0,
            shippingCosts: 15,
            shippingTax: 7,
            adjustment: 0.05,
            total: 495.0,
            notes: 'shipping not including',
            documentFile: new DataFile(),
            paymentDate: new Date(),
            paymentAmount: 1250.0,
            paymentMethod: new PaymentMethod(),
            dateCreated: new Date(),
            lastUpdated: new Date()
        )

        when: 'I copy the purchase invoice using the constructor'
        def p2 = new PurchaseInvoice(p1)

        then: 'some properties are the equal'
        p2.number == p1.number
        p2.subject == p1.subject
        p2.vendorName == p1.vendorName
        !p1.items.is(p2.items)
        p1.items == p2.items
        p2.discountPercent == p1.discountPercent
        p2.discountAmount == p1.discountAmount
        p2.shippingCosts == p1.shippingCosts
        p2.shippingTax == p1.shippingTax
        p2.adjustment == p1.adjustment
        p2.total == p1.total
        p2.notes == p1.notes

        and: 'some instances are the same'
        p1.vendor.is p2.vendor

        and: 'some properties are set to new values'
        null != p2.docDate
        p1.docDate != p2.docDate
        0.0 == p2.paymentAmount

        and: 'some properties are unset'
        null == p2.dueDate
        null == p2.stage
        null == p2.documentFile
        null == p2.paymentDate
        null == p2.paymentMethod
        null == p2.dateCreated
        null == p2.lastUpdated
	}

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty purchase invoice'
        def pi = new PurchaseInvoice()

        when: 'I set the decimal values to null'
        pi.adjustment = null
        pi.discountAmount = null
        pi.discountPercent = null
        pi.shippingCosts = null
        pi.shippingTax = null
        pi.total = null
        pi.paymentAmount = null

        then: 'all decimal values are never null'
        0.0 == pi.adjustment
        0.0 == pi.discountAmount
        0.0 == pi.discountPercent
        0.0 == pi.shippingCosts
        0.0 == pi.shippingTax
        0.0 == pi.total
        0.0 == pi.paymentAmount

        when: 'I create an invoicing transaction with null values'
        pi = new PurchaseInvoice(
            adjustment: null, discountAmount: null, discountPercent: null,
            shippingCosts: null, shippingTax: null, total: null,
            paymentAmount: null
        )

        then: 'all decimal values are never null'
        0.0 == pi.adjustment
        0.0 == pi.discountAmount
        0.0 == pi.discountPercent
        0.0 == pi.shippingCosts
        0.0 == pi.shippingTax
        0.0 == pi.total
        0.0 == pi.paymentAmount
    }

    def 'Compute balance'() {
        given: 'an empty purchase invoice'
        def pi = new PurchaseInvoice()

        when: 'I set discrete values for total and payment amount'
        pi.total = t
        pi.paymentAmount = pa

        then: 'I get the correct balance'
        e == pi.balance

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
        when: 'I create an empty purchase invoice'
        def pi = new PurchaseInvoice()

        then: 'I get the default color'
        'default' == pi.balanceColor

        when: 'I set a zero balance'
        pi = new PurchaseInvoice(paymentAmount: 25.6669, total: 25.666900000)

        then: 'I get the default color'
        'default' == pi.balanceColor

        when: 'I set a positive balance'
        pi = new PurchaseInvoice(paymentAmount: 25.7, total: 25.66666669)

        then: 'I get the default color'
        'green' == pi.balanceColor

        when: 'I set a negative balance'
        pi = new PurchaseInvoice(paymentAmount: 25.7, total: 25.70000001)

        then: 'I get the default color'
        'red' == pi.balanceColor
    }

    def 'Compute discount percent amount'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice(
            items: [
                new PurchaseInvoiceItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new PurchaseInvoiceItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new PurchaseInvoiceItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I set a discrete percentage value'
        pi.discountPercent = d

        then: 'I get the correct discount amount'
        e == pi.discountPercentAmount

        where:
           d    || e
        null    ||   0.0
           0    ||   0.0
           2    ||   8.522381
          10    ||  42.611905
          15.5  ||  66.04845275
          25.78 || 109.85349109
    }

    def 'Get payment state color in special cases'() {
        when: 'I create an empty purchase invoice'
        def pi = new PurchaseInvoice()

        then: 'payment state color is default'
        'default' == pi.paymentStateColor

        when: 'I create a purchase invoice with an empty stage'
        pi = new PurchaseInvoice(stage: new PurchaseInvoiceStage())

        then: 'payment state color is default'
        'default' == pi.paymentStateColor
    }

    def 'Get payment state color for default cases'() {
        when: 'I create an invoice with an empty stage'
        def stage = new PurchaseInvoiceStage()
        stage.id = id
        def pi = new PurchaseInvoice(stage: stage)

        then: 'payment state color is default'
        color == pi.paymentStateColor

        where:
        id      || color
        null    || 'default'    // unset purchase invoice stage
        0L      || 'default'    // unset purchase invoice stage
        1L      || 'default'    // invalid purchase invoice stage
        2100L   || 'default'    // income
        2101L   || 'default'    // revised
        2103L   || 'purple'     // rejected
    }

    def 'Get payment state color for balance-dependent cases'() {
        given: 'a purchase invoice with a balance'
        def pi = new PurchaseInvoice(paymentAmount: pa, total: t)

        when: 'I set stage "paid"'
        pi.stage = new PurchaseInvoiceStage()
        pi.stage.id = 2102L
        pi.dueDate = null

        then: 'I get the correct color'
        color == pi.paymentStateColor
        testPaymentDates pi, color

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

    def 'Get the shipping costs gross'() {
        given:
        def pi = new PurchaseInvoice(shippingCosts: s, shippingTax: t)

        expect:
        e == pi.shippingCostsGross

        where:
        s       | t         || e
        null    | null      || 0.0
        null    | 0.0       || 0.0
        null    | 0.25      || 0.0
        null    | 5         || 0.0
        null    | 8.78      || 0.0
        null    | 19        || 0.0
        0.0     | null      || 0.0
        0.0     | 0.0       || 0.0
        0.0     | 0.25      || 0.0
        0.0     | 5         || 0.0
        0.0     | 8.78      || 0.0
        0.0     | 19        || 0.0
        0.25    | null      || 0.25
        0.25    | 0.0       || 0.25
        0.25    | 0.25      || 0.250625
        0.25    | 5         || 0.2625
        0.25    | 8.78      || 0.27195
        0.25    | 19        || 0.2975
        5       | null      || 5
        5       | 0.0       || 5
        5       | 0.25      || 5.0125
        5       | 5         || 5.25
        5       | 8.78      || 5.439
        5       | 19        || 5.95
        1749.36 | null      || 1749.36
        1749.36 | 0.0       || 1749.36
        1749.36 | 0.25      || 1753.7334
        1749.36 | 5         || 1836.828
        1749.36 | 8.78      || 1902.953808
        1749.36 | 19        || 2081.7384
    }

    def 'Get the subtotal gross without items'() {
        when: 'I create an empty invoicing transaction'
        def pi = new PurchaseInvoice()

        then: 'I get a subtotal gross as zero'
        0.0 == pi.subtotalGross

        when: 'I set the items to an empty list'
        pi.items = []

        then: 'I get a subtotal gross as zero'
        0.0 == pi.subtotalGross
    }

    def 'Get the subtotal gross with items'() {
        given: 'an empty purchase invoice'
        def pi = new PurchaseInvoice(items: [])

        when: 'I add an item'
        pi.items << new PurchaseInvoiceItem(quantity: q, unitPrice: up, tax: t)

        then: 'I get the correct subtotal gross'
        s == pi.subtotalGross

        when: 'I add another item with double quantity'
        pi.items << new PurchaseInvoiceItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'I get the correct subtotal gross'
        (3 * s) == pi.subtotalGross

        when: 'I add another item with triple unit price'
        pi.items << new PurchaseInvoiceItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'I get the correct subtotal gross'
        (6 * s) == pi.subtotalGross

        when: 'I set shipping costs'
        pi.shippingCosts = 4.5
        pi.shippingTax = 7

        then: 'I get the correct subtotal gross'
        (6 * s + 4.815) == pi.subtotalGross

        where:
        q       | up        | t         || s
        null    | null      | null      || 0.0
        null    | null      | 0         || 0.0
        null    | null      | 0.45      || 0.0
        null    | null      | 14.5749   || 0.0
        null    | 0         | null      || 0.0
        null    | 0         | 0         || 0.0
        null    | 0         | 0.45      || 0.0
        null    | 0         | 14.5749   || 0.0
        null    | 0.45      | null      || 0.0
        null    | 0.45      | 0         || 0.0
        null    | 0.45      | 0.45      || 0.0
        null    | 0.45      | 14.5749   || 0.0
        null    | 174.4572  | null      || 0.0
        null    | 174.4572  | 0         || 0.0
        null    | 174.4572  | 0.45      || 0.0
        null    | 174.4572  | 14.5749   || 0.0
        0       | null      | null      || 0.0
        0       | null      | 0         || 0.0
        0       | null      | 0.45      || 0.0
        0       | null      | 14.5749   || 0.0
        0       | 0         | null      || 0.0
        0       | 0         | 0         || 0.0
        0       | 0         | 0.45      || 0.0
        0       | 0         | 14.5749   || 0.0
        0       | 0.45      | null      || 0.0
        0       | 0.45      | 0         || 0.0
        0       | 0.45      | 0.45      || 0.0
        0       | 0.45      | 14.5749   || 0.0
        0       | 174.4572  | null      || 0.0
        0       | 174.4572  | 0         || 0.0
        0       | 174.4572  | 0.45      || 0.0
        0       | 174.4572  | 14.5749   || 0.0
        0.45    | null      | null      || 0.0
        0.45    | null      | 0         || 0.0
        0.45    | null      | 0.45      || 0.0
        0.45    | null      | 14.5749   || 0.0
        0.45    | 0         | null      || 0.0
        0.45    | 0         | 0         || 0.0
        0.45    | 0         | 0.45      || 0.0
        0.45    | 0         | 14.5749   || 0.0
        0.45    | 0.45      | null      || 0.2025
        0.45    | 0.45      | 0         || 0.2025
        0.45    | 0.45      | 0.45      || 0.20341125
        0.45    | 0.45      | 14.5749   || 0.2320141725
        0.45    | 174.4572  | null      || 78.50574
        0.45    | 174.4572  | 0         || 78.50574
        0.45    | 174.4572  | 0.45      || 78.85901583
        0.45    | 174.4572  | 14.5749   || 89.94787309926
    }

    def 'Get the subtotal net without items'() {
        when: 'I use an empty purchase invoice'
        def pi = new PurchaseInvoice()

        then: 'I get zero as subtotal net'
        0.0 == pi.subtotalNet

        when: 'I set the items to an empty list'
        pi.items = []

        then: 'I get zero as subtotal net'
        0.0 == pi.subtotalNet
    }

    def 'Get the subtotal net with items'() {
        given: 'an empty invoicing transaction'
        def pi = new PurchaseInvoice(items: [])

        when: 'I add an item'
        pi.items << new PurchaseInvoiceItem(quantity: q, unitPrice: up, tax: t)

        then: 'I get the correct subtotal net'
        s == pi.subtotalNet

        when: 'I add another item with double quantity'
        pi.items << new PurchaseInvoiceItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'I get the correct subtotal net'
        (3 * s) == pi.subtotalNet

        when: 'I add another item with triple unit price'
        pi.items << new PurchaseInvoiceItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'I get the correct subtotal net'
        (6 * s) == pi.subtotalNet

        when: 'I set shipping costs'
        pi.shippingCosts = 4.5
        pi.shippingTax = 7

        then: 'I get the correct subtotal net'
        (6 * s + 4.5) == pi.subtotalNet

        where:
        q       | up        | t         || s
        null    | null      | null      || 0.0
        null    | null      | 0         || 0.0
        null    | null      | 0.45      || 0.0
        null    | null      | 14.5749   || 0.0
        null    | 0         | null      || 0.0
        null    | 0         | 0         || 0.0
        null    | 0         | 0.45      || 0.0
        null    | 0         | 14.5749   || 0.0
        null    | 0.45      | null      || 0.0
        null    | 0.45      | 0         || 0.0
        null    | 0.45      | 0.45      || 0.0
        null    | 0.45      | 14.5749   || 0.0
        null    | 174.4572  | null      || 0.0
        null    | 174.4572  | 0         || 0.0
        null    | 174.4572  | 0.45      || 0.0
        null    | 174.4572  | 14.5749   || 0.0
        0       | null      | null      || 0.0
        0       | null      | 0         || 0.0
        0       | null      | 0.45      || 0.0
        0       | null      | 14.5749   || 0.0
        0       | 0         | null      || 0.0
        0       | 0         | 0         || 0.0
        0       | 0         | 0.45      || 0.0
        0       | 0         | 14.5749   || 0.0
        0       | 0.45      | null      || 0.0
        0       | 0.45      | 0         || 0.0
        0       | 0.45      | 0.45      || 0.0
        0       | 0.45      | 14.5749   || 0.0
        0       | 174.4572  | null      || 0.0
        0       | 174.4572  | 0         || 0.0
        0       | 174.4572  | 0.45      || 0.0
        0       | 174.4572  | 14.5749   || 0.0
        0.45    | null      | null      || 0.0
        0.45    | null      | 0         || 0.0
        0.45    | null      | 0.45      || 0.0
        0.45    | null      | 14.5749   || 0.0
        0.45    | 0         | null      || 0.0
        0.45    | 0         | 0         || 0.0
        0.45    | 0         | 0.45      || 0.0
        0.45    | 0         | 14.5749   || 0.0
        0.45    | 0.45      | null      || 0.2025
        0.45    | 0.45      | 0         || 0.2025
        0.45    | 0.45      | 0.45      || 0.2025
        0.45    | 0.45      | 14.5749   || 0.2025
        0.45    | 174.4572  | null      || 78.50574
        0.45    | 174.4572  | 0         || 78.50574
        0.45    | 174.4572  | 0.45      || 78.50574
        0.45    | 174.4572  | 14.5749   || 78.50574
    }

    def 'Compute tax rate sums without items'() {
        when: 'I use an empty purchase invoice and obtain the tax rates'
        def pi = new PurchaseInvoice()
        Map<BigDecimal, BigDecimal> taxRateSums = pi.taxRateSums

        then: 'I get an empty map'
        null != taxRateSums
        taxRateSums.isEmpty()

        when: 'I set the items to an empty list and obtain the tax rates'
        pi.items = []
        taxRateSums = pi.taxRateSums

        then: 'I get an empty map'
        null != taxRateSums
        taxRateSums.isEmpty()
    }

    def 'Compute tax rate sums with items'() {
        given: 'an empty purchase invoice'
        def pi = new PurchaseInvoice(items: [])

        when: 'I add an item and obtain the tax rates'
        pi.items << new PurchaseInvoiceItem(quantity: 5, unitPrice: up, tax: t)
        Map<BigDecimal, BigDecimal> taxRateSums = pi.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        1 == taxRateSums.size()
        trs1 == taxRateSums[(Double)(double) (t ?: 0d)]

        when: 'I add another item and obtain the tax rates'
        pi.items << new PurchaseInvoiceItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 2.0
        )
        taxRateSums = pi.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        2 == taxRateSums.size()
        trs1 == taxRateSums[(Double)(double) (t ?: 0d)]
        trs3 == taxRateSums[(Double)(double) ((t ?: 0d) + 2.0d)]

        when: 'I add another item and obtain the tax rates'
        pi.items << new PurchaseInvoiceItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 1.5
        )
        taxRateSums = pi.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        3 == taxRateSums.size()
        trs1 == taxRateSums[(Double)(double) (t ?: 0d)]
        trs2 == taxRateSums[(Double)(double) ((t ?: 0d) + 1.5d)]
        trs3 == taxRateSums[(Double)(double) ((t ?: 0d) + 2.0d)]

        where:
        up      | t         || trs1     | trs2      | trs3
        null    | null      || 0.0      | 0.0       | 0.0
        null    | 0.0       || 0.0      | 0.0       | 0.0
        null    | 5         || 0.0      | 0.0       | 0.0
        null    | 9.23      || 0.0      | 0.0       | 0.0
        null    | 19        || 0.0      | 0.0       | 0.0
        0.0     | null      || 0.0      | 0.0       | 0.0
        0.0     | 0.0       || 0.0      | 0.0       | 0.0
        0.0     | 5         || 0.0      | 0.0       | 0.0
        0.0     | 9.23      || 0.0      | 0.0       | 0.0
        0.0     | 19        || 0.0      | 0.0       | 0.0
        0.28    | null      || 0.0      | 0.021     | 0.028
        0.28    | 0.0       || 0.0      | 0.021     | 0.028
        0.28    | 5         || 0.07     | 0.091     | 0.098
        0.28    | 9.23      || 0.12922  | 0.15022   | 0.15722
        0.28    | 19        || 0.266    | 0.287     | 0.294
    }

    def 'Total value is computed before insert'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new PurchaseInvoiceItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new PurchaseInvoiceItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new PurchaseInvoiceItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I call beforeInsert'
        pi.beforeInsert()

        then: 'I get the correct total value'
        407.00637375 == pi.total
    }

    def 'Total value is computed before update'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new PurchaseInvoiceItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new PurchaseInvoiceItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new PurchaseInvoiceItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I call beforeUpdate'
        pi.beforeUpdate()

        then: 'I get the correct total value'
        407.00637375 == pi.total
    }

    def 'Total value is computed before validate'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new PurchaseInvoiceItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new PurchaseInvoiceItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new PurchaseInvoiceItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I call beforeUpdate'
        pi.beforeValidate()

        then: 'I get the correct total value'
        407.00637375 == pi.total
    }

    def 'Compute total'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice(
            discountPercent: 2.5,
            items: [
                new PurchaseInvoiceItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new PurchaseInvoiceItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new PurchaseInvoiceItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I set discrete values for discount amount and adjustment'
        pi.discountAmount = da
        pi.adjustment = a

        then: 'I get the correct total price'
        e == pi.computeTotal()       // 415,46607375

        where:
        da      | a         || e
        null    | null      || 415.46607375
        null    | 0.0       || 415.46607375
        null    | 0.58      || 416.04607375
        null    | 2.98657   || 418.45264375
        null    | 14.759    || 430.22507375
        null    | -5.47313  || 409.99294375
        0.0     | null      || 415.46607375
        0.0     | 0.0       || 415.46607375
        0.0     | 0.58      || 416.04607375
        0.0     | 2.98657   || 418.45264375
        0.0     | 14.759    || 430.22507375
        0.0     | -5.47313  || 409.99294375
        0.58    | null      || 414.88607375
        0.58    | 0.0       || 414.88607375
        0.58    | 0.58      || 415.46607375
        0.58    | 2.98657   || 417.87264375
        0.58    | 14.759    || 429.64507375
        0.58    | -5.47313  || 409.41294375
        2.98657 | null      || 412.47950375
        2.98657 | 0.0       || 412.47950375
        2.98657 | 0.58      || 413.05950375
        2.98657 | 2.98657   || 415.46607375
        2.98657 | 14.759    || 427.23850375
        2.98657 | -5.47313  || 407.00637375
    }

    def 'Equals is null-safe'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice()

        expect:
        null != pi
        pi != null
        !pi.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a purchase invoice'
        def pi = new PurchaseInvoice()

        expect:
        pi != 'foo'
        pi != 45
        pi != 45.3
        pi != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def pi1 = new PurchaseInvoice(subject: 'Repair')
        def pi2 = new PurchaseInvoice(subject: 'Pipes')
        def pi3 = new PurchaseInvoice(subject: 'Service')

        expect: 'equals() is reflexive'
        pi1 == pi1
        pi2 == pi2
        pi3 == pi3

        and: 'all instances are equal and equals() is symmetric'
        pi1 == pi2
        pi2 == pi1
        pi2 == pi3
        pi3 == pi2

        and: 'equals() is transitive'
        pi1 == pi3
        pi3 == pi1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'two purchase invoices with different properties'
        def pi1 = new PurchaseInvoice(subject: 'Repair')
        pi1.id = 7403L
        def pi2 = new PurchaseInvoice(subject: 'Pipes')
        pi2.id = 7403L
        def pi3 = new PurchaseInvoice(subject: 'Service')
        pi3.id = 7403L

        expect: 'equals() is reflexive'
        pi1 == pi1
        pi2 == pi2
        pi3 == pi3

        and: 'all instances are equal and equals() is symmetric'
        pi1 == pi2
        pi2 == pi1
        pi2 == pi3
        pi3 == pi2

        and: 'equals() is transitive'
        pi1 == pi3
        pi3 == pi1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'two purchase invoices with different properties'
        def pi1 = new PurchaseInvoice(subject: 'Repair')
        pi1.id = 7403L
        def pi2 = new PurchaseInvoice(subject: 'Pipes')
        pi2.id = 7404L
        def pi3 = new PurchaseInvoice(subject: 'Service')
        pi3.id = 8473L

        expect: 'equals() is reflexive'
        pi1 == pi1
        pi2 == pi2
        pi3 == pi3

        and: 'all instances are unequal and equals() is symmetric'
        pi1 != pi2
        pi2 != pi1
        pi2 != pi3
        pi3 != pi2

        and: 'equals() is transitive'
        pi1 != pi3
        pi3 != pi1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def pi = new PurchaseInvoice()

        expect:
        0i == pi.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def pi = new PurchaseInvoice(subject: 'Repair')

        expect:
        0i == pi.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def pi = new PurchaseInvoice(subject: 'Repair')
        pi.id = 7403L

        when: 'I compute the hash code'
        int h = pi.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            pi = new PurchaseInvoice(subject: 'Repair')
            pi.id = 7403L
            h == pi.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'two purchase invoices with different properties'
        def pi1 = new PurchaseInvoice(subject: 'Repair')
        pi1.id = 7403L
        def pi2 = new PurchaseInvoice(subject: 'Pipes')
        pi2.id = 7403L
        def pi3 = new PurchaseInvoice(subject: 'Service')
        pi3.id = 7403L

        expect:
        pi1.hashCode() == pi2.hashCode()
        pi2.hashCode() == pi3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'two purchase invoices with different properties'
        def pi1 = new PurchaseInvoice(subject: 'Repair')
        pi1.id = 7403L
        def pi2 = new PurchaseInvoice(subject: 'Pipes')
        pi2.id = 7404L
        def pi3 = new PurchaseInvoice(subject: 'Service')
        pi3.id = 8473L

        expect:
        pi1.hashCode() != pi2.hashCode()
        pi2.hashCode() != pi3.hashCode()
    }

    def 'Can convert to string'() {
        given: 'an empty purchase invoice'
        def pi = new PurchaseInvoice()

        when: 'I set the subject'
        pi.subject = subject

        then: 'I get a valid string representation'
        s == pi.toString()

        where:
        subject         || s
        null            || ''
        ''              || ''
        '   '           || '   '
        'a'             || 'a'
        'abc'           || 'abc'
        '  foo  '       || '  foo  '
        'Services'      || 'Services'
    }

    def 'Number must not be blank'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the number'
        pi.number = s

        then: 'the instance is valid or not'
        v == pi.validate()

        where:
        s       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    def 'Subject must not be blank'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the subject'
        pi.subject = s

        then: 'the instance is valid or not'
        v == pi.validate()

        where:
        s       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

	def 'Vendor name must not be blank'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the vendor name'
        pi.vendorName = vn

        then: 'the instance is valid or not'
        v == pi.validate()

        where:
        vn      || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
	}

    def 'Document date must not be null'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the document date'
        pi.docDate = new Date()

        then: 'the instance is valid'
        pi.validate()

        when: 'I unset the document date'
        pi.docDate = null

        then: 'the instance is not valid'
        !pi.validate()
    }

    def 'Due date must not be null'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the due date'
        pi.dueDate = new Date()

        then: 'the instance is valid'
        pi.validate()

        when: 'I unset the due date'
        pi.dueDate = null

        then: 'the instance is not valid'
        !pi.validate()
    }

    def 'Stage must not be null'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set the stage'
        pi.stage = new PurchaseInvoiceStage()

        then: 'the instance is valid'
        pi.validate()

        when: 'I unset the stage'
        pi.stage = null

        then: 'the instance is not valid'
        !pi.validate()
    }

    def 'Items must not be null nor empty'() {
        given: 'a quite valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage()
        )

        when: 'I set the items'
        pi.items = [new InvoicingItem()]

        then: 'the instance is valid'
        pi.validate()

        when: 'I unset the items'
        pi.items = null

        then: 'the instance is not valid'
        !pi.validate()

        when: 'I set the items to an empty list'
        pi.items = []

        then: 'the instance is not valid'
        !pi.validate()
    }

	def 'Discount percent must not be less than zero'() {
        given: 'a valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

		when: 'I set various values and validate'
		pi.discountPercent = dp
		pi.validate()

		then: 'the instance is valid or not'
		valid != pi.hasErrors()

		where:
		  dp        | valid
        null        | true
		-100        | false
		  -5        | false
		  -1        | false
		  -0.005    | false
		   0        | true
		   0.005    | true
		   1        | true
		   5        | true
		 100        | true
		 200        | true
	}

	def 'Shipping tax must not be less than zero'() {
        given: 'a valid purchase invoice'
        def pi = new PurchaseInvoice(
            number: '123456',
            subject: 'International delivery',
            vendorName: 'Sugar & salt & bicycle vendor',
            docDate: new Date(),
            dueDate: new Date(),
            stage: new PurchaseInvoiceStage(),
            items: [new PurchaseInvoiceItem()]
        )

        when: 'I set various values and validate'
        pi.shippingTax = st
        pi.validate()

        then: 'the instance is valid or not'
		valid != pi.hasErrors()

		where:
		st				| valid
		null            | true
		-120034.005		| false
		-5				| false
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}


    //-- Non-public methods ---------------------

    private void testPaymentDates(PurchaseInvoice pi, String color) {

        /*
         * Note: only some dates are tested because the current date obtained in
         * method PurchaseInvoice.colorIndicatorByDate() is some milliseconds
         * later than date `d`.
         */

        Date d = new Date()
        pi.dueDate = d + 4
        assert color == pi.paymentStateColor

        pi.dueDate = d + 3
        assert (color == 'default' ? 'yellow' : color) == pi.paymentStateColor

        pi.dueDate = d + 1
        assert (color == 'default' ? 'yellow' : color) == pi.paymentStateColor

        pi.dueDate = d - 1
        assert (color == 'default' ? 'orange' : color) == pi.paymentStateColor

        pi.dueDate = d - 2
        assert (color == 'default' ? 'orange' : color) == pi.paymentStateColor

        pi.dueDate = d - 4
        assert (color == 'default' ? 'red' : color) == pi.paymentStateColor
    }
}