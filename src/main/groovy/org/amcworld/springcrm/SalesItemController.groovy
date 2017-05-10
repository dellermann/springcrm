/*
 * SalesItemController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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
 * The class {@code SalesItemController} represents a base class of controllers
 * which handle products and services.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
class SalesItemController<T extends SalesItem> extends GeneralController<T> {

    //-- Fields ---------------------------------

    SalesItemService salesItemService


    //-- Constructors ---------------------------

    SalesItemController(Class<? extends T> domainType) {
        super(domainType)
    }


    //-- Public methods -------------------------

    def index() {
        if (params.letter) {
            int max = params.int('max')
            int num = domainType.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            params.offset = Math.floor(num / max) * max
        }

        super.index()
    }

    def selectorList() {
        String searchFilter =
            params.search ? "%${params.search}%".toString() : ''
        String letter = params.letter?.toString()
        if (letter) {
            int max = params.int('max')
            int num
            if (params.search) {
                num = domainType.countByNameLessThanAndNameLike(
                    letter, searchFilter
                )
            } else {
                num = domainType.countByNameLessThan(letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / max) * max
        }

        List<T> list
        int count
        if (params.search) {
            list = domainType.findAllByNameLike(searchFilter, params)
            count = domainType.countByNameLike(searchFilter)
        } else {
            list = domainType.list(params)
            count = domainType.count()
        }

        [
            (getDomainInstanceName('List')): list,
            (getDomainInstanceName('Total')): count
        ]
    }


    //-- Non-public methods ---------------------

    @Override
    protected void lowLevelDelete(T instance) {
        if (instance.pricing != null) {
            instance.pricing.delete failOnError: true, flush: true
        }
        instance.delete failOnError: true, flush: true
    }

    @Override
    protected T lowLevelSave() {
        T instance = domainType.newInstance()

        salesItemService.saveSalesItemPricing(instance, params) ? instance
            : null
    }

    @Override
    protected T lowLevelUpdate(T instance) {
        salesItemService.saveSalesItemPricing instance, params
    }
}
