/*
 * InvoicingItemSpec.groovy
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


@TestFor(InvoicingItem)
class InvoicingItemSpec extends Specification {

    //-- Feature Methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty invoicing item'
        def i = new InvoicingItem()

        then: 'the properties are initialized properly'
        0.0 == i.quantity
        null == i.unit
        null == i.name
        null == i.description
        0.0 == i.unitPrice
        0.0 == i.tax
        null == i.salesItem
        null == i.invoicingTransaction
        null == i.id
    }

    def 'Copy an empty item using constructor'() {
        given: 'an empty invoicing item'
        def i1 = new InvoicingItem()

        when: 'I copy the invoicing item using the constructor'
        def i2 = new InvoicingItem(i1)

        then: 'the properties are set properly'
        BigDecimal.ZERO == i2.quantity
        null == i2.unit
        null == i2.name
        null == i2.description
        BigDecimal.ZERO == i2.unitPrice
        BigDecimal.ZERO == i2.tax
        null == i2.salesItem
        null == i2.invoicingTransaction
        null == i2.id
    }

    def 'Copy an item using constructor'() {
        given: 'an invoicing item with different properties'
        def i1 = new InvoicingItem(
            quantity: 15,
            unit: 'unit',
            name: '12" pipe',
            description: 'plastic',
            unitPrice: 188.0,
            tax: 17.0,
            salesItem: new SalesItem(name: '12" pipe', type: 'P')
        )

        when: 'I copy the invoicing item using the constructor'
        def i2 = new InvoicingItem(i1)

        then: 'some properties are the equal'
        i2.quantity == i1.quantity
        i2.unit == i1.unit
        i2.name == i1.name
        i2.description == i1.description
        i2.unitPrice == i1.unitPrice
        i2.tax == i1.tax

        and: 'the sales item is the same'
        i2.salesItem.is i1.salesItem

        and: 'some properties are unset'
        !i2.id
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty invoicing item'
        def i = new InvoicingItem()

        when: 'I set the decimal values to null'
        i.quantity = null
        i.unitPrice = null
        i.tax = null

        then: 'all decimal values are never null'
        0.0 == i.quantity
        0.0 == i.unitPrice
        0.0 == i.tax

        when: 'I create an invoicing item with null values'
        i = new InvoicingItem(quantity: null, unitPrice: null, tax: null)

        then: 'all decimal values are never null'
        0.0 == i.quantity
        0.0 == i.unitPrice
        0.0 == i.tax
    }

    def 'Get the total price'() {
        when: 'I create an invoicing item with quantity and unit price'
        def i = new InvoicingItem(quantity: q, unitPrice: up)

        then: 'the I get the correct net total price'
        t == i.total

        where:
        q       | up        || t
        null    | null      || 0
        null    | 0         || 0
        null    | 1         || 0
        null    | 5         || 0
        null    | 8         || 0
        null    | 20        || 0
        null    | 103.6     || 0
        0       | null      || 0
        0       | 0         || 0
        0       | 1         || 0
        0       | 1.25      || 0
        1       | null      || 0
        1       | 0         || 0
        1       | 1         || 1
        1.38    | 1         || 1.38
        1.57    | 2.78      || 4.3646
        2       | 5         || 10
        2.08    | 5.56      || 11.5648
        28      | 124.04912 || 3473.37536
        20.8945 | 124.04912 || 2591.94433784
    }

    def 'Get the gross total price'() {
        when: 'I create an invoicing item with quantity and unit price'
        def i = new InvoicingItem(quantity: q, unitPrice: up, tax: 19.00)

        then: 'the I get the correct gross total price'
        t == i.totalGross

        where:
        q       | up        || t
        null    | null      || 0
        null    | 0         || 0
        null    | 1         || 0
        null    | 5         || 0
        null    | 8         || 0
        null    | 20        || 0
        null    | 103.6     || 0
        0       | null      || 0
        0       | 0         || 0
        0       | 1         || 0
        0       | 1.25      || 0
        1       | null      || 0
        1       | 0         || 0
        1       | 1         || 1.19
        1.38    | 1         || 1.6422
        1.57    | 2.78      || 5.193874
        2       | 5         || 11.9
        2.08    | 5.56      || 13.762112
        28      | 124.04912 || 4133.3166784
        20.8945 | 124.04912 || 3084.4137620296
    }

    def 'Equals is null-safe'() {
        given: 'an invoicing item'
        def i = new InvoicingItem()

        expect:
        null != i
        i != null
        !i.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'an invoicing item'
        def i = new InvoicingItem()

        expect:
        i != 'foo'
        i != 45
        i != 45.3
        i != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def i1 = new InvoicingItem(
            quantity: 5, name: '12" pipe', unitPrice: 6.8
        )
        def i2 = new InvoicingItem(
            quantity: 8, name: '10" pipe', unitPrice: 6.2
        )
        def i3 = new InvoicingItem(
            quantity: 12, name: '14" pipe', unitPrice: 7.2
        )

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
        def i1 = new InvoicingItem(name: '12" pipe')
        i1.id = 7403L
        def i2 = new InvoicingItem(name: '10" pipe')
        i2.id = 7403L
        def i3 = new InvoicingItem(name: '14" pipe')
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
        def i1 = new InvoicingItem(name: '12" pipe')
        i1.id = 7403L
        def i2 = new InvoicingItem(name: '12" pipe')
        i2.id = 7404L
        def i3 = new InvoicingItem(name: '12" pipe')
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
        def i = new InvoicingItem()

        expect:
        0i == i.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def i = new InvoicingItem(name: '12" pipe')

        expect:
        0i == i.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def i = new InvoicingItem(name: '12" pipe')
        i.id = 7403L

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new InvoicingItem(name: '12" pipe')
            i.id = 7403L
            h == i.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingItem(name: '12" pipe')
        i1.id = 7403L
        def i2 = new InvoicingItem(name: '10" pipe')
        i2.id = 7403L
        def i3 = new InvoicingItem(name: '14" pipe')
        i3.id = 7403L

        expect:
        i1.hashCode() == i2.hashCode()
        i2.hashCode() == i3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'two invoicing items with different properties'
        def i1 = new InvoicingItem(name: '12" pipe')
        i1.id = 7403L
        def i2 = new InvoicingItem(name: '12" pipe')
        i2.id = 7404L
        def i3 = new InvoicingItem(name: '12" pipe')
        i3.id = 8473L

        expect:
        i1.hashCode() != i2.hashCode()
        i2.hashCode() != i3.hashCode()
    }

    def 'Can convert to string'() {
        given: 'an empty invoicing item'
        def i = new InvoicingItem()

        when: 'I set the name'
        i.name = name

        then: 'I get a valid string representation'
        s == i.toString()

        where:
        name            || s
        null            || ''
        ''              || ''
        '   '           || '   '
        'a'             || 'a'
        'abc'           || 'abc'
        '  foo  '       || '  foo  '
        '12" pipe'      || '12" pipe'
    }

    def 'Quantity must be positive or zero'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingItem(
            unit: 'm', name: '12" pipe', unitPrice: 12.8, tax: 19.0,
            invoicingTransaction: new Invoice()
        )

        when: 'I set the quantity'
        i.quantity = q

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        q       || v
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

    def 'Unit must not be blank'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingItem(
            quantity: 5.8, name: '12" pipe', unitPrice: 12.8, tax: 19.0,
            invoicingTransaction: new Invoice()
        )

        when: 'I set the unit'
        i.unit = u

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        u       || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'pcs'   || true
    }

    def 'name must not be blank'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingItem(
            quantity: 5.8, unit: 'm', unitPrice: 12.8, tax: 19.0,
            invoicingTransaction: new Invoice()
        )

        when: 'I set the name'
        i.name = n

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        n       || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'pipe'  || true
    }

    def 'Tax must be positive or zero'() {
        given: 'a quite valid invoicing item'
        def i = new InvoicingItem(
            quantity: 5.8, unit: 'm', name: '12" pipe', unitPrice: 12.8,
            invoicingTransaction: new Invoice()
        )

        when: 'I set the tax'
        i.tax = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t       || v
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
