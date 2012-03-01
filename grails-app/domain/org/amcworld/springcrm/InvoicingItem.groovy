/*
 * InvoicingItem.groovy
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
 * The class {@code InvoicingItem} represents an item in invoicing transactions
 * such as invoices, quotes etc.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class InvoicingItem {

    //-- Class variables ------------------------

	static belongsTo = [invoicingTransaction: InvoicingTransaction]
    static constraints = {
		number(blank: false)
		quantity(min: 0.0)
		unit()
		name(blank: false)
		description(nullable: true)
		unitPrice(scale: 2, widget: 'currency')
		tax(nullable: false, scale: 1, min: 0.0, widget: 'percent')
    }
	static mapping = {
		description type: 'text'
	}
	static searchable = [only: ['number', 'name', 'description']]
	static transients = ['total']


    //-- Instance variables ---------------------

	String number
	BigDecimal quantity
	String unit
	String name
	String description
	BigDecimal unitPrice
	BigDecimal tax


    //-- Constructors ---------------------------

	InvoicingItem() {}

	InvoicingItem(InvoicingItem i) {
		number = i.number
		quantity = i.quantity
		unit = i.unit
		name = i.name
		description = i.description
		unitPrice = i.unitPrice
		tax = i.tax
	}


    //-- Public methods -------------------------

	BigDecimal getQuantity() {
		return quantity ?: 0
	}

	BigDecimal getUnitPrice() {
		return unitPrice ?: 0
	}

	BigDecimal getTotal() {
		return getQuantity() * getUnitPrice()
	}

	String toString() {
		return name
	}
}
