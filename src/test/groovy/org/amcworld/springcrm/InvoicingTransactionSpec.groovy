/*
 * InvoicingTransactionSpec.groovy
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
import org.bson.types.ObjectId
import spock.lang.Specification


class InvoicingTransactionSpec extends Specification
    implements DomainUnitTest<InvoicingTransaction>
{

    //-- Feature methods ------------------------

    void 'Creating an empty instance initializes the properties'() {
        when: 'an empty instance is created'
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

    void 'Copy an empty instance using constructor'() {
        given: 'an empty instance'
        def i1 = new InvoicingTransaction()

        when: 'the instance is copied using the constructor'
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

    void 'Copy an invoicing transaction using constructor'() {
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

        and: 'an instance with various properties'
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
                new TermsAndConditions(name: 'works')
            ],
            createUser: new User(),
            dateCreated: dateCreated,
            lastUpdated: lastUpdated
        )

        when: 'the instance is copied using the constructor'
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

    void 'Set decimal values to null converts them to zero'() {
        given: 'an empty instance'
        def i = new InvoicingTransaction()

        when: 'the decimal values are set to null'
        i.adjustment = null
        i.discountAmount = null
        i.discountPercent = null
        i.shippingCosts = null
        i.shippingTax = null

        then: 'all decimal values are never null'
        0.0 == i.adjustment
        0.0 == i.discountAmount
        0.0 == i.discountPercent
        0.0 == i.shippingCosts
        0.0 == i.shippingTax
        0.0 == i.total

        when: 'an invoicing transaction with null values is created'
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

    void 'Compute discount percent amount'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction(
            items: [
                new InvoicingItem(quantity: 4, unitPrice: 44.99, tax: 19),
                new InvoicingItem(quantity: 10.5, unitPrice: 0.89, tax: 7),
                new InvoicingItem(quantity: 4.25, unitPrice: 39, tax: 19)
            ],
            shippingCosts: 4.5,
            shippingTax: 5
        ) // subtotalGross == 426.11095

        when: 'a discrete percentage value is set'
        i.discountPercent = d

        then: 'the correct discount amount is returned'
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

    void 'Get the full name'(String subject, String fn) {
        given: 'a sequence number'
        SeqNumber seqNumber = new SeqNumber(
            prefix: 'R', startValue: 10000i, suffix: 'S'
        )

        and: 'an instance'
        def i = new InvoicingTransaction(number: 39999i, subject: subject)

        and: 'an organization'
        i.organization = new Organization(number: 17473)

        expect:
        fn == i.computeFullName(seqNumber)

        where:
        subject     || fn
        'a'         || 'R-39999-17473-S a'
        'abc'       || 'R-39999-17473-S abc'
        'Foo bar'   || 'R-39999-17473-S Foo bar'
        'A test'    || 'R-39999-17473-S A test'
    }

    void 'Get the full number without organization'() {
        given: 'a sequence number'
        SeqNumber seqNumber = new SeqNumber(
            prefix: 'R', startValue: 10000i, suffix: 'S'
        )

        and: 'an instance'
        def i = new InvoicingTransaction(number: 39999i)

        and: 'an unset organization'
        i.organization = null

        expect:
        'R-39999-S' == i.computeFullNumber(seqNumber)
    }

    void 'Get the full number with organization'(int n, String fn) {
        given: 'a sequence number'
        SeqNumber seqNumber = new SeqNumber(
            prefix: 'R', startValue: 10000i, suffix: 'S'
        )

        and: 'an instance'
        def i = new InvoicingTransaction(number: 39999i)

        and: 'an organization'
        i.organization = new Organization(number: n)

        expect:
        fn == i.computeFullNumber(seqNumber)

        where:
        n       || fn
        0       || 'R-39999-0-S'
        1       || 'R-39999-1-S'
        25      || 'R-39999-25-S'
        147     || 'R-39999-147-S'
        7473    || 'R-39999-7473-S'
        10405   || 'R-39999-10405-S'
        759734  || 'R-39999-759734-S'
        5749053 || 'R-39999-5749053-S'
    }

    void 'Get the shipping costs gross'() {
        given: 'an instance'
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

    void 'Get the subtotal gross without items'() {
        when: 'an empty invoicing transaction is created'
        def i = new InvoicingTransaction()

        then: 'the subtotal gross is returned as zero'
        0.0 == i.subtotalGross

        when: 'the items are set to an empty list'
        i.items = []

        then: 'the subtotal gross is returned as zero'
        0.0 == i.subtotalGross
    }

    void 'Get the subtotal gross with items'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'an item is added'
        i.items << new InvoicingItem(quantity: q, unitPrice: up, tax: t)

        then: 'the correct subtotal gross is returned'
        s == i.subtotalGross

        when: 'another item with double quantity is added'
        i.items << new InvoicingItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'the correct subtotal gross is returned'
        (3 * s) == i.subtotalGross

        when: 'another item with triple unit price is returned'
        i.items << new InvoicingItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'the correct subtotal gross is returned'
        (6 * s) == i.subtotalGross

        when: 'shipping costs are set'
        i.shippingCosts = 4.5
        i.shippingTax = 7

        then: 'the correct subtotal gross is returned'
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

    void 'Get the subtotal net without items'() {
        when: 'an empty invoicing transaction is used'
        def i = new InvoicingTransaction()

        then: 'zero is returned as subtotal net'
        0.0 == i.subtotalNet

        when: 'the items are set to an empty list'
        i.items = []

        then: 'zero is returned as subtotal net'
        0.0 == i.subtotalNet
    }

    void 'Get the subtotal net with items'() {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'an item is added'
        i.items << new InvoicingItem(quantity: q, unitPrice: up, tax: t)

        then: 'the correct subtotal net is returned'
        s == i.subtotalNet

        when: 'another item with double quantity is added'
        i.items << new InvoicingItem(
            quantity: (q ?: 0) * 2, unitPrice: up, tax: t
        )

        then: 'the correct subtotal net is returned'
        (3 * s) == i.subtotalNet

        when: 'another item with triple unit price is added'
        i.items << new InvoicingItem(
            quantity: q, unitPrice: (up ?: 0) * 3, tax: t
        )

        then: 'the correct subtotal net is returned'
        (6 * s) == i.subtotalNet

        when: 'shipping costs are set'
        i.shippingCosts = 4.5
        i.shippingTax = 7

        then: 'the correct subtotal net is returned'
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

    void 'Compute tax rate sums without items'() {
        when: 'an empty instance is used and the tax rates are obtained'
        def i = new InvoicingTransaction()
        Map<Double, BigDecimal> taxRateSums = i.taxRateSums

        then: 'an empty map is returned'
        null != taxRateSums
        taxRateSums.isEmpty()

        when: 'the items are set to an empty list'
        i.items = []
        taxRateSums = i.taxRateSums

        then: 'an empty map is returned'
        null != taxRateSums
        taxRateSums.isEmpty()
    }

    void 'Compute tax rate sums with items'(BigDecimal up, BigDecimal t,
                                            BigDecimal trs1, BigDecimal trs2,
                                            BigDecimal trs3)
    {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction(items: [])

        when: 'an item is added'
        i.items << new InvoicingItem(quantity: 5, unitPrice: up, tax: t)
        Map<Double, BigDecimal> taxRateSums = i.taxRateSums

        then: 'an ordered map with the tax rates and their sums is returned'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        1 == taxRateSums.size()
        trs1 == taxRateSums[(Double) (t ?: 0d)]

        when: 'another item is added'
        i.items << new InvoicingItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 2.0
        )
        taxRateSums = i.taxRateSums

        then: 'an ordered map with the tax rates and their sums is returned'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        2 == taxRateSums.size()
        trs1 == taxRateSums[(Double) (t ?: 0d)]
        trs3 == taxRateSums[(Double) ((t ?: 0d) + 2.0d)]

        when: 'another item is added'
        i.items << new InvoicingItem(
            quantity: 5, unitPrice: up, tax: (t ?: 0.0) + 1.5
        )
        taxRateSums = i.taxRateSums

        then: 'an ordered map with the tax rates and their sums is returned'
        null != taxRateSums
        taxRateSums instanceof LinkedHashMap
        3 == taxRateSums.size()
        trs1 == taxRateSums[(Double) (t ?: 0d)]
        trs2 == taxRateSums[(Double) ((t ?: 0d) + 1.5d)]
        trs3 == taxRateSums[(Double) ((t ?: 0d) + 2.0d)]

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

    void 'Compute dynamic values before insert'() {
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

        when: 'the beforeInsert event is called'
        i.beforeInsert()

        then: 'the correct dynamic values have been computed'
        359.555 == i['subtotalNet']
        426.119_05 == i['subtotalGross']
        4.725 == i['shippingCostsGross']
        10.652_976_25 == i['discountPercentAmount']
        407.006_373_75 == i['total']

        and: 'the correct dynamic values on items have been computed'
        179.96 == i.items[0]['totalNet']
        214.152_4 == i.items[0]['totalGross']
        9.345 == i.items[1]['totalNet']
        9.999_15 == i.items[1]['totalGross']
        165.75 == i.items[2]['totalNet']
        197.242_5 == i.items[2]['totalGross']
    }

    void 'Compute dynamic values before update'() {
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

        when: 'the beforeUpdate event is called'
        i.beforeUpdate()

        then: 'the correct dynamic values have been computed'
        359.555 == i['subtotalNet']
        426.119_05 == i['subtotalGross']
        4.725 == i['shippingCostsGross']
        10.652_976_25 == i['discountPercentAmount']
        407.006_373_75 == i['total']

        and: 'the correct dynamic values on items have been computed'
        179.96 == i.items[0]['totalNet']
        214.152_4 == i.items[0]['totalGross']
        9.345 == i.items[1]['totalNet']
        9.999_15 == i.items[1]['totalGross']
        165.75 == i.items[2]['totalNet']
        197.242_5 == i.items[2]['totalGross']
    }

    void 'Get total'() {
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

        when: 'discrete values for discount amount and adjustment are set'
        i.discountAmount = da
        i.adjustment = a

        then: 'the correct total price is returned'
        e == i.getTotal()       // 415,46607375

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

    void 'Copy the addresses from a given organization'() {
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

        when: 'the addresses of that organization are copied to the invoice'
        i.copyAddressesFromOrganization org

        then: 'the invoicing transaction addresses are equal'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr

        and: 'the invoicing transaction addresses are copies'
        !addr1.is(i.billingAddr)
        !addr2.is(i.shippingAddr)
    }

    void 'Copy the addresses from the associated organization'() {
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

        when: 'the addresses are copied of the associated organization'
        i.copyAddressesFromOrganization()

        then: 'the invoicing transaction addresses are equal'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr

        and: 'the invoicing transaction addresses are copies'
        !addr1.is(i.billingAddr)
        !addr2.is(i.shippingAddr)
    }

    void 'Equals is null-safe'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction()

        expect:
        null != i
        i != null
        //noinspection ChangeToOperator
        !i.equals(null)
    }

    void 'Instances of other types are always unequal'() {
        given: 'an invoicing transaction'
        def i = new InvoicingTransaction()

        expect:
        i != 'foo'
        i != 45
        i != 45.3
        i != new Date()
    }

    void 'Not persisted instances are equal'() {
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

    void 'Persisted instances are equal if they have the same ID'() {
        given: 'three invoicing transactions with same ID'
        def id = new ObjectId()
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = id
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = id
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = id

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

    void 'Persisted instances are unequal if they have the different ID'() {
        given: 'three invoicing transactions with different IDs'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = new ObjectId()
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = new ObjectId()
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = new ObjectId()

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

    void 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def i = new InvoicingTransaction()

        expect:
        3937i == i.hashCode()
    }

    void 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def i = new InvoicingTransaction(subject: 'Repair')

        expect:
        3937i == i.hashCode()
    }

    void 'Hash codes are consistent'() {
        given: 'an ID'
        def id = new ObjectId()

        and: 'an instance'
        def i = new InvoicingTransaction(subject: 'Repair')
        i.id = id

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new InvoicingTransaction(subject: 'Repair')
            i.id = id
            h == i.hashCode()
        }
    }

    void 'Equal instances produce the same hash code'() {
        given: 'three invoicing transactions with same ID'
        def id = new ObjectId()
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = id
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = id
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = id

        expect:
        i1.hashCode() == i2.hashCode()
        i2.hashCode() == i3.hashCode()
    }

    void 'Different instances produce different hash codes'() {
        given: 'three invoicing transactions with different properties'
        def i1 = new InvoicingTransaction(subject: 'Repair')
        i1.id = new ObjectId()
        def i2 = new InvoicingTransaction(subject: 'Pipes')
        i2.id = new ObjectId()
        def i3 = new InvoicingTransaction(subject: 'Service')
        i3.id = new ObjectId()

        expect:
        i1.hashCode() != i2.hashCode()
        i2.hashCode() != i3.hashCode()
    }

    void 'Can convert to string'(String subject, String s) {
        given: 'an empty invoicing transaction'
        def i = new InvoicingTransaction()

        when: 'the subject is set'
        i.subject = subject

        then: 'a valid string representation is returned'
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

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Type must not be blank or longer than one character'(String t,
                                                               boolean v)
    {
        given: 'a quite valid invoicing item'
        def i = new InvoicingTransaction(
            number: 39999,
            subject: 'Test invoice',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'the type is set'
        i.type = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t       || v
        null    || false
        ''      || false
        'a'     || true
        'I'     || true
        'xx'    || false
        'name'  || false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Subject must not be blank'(String s, boolean v) {
        given: 'a quite valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'the subject is set'
        i.subject = s

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        s       || v
        null    || false
        ''      || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    void 'Organization must not be null'() {
        given: 'a quite valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'the organization is set'
        i.organization = new Organization()

        then: 'the instance is valid'
        i.validate()

        when: 'the organization is unset'
        i.organization = null

        then: 'the instance is not valid'
        !i.validate()
    }

    void 'Document date must not be null'() {
        given: 'a quite valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'the document date is set'
        i.docDate = new Date()

        then: 'the instance is valid'
        i.validate()

        when: 'the document date is unset'
        i.docDate = null

        then: 'the instance is not valid'
        !i.validate()
    }

    void 'Items must not be null nor empty'() {
        given: 'a quite valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address()
        )

        when: 'the items are set'
        i.items = [
            new InvoicingItem(
                unit: 'h', name: 'Administration', invoicingTransaction: i
            )
        ]

        then: 'the instance is valid'
        i.validate()

        when: 'the items are unset'
        i.items = null

        then: 'the instance is not valid'
        !i.validate()

        when: 'the items are set to an empty list'
        i.items = []

        then: 'the instance is not valid'
        !i.validate()
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Discount percent must not be less than zero'() {
        given: 'a valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'various values are set'
        i.discountPercent = dp
        i.validate()

        then: 'the instance is valid or not'
        valid != i.hasErrors()

        where:
          dp        || valid
        null        || true
        -100        || false
          -5        || false
          -1        || false
          -0.005    || false
           0        || true
           0.005    || true
           1        || true
           5        || true
         100        || true
         200        || true
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Shipping tax must not be less than zero'() {
        given: 'a valid invoicing transaction'
        def i = new InvoicingTransaction(
            number: 39999,
            type: 'X',
            subject: 'Services',
            organization: new Organization(),
            person: new Person(),
            billingAddr: new Address(),
            shippingAddr: new Address(),
            items: [new InvoicingItem(unit: 'h', name: 'Administration')],
        )

        when: 'various values are set'
        i.shippingTax = st
        i.validate()

        then: 'the instance is valid or not'
        valid != i.hasErrors()

        where:
        st              || valid
        null            || true
        -120034.005     || false
        -5              || false
        1003            || true
        4               || true
        100D            || true
        100.0d          || true
        1e2d            || true
    }
}
