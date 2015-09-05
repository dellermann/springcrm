/*
 * InvoicingItemSpec.groovy
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

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(InvoicingItem)
@Mock([InvoicingItem])

class InvoicingItemSpec extends Specification {
	
	//-- Feature Methods ------------------------
	
	def 'Copy using constructor'() {
		given: 'an invoicing item with different properties'
		def i = new InvoicingItem(
			quantity: 15,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: 188.0d,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		
		when: 'I copy the invoicing item using the constructor'
		def i2 = new InvoicingItem(i)
		
		then: 'some properties are the same'
		i2.quantity == i.quantity
		i2.unit == i.unit
		i2.name == i.name
		i2.description == i.description
		i2.unitPrice == i.unitPrice
		i2.tax == i.tax
		i2.salesItem == i.salesItem		
		
		and: 'some properties are unset'
		!i2.id
	}
	
	def 'Get the total price'() {
		when: 'I create an invoicing item with quantity and unit price'
		def i = new InvoicingItem(
			quantity: 5,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: unitPrice,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		
		then: 'the I get the total price'
		total == i.getTotal()
		
		where:
		unitPrice		|	total
		1				|	5
		5				|	25
		8				|	40
		20				|	100
		103.6			|	518
		0				|	0
	}
	
	def 'Check for equality'() {
		given: 'two invoicing items with different properties'
		def i1 = new InvoicingItem(
			quantity: 5,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: 2,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		def i2 = new InvoicingItem(
			quantity: 3,
			unit: 'unit2',
			name: 'name2',
			description: 'description2',
			unitPrice: 1,
			tax: 12,
			salesItem: new SalesItem(
				type: 'type1',
				name: 'Johny',
				quantity: 42991221,
				unit: new Unit(),
				unitPrice: 200,
				taxRate: new TaxRate(),
				purchasePrice: 10342,
				salesStart: new Date(),
				salesEnd: new Date(),
				description: 'description2',
				pricing: new SalesItemPricing(),
				dateCreated: new Date(),
				lastUpdated: new Date()
			)
		)
		
		and: 'the same IDs'
		i1.id = 10010
		i2.id = 10010
		
		expect: 'both invoicing items to be equal'
		i1 == i2
		i2 == i1
	}
	
	def 'Check for inequality'() {
		given: 'two invoicing items with same properties'
		def i1 = new InvoicingItem(
			quantity: 5,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: 13,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		def i2 = new InvoicingItem(
			quantity: 5,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: 13,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		
		and: 'different IDs'
		i1.id = 944
		i2.id = 1003
		
		when: 'I compare both these invoicing items'
		boolean b1 = (i1 != i2)
		boolean b2 = (i2 != i1)
		
		then: 'they should not be equal'
		b1
		b2
		
		when:  'I compare with null'
		i2 = null
		
		then: 'they are not equal'
		i1 != i2
		i2 != i1
		
		when: 'I compare to another type'
		int integer = 3
		
		then: 'they are not equal'
		i1 != integer
	}
	
	def 'Compute hash code'() {
		when: 'I create an invoicing item with no ID'
		def i = new InvoicingItem()

		then: 'I get a valid hash code'
		0 == i.hashCode()

		when: 'I create an invoicing item with an ID'
		i.id = id

		then: 'I get a hash code using this ID'
		e == i.hashCode()

		where:
				id 	|   	   e
				 0 	|   	   0
				 1 	|   	   1
				10 	|  		  10
			   123 	|  		 123
			  5324 	|  		5324
			 12344  | 	   12344
		1023991929	| 1023991929
	}
	
	def 'Convert to string'() {
		given: 'an invoicing item'
		def i = new InvoicingItem(
			quantity: 5,
			unit: 'unit',
			name: 'name',
			description: 'description',
			unitPrice: 17,
			tax: 17,
			salesItem: new SalesItem(
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
		)
		
		expect:
		'name' == i.toString()
	}
}