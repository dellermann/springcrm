/*
 * SalesItemPricingSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SalesItemPricing)
@Mock([SalesItemPricing, SalesItemPricingItem])
class SalesItemPricingSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty sales item pricing'
        def s = new SalesItemPricing()

        then: 'the properties are initialized properly'
        1.0 == s.quantity
        null == s.unit
        0.0 == s.discountPercent
        0.0 == s.adjustment
        null == s.items
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty sales item pricing'
        def s = new SalesItemPricing()

        when: 'I set the decimal values to null'
        s.quantity = null
        s.discountPercent = null
        s.adjustment = null

        then: 'all decimal values are never null'
        0.0 == s.quantity
        0.0 == s.discountPercent
        0.0 == s.adjustment

        when: 'I create an invoicing item with null values'
        s = new SalesItemPricing(
            quantity: null, discountPercent: null, adjustment: null
        )

        then: 'all decimal values are never null'
        0.0 == s.quantity
        0.0 == s.discountPercent
        0.0 == s.adjustment
    }

	def 'Compute the discount percent amount'(BigDecimal dp) {
        given: 'a sales item pricing'
        def s = new SalesItemPricing(
            quantity: 1,
            unit: new Unit(),
            discountPercent: 15,
            adjustment: 0,
            items: [
                new SalesItemPricingItem(
                    quantity: 5, unit: 'kg', name: 'name',
                    unitPercent: 0, unitPrice: 2
                ),
                new SalesItemPricingItem(
                    quantity: 3, unit: 'kg', name: 'name',
                    unitPercent: 0, unitPrice: 2
                ),
                new SalesItemPricingItem(
                    quantity: 1, unit: 'kg', name: 'name',
                    unitPercent: 0, unitPrice: 2
                )
            ]
        )   // step1TotalPrice = 18.0

		when: 'I set a discrete discount percent'
		s.discountPercent = dp

		then: 'I get the correct discount percent amount'
		(0.18 * (dp ?: 0.0)) == s.discountPercentAmount

		where:
        dp << [null, 0.0, 0.000001, 1, 10, 15.4749, 20, 100, 200]
	}

    def 'Get total price of step 1'() {
        when: 'I create a sales item pricing with one item'
        def s = new SalesItemPricing(
            items: [new SalesItemPricingItem(quantity: 5, unitPrice: 45)]
        )

        then: 'I get the correct current sum'
        225.0 == s.step1TotalPrice

        when: 'I compute the sum at a particular position'
        s.items << new SalesItemPricingItem(quantity: 2, unitPrice: 30)

        then: 'I get the correct current sum'
        285.0 == s.step1TotalPrice

        when: 'I compute the sum at a particular position'
        s.items << new SalesItemPricingItem(quantity: 3, unitPrice: 30)

        then: 'I get the correct current sum'
        375.0 == s.step1TotalPrice
    }

    def 'Get unit price of step 1'(BigDecimal q) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(quantity: 5, unitPrice: 45),
                new SalesItemPricingItem(quantity: 2, unitPrice: 20)
            ]
        )   // step1TotalPrice = 265.0

        when: 'I unset the quantity'
        s.quantity = null

        then: 'I get no unit price'
        null == s.step1UnitPrice

        when: 'I set a discrete value to quantity'
        s.quantity = 0.0

        then: 'I get no unit price'
        null == s.step1UnitPrice

        when: 'I set a discrete value to quantity'
        s.quantity = q

        then: 'I get no unit price'
        (265.0 / q) == s.step1UnitPrice

        where:
        q << [0.000001, 1, 1.76, 2, 2.000001, 10.475, 15.7604, 294.47]
    }

    def 'Get total price of step 2'(BigDecimal dp, BigDecimal a) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(quantity: 5, unitPrice: 45),
                new SalesItemPricingItem(quantity: 2, unitPrice: 20)
            ]
        )   // step1TotalPrice = 265.0

        when: 'I set discrete values for discount and adjustment'
        s.discountPercent = dp
        s.adjustment = a

        then: 'I get the correct step2Total'
        (265.0 - (2.65 * (dp ?: 0.0)) + (a ?: 0.0)) == s.step2Total

        where:
        dp      | a         || _
        null    | null      || _
        null    | 0.0       || _
        null    | 0.0000001 || _
        null    | 1.0       || _
        null    | 1.7047    || _
        null    | 245.4579  || _
        null    | -0.000001 || _
        null    | -47.475   || _
        0.0     | null      || _
        0.0     | 0.0       || _
        0.0     | 0.0000001 || _
        0.0     | 1.0       || _
        0.0     | 1.7047    || _
        0.0     | 245.4579  || _
        0.0     | -0.000001 || _
        0.0     | -47.475   || _
        0.00001 | null      || _
        0.00001 | 0.0       || _
        0.00001 | 0.0000001 || _
        0.00001 | 1.0       || _
        0.00001 | 1.7047    || _
        0.00001 | 245.4579  || _
        0.00001 | -0.000001 || _
        0.00001 | -47.475   || _
        1.0     | null      || _
        1.0     | 0.0       || _
        1.0     | 0.0000001 || _
        1.0     | 1.0       || _
        1.0     | 1.7047    || _
        1.0     | 245.4579  || _
        1.0     | -0.000001 || _
        1.0     | -47.475   || _
        245.457 | null      || _
        245.457 | 0.0       || _
        245.457 | 0.0000001 || _
        245.457 | 1.0       || _
        245.457 | 1.7047    || _
        245.457 | 245.4579  || _
        245.457 | -0.000001 || _
        245.457 | -47.475   || _
        -0.0001 | null      || _
        -0.0001 | 0.0       || _
        -0.0001 | 0.0000001 || _
        -0.0001 | 1.0       || _
        -0.0001 | 1.7047    || _
        -0.0001 | 245.4579  || _
        -0.0001 | -0.000001 || _
        -0.0001 | -47.475   || _
        -47.475 | null      || _
        -47.475 | 0.0       || _
        -47.475 | 0.0000001 || _
        -47.475 | 1.0       || _
        -47.475 | 1.7047    || _
        -47.475 | 245.4579  || _
        -47.475 | -0.000001 || _
        -47.475 | -47.475   || _
    }

    def 'Get unit price of step 2'(BigDecimal q) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(quantity: 5, unitPrice: 45),
                new SalesItemPricingItem(quantity: 2, unitPrice: 20)
            ],
            discountPercent: 2,
            adjustment: -0.07
        )   // step2Total = 265.0 - 2 * 2.65 - 0.07 = 259.63

        when: 'I unset the quantity'
        s.quantity = null

        then: 'I get no unit price'
        null == s.step2TotalUnitPrice

        when: 'I set a discrete value to quantity'
        s.quantity = 0.0

        then: 'I get no unit price'
        null == s.step2TotalUnitPrice

        when: 'I set a discrete value to quantity'
        s.quantity = q

        then: 'I get no unit price'
        (259.63 / q) == s.step2TotalUnitPrice

        where:
        q << [0.000001, 1, 1.76, 2, 2.000001, 10.475, 15.7604, 294.47]
    }

    def 'Convert sales item pricing to boolean'() {
        when: 'I create an empty sales item pricing'
        def s = new SalesItemPricing()

        then: 'it is considered false'
        !s

        when: 'I set the items to an empty list'
        s.items = []

        then: 'it is considered false'
        !s

        when: 'I add an item'
        s.items << new SalesItemPricingItem()

        then: 'it is considered true'
        s
    }

    def 'Compute current sum in empty lists'(int pos) {
        when: 'I create an empty sales item pricing and compute the sum'
        def s = new SalesItemPricing()
        s.computeCurrentSum()

        then: 'I get an exception'
        thrown NullPointerException

        when: 'I compute the sum at a particular position'
        s.computeCurrentSum pos

        then: 'I get an exception'
        thrown NullPointerException

        when: 'I set an empty list and compute the sum'
        s.items = []

        then: 'I get zero as current sum'
        0.0 == s.computeCurrentSum()

        when: 'I compute the sum at a particular position'
        s.computeCurrentSum pos

        then: 'I get another exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [0, 1, 1057, -1, -1057]
    }

    def 'Compute current sum in non-empty lists'() {
        when: 'I create a sales item pricing with one item'
        def s = new SalesItemPricing(
            items: [new SalesItemPricingItem(quantity: 5, unitPrice: 45)]
        )

        then: 'I get the correct current sum'
        225.0 == s.computeCurrentSum()
        225.0 == s.computeCurrentSum(0)

        when: 'I compute the sum at a particular position'
        s.items << new SalesItemPricingItem(quantity: 2, unitPrice: 30)

        then: 'I get the correct current sum'
        285.0 == s.computeCurrentSum()
        225.0 == s.computeCurrentSum(0)
        285.0 == s.computeCurrentSum(1)

        when: 'I compute the sum at a particular position'
        s.items << new SalesItemPricingItem(quantity: 3, unitPrice: 30)

        then: 'I get the correct current sum'
        375.0 == s.computeCurrentSum()
        225.0 == s.computeCurrentSum(0)
        285.0 == s.computeCurrentSum(1)
        375.0 == s.computeCurrentSum(2)
    }

    def 'Compute current sum with illegal positions'(int pos) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem()
            ]
        )

        when: 'I compute the current sum'
        s.computeCurrentSum pos

        then: 'I get an exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [5i, 6i, 10i, 10574i, -1i, -10i, -45709i]
    }

    def 'Compute total in empty lists'(int pos) {
        when: 'I create an empty sales item pricing and compute the total'
        def s = new SalesItemPricing()
        s.computeTotalOfItem pos

        then: 'an exception is thrown'
        thrown NullPointerException

        when: 'I set an empty list and compute the total'
        s.items = []
        s.computeTotalOfItem pos

        then: 'I get another exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [0i, 1i, 10i, 10574i, -1i, -10i, -45709i]
    }

    def 'Compute total with illegal positions'(int pos) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem()
            ]
        )

        when: 'I compute the total'
        s.computeTotalOfItem pos

        then: 'I get an exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [5i, 6i, 10i, 10574i, -1i, -10i, -45709i]
    }

    def 'Compute total of absolute items'(BigDecimal q, BigDecimal up) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: q, unitPrice: up
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2 * (q ?: 0.0),
                    unitPrice: up
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3 * (q ?: 0.0),
                    unitPrice: 2 * (up ?: 0.0)
                ),
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(0)

        then: 'I get the correct value'
        ((q ?: 0.0) * (up ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(1)

        then: 'I get the correct value'
        (2 * (q ?: 0.0) * (up ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(2)

        then: 'I get the correct value'
        (6 * (q ?: 0.0) * (up ?: 0.0)) == total

        where:
        q           | up            || _
        null        | null          || _
        null        | 0             || _
        null        | 0.00000000001 || _
        null        | 1             || _
        null        | 2.478         || _
        null        | 17450.4739    || _
        0.0         | null          || _
        0.0         | 0             || _
        0.0         | 0.00000000001 || _
        0.0         | 1             || _
        0.0         | 2.478         || _
        0.0         | 17450.4739    || _
        0.000000001 | null          || _
        0.000000001 | 0             || _
        0.000000001 | 0.00000000001 || _
        0.000000001 | 1             || _
        0.000000001 | 2.478         || _
        0.000000001 | 17450.4739    || _
        1           | null          || _
        1           | 0             || _
        1           | 0.00000000001 || _
        1           | 1             || _
        1           | 2.478         || _
        1           | 17450.4739    || _
        2.478       | null          || _
        2.478       | 0             || _
        2.478       | 0.00000000001 || _
        2.478       | 1             || _
        2.478       | 2.478         || _
        2.478       | 17450.4739    || _
        17450.4739  | null          || _
        17450.4739  | 0             || _
        17450.4739  | 0.00000000001 || _
        17450.4739  | 1             || _
        17450.4739  | 2.478         || _
        17450.4739  | 17450.4739    || _
    }

    def 'Compute total of relativeToPos items'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2.5,
                    unitPrice: 45.78
                ),  // e0 = 2.5 * 45.78 = 114.45
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, unitPercent: p
                ),  // e1 = 0.0, because not related to any item
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 0i,
                    quantity: 1, unitPercent: p
                ),  // e2 = 1 * e0 * p / 100 = 1.1445p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 1i,
                    unitPercent: p
                ),  // e3 = 0.0, because related to an item without relation
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 2i,
                    quantity: 2.0, unitPercent: p
                ),  // e4 = 2 * e2 * p / 100 = 0.02289p²
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 6i,
                    quantity: 2.5, unitPercent: p
                ),  // e5 = 2.5 * e6 * p / 100 = 2.86125p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2.5,
                    unitPrice: 45.78
                ),  // e6 = 2.5 * 45.78 = 114.45
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(1)

        then: 'I get zero because the item has no relation'
        0.0 == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(2)

        then: 'I get the correct value because it relates to a absolute item'
        (1.1445 * (p ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(3)

        then: 'I get zero because it relates to an item that has no relation'
        0.0 == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(4)

        then: 'I get the correct value because it relates to a absolute item'
        (0.02289 * (p ?: 0.0) * (p ?: 0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(5)

        then: 'I get the correct value because it relates to a absolute item'
        (2.86125 * (p ?: 0.0)) == total

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute total of relativeToCurrentSum items'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3, unitPrice: 40
                ),  // currentSum == 190.0
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToCurrentSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 2.5 * 190.0 * p / 100.0 = 4.75p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),  // currentSum == 290.0 + e1 = 290.0 + 4.75p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToCurrentSum, quantity: 2,
                    unitPercent: p
                )   // e2 = 2 * (290.0 + 4.75p) * p / 100 = 5.8p + 0.095p²
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(3)

        then: 'I get the correct value'
        (4.75 * (p ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(5)

        then: 'I get the correct value'
        (5.8 * (p ?: 0.0) + 0.095 * (p ?: 0.0) * (p ?: 0.0)) == total

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute total of relativeToLastSum items without sums'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),  // currentSum = 70
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 2.5 * 0.7p = 1.75p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),  // currentSum = 170 + e1 = 170 + 1.75p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2,
                    unitPercent: p
                )   // e2 = 2 * (170 + 1.75p) * p / 100 = 3.4p + 0.035p²
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(2)

        then: 'I get the correct value'
        (1.75 * (p ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(4)

        then: 'I get the correct value'
        (3.4 * (p ?: 0.0) + 0.035 * (p ?: 0) * (p ?: 0)) == total

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute total of relativeToLastSum items with sums'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),  // currentSum = 70
                new SalesItemPricingItem(type: PricingItemType.sum),    // currentSum = 70
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3, unitPrice: 40
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 2.5 * 0.7p = 1.75p
                new SalesItemPricingItem(type: PricingItemType.sum),    // currentSum = 190 + e1 = 190 + 1.75p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2,
                    unitPercent: p
                )   // e2 = 2 * (190 + 1.75p) * p / 100 = 3.8p + 0.035p²
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(4)

        then: 'I get the correct value'
        (1.75 * (p ?: 0.0)) == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(7)

        then: 'I get the correct value'
        (3.8 * (p ?: 0.0) + 0.035 * (p ?: 0) * (p ?: 0)) == total

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute total of sum items'() {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2.5,
                    unitPrice: 40.8
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3.9,
                    unitPrice: 12.7
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1.4,
                    unitPrice: 34.6
                ),
                new SalesItemPricingItem(type: PricingItemType.sum),    // e1 = 199.97
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 7.3,
                    unitPrice: 21.9
                ),
                new SalesItemPricingItem(type: PricingItemType.sum)     // e2 = e1 + 159.87 = 359.84
            ]
        )

        when: 'I compute the total'
        BigDecimal total = s.computeTotalOfItem(3)

        then: 'I get the correct value'
        199.97 == total

        when: 'I compute the total of another item'
        total = s.computeTotalOfItem(5)

        then: 'I get the correct value'
        359.84 == total
    }

    def 'Compute unit price in empty lists'(int pos) {
        when: 'I create an empty sales item pricing and compute the unit price'
        def s = new SalesItemPricing()
        s.computeUnitPriceOfItem pos

        then: 'an exception is thrown'
        thrown NullPointerException

        when: 'I set an empty list and compute the unit price'
        s.items = []
        s.computeUnitPriceOfItem pos

        then: 'I get another exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [0i, 1i, 10i, 10574i, -1i, -10i, -45709i]
    }

    def 'Compute unit price with illegal positions'(int pos) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem(),
                new SalesItemPricingItem()
            ]
        )

        when: 'I compute the unit price'
        s.computeUnitPriceOfItem pos

        then: 'I get an exception'
        thrown ArrayIndexOutOfBoundsException

        where:
        pos << [5i, 6i, 10i, 10574i, -1i, -10i, -45709i]
    }

    def 'Compute unit price of absolute items'(BigDecimal up) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, unitPrice: up
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, unitPrice: 2 * (up ?: 0.0)
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, unitPrice: 3 * (up ?: 0.0)
                ),
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(0)

        then: 'I get the correct value'
        (up ?: 0.0) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(1)

        then: 'I get the correct value'
        (2 * (up ?: 0.0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(2)

        then: 'I get the correct value'
        (3 * (up ?: 0.0)) == unitPrice

        where:
        up << [null, 0, 0.00000000001, 1, 2.478, 17450.4739]
    }

    def 'Compute unit price of relativeToPos items'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2.5,
                    unitPrice: 45.78
                ),  // e0 = 2.5 * 45.78 = 114.45
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, unitPercent: p
                ),  // e1 = 0.0, because not related to any item
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 0i,
                    quantity: 1, unitPercent: p
                ),  // e2 = e0 * p / 100 = 1.1445p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 1i,
                    unitPercent: p
                ),  // e3 = 0.0, because related to an item without relation
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 2i,
                    quantity: 2.0, unitPercent: p
                ),  // e4 = 1 * e2 * p / 100 = 0.011445p²
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToPos, relToPos: 6i,
                    quantity: 2.5, unitPercent: p
                ),  // e5 = e6 * p / 100 = 1.1445p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2.5,
                    unitPrice: 45.78
                ),  // e6 = 2.5 * 45.78 = 114.45
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(1)

        then: 'I get zero because the item has no relation'
        0.0 == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(2)

        then: 'I get the correct value because it relates to a absolute item'
        (1.1445 * (p ?: 0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(3)

        then: 'I get zero because it relates to an item that has no relation'
        0.0 == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(4)

        then: 'I get the correct value because it relates to a absolute item'
        (0.011445 * (p ?: 0) * (p ?: 0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(5)

        then: 'I get the correct value because it relates to a absolute item'
        (1.1445 * (p ?: 0)) == unitPrice

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute unit price of relativeToCurrentSum items'(BigDecimal p) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3, unitPrice: 40
                ),  // currentSum == 190.0
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToCurrentSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 190.0 * p / 100.0 = 1.9p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),  // currentSum == 290.0 + 2.5e1 = 290.0 + 4.75p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToCurrentSum, quantity: 2,
                    unitPercent: p
                )   // e2 = (290.0 + 4.75p) * p / 100 = 2.9p + 0.0475p²
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(3)

        then: 'I get the correct value'
        (1.9 * (p ?: 0.0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(5)

        then: 'I get the correct value'
        (2.9 * (p ?: 0.0) + 0.0475 * (p ?: 0) * (p ?: 0)) == unitPrice

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute unit price of relativeToLastSum items without sums'(BigDecimal p)
    {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),  // currentSum = 70
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 0.7p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),  // currentSum = 170 + 2.5 * 0.7p = 170 + 1.75p
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2,
                    unitPercent: p
                )   // e2 = (170 + 1.75p) * p / 100 = 1.7p + 0.0175p²
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(2)

        then: 'I get the correct value'
        (0.7 * (p ?: 0.0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(4)

        then: 'I get the correct value'
        (1.7 * (p ?: 0.0) + 0.0175 * (p ?: 0) * (p ?: 0)) == unitPrice

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute unit price of relativeToLastSum items with sums'(BigDecimal p)
    {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 20
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 1, unitPrice: 30
                ),  // currentSum = 70
                new SalesItemPricingItem(type: PricingItemType.sum),    // currentSum = 70
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 3, unitPrice: 40
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2.5,
                    unitPercent: p
                ),  // e1 = 0.7p
                new SalesItemPricingItem(type: PricingItemType.sum),    // currentSum = 190 + 2.5 * 0.7p = 190 + 1.75p
                new SalesItemPricingItem(
                    type: PricingItemType.absolute, quantity: 2, unitPrice: 50
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.relativeToLastSum, quantity: 2,
                    unitPercent: p
                )   // e2 = (190 + 1.75p) * p / 100 = 1.9p + 0.0175p²
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(4)

        then: 'I get the correct value'
        (0.7 * (p ?: 0.0)) == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(7)

        then: 'I get the correct value'
        (1.9 * (p ?: 0.0) + 0.0175 * (p ?: 0) * (p ?: 0)) == unitPrice

        where:
        p << [null, 0.0, 0.00001, 1, 15.47, 50, 100, 200, 1000]
    }

    def 'Compute unit price of sum items'(BigDecimal up) {
        given: 'a sales item pricing with some items'
        def s = new SalesItemPricing(
            items: [
                new SalesItemPricingItem(
                    type: PricingItemType.sum, unitPrice: up
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.sum, unitPrice: 2 * (up ?: 0.0)
                ),
                new SalesItemPricingItem(
                    type: PricingItemType.sum, unitPrice: 3 * (up ?: 0.0)
                ),
            ]
        )

        when: 'I compute the unit price'
        BigDecimal unitPrice = s.computeUnitPriceOfItem(0)

        then: 'I get the correct value'
        0.0 == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(1)

        then: 'I get the correct value'
        0.0 == unitPrice

        when: 'I compute the unit price of another item'
        unitPrice = s.computeUnitPriceOfItem(2)

        then: 'I get the correct value'
        0.0 == unitPrice

        where:
        up << [null, 0, 0.00000000001, 1, 2.478, 17450.4739]
    }

    def 'Equals is null-safe'() {
        given: 'a sales item pricing'
        def s = new SalesItemPricing()

        expect:
        null != s
        s != null
        !s.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a sales item pricing'
        def s = new SalesItemPricing()

        expect:
        s != 'foo'
        s != 45
        s != 45.3
        s != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def s1 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        def s2 = new SalesItemPricing(quantity: 8, unit: new Unit(name: 'm'))
        def s3 = new SalesItemPricing(
            quantity: 10, unit: new Unit(name: 'units')
        )

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
        def s1 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s1.id = 7403L
        def s2 = new SalesItemPricing(quantity: 8, unit: new Unit(name: 'm'))
        s2.id = 7403L
        def s3 = new SalesItemPricing(
            quantity: 10, unit: new Unit(name: 'units')
        )
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
        def s1 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s1.id = 7403L
        def s2 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s2.id = 7404L
        def s3 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
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

    def 'Find position of the last sum'() {
        when: 'I create an empty sales item pricing'
        def s = new SalesItemPricing()

        then: 'no sum has been found'
        -1 == s.findPosOfLastSum()
        -1 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        -1 == s.findPosOfLastSum(1)

        when: 'I set an empty list'
        s.items = []

        then: 'a sum has been found'
        -1 == s.findPosOfLastSum()
        -1 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        -1 == s.findPosOfLastSum(1)

        when: 'I add a sum item'
        s.items << new SalesItemPricingItem(type: PricingItemType.sum)

        then: 'a sum has been found'
        0 == s.findPosOfLastSum()
        0 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        0 == s.findPosOfLastSum(1)

        when: 'I add one hundred other items'
        for (int i = 0; i < 100; i++) {
            s.items << new SalesItemPricingItem(
                type: PricingItemType.relativeToPos
            )
        }

        then: 'a sum has been found'
        0 == s.findPosOfLastSum()
        0 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        0 == s.findPosOfLastSum(1)
        0 == s.findPosOfLastSum(50)
        0 == s.findPosOfLastSum(100)
        0 == s.findPosOfLastSum(1000)

        when: 'I add another sum item'
        s.items << new SalesItemPricingItem(type: PricingItemType.sum)

        then: 'another sum has been found'
        101 == s.findPosOfLastSum()
        0 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        0 == s.findPosOfLastSum(1)
        0 == s.findPosOfLastSum(50)
        0 == s.findPosOfLastSum(100)
        101 == s.findPosOfLastSum(101)
        101 == s.findPosOfLastSum(102)
        101 == s.findPosOfLastSum(1000)

        when: 'I add another one hundred other items'
        for (int i = 0; i < 100; i++) {
            s.items << new SalesItemPricingItem(type: PricingItemType.absolute)
        }

        then: 'the last sum has been found'
        101 == s.findPosOfLastSum()
        0 == s.findPosOfLastSum(0)
        -1 == s.findPosOfLastSum(-1)
        0 == s.findPosOfLastSum(1)
        0 == s.findPosOfLastSum(50)
        0 == s.findPosOfLastSum(100)
        101 == s.findPosOfLastSum(101)
        101 == s.findPosOfLastSum(102)
        101 == s.findPosOfLastSum(201)
        101 == s.findPosOfLastSum(202)
        101 == s.findPosOfLastSum(1000)
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def i = new SalesItemPricing()

        expect:
        0i == i.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def i = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'm'))

        expect:
        0i == i.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def i = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'm'))
        i.id = 7403L

        when: 'I compute the hash code'
        int h = i.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            i = new SalesItemPricing(quantity: 7, unit: new Unit(name: 'h'))
            i.id = 7403L
            h == i.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s1.id = 7403L
        def s2 = new SalesItemPricing(quantity: 8, unit: new Unit(name: 'm'))
        s2.id = 7403L
        def s3 = new SalesItemPricing(
            quantity: 10, unit: new Unit(name: 'units')
        )
        s3.id = 7403L

        expect:
        s1.hashCode() == s2.hashCode()
        s2.hashCode() == s3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s1.id = 7403L
        def s2 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s2.id = 7404L
        def s3 = new SalesItemPricing(quantity: 5, unit: new Unit(name: 'h'))
        s3.id = 8473L

        expect:
        s1.hashCode() != s2.hashCode()
        s2.hashCode() != s3.hashCode()
    }

    def 'Can convert to string'(Long id) {
        given: 'an empty item'
        def s = new SalesItemPricing()

        when: 'I set the ID'
        s.id = id

        then: 'I get a valid string representation'
        ('Sales item pricing ' + id) == s.toString()

        where:
        id << [null, 0L, 1L, 470L, 75041L, 547042L]
    }

    def 'Quantity must be positive'(BigDecimal q, boolean v) {
        given: 'a quite valid instance'
        def s = new SalesItemPricing(
            unit: new Unit(name: 'h'),
            items: [new SalesItemPricingItem(unit: 'h', name: 'Service')]
        )

        when: 'I set the quantity'
        s.quantity = q

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        q       || v
        null    || false
        0.00    || false
        0.00001 || true
        1       || true
        1.27543 || true
        4750.79 || true
        -0.0001 || false
        -1      || false
        -1750.7 || false
    }

    def 'Unit must not be null'() {
        given: 'a quite valid instance'
        def s = new SalesItemPricing(
            quantity: 5.8,
            items: [new SalesItemPricingItem(unit: 'h', name: 'Service')]
        )

        when: 'I set the unit'
        s.unit = new Unit(name: 'h')

        then: 'the instance is valid'
        s.validate()

        when: 'I unset the unit'
        s.unit = null

        then: 'the instance is not valid'
        !s.validate()
    }

    def 'Discount percentage must be positive or zero'(BigDecimal dp, boolean v)
    {
        given: 'a quite valid instance'
        def s = new SalesItemPricing(
            quantity: 5.8,
            unit: new Unit(name: 'h'),
            items: [new SalesItemPricingItem(unit: 'h', name: 'Service')]
        )

        when: 'I set the discount percentage'
        s.discountPercent = dp

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        dp      || v
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

    def 'Items must be a non-empty list'() {
        given: 'a quite valid instance'
        def s = new SalesItemPricing(
            quantity: 5.8,
            unit: new Unit(name: 'h')
        )

        and: 'a pricing item'
        def pi = new SalesItemPricingItem(
            unit: 'h',
            name: 'Service',
            pricing: s
        ).save(failOnError: true)

        when: 'I unset the items'
        s.items = null

        then: 'the instance is not valid'
        !s.validate()

        when: 'I set the items to an empty list'
        s.items = []

        then: 'the instance is not valid'
        !s.validate()

        when: 'I add an item'
        s.items << pi

        then: 'the instance is valid'
        s.validate()
    }
}
