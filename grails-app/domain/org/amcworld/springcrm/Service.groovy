/*
 * Service.groovy
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
 * The class {@code Service} represents a service from the service catalog.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 * @see     Product
 */
class Service {

    //-- Class variables ------------------------

    static constraints = {
		number(unique: true, widget: 'autonumber')
		name(blank: false)
		category(nullable: true)
		quantity(min: 0.0)
		unit(nullable: true)
		unitPrice(scale: 2, min: 0.0, widget: 'currency')
		taxRate(nullable: true)
		commission(nullable: true, min: 0.0, widget: 'percent')
		salesStart(nullable: true)
		salesEnd(nullable: true)
        description(nullable: true, widget: 'textarea')
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		sort 'number'
		description type: 'text'
    }
	static searchable = true
	static transients = ['fullNumber']


    //-- Instance variables ---------------------

	def seqNumberService

	int number
	String name
	ServiceCategory category
	BigDecimal quantity
	Unit unit
	BigDecimal unitPrice
	TaxRate taxRate
	BigDecimal commission
	Date salesStart
	Date salesEnd
	String description
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	Service() {}

	Service(Service s) {
		name = s.name
		category = s.category
		quantity = s.quantity
		unit = s.unit
		unitPrice = s.unitPrice
		taxRate = s.taxRate
		commission = s.commission
		salesStart = s.salesStart
		salesEnd = s.salesEnd
		description = s.description
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
