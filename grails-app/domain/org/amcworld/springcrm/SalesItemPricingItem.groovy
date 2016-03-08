/*
 * SalesItemPricingItem.groovy
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
import static org.amcworld.springcrm.PricingItemType.*


/**
 * The class {@code SalesItemPricingItem} represents an item in the pricing of
 * a sales item.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.3
 */
class SalesItemPricingItem {

    //-- Class fields ---------------------------

    static belongsTo = [pricing: SalesItemPricing]
    static constraints = {
        quantity min: ZERO, scale: 6
        unit nullable: true, validator: { unit, pricing ->
            unit || pricing.type == sum ? null : 'default.null.message'
        }
        name nullable: true, validator: { name, pricing ->
            name || pricing.type == sum ? null : 'default.null.message'
        }
        relToPos nullable: true, min: 0i, validator: { relToPos, pricing ->
            (relToPos == null && pricing.type == relativeToPos) \
                ? 'default.null.message'
                : null
        }
        unitPercent min: ZERO, scale: 2, widget: 'percent'
        unitPrice scale: 6, widget: 'currency'
    }


    //-- Fields ---------------------------------

    /**
     * The quantity of this item.
     */
    BigDecimal quantity = ZERO

    /**
     * The unit of this item.
     */
    String unit

    /**
     * The name of this item.
     */
    String name

    /**
     * The type of this item which specifies how it is used in computation.
     */
    PricingItemType type = PricingItemType.absolute

    /**
     * The zero-based position of a related item if field {@code type} has
     * value {@code PricingItemType.relativeToPos}.
     */
    Integer relToPos

    /**
     * The percentage value used in computation if field {@code type} has one
     * of the following values:
     * <ul>
     *   <li>{@code PricingItemType.relativeToPos}</li>
     *   <li>{@code PricingItemType.relativeToLastSum}</li>
     *   <li>{@code PricingItemType.relativeToCurrentSum}</li>
     * </ul>
     */
    BigDecimal unitPercent = ZERO

    /**
     * The absolute unit price of this item.
     */
    BigDecimal unitPrice = ZERO


    //-- Properties -----------------------------

    /**
     * Sets the quantity of this item.
     *
     * @param quantity  the quantity which should be set; if {@code null} it is
     *                  converted to zero
     * @since           2.0
     */
    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? ZERO : quantity;
    }

    /**
     * Sets the percentage value used in computation if field {@code type} has
     * one of the following values:
     * <ul>
     *   <li>{@code PricingItemType.relativeToPos}</li>
     *   <li>{@code PricingItemType.relativeToLastSum}</li>
     *   <li>{@code PricingItemType.relativeToCurrentSum}</li>
     * </ul>
     *
     * @param unitPercent   the percentage value which should be set; if
     *                      {@code null} it is converted to zero
     * @since               2.0
     */
    void setUnitPercent(BigDecimal unitPercent) {
        this.unitPercent = unitPercent == null ? ZERO : unitPercent
    }

    /**
     * Sets the absolute unit price of this item.
     *
     * @param unitPrice the absolute unit price which should be set; if
     *                  {@code null} it is converted to zero
     * @since           2.0
     */
    void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice == null ? ZERO : unitPrice
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof SalesItemPricingItem && obj.id == id
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
