/*
 * InvoicingItem.groovy
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


/**
 * The class {@code InvoicingItem} represents an item in invoicing transactions
 * such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class InvoicingItem {

    //-- Class variables ------------------------

    static belongsTo = [invoicingTransaction: InvoicingTransaction]
    static constraints = {
        quantity min: 0.0d
        unit blank: false
        name blank: false
        description nullable: true
        unitPrice widget: 'currency'
        tax scale: 1, min: 0.0d, widget: 'percent'
        salesItem nullable: true
    }
    static mapping = {
        description type: 'text'
    }
    static transients = ['total']


    //-- Instance variables ---------------------

    double quantity
    String unit
    String name
    String description
    double unitPrice
    double tax
    SalesItem salesItem


    //-- Constructors ---------------------------

    InvoicingItem() {}

    InvoicingItem(InvoicingItem i) {
        quantity = i.quantity
        unit = i.unit
        name = i.name
        description = i.description
        unitPrice = i.unitPrice
        tax = i.tax
        salesItem = i.salesItem
    }


    //-- Public methods -------------------------

    double getTotal() {
        quantity * unitPrice
    }

    @Override
    public boolean equals(Object obj) {
        (obj instanceof InvoicingItem) ? obj.id == id : false
    }

    @Override
    public int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        name
    }
}
