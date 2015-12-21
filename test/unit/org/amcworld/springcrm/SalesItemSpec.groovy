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


@TestFor(SalesItem)
@Mock([SalesItem, SalesItemPricing])
class SalesItemSpec extends Specification {

	//-- Feature Methods --------------------

	def 'Copy using constructor'() {
		given:
		def s1 = new SalesItem(
			number: 1991,
			type: 'type',
			name: 'John',
			quantity: 4299122,
			unit: new Unit(),
			unitPrice: 199,
			taxRate: new TaxRate(),
			purchasePrice: 10442,
			salesStart: new Date(),
			salesEnd: new Date(),
			description: 'description',
			pricing: new SalesItemPricing(),
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		when:
		def s2 = new SalesItem(s1)

		then:
		s1.name == s2.name
		s1.quantity == s2.quantity
		s1.unit == s2.unit
		s1.unitPrice == s2.unitPrice
		s1.taxRate == s2.taxRate
		s1.purchasePrice == s2.purchasePrice
		s1.salesStart == s2.salesStart
		s1.salesEnd == s2.salesEnd
		s1.description == s2.description

		and:
		0 == s2.number
		null == s2.dateCreated
		null == s2.lastUpdated
		null == s2.pricing
		null == s2.type
	}

	def 'Get the full number'() {
		given: 'a sales item with mocked sequence number service'
		def s = new SalesItem()
		s.seqNumberService = Mock(SeqNumberService)
		s.seqNumberService.format(_, _) >> 'O-11332'

		expect:
		'O-11332' == s.fullNumber
	}

	def 'Get the unit price with different units'() {
		given: 'some units'
		def unit1 = new Unit(name: 'Stück')
		unit1.id = 1
		def unit2 = new Unit(name: 'm')
		unit2.id = 2

		and: 'a sales item'
		def s = new SalesItem(
			number: 1991,
			type: 'type',
			name: 'John',
			quantity: 1,
			unit: unit1,
			unitPrice: 199,
			taxRate: new TaxRate(),
			purchasePrice: 12,
			salesStart: new Date(),
			salesEnd: new Date(),
			description: 'description',
			pricing: new SalesItemPricing(
				quantity: 100,
				unit: unit2,
				discountPercent: 0,
				adjustment: 0,
				items: [
					new SalesItemPricingItem(
						quantity: 100,
						unitPrice: 1.2
					)
				]
			),
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		expect:
		1.2 == s.unitPrice
	}

	def 'Get the unit price with same units'() {
		given: 'some units'
		def unit = new Unit(name: 'm')
		unit.id = 1

		and: 'a sales item'
		def s = new SalesItem(
			number: 1991,
			type: 'type',
			name: 'John',
			quantity: 10,
			unit: unit,
			unitPrice: 199,
			taxRate: new TaxRate(),
			purchasePrice: 12,
			salesStart: new Date(),
			salesEnd: new Date(),
			description: 'description',
			pricing: new SalesItemPricing(
				quantity: 100,
				unit: unit,
				discountPercent: 0,
				adjustment: 0,
				items: [
			        new SalesItemPricingItem(
		        		quantity: 100,
		        		unitPrice: 1.2
	        		)
				]
			),
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		expect:
		0.012 == s.unitPrice
	}

	def 'Get the total price'() {
		when: 'I create a sales item with quantity and unit price'
		def s = new SalesItem(
			type: 'type',
			name: 'name',
			quantity: 5,
			unitPrice: unitPrice,
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		then: 'the method will calculate the total price'
		total == s.getTotal()

		where:
		unitPrice		|	total
		1				|	5
		5				|	25
		8				|	40
		20				|	100
		103.6			|	518
		0				|	0
	}

	def 'Simulate the save method in insert mode and check number'() {
		given: 'a sales item without number'
		def s = new SalesItem()
		s.seqNumberService = Mock(SeqNumberService)
		s.seqNumberService.nextNumber(_) >> 92283

		when: 'I simulate calling save() in insert mode'
		s.beforeInsert()

		then: 'the sequence number must be set'
		92283 == s.number
	}

	def 'Check for equality'() {
		given: 'two objects with different properties'
		def s1 = new SalesItem(
			type: 'type1',
			name: 'John',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		def s2 = new SalesItem(
			type: 'type2',
			name: 'Don',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		and: 'the same IDs'
		s1.id = 10010
		s2.id = 10010

		expect: 'both sales items to be equal'
		s1 == s2
		s2 == s1
	}

	def 'Check for inequality'() {
		given: 'two objects with same properties'
		def s1 = new SalesItem(
			type: 'type',
			name: 'John',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		def s2 = new SalesItem(
			type: 'type',
			name: 'John',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		and: 'different IDs'
		s1.id = 944
		s2.id = 1003

		when: 'I compare both these sales items'
		boolean b1 = (s1 != s2)
		boolean b2 = (s2 != s1)

		then: 'they should not be equal'
		b1
		b2

		when:  'I compare with null'
		s2 = null

		then: 'they are not equal'
		s1 != s2
		s2 != s1

		when: 'I compare to another type'
		int i = 3

		then: 'they are not equal'
		s1 != i
	}

	def 'Compute hash code'() {
		when: 'I create a sales item with no ID'
		def s = new SalesItem()

		then: 'I get a valid hash code'
		0 == s.hashCode()

		when: 'I create a sales item with an ID'
		s.id = id

		then: 'I get a hash code using this ID'
		e == s.hashCode()

		where:
				id 	|   	   e
				 0 	|   	   0
				 1 	|   	   1
				10 	|  		  10
			   123 	|  		 123
			  5324 	|  		5324
			 12344 	| 	   12344
		1023991929	| 1023991929
	}

	def 'Convert to string'() {
		given: 'a sales item with a name'
		def s = new SalesItem(
			type: 'type',
			name: 'book',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)

		expect:
		s.name == s.toString()

		when:
		s.name = ''

		then:
		'' == s.toString()

		when:
		s.name = null

		then:
		'' == s.toString()
	}

	def 'Type constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a type and validate it'
		def s = new SalesItem(
			type: type, name: 'name', quantity: 4, unitPrice: 130,
			dateCreated: new Date(), lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		type			| valid
		null			| false
		''				| false
		' '				| false
		1003			| false
		'abc'*100		| false
		'abc'*1000		| false
		1				| true
		'P'				| true
	}

	def 'Name constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a name and validate it'
		def s = new SalesItem(
			type: 'P', name: name, quantity: 4,
			dateCreated: new Date(), lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		name			| valid
		null			| false
		''				| false
		' '				| false
		1003			| true
		'Title'			| true
		'Title 1003'	| true
		'abc'*100		| true
		'abc'*1000		| true
	}

	def 'Quantity constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a quantity and validate it'
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: quantity, unitPrice: 130d,
			dateCreated: new Date(), lastUpdated: new Date()
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

	def 'Unit constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with an unit and validate it'
		def s = new SalesItem(
			type: 'P', name: 'Test', quantity: 0.0d, unit: unit,
			dateCreated: new Date(), lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		unit			| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
		'String'		| true
		'String 1003'	| true
	}

	def 'Unit price constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with an unit price and validate it'
		def s = new SalesItem(
			type: 'P', name: 'Test', quantity: 0.0d, unitPrice: unitPrice,
			dateCreated: new Date(), lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		unitPrice		| valid
		null			| true
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

	def 'Tax rate constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a tax rate and validate it'
		def s = new SalesItem(
			type: 'P', name: 'Test', quantity: 0.0d,
			taxRate: new TaxRate(taxValue: taxRate), dateCreated: new Date(),
			lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		taxRate			| valid
		null			| true
		''				| true
		' '				| true
		100D			| true
		100.0d			| true
		1e2d			| true
		100.12			| true
	}

	def 'Purchase price constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a purchase price and validate it'
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: 12, unitPrice: 13,
			purchasePrice: purchasePrice, dateCreated: new Date(),
			lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		purchasePrice	| valid
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

	def 'Description constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a description and validate it'
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: 12, unitPrice: 130,
			description: description, dateCreated: new Date(),
			lastUpdated: new Date()
		)
		s.validate()

		then:
		!valid == s.hasErrors()

		where:
		description		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'String'		| true
		'String 1003'	| true
	}

	def 'Pricing constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: 12, unit: new Unit(),
			unitPrice: 130, dateCreated: new Date(), lastUpdated: new Date()
		)

		when: 'I set a pricing and validate it'
		s.pricing = new SalesItemPricing(
			quantity: 40.0d, unit: new Unit(), discountPercent: 12.0d,
			adjustment: 14.5d,
			items:new SalesItemPricingItem(
	            quantity: 4, unit: 'pcs.',
	            name: 'books', type: 'P', relToPos: 2,
				unitPercent: 10.0d, unitPrice: 44.99
            )
		)
		s.validate()

		then:
		!s.hasErrors()
	}

	def 'Sales start constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a date for sales start and validate it'
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: 12, unitPrice: 130,
			dateCreated: new Date(), lastUpdated: new Date(),
			salesStart: new Date()
		)
		s.validate()

		then:
		!s.hasErrors()

		when:
		s.salesStart = null

		then:
		!s.hasErrors()
	}

	def 'Sales end constraints'() {
		setup:
		mockForConstraintsTests(SalesItem)

		when: 'I create a sales item with a date for sales end and validate it'
		def s = new SalesItem(
			type: 'P', name: 'name', quantity: 12, unitPrice: 130,
			dateCreated: new Date(), lastUpdated: new Date(),
			salesEnd: new Date()
		)
		s.validate()

		then:
		!s.hasErrors()

		when:
		s.salesEnd = null

		then:
		!s.hasErrors()
	}
}