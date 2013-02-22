/*
 * PurchaseInvoiceItem.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * @version 1.3
 */
class PurchaseInvoiceItem {

    //-- Class variables ------------------------

	static belongsTo = [invoice: PurchaseInvoice]
    static constraints = {
		number()
		quantity(min: 0.0d)
		unit()
		name(blank: false)
		description(nullable: true)
		unitPrice(widget: 'currency')
		tax(scale: 1, min: 0.0d, widget: 'percent')
    }
	static searchable = [only: ['number', 'name', 'description']]
	static transients = ['total']


    //-- Instance variables ---------------------

	String number
	double quantity
	String unit
	String name
	String description
	double unitPrice
	double tax


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

	double getTotal() {
		return quantity * unitPrice
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PurchaseInvoiceItem) {
            return obj.id == id
        } else {
            return false
        }
    }

    @Override
    public int hashCode() {
        return id as int
    }

    @Override
	String toString() {
		return name
	}
}
