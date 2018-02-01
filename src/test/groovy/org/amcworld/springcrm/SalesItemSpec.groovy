/*
 * SalesItemSpec.groovy
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


class SalesItemSpec extends Specification implements DomainUnitTest<SalesItem> {

	//-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'an empty sales item is created'
        def item = new SalesItem()

        then: 'the properties are initialized properly'
        0i == item.number
        null == item.type
        null == item.name
        0.0 == item.quantity
        null == item.unit
        0.0 == item.unitPrice
        null == item.taxRate
        null == item.purchasePrice
        null == item.salesStart
        null == item.salesEnd
        null == item.description
        null == item.pricing
        null == item.dateCreated
        null == item.lastUpdated
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty sales item'
        def s1 = new SalesItem()

        when: 'the sales item is copied using the constructor'
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

        when: 'the sales item is copied using the constructor'
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
        def item = new SalesItem()

        when: 'I set the decimal values to null'
        item.quantity = null
        item.unitPrice = null

        then: 'all decimal values are never null'
        0.0 == item.quantity
        0.0 == item.unitPrice

        when: 'I create a sales item with null values'
        item = new SalesItem(quantity: null, unitPrice: null)

        then: 'all decimal values are never null'
        0.0 == item.quantity
        0.0 == item.unitPrice
    }

    def 'Get the total price'(BigDecimal q, BigDecimal up) {
        when: 'I create a sales item with quantity and unit price'
        def item = new SalesItem(quantity: q, unitPrice: up)

        then: 'I get a valid total price'
        ((q ?: 0.0) * (up ?: 0.0)) == item.getTotal()

        where:
        q           || up
        null        || null
        null        || 0.0
        null        || 0.00000001
        null        || 4.475
        null        || 23874.45
        0.0         || null
        0.0         || 0.0
        0.0         || 0.00000001
        0.0         || 4.475
        0.0         || 23874.45
        0.00000001  || null
        0.00000001  || 0.0
        0.00000001  || 0.00000001
        0.00000001  || 4.475
        0.00000001  || 23874.45
        4.475       || null
        4.475       || 0.0
        4.475       || 0.00000001
        4.475       || 4.475
        4.475       || 23874.45
        23874.45    || null
        23874.45    || 0.0
        23874.45    || 0.00000001
        23874.45    || 4.475
        23874.45    || 23874.45
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

    def 'Equals is null-safe'() {
        given: 'a sales item'
        def item = new SalesItem()

        expect:
        null != item
        item != null
        !item.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a sales item'
        def item = new SalesItem()

        expect:
        item != 'foo'
        item != 45
        item != 45.3
        item != new Date()
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
        def id = new ObjectId()
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = id
        def s2 = new SalesItem(name: '10" pipe')
        s2.id = id
        def s3 = new SalesItem(name: '12" pipe')
        s3.id = id

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
        s1.id = new ObjectId()
        def s2 = new SalesItem(name: '8" pipe')
        s2.id = new ObjectId()
        def s3 = new SalesItem(name: '8" pipe')
        s3.id = new ObjectId()

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
        def item = new SalesItem()

        expect:
        3937i == item.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def item = new SalesItem(name: '8" pipe')

        expect:
        3937i == item.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def id = new ObjectId()
        def item = new SalesItem(name: '8" pipe')
        item.id = id

        when: 'I compute the hash code'
        int h = item.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            item = new SalesItem(name: '10" pipe')
            item.id = id
            h == item.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def id = new ObjectId()
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = id
        def s2 = new SalesItem(name: '10" pipe')
        s2.id = id
        def s3 = new SalesItem(name: '12" pipe')
        s3.id = id

        expect:
        s1.hashCode() == s2.hashCode()
        s2.hashCode() == s3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SalesItem(name: '8" pipe')
        s1.id = new ObjectId()
        def s2 = new SalesItem(name: '8" pipe')
        s2.id = new ObjectId()
        def s3 = new SalesItem(name: '8" pipe')
        s3.id = new ObjectId()

        expect:
        s1.hashCode() != s2.hashCode()
        s2.hashCode() != s3.hashCode()
    }

    def 'Can convert to string'(String name) {
        given: 'an instance'
        def item = new SalesItem(name: name)

        expect:
        (name?.trim() ?: '') == item.toString()

        where:
        name << [null, '', '   ', 'a', 'abc', '  foo  ', 'Services']
    }

    def 'Type must not be blank and max one char long'(String t, boolean v) {
        given: 'a quite valid sales item'
        def item = new SalesItem(
            number: 39999,
            name: '8" pipe',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the type'
        item.type = t

        then: 'the instance is valid or not'
        v == item.validate()

        where:
        t       || v
        null    || false
        ''      || false
        'a'     || true
        'S'     || true
        'abc'   || false
        'a  x ' || false
        ' name' || false
    }

    def 'Name must not be blank'(String n, boolean v) {
        given: 'a quite valid sales item'
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the name'
        item.name = n

        then: 'the instance is valid or not'
        v == item.validate()

        where:
        n       || v
        null    || false
        ''      || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    def 'Quantity must be positive if pricing available'(BigDecimal q,
                                                         boolean v)
    {
        given: 'a quite valid sales item with pricing'
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            unit: new Unit(),
            unitPrice: 3.45,
            pricing: new SalesItemPricing(
                unit: new Unit(),
                items: [new SalesItemPricingItem(unit: 'h', name: 'Service')]
            )
        )

        when: 'I set the quantity'
        item.quantity = q

        then: 'the instance is valid or not'
        v == item.validate()

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

    def 'Quantity must be positive or zero if no pricing is available'(
        BigDecimal q, boolean v
    ) {
        given: 'a quite valid sales item without pricing'
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            unit: new Unit(),
            unitPrice: 3.45
        )

        when: 'I set the quantity'
        item.quantity = q

        then: 'the instance is valid or not'
        v == item.validate()

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
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unitPrice: 3.45,
            pricing: new SalesItemPricing(
                unit: new Unit(),
                items: [new SalesItemPricingItem(unit: 'h', name: 'Service')]
            )
        )

        when: 'I set the unit'
        item.unit = new Unit()

        then: 'the instance is valid'
        item.validate()

        when: 'I unset the unit'
        item.unit = null

        then: 'the instance is not valid'
        !item.validate()
    }

    def 'Unit may be null if no pricing is available'() {
        given: 'a quite valid sales item with pricing'
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unitPrice: 3.45
        )

        when: 'I set the unit'
        item.unit = new Unit()

        then: 'the instance is valid'
        item.validate()

        when: 'I unset the unit'
        item.unit = null

        then: 'the instance is valid'
        item.validate()
    }

    def 'Unit price must be positive or zero'(BigDecimal up, boolean v) {
        given: 'a quite valid sales item without pricing'
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unit: new Unit()
        )

        when: 'I set the unit price'
        item.unitPrice = up

        then: 'the instance is valid or not'
        v == item.validate()

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
        def item = new SalesItem(
            number: 39999,
            type: 'S',
            name: '8" pipe',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 76.7,
            pricing: p
        )

		expect:
        item.validate()
        !item.validate()
	}
}
