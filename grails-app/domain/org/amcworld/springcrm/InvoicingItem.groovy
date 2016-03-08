/*
 * InvoicingItem.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import static java.math.BigDecimal.ZERO


/**
 * The class {@code InvoicingItem} represents an item in invoicing transactions
 * such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class InvoicingItem {

    //-- Constants ------------------------------

    private static final BigInteger HUNDRED = new BigDecimal(100i)


    //-- Class fields ---------------------------

    static belongsTo = [invoicingTransaction: InvoicingTransaction]
    static constraints = {
        quantity min: ZERO, scale: 6
        unit blank: false
        name blank: false
        description nullable: true
        unitPrice scale: 6, widget: 'currency'
        tax scale: 2, min: ZERO, widget: 'percent'
        salesItem nullable: true
    }
    static mapping = {
        description type: 'text'
    }
    static transients = ['total', 'totalGross']


    //-- Fields ---------------------------------

    /**
     * The quantity of this item.
     */
    BigDecimal quantity = ZERO

    /**
     * The unit associated with the quantity of this item.
     */
    String unit

    /**
     * The name of this item.
     */
    String name

    /**
     * The description of this item.
     */
    String description

    /**
     * The net unit price of this item.
     */
    BigDecimal unitPrice = ZERO

    /**
     * The tax rate of this item in percent.
     */
    BigDecimal tax = ZERO

    /**
     * An optional associated sales item pointing to the product or works that
     * represents this item.
     */
    SalesItem salesItem


    //-- Constructors ---------------------------

    /**
     * Creates an empty invoicing item.
     */
    InvoicingItem() {}

    /**
     * Creates a new invoicing item using the values of the given item.
     *
     * @param i the given invoicing item
     */
    InvoicingItem(InvoicingItem i) {
        quantity = i.quantity
        unit = i.unit
        name = i.name
        description = i.description
        unitPrice = i.unitPrice
        tax = i.tax
        salesItem = i.salesItem
    }


    //-- Properties -----------------------------

    /**
     * Sets the quantity of this item.
     *
     * @param quantity  the quantity which should be set; if {@code null} it is
     *                  converted to zero
     * @since           2.0
     */
    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? ZERO : quantity
    }

    /**
     * Sets the tax rate of this item in percent.
     *
     * @param tax the tax rate which should be set; if {@code null} it is
     *            converted to zero
     * @since     2.0
     */
    void setTax(BigDecimal tax) {
        this.tax = tax == null ? ZERO : tax
    }

    /**
     * Gets the net total of this item.
     *
     * @return  the net total
     */
    BigDecimal getTotal() {
        quantity * unitPrice
    }

    /**
     * Gets the gross total of this item.
     *
     * @return  the gross total
     * @since   2.0
     */
    BigDecimal getTotalGross() {
        total * (HUNDRED + tax) / HUNDRED
    }

    /**
     * Sets the net unit price of this item.
     *
     * @param unitPrice the net unit price which should be set; if {@code null}
     *                  it is converted to zero
     * @since           2.0
     */
    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? ZERO : unitPrice
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof InvoicingItem && obj.id == id
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
