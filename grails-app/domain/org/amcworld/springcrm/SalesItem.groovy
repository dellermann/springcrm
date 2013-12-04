/*
 * SalesItem.groovy
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
 * The class {@code SalesItem} acts as a base class for products and services.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class SalesItem {

    //-- Class variables ------------------------

    static constraints = {
        number unique: 'type', widget: 'autonumber'
        type blank: false, maxSize: 1
        name blank: false
        quantity min: 0.0d, validator: { quantity, salesItem ->
            ((quantity <= 0.0d) && (salesItem.pricing != null)) ? ['default.invalid.notGreater.message', 0] : null
        }
        unit nullable: true, validator: { unit, salesItem ->
            ((unit == null) && (salesItem.pricing != null)) ? 'default.null.message' : null
        }
        unitPrice min: 0.0d, widget: 'currency'
        taxRate nullable: true
        purchasePrice nullable: true, min: 0.0d, widget: 'currency'
        salesStart nullable: true
        salesEnd nullable: true
        description nullable: true, widget: 'textarea'
        pricing nullable: true, validator: { it?.validate() }
        dateCreated()
        lastUpdated()
    }
    static mapping = {
        description type: 'text'
        name index: 'name'
        sort 'number'
    }
    static transients = ['fullNumber', 'total']


    //-- Instance variables ---------------------

    def seqNumberService

    int number
    String type
    String name
    double quantity
    Unit unit
    double unitPrice
    TaxRate taxRate
    Double purchasePrice
    Date salesStart
    Date salesEnd
    String description
    SalesItemPricing pricing
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    SalesItem() {}

    SalesItem(SalesItem si) {
        name = si.name
        quantity = si.quantity
        unit = si.unit
        unitPrice = si.unitPrice
        taxRate = si.taxRate
        purchasePrice = si.purchasePrice
        salesStart = si.salesStart
        salesEnd = si.salesEnd
        description = si.description
    }


    //-- Properties -----------------------------

    String getFullNumber() {
        seqNumberService?.format getClass(), number
    }

    double getUnitPrice() {
        if (pricing) {
            double qty = (unit == pricing.unit) ? pricing.quantity : quantity
            this.unitPrice = qty ? pricing.step2Total / qty : 0.0d
        }
        this.unitPrice
    }

    double getTotal() {
        quantity * unitPrice
    }


    //-- Public methods -------------------------

    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
    }

    @Override
    boolean equals(Object obj) {
        (obj instanceof SalesItem) ? obj.id == id : false
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        name ?: ''
    }
}
