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


class SalesItemPricingItem {

    //-- Class variables ------------------------

    static constraints = {
        quantity(min: 0.0)
        unit()
        name(blank: false)
        type()
        relToPos(nullable: true, min: 0i)
        unitPercent(nullable: true, scale: 2, min: 0.0, widget: 'percent')
        unitPrice(nullable: true, scale: 2, widget: 'currency')
    }
    static belongsTo = [pricing: SalesItemPricing]
    static searchable = [only: ['name']]


    //-- Instance variables ---------------------

    BigDecimal quantity
    String unit
    String name
    PricingItemType type = PricingItemType.ABSOLUTE
    Integer relToPos
    BigDecimal unitPercent
    BigDecimal unitPrice
}
