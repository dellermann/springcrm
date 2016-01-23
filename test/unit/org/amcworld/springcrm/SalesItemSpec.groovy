/*
 * SalesItemSpec.groovy
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


@TestFor(SalesItem)
class SalesItemSpec extends Specification {

	//-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty sales item'
        def s = new SalesItem()

        then: 'the properties are initialized properly'
        0i == s.number
        null == s.type
        null == s.name
        0.0 == s.quantity
        null == s.unit
        0.0 == s.unitPrice
        null == s.taxRate
        null == s.purchasePrice
        null == s.salesStart
        null == s.salesEnd
        null == s.description
        null == s.pricing
        null == s.dateCreated
        null == s.lastUpdated
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty sales item'
        def s1 = new SalesItem()

        when: 'I copy the sales item using the constructor'
        def s2 = new SalesItem(s1)

        then: 'the properties are set properly'
        0i == s2.number
        null == s2.type
        null == s2.name
        0.0 == s2.quantity
        null == s2.unit
        0.0 == s2.unitPrice
        null == s2.taxRate
        null == s2.purchasePrice
        null == s2.salesStart
        null == s2.salesEnd
        null == s2.description
        null == s2.pricing
        null == s2.dateCreated
        null == s2.lastUpdated
    }

    def 'Copy a sales item using constructor'() {
        given: 'some dates'
        Date start = new Date()
        Date end = start + 7

        and: 'a sales item with various properties'
        def s1 = new SalesItem(
            number: 1991,
            type: 'S',
            name: 'socket',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 2.5,
            taxRate: new TaxRate(),
            purchasePrice: 2.0,
            salesStart: start,
            salesEnd: end,
            description: 'description',
            pricing: new SalesItemPricing(),
            dateCreated: new Date(),
            lastUpdated: new Date()
        )

        when: 'I copy the sales item using the constructor'
        def s2 = new SalesItem(s1)

        then: 'some properties are the equal'
        s1.name == s2.name
        s1.quantity == s2.quantity
        s1.unitPrice == s2.unitPrice
        s1.purchasePrice == s2.purchasePrice
        s1.salesStart == s2.salesStart
        !s1.salesStart.is(s2.salesStart)
        s1.salesEnd == s2.salesEnd
        !s1.salesEnd.is(s2.salesEnd)
        s1.description == s2.description

        and: 'some instances are the same'
        s1.unit.is s2.unit
        s1.taxRate.is s2.taxRate
        s1.pricing.is s2.pricing

        and: 'some properties are unset'
        null == s2.id
        0i == s2.number
        null == s2.type
        null == s2.dateCreated
        null == s2.lastUpdated
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty sales item'
        def s = new SalesItem()

        when: 'I set the decimal values to null'
        s.quantity = null
        s.unitPrice = null

        then: 'all decimal values are never null'
        0.0 == s.quantity
        0.0 == s.unitPrice

        when: 'I create a sales item with null values'
        s = new SalesItem(quantity: null, unitPrice: null)

        then: 'all decimal values are never null'
        0.0 == s.quantity
        0.0 == s.unitPrice
    }

	def 'Get the full number'() {
		given: 'a sales item with mocked sequence number service'
		def s = new SalesItem()
		s.seqNumberService = Mock(SeqNumberService)
		s.seqNumberService.format(_, _) >> 'P-11332'

		expect:
		'P-11332' == s.fullNumber
	}

    def 'Get the total price'(BigDecimal q, BigDecimal up) {
        when: 'I create a sales item with quantity and unit price'
        def s = new SalesItem(quantity: q, unitPrice: up)

        then: 'I get a valid total price'
        ((q ?: 0.0) * (up ?: 0.0)) == s.getTotal()

        where:
        q           | up
        null        | null
        null        | 0.0
        null        | 0.00000001
        null        | 4.475
        null        | 23874.45
        0.0         | null
        0.0         | 0.0
        0.0         | 0.00000001
        0.0         | 4.475
        0.0         | 23874.45
        0.00000001  | null
        0.00000001  | 0.0
        0.00000001  | 0.00000001
        0.00000001  | 4.475
        0.00000001  | 23874.45
        4.475       | null
        4.475       | 0.0
        4.475       | 0.00000001
        4.475       | 4.475
        4.475       | 23874.45
        23874.45    | null
        23874.45    | 0.0
        23874.45    | 0.00000001
        23874.45    | 4.475
        23874.45    | 23874.45
    }

    def 'Get the unit price without pricing'(BigDecimal up) {
        when: 'I create a sales item with unit price and no pricing'
        def s = new SalesItem(unitPrice: up)

        then: 'I get a valid total price'
        (up ?: 0.0) == s.getUnitPrice()

        where:
        up << [null, 0.0, 0.00000001, 1.0, 1.475, 5.67, 14.47, 47504.4143]
    }

	def 'Get the unit price with pricing and different units'(BigDecimal q,
                                                              BigDecimal up)
    {
		given: 'some units'
		def unit1 = new Unit(name: 'pcs.')
		unit1.id = 1
		def unit2 = new Unit(name: 'm')
		unit2.id = 2

        and: 'a sales item pricing'
        SalesItemPricing p = Mock()
        p.asBoolean() >> true
        p.getQuantity() >> q
        p.getUnit() >> unit1
        p.getStep2TotalUnitPrice() >> up

        and: 'a sales item'
        def s = new SalesItem(quantity: q, unit: unit2, pricing: p)

        expect:
        (q == 0.0 ? 0.0 : up / q) == s.unitPrice

        where:
        q << [0.0, 1.0, 1.5, 2.0, 47.751, 1047.47]
        up << [17.98, 45.0, 407.74, 588.06, 1123.09, 34093.47112]
	}

    def 'Get the unit price with pricing and the same units'(BigDecimal q,
                                                             BigDecimal up)
    {
        given: 'a unit'
        def unit = new Unit(name: 'pcs.')
        unit.id = 1

        and: 'a sales item pricing'
        SalesItemPricing p = Mock()
        p.asBoolean() >> true
        p.getQuantity() >> q
        p.getUnit() >> unit
        p.getStep2TotalUnitPrice() >> up

        and: 'a sales item'
        def s = new SalesItem(quantity: 0, unit: unit, pricing: p)

        expect:
        (q == 0.0 ? 0.0 : up / q) == s.unitPrice

        where:
        q << [0.0, 1.0, 1.5, 2.0, 47.751, 1047.47]
        up << [17.98, 45.0, 407.74, 588.06, 1123.09, 34093.47112]
    }

	def 'Number is computed before insert'() {
        given: 'a mocked sequence number service'
		def s = new SalesItem()
		s.seqNumberService = Mock(SeqNumberService)
		s.seqNumberService.nextNumber(_) >> 92283

		when: 'I simulate calling save() in insert mode'
		s.beforeInsert()

		then: 'the sequence number must be set'
		92283 == s.number
	}

    def 'Equals is null-safe'() {
        given: 'a sales item'
        def s = new SalesItem()

        expect:
        null != s
        s != null
        !s.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a sales item'
        def s = new SalesItem()

        expect:
        s != 'foo'
        s != 45
        s != 45.3
        s != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def s1 = new SalesItem(name: '8" pipe')
        def s2 = new SalesItem(name: '8" pipe')
        def s3 = new SalesItem(name: '8" pipe')

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = 7403L
        def s2 = new SalesItem(name: '10" pipe')
        s2.id = 7403L
        def s3 = new SalesItem(name: '12" pipe')
        s3.id = 7403L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = 7403L
        def s2 = new SalesItem(name: '8" pipe')
        s2.id = 7404L
        def s3 = new SalesItem(name: '8" pipe')
        s3.id = 8473L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are unequal and equals() is symmetric'
        s1 != s2
        s2 != s1
        s2 != s3
        s3 != s2

        and: 'equals() is transitive'
        s1 != s3
        s3 != s1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def i = new SalesItem()

        expect:
        0i == i.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def i = new SalesItem(name: '8" pipe')

        expect:
        0i == i.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def i = new SalesItem(name: '8" pipe')
        i.id = 7403L

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new SalesItem(name: '10" pipe')
            i.id = 7403L
            h == i.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = 7403L
        def s2 = new SalesItem(name: '10" pipe')
        s2.id = 7403L
        def s3 = new SalesItem(name: '12" pipe')
        s3.id = 7403L

        expect:
        s1.hashCode() == s2.hashCode()
        s2.hashCode() == s3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = 7403L
        def s2 = new SalesItem(name: '8" pipe')
        s2.id = 7404L
        def s3 = new SalesItem(name: '8" pipe')
        s3.id = 8473L

        expect:
        s1.hashCode() != s2.hashCode()
        s2.hashCode() != s3.hashCode()
    }

    def 'Can convert to string'(String name) {
        given: 'an empty item'
        def s = new SalesItem()

        when: 'I set the name'
        s.name = name

        then: 'I get a valid string representation'
        (name ?: '') == s.toString()

        where:
        name << [null, '', '   ', 'a', 'abc', '  foo  ', 'Services']
    }

    def 'Type must not be blank and max one char long'(String t, boolean v) {
        given: 'a quite valid sales item'
        def s = new SalesItem(
            number: 39999,
            name: '8" pipe',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the type'
        s.type = t

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        t       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'S'     || true
        'abc'   || false
        'a  x ' || false
        ' name' || false
    }

    def 'Name must not be blank'(String n, boolean v) {
        given: 'a quite valid sales item'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the name'
        s.name = n

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        n       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    def 'Quantity must be positive if pricing available'(BigDecimal q,
                                                         boolean v)
    {
        given: 'a quite valid sales item with pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            unit: new Unit(),
            unitPrice: 3.45,
            pricing: new SalesItemPricing()
        )

        when: 'I set the quantity'
        s.quantity = q

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        q       || v
        null    || false
        0.0     || false
        0.00001 || true
        1.474   || true
        4703.79 || true
        -0.0001 || false
        -450.31 || false
    }

    def 'Quantity must be positive or zero if no pricing is available'(BigDecimal q, boolean v)
    {
        given: 'a quite valid sales item without pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the quantity'
        s.quantity = q

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        q       || v
        null    || true
        0.0     || true
        0.00001 || true
        1.474   || true
        4703.79 || true
        -0.0001 || false
        -450.31 || false
    }

    def 'Unit must not be null if pricing available'() {
        given: 'a quite valid sales item with pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unitPrice: 3.45,
            pricing: new SalesItemPricing()
        )

        when: 'I set the unit'
        s.unit = new Unit()

        then: 'the instance is valid'
        s.validate()

        when: 'I unset the unit'
        s.unit = null

        then: 'the instance is not valid'
        !s.validate()
    }

    def 'Unit may be null if no pricing is available'() {
        given: 'a quite valid sales item with pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unitPrice: 3.45
        )

        when: 'I set the unit'
        s.unit = new Unit()

        then: 'the instance is valid'
        s.validate()

        when: 'I unset the unit'
        s.unit = null

        then: 'the instance is valid'
        s.validate()
    }

    def 'Unit price must be positive or zero'(BigDecimal up, boolean v) {
        given: 'a quite valid sales item without pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unit: new Unit()
        )

        when: 'I set the unit price'
        s.unitPrice = up

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        up      || v
        null    || true
        0.0     || true
        0.00001 || true
        1.474   || true
        4703.79 || true
        -0.0001 || false
        -450.31 || false
    }

	def 'Pricing is validated for itself'() {
        given: 'a mocked pricing'
        SalesItemPricing p = Mock()
        p.validate() >>> [true, false]

        and: 'a quite valid sales item with pricing'
        def s = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 76.7,
            pricing: p
        )

		expect:
        s.validate()
        !s.validate()
	}
}
