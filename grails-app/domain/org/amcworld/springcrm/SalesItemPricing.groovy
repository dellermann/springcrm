/*
 * SalesItemPricing.groovy
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

import groovy.transform.CompileStatic


/**
 * The class {@code SalesItemPricing} represents a pricing for a sales item
 * such as a product or service.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.3
 */
class SalesItemPricing {

    //-- Constants ------------------------------

    private static final BigInteger HUNDRED = new BigDecimal(100i)


    //-- Class fields ---------------------------

    static constraints = {
        quantity min: ZERO, scale: 6, validator: {
            it <= ZERO ? ['default.invalid.notGreater.message', 0] : null
        }
        unit()
        discountPercent min: ZERO, scale: 2, widget: 'percent'
        adjustment scale: 6, widget: 'currency'
        items nullable: false, minSize: 1
    }
    static hasMany = [items: SalesItemPricingItem]
    static mapping = {
        items cascade: 'all-delete-orphan'
    }
    static transients = [
        'discountPercentAmount', 'step1TotalPrice', 'step1UnitPrice',
        'step2Total', 'step2TotalUnitPrice'
    ]


    //-- Fields ---------------------------------

    /**
     * The quantity specifying for how many this pricing is done.
     */
    BigDecimal quantity = BigDecimal.ONE

    /**
     * The unit associated to the quantity in field {@code quantity} this
     * pricing is done for.
     */
    Unit unit

    /**
     * A percentage discount value which is subtracted from the computed price.
     */
    BigDecimal discountPercent = ZERO

    /**
     * An additional adjustment of the computed price.
     */
    BigDecimal adjustment = ZERO

    /**
     * The pricing items used to compute the sales price.
     */
    List<SalesItemPricingItem> items


    //-- Properties -----------------------------

