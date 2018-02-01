/*
 * SalesItem.groovy
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
import org.bson.types.ObjectId


/**
 * The class {@code SalesItem} acts as a base class for products and works.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.3
 */
@EqualsAndHashCode(includes = ['id'])
class SalesItem implements NumberedDomain {

    //-- Class fields ---------------------------

    static constraints = {
        number unique: 'type', widget: 'autonumber'
        type blank: false, maxSize: 1
        name blank: false
        quantity min: ZERO, scale: 6, validator: { quantity, salesItem ->
            (quantity <= ZERO && salesItem.pricing != null) \
                ? ['default.invalid.notGreater.message', 0]
                : null
        }
        unit nullable: true, validator: { unit, salesItem ->
            (unit == null && salesItem.pricing != null) \
                ? 'default.null.message'
                : null
        }
        unitPrice min: ZERO, scale: 6, widget: 'currency'
        taxRate nullable: true
        purchasePrice nullable: true, min: ZERO, scale: 6, widget: 'currency'
        salesStart nullable: true
        salesEnd nullable: true
        description nullable: true, widget: 'textarea'
        pricing nullable: true, validator: { it?.validate() }
    }
    static mapping = {
        description type: 'text'
        name index: true
        sort 'number'
    }
    static transients = ['total']


    //-- Fields ---------------------------------

    /**
     * The timestamp when this sales item has been created.
     */
    Date dateCreated

    /**
     * A description of this sales item.
     */
    String description

    /**
     * The ID of the sales item.
     */
    ObjectId id

    /**
     * The timestamp when the sales item has been modified.
     */
    Date lastUpdated

    /**
     * The name of the sales item.
     */
    String name

    /**
     * A detailed pricing for the sales item.  If not {@code null} the unit
     * price is obtained from the pricing.
     */
    SalesItemPricing pricing

    /**
     * The net purchase price of the sales item.
     */
    BigDecimal purchasePrice

    /**
     * The quantity of the sales item.
     */
    BigDecimal quantity = ZERO

    /**
     * The date when sale of the item starts.
     */
    Date salesStart

    /**
     * The date when sale of the item ends.
     */
    Date salesEnd

    /**
     * The tax rate of the sales item.
     */
    TaxRate taxRate

    /**
     * The type of the sales item, either {@code P} for products or {@code S}
     * for works.
     */
    String type

    /**
     * The unit associated with the quantity of the sales item.
     */
    Unit unit

    /**
     * The net unit price of the sales item.
     */
    BigDecimal unitPrice = ZERO


    //-- Constructors ---------------------------

    /**
     * Creates an empty sales item.
     */
    SalesItem() {}

    /**
     * Creates a sales item using the data of the given sales item.
     *
     * @param item  the given sales item
     */
    SalesItem(SalesItem item) {
        name = item.name
        quantity = item.quantity
        unit = item.unit
        unitPrice = item.unitPrice
        taxRate = item.taxRate
        purchasePrice = item.purchasePrice
        salesStart = item.salesStart ? new Date(item.salesStart.time) : null
        salesEnd = item.salesEnd ? new Date(item.salesEnd.time) : null
        description = item.description
        pricing = item.pricing
    }


    //-- Properties -----------------------------

    /**
     * Sets the quantity of the sales item.
     *
     * @param quantity  the quantity that should be set; if {@code null} it is
     *                  converted to zero
     * @since 2.0
     */
    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? ZERO : quantity
    }

    /**
     * Gets the total price of the sales item.
     *
     * @return  the total price
     */
    BigDecimal getTotal() {
        quantity * unitPrice
    }

    /**
     * Gets the unit price of the sales item.  The unit price is either
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
     * Set the unit price of the sales item.
     *
     * @param unitPrice the unit price that should be set; if {@code null} it
     *                  is converted to zero
     * @since 2.0
     */
    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? ZERO : unitPrice
    }


    //-- Public methods -------------------------

    @Override
    String toString() {
        name ?: ''
    }
}
