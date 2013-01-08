/*
 * SalesItemService.groovy
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
 * The class {@code SalesItemService} contains service methods which handle
 * sales items such as products and services as well as handling for pricing.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemService {

    //-- Public methods -------------------------

    boolean saveSalesItemPricing(SalesItem salesItem, def params) {
        if (params.autoNumber) {
            params.number = salesItem.number
        }
        salesItem.properties = params.findAll { !it.key.startsWith('pricing.') && it.key != 'pricing' }

        /*
         * Implementation notes:
         *
         * There are a lot of problems when we try to simply use data binding
         * and 1:n relation storage in GORM.  It simply doesn't work, at least
         * in Grails 2.1.1.
         *
         * I change the way data binding works.  The client submits the items
         * as array (items[...]) with subsequent indices.  The client must
         * ensure that no gaps (i. e. items[0], items[1], items[3], ...) are
         * submitted.  The following implementation simply clears the items
         * list and adds all submitted items, which itself undergo data
         * binding, to the list.  A disadvantage of this way is that GORM
         * deletes all previous items and then adds back all new items which is
         * somewhat inefficient and permanently allocates new IDs.
         */
        def pricing = salesItem.pricing
        if (params.pricingEnabled) {
            if (pricing == null) {
                pricing = new SalesItemPricing(salesItem: salesItem)
                salesItem.pricing = pricing
            }
            pricing.properties = params.pricing
            if (pricing.items == null) {
                pricing.items = []
            } else {
                pricing.items.clear()
            }
            for (int i = 0; params.pricing?."items[${i}]"; i++) {
                pricing.addToItems(params.pricing."items[${i}]")
            }

            boolean anyItemErrors = false
            for (SalesItemPricingItem item in pricing.items) {
                if (item.type != PricingItemType.sum) {
                    if (item.quantity == null) {
                        item.errors.rejectValue('quantity', 'default.null.message')
                    }
                    if (!item.name) {
                        item.errors.rejectValue('name', 'default.null.message')
                    }
                    if (item.unitPrice == null) {
                        item.errors.rejectValue('unitPrice', 'default.null.message')
                    }
                }
                if ((item.type == PricingItemType.relativeToPos)
                    && (item.relToPos == null))
                {
                    item.errors.rejectValue('relToPos', 'default.null.message')
                }
                if (((item.type == PricingItemType.relativeToCurrentSum)
                    || (item.type == PricingItemType.relativeToLastSum)
                    || (item.type == PricingItemType.relativeToPos))
                    && (item.unitPercent == null))
                {
                    item.errors.rejectValue('unitPercent', 'default.null.message')
                }
                anyItemErrors |= item.hasErrors()
            }
            if (anyItemErrors || !pricing.save()) {
                return false
            }
        } else if (pricing) {
            pricing.delete(flush: true)
            salesItem.pricing = null
        }

        return salesItem.save(flush: true)
    }
}
