/*
 * InvoicingTransactionSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


@TestFor(InvoicingTransaction)
@Mock([Invoice, InvoicingTransaction, Organization, SeqNumber])
class InvoicingTransactionSpec extends Specification {

    //-- Instance variables ---------------------

    Invoice i = new Invoice(
            adjustment: 0.54,
            billingAddr: new Address(),
            discountAmount: 5,
            discountPercent: 2,
            footerText: 'my footer text',
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
            notes: 'my notes',
            number: 39999,
            organization: new Organization(
                number: 10405, recType: 1, name: 'YourOrganization Ltd.',
                billingAddr: new Address(), shippingAddr: new Address()
            ),
            person: new Person(firstName: 'Marc', lastName: 'Webber'),
            shippingAddr: new Address(),
            shippingCosts: 4.5,
            shippingTax: 7,
            subject: 'Test invoice',
            termsAndConditions: [
                new TermsAndConditions(name: 'wares'),
                new TermsAndConditions(name: 'services')
            ]
        )


    //-- Feature methods ------------------------

    def 'Copy using constructor'() {
        when: 'I let compute the total price'
        i.beforeUpdate()

        and: 'I copy the given invoice using the constructor'
        Invoice i2 = new Invoice(i)

        then: 'I have some properties of the first invoice in the second one'
        i.subject == i2.subject
        i.organization.is i2.organization
        i.person.is i2.person
        !i.billingAddr.is(i2.billingAddr)
        i.billingAddr == i2.billingAddr
        !i.shippingAddr.is(i2.shippingAddr)
        i.shippingAddr == i2.shippingAddr
        i.headerText == i2.headerText
        !i.items.is(i2.items)
        i.items == i2.items
        i.footerText == i2.footerText
        i.discountPercent == i2.discountPercent
        i.discountAmount == i2.discountAmount
        i.shippingCosts == i2.shippingCosts
        i.shippingTax == i2.shippingTax
        i.adjustment == i2.adjustment
        i.total == i2.total
        i.notes == i2.notes
        !i.termsAndConditions.is(i2.termsAndConditions)
        i.termsAndConditions == i2.termsAndConditions

        and: 'some properties are unset'
        !i2.id
    }

    def 'Compute subtotal net'() {
        expect:
        359.555 == i.subtotalNet
    }

    def 'Compute tax rate sums'() {
        when: 'I compute the sums for each tax rate'
        Map t = i.taxRateSums

        then: 'I get an ordered map containing the tax rates and their sums'
        t instanceof LinkedHashMap
        ([7d: 0.96915d, 19d: 65.6849d] as LinkedHashMap) == t
    }

    def 'Compute subtotal gross'() {
        expect:
        426.20905 == i.subtotalGross
    }

    def 'Compute discount percent amount'() {
        given: 'the discrete percentage values'
        i.discountPercent = d

        expect: 'the correct discount amount'
        e == i.discountPercentAmount

        where:
         d  |  e
         0  |  0.0
         2  |  8.524181
        10  | 42.620905
    }

    def 'Compute total'() {
        given: 'the discrete discount percent, amount, and adjustment values'
        i.discountPercent = dp
        i.discountAmount = da
        i.adjustment = a

        expect: 'the correct total price'
        e == i.computeTotal()

        where:
        dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 426.20905
         0  |  0.00 |  0.58 | 426.78905
         0  |  0.00 | -1.12 | 425.08905
         0  |  0.18 |  0.0  | 426.02905
         0  |  1.00 |  0.58 | 425.78905
         0  |  4.50 | -1.12 | 420.58905
         2  |  0.18 |  0.0  | 417.504869
         2  |  1.00 |  0.58 | 417.264869
         2  |  4.50 | -1.12 | 412.064869
        10  |  0.18 |  0.0  | 383.408145
        10  |  1.00 |  0.58 | 383.168145
        10  |  4.50 | -1.12 | 377.968145
    }

    def 'Simulate the validate method and check total'() {
        given: 'the discrete discount percent, amount, and adjustment values'
        i.discountPercent = dp
        i.discountAmount = da
        i.adjustment = a

        when: 'I simulate calling validate()'
        i.beforeValidate()

        then: 'the total property must be set'
        e == i.total

        where:
        dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 426.20905
         0  |  0.00 |  0.58 | 426.78905
         0  |  0.00 | -1.12 | 425.08905
         0  |  0.18 |  0.0  | 426.02905
         0  |  1.00 |  0.58 | 425.78905
         0  |  4.50 | -1.12 | 420.58905
         2  |  0.18 |  0.0  | 417.504869
         2  |  1.00 |  0.58 | 417.264869
         2  |  4.50 | -1.12 | 412.064869
        10  |  0.18 |  0.0  | 383.408145
        10  |  1.00 |  0.58 | 383.168145
        10  |  4.50 | -1.12 | 377.968145
    }

    def 'Simulate the save method in insert mode and check total'() {
        given: 'the discrete discount percent, amount, and adjustment values'
        i.seqNumberService = Mock(SeqNumberService)
        i.discountPercent = dp
        i.discountAmount = da
        i.adjustment = a

        when: 'I simulate calling save() in insert mode'
        i.beforeInsert()

        then: 'the total property must be set'
        e == i.total

        where:
        dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 426.20905
         0  |  0.00 |  0.58 | 426.78905
         0  |  0.00 | -1.12 | 425.08905
         0  |  0.18 |  0.0  | 426.02905
         0  |  1.00 |  0.58 | 425.78905
         0  |  4.50 | -1.12 | 420.58905
         2  |  0.18 |  0.0  | 417.504869
         2  |  1.00 |  0.58 | 417.264869
         2  |  4.50 | -1.12 | 412.064869
        10  |  0.18 |  0.0  | 383.408145
        10  |  1.00 |  0.58 | 383.168145
        10  |  4.50 | -1.12 | 377.968145
    }

    def 'Simulate the save method in update mode and check total'() {
        given: 'the discrete discount percent, amount, and adjustment values'
        i.discountPercent = dp
        i.discountAmount = da
        i.adjustment = a

        when: 'I simulate calling save() in update mode'
        i.beforeUpdate()

        then: 'the total property must be set'
        e == i.total

        where:
        dp  | da    |  a    |   e
         0  |  0.00 |  0.0  | 426.20905
         0  |  0.00 |  0.58 | 426.78905
         0  |  0.00 | -1.12 | 425.08905
         0  |  0.18 |  0.0  | 426.02905
         0  |  1.00 |  0.58 | 425.78905
         0  |  4.50 | -1.12 | 420.58905
         2  |  0.18 |  0.0  | 417.504869
         2  |  1.00 |  0.58 | 417.264869
         2  |  4.50 | -1.12 | 412.064869
        10  |  0.18 |  0.0  | 383.408145
        10  |  1.00 |  0.58 | 383.168145
        10  |  4.50 | -1.12 | 377.968145
    }

    def 'Get the full number'() {
        given:
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'

        expect:
        'R-39999-10405' == i.fullNumber
    }

    def 'Get the full name'() {
        given:
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'

        expect:
        'R-39999-10405 Test invoice' == i.fullName
    }

    def 'Simulate the save method in insert mode and check number'() {
        given: 'the sequence number of the invoice is unset'
        i.number = 0
        i.seqNumberService = Mock(SeqNumberService)
        i.seqNumberService.nextNumber(_) >> 14128

        when: 'I simulate calling save() in insert mode'
        i.beforeInsert()

        then: 'the sequence number must be set'
        14128 == i.number
    }

    def 'Copy the addresses from a given organization'() {
        given: 'two addresses'
        Address addr1 = new Address(
            street: '45, Nelson Rd.', postalCode: '03037',
            location: 'Springfield', state: 'CA', country: 'USA'
        )
        Address addr2 = new Address(
            street: '122, Granberry Ave.', postalCode: '12939',
            location: 'Mt. Elber', state: 'NY', country: 'USA'
        )

        and: 'an organization using these addresses'
        Organization org = new Organization(
            recType: 1, name: 'YourOrganization Ltd.', billingAddr: addr1,
            shippingAddr: addr2
        )

        when: 'I copy the addresses of that organization to the invoice'
        i.copyAddressesFromOrganization org

        then: 'the invoice addresses must be set correctly'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr
    }

    def 'Copy the addresses from the associated organization'() {
        given: 'two addresses'
        Address addr1 = new Address(
            street: '45, Nelson Rd.', postalCode: '03037',
            location: 'Springfield', state: 'CA', country: 'USA'
        )
        Address addr2 = new Address(
            street: '122, Granberry Ave.', postalCode: '12939',
            location: 'Mt. Elber', state: 'NY', country: 'USA'
        )

        and: 'our invoice with an organization using these addresses'
        i.organization.billingAddr = addr1
        i.organization.shippingAddr = addr2

        when: 'I copy the addresses of the associated organization to the invoice'
        i.copyAddressesFromOrganization()

        then: 'the invoice addresses must be set correctly'
        addr1 == i.billingAddr
        addr2 == i.shippingAddr
    }

    def 'Check for equality'() {
        given: 'a second invoice'
        Invoice i2 = new Invoice(
            adjustment: 0.27,
            discountAmount: 1.5,
            discountPercent: 3,
            items: [
                new InvoicingItem(
                    number: 'P-20000', quantity: 4, unit: 'pcs.',
                    name: 'books', unitPrice: 44.99, tax: 19
                ),
                new InvoicingItem(
                    number: 'P-30000', quantity: 10.5, unit: 'm',
                    name: 'tape', unitPrice: 0.89, tax: 7
                ),
                new InvoicingItem(
                    number: 'S-60100', quantity: 4.25, unit: 'h',
                    name: 'repairing', unitPrice: 39, tax: 19
                )
            ],
            number: 12093,
            organization: new Organization(
                number: 47474, recType: 1, name: 'YourOrganization Ltd.',
                billingAddr: new Address(), shippingAddr: new Address()
            ),
            shippingCosts: 5.7,
            shippingTax: 19,
            subject: 'Another test invoice'
        )

        and: 'Set both the IDs to the same value'
        i2.id = 503
        i.id = 503

        expect: 'both these invoices are equal'
        i2 == i
        i == i2
    }

    def 'Check for inequality'() {
        given: 'a second invoice'
        Invoice i2 = new Invoice(
            adjustment: 0.54,
            discountAmount: 5,
            discountPercent: 2,
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
            number: 39999,
            organization: new Organization(
                number: 10405, recType: 1, name: 'YourOrganization Ltd.',
                billingAddr: new Address(), shippingAddr: new Address()
            ),
            shippingCosts: 4.5,
            shippingTax: 7,
            subject: 'Test invoice'
        )

        and: 'set both the IDs to different values'
        i2.id = 504
        i.id = 503

        when: 'I compare both these invoices'
        boolean b1 = (i2 != i)
        boolean b2 = (i != i2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        i2 = null

        then: 'they are not equal'
        i2 != i
        i != i2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        i != s
    }

    def 'Compute hash code'() {
        when: 'an invoice with no ID'
        i.id = null

        then: 'a valid hash code'
        0 == i.hashCode()

        when: 'an invoice with discrete IDs'
        i.id = id

        then: 'a hash code using this ID'
        e == i.hashCode()

        where:
           id |     e
            0 |     0
            1 |     1
           10 |    10
          105 |   105
         9404 |  9404
        37603 | 37603
    }

    def 'Convert to string'() {
        expect:
        'Test invoice' == i.toString()
    }
}
