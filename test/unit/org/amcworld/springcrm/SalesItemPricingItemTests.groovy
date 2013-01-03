/*
 * SalesItemPricingItemTests.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SalesItemPricingItem)
@Mock([SalesItemPricing, SalesItemPricingItem])
class SalesItemPricingItemTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def salesItemPricingItem = new SalesItemPricingItem()
        assert PricingItemType.absolute == salesItemPricingItem.type
    }

    void testConstraints() {
        mockForConstraintsTests(SalesItemPricingItem)
        def pricing = mockDomain(SalesItemPricing)

        def salesItemPricingItem = new SalesItemPricingItem(pricing: pricing)
        assert salesItemPricingItem.validate()

        salesItemPricingItem = new SalesItemPricingItem(
            pricing: pricing, quantity: -1.0, relToPos: -1, unitPercent: -1.0
        )
        assert !salesItemPricingItem.validate()
        assert 'min' == salesItemPricingItem.errors['quantity']
        assert 'min' == salesItemPricingItem.errors['relToPos']
        assert 'min' == salesItemPricingItem.errors['unitPercent']
    }
}