    /**
     * Sets an additional adjustment of the computed price.
     *
     * @param adjustment    the adjustment which should be set; if {@code null}
     *                      it is converted to zero
     * @since               2.0
     */
    void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment == null ? ZERO : adjustment
    }

    /**
     * Sets a percentage discount value which is subtracted from the computed
     * price.
     *
     * @param discountPercent   the discount percentage amount which should be
     *                          set; if {@code null} it is converted to zero
     * @since                   2.0
     */
    void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent == null ? ZERO : discountPercent
    }

    /**
     * Gets the amount of discount for this sales item in step 2.
     *
     * @return  the discount amount
     */
    @CompileStatic
    BigDecimal getDiscountPercentAmount() {
        step1TotalPrice * discountPercent / HUNDRED
    }

    /**
     * Sets the quantity specifying for how many this pricing is done.
     *
     * @param quantity  the quantity which should be set; if {@code null} it is
     *                  converted to zero
     * @since           2.0
     */
    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity == null ? ZERO : quantity
    }

    /**
     * Gets the total price of this sales item in step 1 as sum of all pricing
     * items.
     *
     * @return  the total price of the sales item in step 1
     */
    @CompileStatic
    BigDecimal getStep1TotalPrice() {
        computeCurrentSum()
    }

    /**
     * Gets the unit price of this sales item in step 1 as ratio between the
     * sum of all pricing items and the quantity.
     *
     * @return  the unit price of the sales item in step 1; {@code null} if
     *          quantity is zero
     */
    @CompileStatic
    BigDecimal getStep1UnitPrice() {
        quantity == ZERO ? null : step1TotalPrice / quantity
    }

    /**
     * Gets the total of this sales item in step 2 which is the total price of
     * step 1 minus discount plus adjustment.
     *
     * @return  the total of the sales item in step 2
     */
    @CompileStatic
    BigDecimal getStep2Total() {
        step1TotalPrice - discountPercentAmount + adjustment
    }

    /**
     * Gets the total unit price of this sales item in step 2 as ratio between
     * the total of step 2 and the quantity.
     *
     * @return  the total unit price of the sales item in step 2; {@code null}
     *          if quantity is zero
     */
    @CompileStatic
    BigDecimal getStep2TotalUnitPrice() {
        quantity == ZERO ? null : step2Total / quantity
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
     * Computes the sum of all items' total prices.
     *
     * @return                      the current sum
     * @throws NullPointerException if the list of items is {@code null}
     */
    @CompileStatic
    BigDecimal computeCurrentSum() {
        if (items == null) {
            throw new NullPointerException('List of items is null.')
        }

        items ? computeCurrentSum(items.size() - 1) : ZERO
    }

    /**
     * Computes the sum of all items' total prices at the given position and
     * before.
     *
     * @param pos                               the given zero-based position
     * @return                                  the current sum
     * @throws NullPointerException             if the list of items is
     *                                          {@code null}
     * @throws ArrayIndexOutOfBoundsException   if the given item position is
     *                                          either less than zero or
     *                                          greater than or equal to the
     *                                          number of items
     */
    @CompileStatic
    BigDecimal computeCurrentSum(int pos) {
        if (items == null) {
            throw new NullPointerException('List of items is null.')
        }
        int n = items.size()
        if (pos < 0 || pos >= n) {
            throw new ArrayIndexOutOfBoundsException(
                "Parameter pos is out of bound [0; ${n}[."
            )
        }

        BigDecimal sum = ZERO
        for (int i = pos; i >= 0; --i) {
            if (items[i] && PricingItemType.sum != items[i].type) {
                sum += computeTotalOfItem(i)
            }
        }

        sum
    }

    /**
     * Computes the total price for the item at the given position.
     *
     * @param pos                               the given zero-based item
     *                                          position
     * @return                                  the total price of the item at
     *                                          the given position
     * @throws NullPointerException             if the list of items is
     *                                          {@code null}
     * @throws ArrayIndexOutOfBoundsException   if the given item position is
     *                                          either less than zero or
     *                                          greater than or equal to the
     *                                          number of items
     */
    @CompileStatic
    BigDecimal computeTotalOfItem(int pos) {
        if (items == null) {
            throw new NullPointerException('List of items is null.')
        }
        int n = items.size()
        if (pos < 0 || pos >= n) {
            throw new ArrayIndexOutOfBoundsException(
                "Parameter pos is out of bound [0; ${n}[."
            )
        }

        SalesItemPricingItem item = items[pos]

        PricingItemType.sum == item.type ? computeCurrentSum(pos - 1)
            : item.quantity * computeUnitPriceOfItem(pos)
    }

    /**
     * Computes the unit price for the item at the given position.
     *
     * @param pos                               the given zero-based item
     *                                          position
     * @return                                  the unit price of the item at
     *                                          the given position; zero if the
     *                                          item is of type {@code SUM}
     *                                          which does not have a unit
     *                                          price
     * @throws NullPointerException             if the list of items is
     *                                          {@code null}
     * @throws ArrayIndexOutOfBoundsException   if the given item position is
     *                                          either less than zero or
     *                                          greater than or equal to the
     *                                          number of items
     */
    @CompileStatic
    BigDecimal computeUnitPriceOfItem(int pos) {
        if (items == null) {
            throw new NullPointerException('List of items is null.')
        }
        int n = items.size()
        if (pos < 0 || pos >= n) {
            throw new ArrayIndexOutOfBoundsException(
                "Parameter pos is out of bound [0; ${n}[."
            )
        }

        SalesItemPricingItem item = items[pos]
        switch (item.type) {
        case PricingItemType.absolute:
            return item.unitPrice
        case PricingItemType.relativeToPos:
            return (item.relToPos == null) ? ZERO
                : item.unitPercent * computeTotalOfItem(item.relToPos) / HUNDRED
        case PricingItemType.relativeToLastSum:
            int otherPos = findPosOfLastSum(pos - 1)
            if (otherPos >= 0) {
                return item.unitPercent * computeTotalOfItem(otherPos) / HUNDRED
            }
            // fall through
        case PricingItemType.relativeToCurrentSum:
            return item.unitPercent * computeCurrentSum(pos - 1) / HUNDRED
        default:
            return ZERO
        }
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof SalesItemPricing && obj.id == id
    }

    /**
     * Computes the last position of the item of type {@code SUM}.
     *
     * @return  the zero-based position of the last subtotal sum; -1 if no such
     *          an item exists
     */
    @CompileStatic
    int findPosOfLastSum() {
        items ? findPosOfLastSum(items.size() - 1) : -1
    }

    /**
     * Computes the last position of the item of type {@code SUM} starting at
     * the given position and searching backwards.
     *
     * @param pos   the given zero-based position
     * @return      the zero-based position of the last subtotal sum; -1 if no
     *              such an item exists
     */
    @CompileStatic
    int findPosOfLastSum(int start) {
        if (items != null) {
            for (int i = start; i >= 0; --i) {
                if (items[i] && PricingItemType.sum == items[i].type) {
                    return i
                }
            }
        }

        -1
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
