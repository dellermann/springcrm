/*
 * InvoicingItem.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope


/**
 * The class {@code InvoicingItem} represents an item in invoicing transactions
 * such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode
class InvoicingItem {

    //-- Constants ------------------------------

    private static final BigDecimal HUNDRED = new BigDecimal(100i)


    //-- Class fields ---------------------------

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
    static transients = ['totalNet', 'totalGross']


    //-- Fields ---------------------------------

    /**
     * The description of the item.
     */
    String description

    /**
     * The name of the item.
     */
    String name

    /**
     * The quantity of the item.
     */
    BigDecimal quantity = ZERO

    /**
     * An optional associated sales item pointing to the product or works that
     * represents the item.
     */
    SalesItem salesItem

    /**
     * The tax rate of the item in percent.
     */
    BigDecimal tax = ZERO

    /**
     * The unit associated with the quantity of the item.
     */
    String unit

    /**
     * The net unit price of the item.
     */
    BigDecimal unitPrice = ZERO


    //-- Constructors ---------------------------

    /**
     * Creates an empty invoicing item.
     */
    InvoicingItem() {}

    /**
     * Creates a new invoicing item using the values of the given item.
     *
     * @param item  the given invoicing item
     */
    InvoicingItem(InvoicingItem item) {
        quantity = item.quantity
        unit = item.unit
        name = item.name
        description = item.description
        unitPrice = item.unitPrice
        tax = item.tax
        salesItem = item.salesItem
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
    BigDecimal getTotalNet() {
        BigDecimal res = quantity * unitPrice

        res
    }

    /**
     * Gets the gross total of this item.
     *
     * @return  the gross total
     * @since   2.0
     */
    BigDecimal getTotalGross() {
        BigDecimal res = totalNet * (HUNDRED + tax) / HUNDRED

        res
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

    /**
     * Called before the customer account is created in the underlying data
     * store.  The method computes the total values.
     *
     * @since 3.0
     */
    def beforeInsert() {
        computeDynamicValues()
    }

    /**
     * Called before the customer account is updated in the underlying data
     * store.  The method computes the total values.
     *
     * @since 3.0
     */
    def beforeUpdate() {
        computeDynamicValues()
    }

    @Override
    String toString() {
        name ?: ''
    }


    //-- Non-public methods ---------------------

    /**
     * Computes dynamic values such as the net and gross total.
     *
     * @since 3.0
     */
    @PackageScope
    void computeDynamicValues() {
        this['totalGross'] = totalGross
        this['totalNet'] = totalNet
    }
}
