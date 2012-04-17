/*
 * Product.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


/**
 * The class {@code Product} represents a product.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @see     Service
 */
class Product {

    //-- Class variables ------------------------

    static constraints = {
		number(unique: true, widget: 'autonumber')
		name(blank: false)
		category(nullable: true)
		manufacturer(nullable: true)
		retailer(nullable: true)
		quantity(min: 0.0)
		unit(nullable: true)
		unitPrice(scale: 2, min: 0.0, widget: 'currency')
		taxRate(nullable: true)
		purchasePrice(nullable: true, scale: 2, min: 0.0, widget: 'currency')
		weight(nullable: true, min: 0.0)
		commission(nullable: true, min: 0.0, widget: 'percent')
		salesStart(nullable: true)
		salesEnd(nullable: true)
        description(nullable: true, widget: 'textarea')
		dateCreated()
		lastUpdated()
    }
	static mapping = {
        description type: 'text'
        name index: 'name'
		sort 'number'
    }
	static searchable = true
	static transients = ['fullNumber']


    //-- Instance variables ---------------------

	def seqNumberService

	int number
	String name
	ProductCategory category
	String manufacturer
	String retailer
	BigDecimal quantity
	Unit unit
	BigDecimal unitPrice
	TaxRate taxRate
	BigDecimal purchasePrice
	BigDecimal weight
	BigDecimal commission
	Date salesStart
	Date salesEnd
	String description
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	Product() {}

	Product(Product p) {
		name = p.name
		category = p.category
		manufacturer = p.manufacturer
		retailer = p.retailer
		quantity = p.quantity
		unit = p.unit
		unitPrice = p.unitPrice
		taxRate = p.taxRate
		purchasePrice = p.purchasePrice
		weight = p.weight
		commission = p.commission
		salesStart = p.salesStart
		salesEnd = p.salesEnd
		description = p.description
	}


    //-- Public methods -------------------------

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String toString() {
		return name ?: ''
	}

	def beforeInsert() {
		if (number == 0) {
			number = seqNumberService.nextNumber(getClass())
		}
	}
}
