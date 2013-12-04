/*
 * SalesItemPricing.groovy
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
 * The class {@code SalesItemPricing} represents a pricing for a sales item
 * such as a product or service.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class SalesItemPricing {

    //-- Class variables ------------------------

    static constraints = {
        quantity min: 0.0d, validator: {
            it <= 0.0d ? ['default.invalid.notGreater.message', 0] : null
        }
        unit()
        discountPercent scale: 2, min: 0.0d, widget: 'percent'
        adjustment widget: 'currency'
        items minSize: 1
    }
    static hasMany = [items: SalesItemPricingItem]
    static mapping = {
        items cascade: 'all-delete-orphan'
    }
    static transients = [
        'discountPercentAmount', 'step1TotalPrice', 'step1UnitPrice',
        'step2Total', 'step2TotalUnitPrice'
    ]


    //-- Instance variables ---------------------

    double quantity = 1.0d
    Unit unit
    double discountPercent
    double adjustment
    List<SalesItemPricingItem> items


    //-- Properties -----------------------------

    /**
     * Gets the amount of discount for this sales item in step 2.
     *
     * @return  the discount amount
     */
    double getDiscountPercentAmount() {
        step1TotalPrice * discountPercent / 100.0d
    }

    /**
     * Gets the total price of this sales item in step 1 as sum of all pricing
     * items.
     *
     * @return  the total price of the sales item in step 1
     */
    double getStep1TotalPrice() {
        computeCurrentSum()
    }

    /**
     * Gets the unit price of this sales item in step 1 as ratio between the
     * sum of all pricing items and the quantity.
     *
     * @return  the unit price of the sales item in step 1; {@code null} if
     *          quantity is zero
     */
    Double getStep1UnitPrice() {
        (quantity == 0.0d) ? null : step1TotalPrice / quantity
    }

    /**
     * Gets the total of this sales item in step 2 which is the total price of
     * step 1 minus discount plus adjustment.
     *
     * @return  the total of the sales item in step 2
     */
    double getStep2Total() {
        step1TotalPrice - discountPercentAmount + (adjustment ?: 0.0d)
    }

    /**
     * Gets the total unit price of this sales item in step 2 as ratio between
     * the total of step 2 and the quantity.
     *
     * @return  the total unit price of the sales item in step 2; {@code null}
     *          if quantity is zero
     */
    Double getStep2TotalUnitPrice() {
        (quantity == 0.0d) ? null : step2Total / quantity
    }


    //-- Public methods -------------------------

    /**
     * Represents the boolean value of this sales item pricing.  The pricing is
     * {@code true} if and only if there are pricing items.
     *
     * @return  the boolean value of this pricing
     */
    boolean asBoolean() {
        !!items
    }

    /**
     * Computes the sum of all items' total prices at the given position and
     * before.
     *
     * @param pos   the given zero-based position
     * @return      the current sum
     */
    double computeCurrentSum(int pos = items.size() - 1) {
        double sum = 0.0d
        for (int i = pos; i >= 0; --i) {
            if (items[i] && PricingItemType.sum != items[i].type) {
                sum += computeTotalOfItem(i)
            }
        }
        sum
    }

    /**
     * Computes the last position of the item of type {@code SUM}.
     *
     * @param pos   the given zero-based position
     * @return      the zero-based position of the last subtotal sum; -1 if no
     *              such an item exists
     */
    int computeLastSumPos(int start = items.size() - 1) {
        for (int i = start; i >= 0; --i) {
            if (items[i] && PricingItemType.sum == items[i].type) {
                return i
            }
        }

        -1
    }

    /**
     * Computes the total price for the item at the given position.
     *
     * @param pos   the given zero-based item position
     * @return      the total price of the item at the given position
     */
    double computeTotalOfItem(int pos) {
        SalesItemPricingItem item = items[pos]
        (PricingItemType.sum == item.type) ? computeCurrentSum(pos - 1) \
            : item.quantity * computeUnitPriceOfItem(pos)
    }

    /**
     * Computes the unit price for the item at the given position.
     *
     * @param pos   the given zero-based item position
     * @return      the unit price of the item at the given position;
     *              {@code null} if the item is of type {@code SUM} which does
     *              not have a unit price
     */
    double computeUnitPriceOfItem(int pos) {
        SalesItemPricingItem item = items[pos]
        switch (item.type) {
        case PricingItemType.absolute:
            return item.unitPrice
        case PricingItemType.relativeToPos:
            return (item.relToPos == null) ? 0.0d
                : item.unitPercent * computeTotalOfItem(item.relToPos) / 100.0d
        case PricingItemType.relativeToLastSum:
            int otherPos = computeLastSumPos(pos - 1)
            if (otherPos >= 0) {
                return item.unitPercent * computeTotalOfItem(otherPos) / 100.0d
            }
            // fall through
        case PricingItemType.relativeToCurrentSum:
            return item.unitPercent * computeCurrentSum(pos - 1) / 100.0d
        default:
            return 0.0d
        }
    }

    @Override
    boolean equals(Object obj) {
        (obj instanceof SalesItemPricing) ? obj.id == id : false
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        "Sales item pricing ${id}"
    }
}
