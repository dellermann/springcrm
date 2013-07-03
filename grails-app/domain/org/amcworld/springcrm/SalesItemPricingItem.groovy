/*
 * SalesItemPricingItem.groovy
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
 * The class {@code SalesItemPricingItem} represents an item in the pricing of
 * a sales item.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemPricingItem {

    //-- Class variables ------------------------

    static belongsTo = [pricing: SalesItemPricing]
    static constraints = {
        quantity min: 0.0d
        unit nullable: true, validator: { unit, pricing ->
            ((unit == null) && (pricing.type != PricingItemType.sum)) ? 'default.null.message' : null
        }
        name nullable: true, validator: { name, pricing ->
            ((name == null) && (pricing.type != PricingItemType.sum)) ? 'default.null.message' : null
        }
        type()
        relToPos nullable: true, min: 0i, validator: { relToPos, pricing ->
            ((relToPos == null) && (pricing.type == PricingItemType.relativeToPos)) ? 'default.null.message' : null
        }
        unitPercent scale: 2, min: 0.0d, widget: 'percent'
        unitPrice widget: 'currency'
    }
    static searchable = [only: ['name']]


    //-- Instance variables ---------------------

    double quantity
    String unit
    String name
    PricingItemType type = PricingItemType.absolute
    Integer relToPos
    double unitPercent
    double unitPrice


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        (obj instanceof SalesItemPricingItem) ? obj.id == id : false
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
