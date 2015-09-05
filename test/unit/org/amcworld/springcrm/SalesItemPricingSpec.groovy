/*
 * ProjectSpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(SalesItemPricing)
@Mock([SalesItemPricing, SalesItemPricingItem])

class SalesItemPricingSpec extends Specification {
	
	//-- Instance Variables ----------------------
	
	SalesItemPricing s = new SalesItemPricing(
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
	)
	
	//-- Feature Methods -------------------------
	
	def 'Compute the discount percent amount'() {
		when: 'I set a discrete discount percent'
		s.discountPercent = dp
		
		then: 'I get the correct discount percent amount'
		e == s.discountPercentAmount
		s.validate()
		
		where:
		dp		| e
		1		| 0.18
		10		| 1.8
		20		| 3.6
	}
	
	def 'Compute step1TotalPrice'() {
		expect:
		s.step1TotalPrice == s.computeCurrentSum()
	}
	
	def 'Compute step1UnitPrice'() {
		when: 'I set a discrete quantity'
		s.quantity = q
		
		then: 'I get the correct unit price'
		e == s.step1UnitPrice
		
		where:
		q		| e
		0		| null
		1		| 18
		2		| 9
		10		| 1.8
		26		| 0.6923076923076923 
	}
	
	def 'Compute step2Total'() {
		when: 'I create a sales item pricing and set discrete discount percent and adjustment'
		def s = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0,
			items: [
				new SalesItemPricingItem(
					quantity: 10, unit: 'kg', name: 'name',
					unitPercent: 0, unitPrice: 10
				)
			]
		)
		
		s.discountPercent = dp 
		s.adjustment = adjustment 
		
		then: 'I get the correct step2Total'
		e == s.step2Total
		
		where:
		dp		| adjustment	| e
		0		| 0				| 100
		10		| 0				| 90
		5		| 0				| 95
		0.5		| 0				| 99.5
		0		| 10			| 110
		0		| 6				| 106
		0		| 250			| 350
		0		| 0.5			| 100.5
		10		| 10			| 100
		4		| 8				| 104
		35		| 15			| 80
	}
	
	def 'Compute step2TotalUnitPrice'() {
		when: 'I set a discrete quantity'
		s.quantity = q
		
		then: 'I get the correct unit price'
		e == s.step2TotalUnitPrice
		
		where:
		q		| e
		0		| null
		1		| 15.3
		2		| 7.65
		10		| 1.53
		26		| 0.5884615384615385
	}
	
	def 'Check if sales item pricing is true'() {
		when: 'I create a sales item pricing with items'
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
		)
		
		then: 'it is true'
		s.asBoolean() == true
		
		when: 'I unser the items'
		s.items = null
		
		then: 'it is false'
		s.asBoolean() == false
		
		when: 'I create a sales item pricing without items'
		def s2 = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0,
		)
		
		then: 'it is false'
		s.asBoolean() == false
	}
	
	def 'Compute total of item'() {
		expect:
		10 == s.computeTotalOfItem(0)
		6 == s.computeTotalOfItem(1)
		2 == s.computeTotalOfItem(2)
		
		when: 'I create only one item with discrete unit price and quantity'
		s.items = null
		s.items = [
			new SalesItemPricingItem(
				quantity: quantity, unit: 'kg', name: 'name',
				unitPercent: 0, unitPrice: up
			)
		]
		
		then:
		e == s.computeTotalOfItem(0)
		
		where:
		quantity	| up		| e
		1			| 5			| 5
		5			| 5			| 25
		46			| 3.6		| 165.6
		0			| 633		| 0
		37			| 81		| 2997
	}
	
	def 'Compute the current sum'() {
		when: 'I create a sales item pricing with one item'
		SalesItemPricing s = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0,
			items: [
				new SalesItemPricingItem(
					quantity: 5, unit: 'kg', name: 'name1',
					unitPercent: 0, unitPrice: 2
				)
			]
		)
		
		then:
		10 == s.computeCurrentSum()
		
		when: 'I add another item'
		s.items << new SalesItemPricingItem(
			quantity: 5, unit: 'kg', name: 'name2',
			unitPercent: 0, unitPrice: 2
		)
		
		then:
		20 == s.computeCurrentSum()
		
		when: 'I add two more items'
		s.items << new SalesItemPricingItem(
			quantity: 5, unit: 'kg', name: 'name3',
			unitPercent: 0, unitPrice: 2
		)
		s.items << new SalesItemPricingItem(
			quantity: 5, unit: 'kg', name: 'name4',
			unitPercent: 0, unitPrice: 2
		)
		
		then:
		40 == s.computeCurrentSum()
	}
	
	def 'Compute the last position of the item'() {
		given: 'a sales item pricing with items of type {@code SUM}'
		def s = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0,
			items: [
				new SalesItemPricingItem(
					quantity: 5, unit: 'kg', name: 'name',
					type: PricingItemType.sum,
					unitPercent: 0, unitPrice: 2
				),
				new SalesItemPricingItem(
					quantity: 3, unit: 'kg', name: 'name',
					type: PricingItemType.sum,
					unitPercent: 0, unitPrice: 2
				),
				new SalesItemPricingItem(
					quantity: 1, unit: 'kg', name: 'name',
					type: PricingItemType.sum,
					unitPercent: 0, unitPrice: 2
				)
			]
		)
		
		expect:
		2 == s.computeLastSumPos()
		
		when: 'I add another item of type {@code SUM}'
		s.items << new SalesItemPricingItem(
			quantity: 5, unit: 'kg', name: 'name',
			type: PricingItemType.sum,
			unitPercent: 0, unitPrice: 2
		)
		
		then:
		3 == s.computeLastSumPos()
		
		when: 'there are not items with type {@code SUM'
		def s2 = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0,
			items: [
				new SalesItemPricingItem(
					quantity: 5, unit: 'kg', name: 'name',
					type: PricingItemType.absolute,
					unitPercent: 0, unitPrice: 2
				),
				new SalesItemPricingItem(
					quantity: 3, unit: 'kg', name: 'name',
					type: PricingItemType.absolute,
					unitPercent: 0, unitPrice: 2
				),
				new SalesItemPricingItem(
					quantity: 1, unit: 'kg', name: 'name',
					type: PricingItemType.absolute,
					unitPercent: 0, unitPrice: 2
				)
			]
		)
		
		then:
		-1 == s2.computeLastSumPos()
	}
	
	def 'Compute the unit price for the item at the given position'() {
		given: 'a sales item pricing'
		def s = new SalesItemPricing(
			quantity: 1,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 0
		)
		
		when: 'I create an item with type {@code absolute}'
		s.items = [
			new SalesItemPricingItem(
				quantity: 6, unit: 'kg', name: 'name',
				type: PricingItemType.absolute,
				unitPercent: 0, unitPrice: 9
			)
		]
		
		then:
		9 == s.computeUnitPriceOfItem(0)
		
		when: 'I add an item with type {@code relativeToPos}'
		s.items << new SalesItemPricingItem(
			quantity: 6, unit: 'kg', name: 'name',
			type: PricingItemType.relativeToPos,
			unitPercent: 0, unitPrice: 9
		)
		
		then:
		0 == s.computeUnitPriceOfItem(1)
		
		when: 'I add an item with type {@code relativeToLastSum}'
		s.items << new SalesItemPricingItem(
			quantity: 6, unit: 'kg', name: 'name',
			type: PricingItemType.relativeToLastSum,
			unitPercent: 0, unitPrice: 9
		)
		
		then:
		0 == s.computeUnitPriceOfItem(2)
		
		// TODO
	}
	
	def 'Check for equality'() {
		when: 'I create to sales item pricing objects with different properties'
		def s1 = new SalesItemPricing(
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
		)
		def s2 = new SalesItemPricing(
			quantity: 2,
			unit: new Unit(),
			discountPercent: 5,
			adjustment: 13,
			items: [
				new SalesItemPricingItem(
					quantity: 2, unit: 'g', name: 'name2',
					unitPercent: 10, unitPrice: 12
				)
			]
		)
		
		and: 'same IDs'
		s1.id = 1
		s2.id = 1
		
		then: 'they are equal'
		s1 == s2
		s2 == s2
	}
	
	def 'Check for inequality'() {
		given: 'two sales item pricing objects with same properties'
		def s1 = new SalesItemPricing(
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
		)
		def s2 = new SalesItemPricing(
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
		)
		
		and: 'different IDs'
		s1.id = 1
		s2.id = 2
		
		when: 'I compare them'
		def b1 = (s1 != s2)
		def b2 = (s2 != s1)
		
		then: 'they are not eqal'
		b1
		b2
		
		when: 'I compare with null'
		s2 = null
		
		then: 'they are not equal'
		b1
		b2
		
		when: 'I compare with another type'
		int i = 4
		
		then: 'they are not equal'
		s1 != i
		i != s1
	}
	
	def 'Compute hash code'() {
		when: 'I create an sales item pricing with no ID'
		def s = new SalesItemPricing()

		then: 'I get a valid hash code'
		0 == s.hashCode()

		when: 'I create an sales item pricing with discrete IDs'
		s.id = id

		then: 'I get a hash code using this ID'
		e == s.hashCode()

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
		when: 'I set a discrete ID'
		s.id = 16
		
		then:
		"Sales item pricing 16" == s.toString()
	}
	
	def 'Quantity constraints'() {
		setup:
		mockForConstraintsTests(SalesItemPricing)
		
		when: 'I create a sales item pricing with discrete quantity and validate it'
		def s = new SalesItemPricing(
			quantity: quantity,
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
		)
		s.validate()
		
		then:
		!valid == s.hasErrors()
		
		where:
		quantity		| valid
		''				| true
		' '				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
		-50.0d			| false
		-19442			| false
	}
	
	def 'Discount percent constraints'() {
		setup:
		mockForConstraintsTests(SalesItemPricing)
		
		when: 'I create a sales item pricing with discrete quantity and validate it'
		def s = new SalesItemPricing(
			quantity: 3,
			unit: new Unit(),
			discountPercent: dp,
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
		)
		s.validate()
		
		then:
		!valid == s.hasErrors()
		
		where:
		dp		| valid
		''				| true
		' '				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
		-50.0d			| false
		-19442			| false
	}
	
	def 'Adjustment constraints'() {
		setup:
		mockForConstraintsTests(SalesItemPricing)
		
		when: 'I create a sales item pricing with discrete quantity and validate it'
		def s = new SalesItemPricing(
			quantity: 3,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: adjustment,
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
		)
		s.validate()
		
		then:
		!valid == s.hasErrors()
		
		where:
		adjustment		| valid
		''				| true
		' '				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
		-50.0d			| true
		-19442			| true
	}
	
	def 'Sales item pricing items constraints'() {
		setup:
		mockForConstraintsTests(SalesItemPricing)
		def s = new SalesItemPricing(
			quantity: 3,
			unit: new Unit(),
			discountPercent: 15,
			adjustment: 3,
		)
		
		when: 'I set one item and validate it'
		s.items = [
			new SalesItemPricingItem(
				quantity: 5, unit: 'kg', name: 'name',
				unitPercent: 0, unitPrice: 2
			)
		]
		s.validate()
		
		then: 'it is valid'
		!s.hasErrors()
		
		when: 'I set two items and validate it'
		s.items = [
			new SalesItemPricingItem(
				quantity: 5, unit: 'kg', name: 'name',
				unitPercent: 0, unitPrice: 2
			),
			new SalesItemPricingItem(
				quantity: 3, unit: 'kg', name: 'name',
				unitPercent: 0, unitPrice: 2
			)
		]
		s.validate()
		
		then: 'it is valid'
		!s.hasErrors()
		
		when: 'I set no items and validate it'
		s.items = []
		s.validate()
		
		then: 'it is not valid'
		s.hasErrors()
	}
}