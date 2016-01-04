/*
 * InvoicingTransactionSpec.groovy
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


@TestFor(InvoicingTransaction)
class InvoicingTransactionSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty invoicing transaction'
        def i = new InvoicingTransaction()

        then: 'the properties are initialized properly'
        0i == i.number
        null == i.type
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
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty invoicing transaction'
        def i1 = new InvoicingTransaction()

        when: 'I copy the invoicing transaction using the constructor'
        def i2 = new InvoicingTransaction(i1)

        then: 'the properties are set properly'
        0i == i2.number
        null == i2.type
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
    }

    def 'Copy an invoicing transaction using constructor'() {
        given: 'some dates'
        Date docDate = new Date()
        Date shippingDate = docDate + 7
        Date dateCreated = docDate - 2
        Date lastUpdated = docDate - 1

        and: 'an organization and a person'
        def organization = new Organization(name: 'Plumbing inc.')
        def person = new Person(
            organization: organization, firstName: 'Peter', lastName: 'Miller'
        )

        and: 'an invoicing transaction with various properties'
        def i1 = new InvoicingTransaction(
            number: 39999,
            type: 'X',
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
            lastUpdated: lastUpdated
        )

        when: 'I copy the invoicing transaction using the constructor'
        def i2 = new InvoicingTransaction(i1)

        then: 'some properties are the equal'
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

        and: 'some properties are set to new values'
        null != i2.docDate
        i1.docDate != i2.docDate

        and: 'some properties are unset'
        null == i2.id
        0i == i2.number
        null == i2.type
        null == i2.carrier
        null == i2.shippingDate
        null == i2.createUser
        null == i2.dateCreated
        null == i2.lastUpdated
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction()

        when: 'I set the decimal values to null'
        i.adjustment = null
        i.discountAmount = null
        i.discountPercent = null
        i.shippingCosts = null
        i.shippingTax = null
        i.total = null

        then: 'all decimal values are never null'
        0.0 == i.adjustment
        0.0 == i.discountAmount
        0.0 == i.discountPercent
        0.0 == i.shippingCosts
        0.0 == i.shippingTax
        0.0 == i.total

        when: 'I create an invoicing transaction with null values'
        i = new InvoicingTransaction(
            adjustment: null, discountAmount: null, discountPercent: null,
            shippingCosts: null, shippingTax: null, total: null
        )

        then: 'all decimal values are never null'
        0.0 == i.adjustment
        0.0 == i.discountAmount
        0.0 == i.discountPercent
        0.0 == i.shippingCosts
        0.0 == i.shippingTax
        0.0 == i.total
    }

    def 'Compute discount percent amount'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I set a discrete percentage value'
        i.discountPercent = d

        then: 'I get the correct discount amount'
        e == i.discountPercentAmount

        where:
           d    || e
        null    ||   0.0
           0    ||   0.0
           2    ||   8.522381
          10    ||  42.611905
          15.5  ||  66.04845275
          25.78 || 109.85349109
    }

    def 'Get the full name'() {
        given: 'a mocked sequence number service'
        def i = new InvoicingTransaction()
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'

        and: 'an organization'
        i.organization = new Organization(number: 10405)

        when: 'I set the subject'
        i.subject = s

        then: 'I get the correct full name'
        f == i.fullName

        where:
        s                       || f
        null                    || 'R-39999-10405 null'
        ''                      || 'R-39999-10405 '
        'foo'                   || 'R-39999-10405 foo'
        'Test invoice'          || 'R-39999-10405 Test invoice'
    }

    def 'Get the full number without organization'() {
        given: 'a mocked sequence number service'
        def i = new InvoicingTransaction()
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'

        when: 'I unset the organization'
        i.organization = null

        then: 'I get the correct full number'
        'R-39999' == i.fullNumber
    }

    def 'Get the full number with organization'() {
        given: 'a mocked sequence number service'
        def i = new InvoicingTransaction()
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'

        when: 'I set the number'
        i.organization = new Organization(number: n)

        then: 'I get the correct full number'
        fn == i.fullNumber

        where:
        n       || fn
        0       || 'R-39999-0'
        1       || 'R-39999-1'
        25      || 'R-39999-25'
        147     || 'R-39999-147'
        7473    || 'R-39999-7473'
        10405   || 'R-39999-10405'
        759734  || 'R-39999-759734'
        5749053 || 'R-39999-5749053'
    }

    def 'Get the shipping costs gross'() {
        given:
        def i = new InvoicingTransaction(shippingCosts: s, shippingTax: t)

        expect:
        e == i.shippingCostsGross

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
        def i = new InvoicingTransaction()

        then: 'I get a subtotal gross as zero'
        0.0 == i.subtotalGross

        when: 'I set the items to an empty list'
        i.items = []

        then: 'I get a subtotal gross as zero'
        0.0 == i.subtotalGross
    }

    def 'Get the subtotal gross with items'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'I add an item'
        i.items << new InvoicingItem(quantity: q, unitPrice: up, tax: t)

        then: 'I get the correct subtotal gross'
        s == i.subtotalGross

        when: 'I add another item with double quantity'
        i.items << new InvoicingItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'I get the correct subtotal gross'
        (3 * s) == i.subtotalGross

        when: 'I add another item with triple unit price'
        i.items << new InvoicingItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'I get the correct subtotal gross'
        (6 * s) == i.subtotalGross

        when: 'I set shipping costs'
        i.shippingCosts = 4.5
        i.shippingTax = 7

        then: 'I get the correct subtotal gross'
        (6 * s + 4.815) == i.subtotalGross

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
        when: 'I use an empty invoicing transaction'
        def i = new InvoicingTransaction()

        then: 'I get zero as subtotal net'
        0.0 == i.subtotalNet

        when: 'I set the items to an empty list'
        i.items = []

        then: 'I get zero as subtotal net'
        0.0 == i.subtotalNet
    }

    def 'Get the subtotal net with items'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'I add an item'
        i.items << new InvoicingItem(quantity: q, unitPrice: up, tax: t)

        then: 'I get the correct subtotal net'
        s == i.subtotalNet

        when: 'I add another item with double quantity'
        i.items << new InvoicingItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'I get the correct subtotal net'
        (3 * s) == i.subtotalNet

        when: 'I add another item with triple unit price'
        i.items << new InvoicingItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'I get the correct subtotal net'
        (6 * s) == i.subtotalNet

        when: 'I set shipping costs'
        i.shippingCosts = 4.5
        i.shippingTax = 7

        then: 'I get the correct subtotal net'
        (6 * s + 4.5) == i.subtotalNet

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
        when: 'I use an empty invoicing transaction and obtain the tax rates'
        def i = new InvoicingTransaction()
        Map<BigDecimal, BigDecimal> taxRateSums = i.taxRateSums

        then: 'I get an empty map'
        null != taxRateSums
        taxRateSums.isEmpty()

        when: 'I set the items to an empty list and obtain the tax rates'
        i.items = []
        taxRateSums = i.taxRateSums

        then: 'I get an empty map'
        null != taxRateSums
        taxRateSums.isEmpty()
    }

    def 'Compute tax rate sums with items'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'I add an item and obtain the tax rates'
        i.items << new InvoicingItem(quantity: 5, unitPrice: up, tax: t)
        Map<BigDecimal, BigDecimal> taxRateSums = i.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        1 == taxRateSums.size()
        trs1 == taxRateSums[(Double)(double) (t ?: 0d)]

        when: 'I add another item and obtain the tax rates'
        i.items << new InvoicingItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 2.0
        )
        taxRateSums = i.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        2 == taxRateSums.size()
        trs1 == taxRateSums[(Double)(double) (t ?: 0d)]
        trs3 == taxRateSums[(Double)(double) ((t ?: 0d) + 2.0d)]

        when: 'I add another item and obtain the tax rates'
        i.items << new InvoicingItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 1.5
        )
        taxRateSums = i.taxRateSums

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

    def 'Number is computed before insert'() {
        given: 'a mocked sequence number service'
        def i = new InvoicingTransaction()
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeInsert'
        i.beforeInsert()

        then: 'the next sequence number has been set'
        20473 == i.number
    }

    def 'Total value is computed before insert'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        and: 'a mocked sequence number service'
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeInsert'
        i.beforeInsert()

        then: 'I get the correct total value'
        407.00637375 == i.total
    }

    def 'Number is not altered before update'() {
        given: 'an invoicing transaction with a number'
        def i = new InvoicingTransaction(number: 12345)

        and: 'a mocked sequence number service'
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeUpdate'
        i.beforeUpdate()

        then: 'the sequence number has not been altered'
        12345 == i.number
    }

    def 'Total value is computed before update'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        and: 'a mocked sequence number service'
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeUpdate'
        i.beforeUpdate()

        then: 'I get the correct total value'
        407.00637375 == i.total
    }

    def 'Number is not altered before validate'() {
        given: 'an invoicing transaction with a number'
        def i = new InvoicingTransaction(number: 12345)

        and: 'a mocked sequence number service'
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeValidate'
        i.beforeValidate()

        then: 'the sequence number has not been altered'
        12345 == i.number
    }

    def 'Total value is computed before validate'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            adjustment: -5.47313,
            discountAmount: 2.98657,
            discountPercent: 2.5,
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        and: 'a mocked sequence number service'
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 20473

        when: 'I call beforeValidate'
        i.beforeValidate()

        then: 'I get the correct total value'
        407.00637375 == i.total
    }

    def 'Compute total'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            discountPercent: 2.5,
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        )

        when: 'I set discrete values for discount amount and adjustment'
        i.discountAmount = da
        i.adjustment = a

        then: 'I get the correct total price'
        e == i.computeTotal()       // 415,46607375

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

    def 'Copy the addresses from a given organization'() {
        given: 'two addresses'
        def addr1 = new Address(
            street: '45 Nelson Rd.', postalCode: '03037',
            location: 'Springfield', state: 'CA', country: 'USA'
        )
        def addr2 = new Address(
            street: '122 Granberry Ave.', postalCode: '12939',
            location: 'Mt. Elber', state: 'NY', country: 'USA'
        )

        and: 'an organization using these addresses'
        def org = new Organization(
            name: 'YourOrganization Ltd.', billingAddr: addr1,
            shippingAddr: addr2
        )

        and: 'an empty invoicing transaction'
        def i = new InvoicingTransaction()

        when: 'I copy the addresses of that organization to the invoice'
        i.copyAddressesFromOrganization org

        then: 'the invoicing transaction addresses are equal'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr

        and: 'the invoicing transaction addresses are copies'
        !addr1.is(i.billingAddr)
        !addr2.is(i.shippingAddr)
    }

    def 'Copy the addresses from the associated organization'() {
        given: 'two addresses'
        def addr1 = new Address(
            street: '45 Nelson Rd.', postalCode: '03037',
            location: 'Springfield', state: 'CA', country: 'USA'
        )
        def addr2 = new Address(
            street: '122 Granberry Ave.', postalCode: '12939',
            location: 'Mt. Elber', state: 'NY', country: 'USA'
        )

        and: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            organization: new Organization(
                name: 'YourOrganization Ltd.', billingAddr: addr1,
                shippingAddr: addr2
            )
        )

        when: 'I copy the addresses of the associated organization'
        i.copyAddressesFromOrganization()

        then: 'the invoicing transaction addresses are equal'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr

        and: 'the invoicing transaction addresses are copies'
        !addr1.is(i.billingAddr)
        !addr2.is(i.shippingAddr)
    }

    def 'Equals is null-safe'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction()

        expect:
        null != i
        i != null
        !i.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction()

        expect:
        i != 'foo'
        i != 45
        i != 45.3
        i != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        def i3 = new InvoicingTransaction(subject: 'Service')

        expect: 'equals() is reflexive'
        i1 == i1
        i2 == i2
        i3 == i3

        and: 'all instances are equal and equals() is symmetric'
        i1 == i2
        i2 == i1
        i2 == i3
        i3 == i2

        and: 'equals() is transitive'
        i1 == i3
        i3 == i1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = 7403L
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = 7403L
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = 7403L

        expect: 'equals() is reflexive'
        i1 == i1
        i2 == i2
        i3 == i3

        and: 'all instances are equal and equals() is symmetric'
        i1 == i2
        i2 == i1
        i2 == i3
        i3 == i2

        and: 'equals() is transitive'
        i1 == i3
        i3 == i1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = 7403L
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = 7404L
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = 8473L

        expect: 'equals() is reflexive'
        i1 == i1
        i2 == i2
        i3 == i3

        and: 'all instances are unequal and equals() is symmetric'
        i1 != i2
        i2 != i1
        i2 != i3
        i3 != i2

        and: 'equals() is transitive'
        i1 != i3
        i3 != i1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def i = new InvoicingTransaction()

        expect:
        0i == i.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def i = new InvoicingTransaction(subject: 'Repair')

        expect:
        0i == i.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def i = new InvoicingTransaction(subject: 'Repair')
        i.id = 7403L

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new InvoicingTransaction(subject: 'Repair')
            i.id = 7403L
            h == i.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = 7403L
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = 7403L
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = 7403L

        expect:
        i1.hashCode() == i2.hashCode()
        i2.hashCode() == i3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = 7403L
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = 7404L
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = 8473L

        expect:
        i1.hashCode() != i2.hashCode()
        i2.hashCode() != i3.hashCode()
    }

    def 'Can convert to string'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction()

        when: 'I set the subject'
        i.subject = subject

        then: 'I get a valid string representation'
        s == i.toString()

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

    def 'Type must not be blank or longer than one character'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            subject: 'Test invoice',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem()],
        )

        when: 'I set the type'
        i.type = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'I'     || true
        'xx'    || false
        'name'  || false
    }

    def 'Subject must not be blank'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem()],
        )

        when: 'I set the subject'
        i.subject = s

        then: 'the instance is valid or not'
        v == i.validate()

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

    def 'Organization must not be null'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem()],
        )

        when: 'I set the organization'
        i.organization = new Organization()

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the organization'
        i.organization = null

        then: 'the instance is not valid'
        !i.validate()
    }

    def 'Document date must not be null'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem()],
        )

        when: 'I set the document date'
        i.docDate = new Date()

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the document date'
        i.docDate = null

        then: 'the instance is not valid'
        !i.validate()
    }

    def 'Items must not be null nor empty'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address()
        )

        when: 'I set the items'
        i.items = [new InvoicingItem()]

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the items'
        i.items = null

        then: 'the instance is not valid'
        !i.validate()

        when: 'I set the items to an empty list'
        i.items = []

        then: 'the instance is not valid'
        !i.validate()
    }
}
