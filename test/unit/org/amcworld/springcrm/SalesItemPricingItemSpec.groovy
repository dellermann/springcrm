/*
 * SalesItemPricingItemSpec.groovy
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


@TestFor(SalesItemPricingItem)
class SalesItemPricingItemSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty sales item pricing item'
        def i = new SalesItemPricingItem()

        then: 'the properties are initialized properly'
        0.0 == i.quantity
        null == i.unit
        null == i.name
        PricingItemType.absolute == i.type
        null == i.relToPos
        0.0 == i.unitPercent
        0.0 == i.unitPrice
        null == i.pricing
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty sales item pricing item'
        def i = new SalesItemPricingItem()

        when: 'I set the decimal values to null'
        i.quantity = null
        i.unitPercent = null
        i.unitPrice = null

        then: 'all decimal values are never null'
        0.0 == i.quantity
        0.0 == i.unitPercent
        0.0 == i.unitPrice

        when: 'I create an invoicing item with null values'
        i = new SalesItemPricingItem(
            quantity: null, unitPercent: null, unitPrice: null
        )

        then: 'all decimal values are never null'
        0.0 == i.quantity
        0.0 == i.unitPercent
        0.0 == i.unitPrice
    }

    def 'Equals is null-safe'() {
        given: 'an instance'
        def i = new SalesItemPricingItem()

        expect:
        null != i
        i != null
        !i.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'an instance'
        def i = new SalesItemPricingItem()

        expect:
        i != 'foo'
        i != 45
        i != 45.3
        i != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def i1 = new SalesItemPricingItem(
            quantity: 5, name: '12" pipe', unitPrice: 6.8
        )
        def i2 = new SalesItemPricingItem(
            quantity: 8, name: '10" pipe', unitPrice: 6.2
        )
        def i3 = new SalesItemPricingItem(
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
        given: 'three instances with different properties but same IDs'
        def i1 = new SalesItemPricingItem(name: 'working hours')
        i1.id = 7403L
        def i2 = new SalesItemPricingItem(name: 'wares')
        i2.id = 7403L
        def i3 = new SalesItemPricingItem(name: 'profit')
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
        given: 'three instances with same properties but different IDs'
        def i1 = new SalesItemPricingItem(name: 'working hours')
        i1.id = 7403L
        def i2 = new SalesItemPricingItem(name: 'wares')
        i2.id = 7404L
        def i3 = new SalesItemPricingItem(name: 'profit')
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
        def i = new SalesItemPricingItem()

        expect:
        0i == i.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def i = new SalesItemPricingItem(name: 'working hours')

        expect:
        0i == i.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def i = new SalesItemPricingItem(name: 'working hours')
        i.id = 7403L

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new SalesItemPricingItem(name: 'services')
            i.id = 7403L
            h == i.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same ID'
        def i1 = new SalesItemPricingItem(name: 'working hours')
        i1.id = 7403L
        def i2 = new SalesItemPricingItem(name: 'wares')
        i2.id = 7403L
        def i3 = new SalesItemPricingItem(name: 'profit')
        i3.id = 7403L

        expect:
        i1.hashCode() == i2.hashCode()
        i2.hashCode() == i3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def i1 = new SalesItemPricingItem(name: 'working hours')
        i1.id = 7403L
        def i2 = new SalesItemPricingItem(name: 'wares')
        i2.id = 7404L
        def i3 = new SalesItemPricingItem(name: 'profit')
        i3.id = 8473L

        expect:
        i1.hashCode() != i2.hashCode()
        i2.hashCode() != i3.hashCode()
    }

    def 'Can convert to string'() {
        given: 'an empty item'
        def i = new SalesItemPricingItem()

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
        'working hours' || 'working hours'
    }

    def 'Quantity must be positive or zero'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            unit: 'h',
            name: 'working hours',
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
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

    def 'Unit must not be null unless type is sum'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            name: 'working hours',
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
        )

        when: 'I set the unit'
        i.unit = 'h'

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the unit and change the type'
        i.unit = null
        i.type = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t                                       || v
        PricingItemType.absolute                || false
        PricingItemType.relativeToPos           || false
        PricingItemType.relativeToLastSum       || false
        PricingItemType.relativeToCurrentSum    || false
        PricingItemType.sum                     || true
    }

    def 'Name must not be null unless type is sum'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            unit: 'h',
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
        )

        when: 'I set the name'
        i.name = 'working hours'

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the name and change the type'
        i.name = null
        i.type = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t                                       || v
        PricingItemType.absolute                || false
        PricingItemType.relativeToPos           || false
        PricingItemType.relativeToLastSum       || false
        PricingItemType.relativeToCurrentSum    || false
        PricingItemType.sum                     || true
    }

    def 'Type must not be null'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            unit: 'h',
            name: 'working hours',
            relToPos: 5i,
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
        )

        when: 'I set the type'
        i.type = t

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the type'
        i.type = null

        then: 'the instance is not valid'
        !i.validate()

        where:
        t << PricingItemType.values()
    }

    def 'Relative to position must not be null if type is relativeToPos'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            unit: 'h',
            name: 'working hours',
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
        )

        when: 'I set the position'
        i.relToPos = 5i

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the position and change the type'
        i.relToPos = null
        i.type = t

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        t                                       || v
        PricingItemType.absolute                || true
        PricingItemType.relativeToPos           || false
        PricingItemType.relativeToLastSum       || true
        PricingItemType.relativeToCurrentSum    || true
        PricingItemType.sum                     || true
    }

    def 'Unit percentage must be positive or zero'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            unit: 'h',
            name: 'working hours',
            unitPrice: 12.8,
            pricing: new SalesItemPricing()
        )

        when: 'I set the unit percentage value'
        i.unitPercent = up

        then: 'the instance is valid or not'
        v == i.validate()

        where:
        up      || v
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

    def 'Pricing must not be null'() {
        given: 'a quite valid item'
        def i = new SalesItemPricingItem(
            quantity: 5.0,
            unit: 'h',
            name: 'working hours',
            relToPos: 5i,
            unitPrice: 12.8
        )

        when: 'I associate a pricing'
        i.pricing = new SalesItemPricing()

        then: 'the instance is valid'
        i.validate()

        when: 'I unset the pricing'
        i.pricing = null

        then: 'the instance is not valid'
        !i.validate()
    }
}
