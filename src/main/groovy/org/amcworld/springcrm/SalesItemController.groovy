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
abstract class SalesItemController<T extends SalesItem>
    extends GenericDomainController<T>
{

    //-- Constructors ---------------------------

    SalesItemController(Class<? extends T> domainType) {
        super(domainType)
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
    protected <D extends T> D saveInstance(T instance) {
        saveOrUpdateInstance instance
    }

    private <D extends T> D saveOrUpdateInstance(T instance) {
        if (params.autoNumber) {
            params.number = instance.number
        }

        List<String> excludeList = ['pricing']
        for (String name : params.keySet()) {
            if (name.startsWith('pricing.')) {
                excludeList << name
            }
        }
        bindData instance, params, [exclude: excludeList]

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
        SalesItemPricing pricing = instance.pricing
        if (params.pricingEnabled) {
            if (pricing == null) {
                pricing = new SalesItemPricing()
                instance.pricing = pricing
            }
            pricing.properties = params.pricing
            if (pricing.items == null) {
                pricing.items = []
            } else {
                pricing.items.clear()
            }
            for (int i = 0; params.pricing?."items[${i}]"; i++) {
                pricing.addToItems params.pricing."items[${i}]"
            }
            if (!instance.validate() || !pricing.save(flush: true)) {
                instance.discard()
                pricing.discard()
                return null
            }
        } else if (pricing) {
            pricing.delete flush: true
            instance.pricing = null
        }

        lowLevelSave instance
    }

    @Override
    protected <D extends T> D updateInstance(T instance) {
        saveOrUpdateInstance instance
    }
}
