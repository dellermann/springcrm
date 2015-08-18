/*
 * ProductSpec.groovy
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

@TestFor(Product)
@Mock([Product])

class ProductSpec extends Specification {
	
	//-- Feature Methods ---------------------
	
	def 'Copy using constructor'() {
		given: 'a product with different properties'
		def p1 = new Product(
			category: new ProductCategory(),
			manufacturer: 'manufacturer',
			retailer: 'retailer',
			weight: 1993
		)
		when:
		def p2 = new Product(p1)
		
		then:
		p2.category == p1.category
		p2.manufacturer == p1.manufacturer
		p2.retailer == p1.retailer
		p2.weight == p1.weight
		
		and:
		p2.type == 'P'
	}
	
	def 'Manufacturer constraints'() {
		setup:
		mockForConstraintsTests(Product)
		
		when: 'I create a product with a manufacturer and validate it'
		def p = new Product(
			name: 'name',
			category: new ProductCategory(),
			manufacturer: manufacturer,
			retailer: 'test',
			weight: 115
		)
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		manufacturer	| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'Manufacturer'	| true
		'Manu 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'retailer constraints'() {
		setup:
		mockForConstraintsTests(Product)
		
		when: 'I create a product with a retailer and validate it'
		def p = new Product(
			name: 'name',
			category: new ProductCategory(),
			manufacturer: 'test',
			retailer: retailer,
			weight: 115
		)
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		retailer		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'Manufacturer'	| true
		'Manu 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'weight constraints'() {
		setup:
		mockForConstraintsTests(Product)
		
		when: 'I create a product with a weight and validate it'
		def p = new Product(
			name: 'name',
			category: new ProductCategory(),
			manufacturer: 'test',
			retailer: 'retailer',
			weight: weight
		)
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		weight			| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		4				| true
		100D			| true
		100.0d			| true
		1e2d			| true
	}
}