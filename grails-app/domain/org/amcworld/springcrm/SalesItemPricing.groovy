/*
 * SalesItemPricing.groovy
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
 * The class {@code SalesItemPricing} represents a pricing for a sales item
 * such as a product or service.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemPricing {

    //-- Class variables ------------------------

    static constraints = {
        name(blank: false)
        quantity(min: 0.0)
        unit()
        unitPrice(nullable: true, scale: 2, min: 0.0, widget: 'currency')
        items(minSize: 1)
    }
    static hasMany = [items: SalesItemPricingItem, salesItems: SalesItem]


    //-- Instance variables ---------------------

    String name
    BigDecimal quantity
    String unit
    BigDecimal unitPrice
    List<SalesItemPricingItem> items


    //-- Public methods -------------------------

    boolean asBoolean() {
        return !!items
    }

    /**
     * Computes the total price for the item at the given position.
     *
     * @param pos   the given zero-based item position
     * @return      the total price of the item at the given position
     */
    BigDecimal computeTotalOfItem(int pos) {
        def item = items[pos]
        if (PricingItemType.sum == item.type) {
            return getCurrentSum(pos - 1)
        } else {
            return item.quantity * computeUnitPriceOfItem(pos)
        }
    }

    /**
     * Computes the unit price for the item at the given position.
     *
     * @param pos   the given zero-based item position
     * @return      the unit price of the item at the given position;
     *              {@code null} if the item is of type {@code SUM} which does
     *              not have a unit price
     */
    BigDecimal computeUnitPriceOfItem(int pos) {
        def item = items[pos]
        switch (item.type) {
        case PricingItemType.absolute:
            return item.unitPrice
        case PricingItemType.relativeToPos:
            return item.unitPercent * computeTotalOfItem(item.relToPos) / 100
        case PricingItemType.relativeToLastSum:
            Integer otherPos = getLastSumPos(pos - 1)
            if (otherPos >= 0) {
                return item.unitPercent * computeTotalOfItem(otherPos) / 100
            }
            // fall through
        case PricingItemType.relativeToCurrentSum:
            return item.unitPercent * getCurrentSum(pos - 1) / 100
        default:
            return null
        }
    }

    /**
     * Gets the sum of all items' total prices at the given position and
     * before.
     *
     * @param pos   the given zero-based position
     * @return      the current sum
     */
    BigDecimal getCurrentSum(Integer pos = items.size() - 1) {
        BigDecimal sum = 0.0
        for (int i = pos; i >= 0; --i) {
            if (sum != items[i].type) {
                sum += computeTotalOfItem(i)
            }
        }
        return sum
    }

    /**
     * Gets the last position of the item of type {@code SUM}.
     *
     * @param pos   the given zero-based position
     * @return      the zero-based position of the last subtotal sum; -1 if no
     *              such an item exists
     */
    int getLastSumPos(Integer start = items.size() - 1) {
        for (int i = start; i >= 0; --i) {
            if (PricingItemType.sum == items[i].type) {
                return i
            }
        }

        return -1
    }
}
