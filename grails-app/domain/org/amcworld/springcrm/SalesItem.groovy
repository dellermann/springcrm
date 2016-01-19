/*
 * SalesItem.groovy
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
 * The class {@code SalesItem} acts as a base class for products and services.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.3
 */
class SalesItem {

    //-- Class fields ---------------------------

    static constraints = {
        number unique: 'type', widget: 'autonumber'
        type blank: false, maxSize: 1
        name blank: false
        quantity min: ZERO, validator: { quantity, salesItem ->
            (quantity <= ZERO && salesItem.pricing != null) \
                ? ['default.invalid.notGreater.message', 0]
                : null
        }
        unit nullable: true, validator: { unit, salesItem ->
            (unit == null && salesItem.pricing != null) \
                ? 'default.null.message'
                : null
        }
        unitPrice min: ZERO, widget: 'currency'
        taxRate nullable: true
        purchasePrice nullable: true, min: ZERO, widget: 'currency'
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


    //-- Fields ---------------------------------

    /**
     * The service used to obtain sequence numbers.
     */
    def seqNumberService

    /**
     * The number of this sales item.
     */
    int number

    /**
     * The type of this sales item, either {@code P} for products or {@code S}
     * for services.
     */
    String type

    /**
     * The name of this sales item.
     */
    String name

    /**
     * The quantity of this sales item.
     */
    BigDecimal quantity = ZERO

    /**
     * The unit associated with the quantity of this sales item.
     */
    Unit unit

    /**
     * The net unit price of this sales item.
     */
    BigDecimal unitPrice = ZERO

    /**
     * The tax rate of this sales item.
     */
    TaxRate taxRate

    /**
     * The net purchase price of this sales item.
     */
    BigDecimal purchasePrice

    /**
     * The date when sale of this item starts.
     */
    Date salesStart

    /**
     * The date when sale of this item ends.
     */
    Date salesEnd

    /**
     * A description of this sales item.
     */
    String description

    /**
     * A detailed pricing for this sales item.  If not {@code null} the unit
     * price is obtained from this pricing.
     */
    SalesItemPricing pricing

    /**
     * The timestamp when this sales item has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this sales item has been modified.
     */
    Date lastUpdated


    //-- Constructors ---------------------------

    /**
     * Creates an empty sales item.
     */
    SalesItem() {}

    /**
     * Creates a sales item using the data of the given sales item.
     *
     * @param si    the given sales item
     */
    SalesItem(SalesItem si) {
        name = si.name
        quantity = si.quantity
        unit = si.unit
        unitPrice = si.unitPrice
        taxRate = si.taxRate
        purchasePrice = si.purchasePrice
        salesStart = si.salesStart ? new Date(si.salesStart.time) : null
        salesEnd = si.salesEnd ? new Date(si.salesEnd.time) : null
        description = si.description
        pricing = si.pricing
    }


    //-- Properties -----------------------------

    /**
     * Gets the full number of this sales item including prefix and suffix.
     *
     * @return  the full number
     */
    String getFullNumber() {
        seqNumberService?.format getClass(), number
    }

    /**
     * Sets the quantity of this sales item.
     *
     * @param quantity  the quantity that should be set; if {@code null} it is
     *                  converted to zero
     * @since 2.0
     */
    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? ZERO : quantity
    }

    /**
     * Gets the total price of this sales item.
     *
     * @return  the total price
     */
    BigDecimal getTotal() {
        quantity * unitPrice
    }

    /**
     * Gets the unit price of this sales item.  The unit price is either
     * obtained by the underlying pricing or from field {@code unitPrice}.
     *
     * @return  the unit price
     */
    BigDecimal getUnitPrice() {
        if (pricing) {
            BigDecimal qty =
                (unit == pricing.unit) ? pricing.quantity : quantity
            unitPrice = qty ? pricing.step2TotalUnitPrice / qty : ZERO
        }

        unitPrice
    }

    /**
     * Set the unit price of this sales item.
     *
     * @param unitPrice the unit price that should be set; if {@code null} it
     *                  is converted to zero
     * @since 2.0
     */
    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? ZERO : unitPrice
    }


    //-- Public methods -------------------------

    /**
     * Called before this sales item is created in the underlying data store.
     * The method obtains the next available sequence number.
     */
    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof SalesItem && obj.id == id
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
