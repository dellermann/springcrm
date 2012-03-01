/*
 * PurchaseInvoiceItem.groovy
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
 * The class {@code PurchaseInvoiceItem} represents an item in a purchase
 * invoice.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class PurchaseInvoiceItem {

    //-- Class variables ------------------------

	static belongsTo = [invoice: PurchaseInvoice]
    static constraints = {
		number()
		quantity(min: 0.0)
		unit()
		name(blank: false)
		description(nullable: true)
		unitPrice(scale: 2, min: 0.0)
		tax(scale: 1, min: 0.0)
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

	PurchaseInvoiceItem() {}

	PurchaseInvoiceItem(PurchaseInvoiceItem i) {
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
