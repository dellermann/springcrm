/*
 * SalesItemCosting.groovy
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
 * The class {@code SalesItemCosting} represents costings for a sales item
 * such as a product or service.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemCosting {

    //-- Class variables ------------------------

    static constraints = {
        name(blank: false)
        quantity(min: 0.0)
        unit(nullable: true)
        unitPrice(nullable: true, scale: 2, min: 0.0, widget: 'currency')
    }
    static hasMany = [items: SalesItemCostingItem]


    //-- Instance variables ---------------------

    String name
    BigDecimal quantity
    Unit unit
    BigDecimal unitPrice
    List<SalesItemCostingItem> items


    //-- Public methods -------------------------

    /**
     * Computes the total price for the item at the given position.
     *
     * @param pos   the given zero-based item position
     * @return      the total price of the item at the given position
     */
    BigDecimal computeTotalOfItem(int pos) {
        def item = items[pos]
        if (CostingItemType.SUM == item.type) {
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
        case CostingItemType.ABSOLUTE:
            return item.unitPrice
        case CostingItemType.RELATIVE_TO_POS:
            return item.unitPercent * computeTotalOfItem(item.relToPos) / 100
        case CostingItemType.RELATIVE_TO_LAST_SUM:
            Integer otherPos = getLastSumPos(pos - 1)
            if (otherPos >= 0) {
                return item.unitPercent * computeTotalOfItem(otherPos) / 100
            }
            // break through
        case CostingItemType.RELATIVE_TO_CURRENT_SUM:
            return item.unitPercent * getCurrentSum(pos - 1) / 100
        default:
            return null
        }
    }

    /**
     * Gets the sum of all items' total prices at the given position and
     * before.
     *
     * @return  the current sum
     */
    BigDecimal getCurrentSum(Integer pos = items.size() - 1) {
        BigDecimal sum = 0.0
        for (int i = pos; i >= 0; --i) {
            if (CostingItemType.SUM != items[i].type) {
                sum += computeTotalOfItem(i)
            }
        }
        return sum
    }

    /**
     * Gets the last position of the item of type {@code SUM}.
     *
     * @return  the zero-based position of the last subtotal sum; -1 if no such
     *          an item exists
     */
    int getLastSumPos(Integer start = items.size() - 1) {
        for (int i = start; i >= 0; --i) {
            if (items[i].type == CostingItemType.SUM) {
                return i
            }
        }

        return -1
    }
}
