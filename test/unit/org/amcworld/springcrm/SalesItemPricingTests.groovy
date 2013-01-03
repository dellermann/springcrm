/*
 * SalesItemPricingTests.groovy
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
 * The class {@code SalesItemPricingTests} contains the unit test cases for
 * {@code SalesItemPricing}.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class SalesItemPricingTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def pricing = new SalesItemPricing()
        assert 1.0 == pricing.quantity
    }

    void testAsBoolean() {
        def pricing = mockDomain()
        assert pricing

        pricing.items = []
        assert !pricing

        pricing.items = null
        assert !pricing
    }

    void testComputeTotalOfItem() {
        def pricing = mockDomain()
        assert 300 == pricing.computeTotalOfItem(0)
        assert 25.0 == pricing.computeTotalOfItem(1)
        assert 325 == pricing.computeTotalOfItem(2)
        assert 16.25 == pricing.computeTotalOfItem(3)
        assert 16.25 == pricing.computeTotalOfItem(4)
        assert 35.75 == pricing.computeTotalOfItem(5)
    }

    void testComputeUnitPriceOfItem() {
        def pricing = mockDomain()
        assert 0.1 == pricing.computeUnitPriceOfItem(0)
        assert 50.0 == pricing.computeUnitPriceOfItem(1)
        assert null == pricing.computeUnitPriceOfItem(2)
        assert 16.25 == pricing.computeUnitPriceOfItem(3)
        assert 16.25 == pricing.computeUnitPriceOfItem(4)
        assert 35.75 == pricing.computeUnitPriceOfItem(5)
    }

    void testGetCurrentSum() {
        def pricing = mockDomain()
        assert 393.25 == pricing.getCurrentSum()
        assert 393.25 == pricing.getCurrentSum(5)
        assert 357.5 == pricing.getCurrentSum(4)
        assert 341.25 == pricing.getCurrentSum(3)
        assert 325 == pricing.getCurrentSum(2)
        assert 325 == pricing.getCurrentSum(1)
        assert 300 == pricing.getCurrentSum(0)

        pricing.items = []
        assert 0.0 == pricing.getCurrentSum()

        pricing.items = null
        assert 0.0 == pricing.getCurrentSum()
    }

    void testGetDiscoundPercentAmount() {
        def pricing = mockDomain()
        assert 0.0 == pricing.discountPercentAmount

        pricing.discountPercent = 10.0
        assert 39.325 == pricing.discountPercentAmount

        pricing.items = []
        assert 0.0 == pricing.discountPercentAmount

        pricing.items = null
        assert 0.0 == pricing.discountPercentAmount
    }

    void testGetLastSumPos() {
        def pricing = mockDomain()
        assert 2 == pricing.getLastSumPos()
        assert 2 == pricing.getLastSumPos(5)
        assert 2 == pricing.getLastSumPos(4)
        assert 2 == pricing.getLastSumPos(3)
        assert 2 == pricing.getLastSumPos(2)
        assert -1 == pricing.getLastSumPos(1)
        assert -1 == pricing.getLastSumPos(0)
    }


    //-- Non-public methods ---------------------

    protected SalesItemPricing mockDomain() {
        return new SalesItemPricing(
            name: 'Netzwerkkabel', quantity: 3000.0, unit: 'm',
            items: [
                new SalesItemPricingItem(
                    quantity: 3000.0, unit: 'm', name: 'Netzwerkkabel',
                    type: PricingItemType.absolute, unitPrice: 0.1
                ),
                new SalesItemPricingItem(
                    quantity: 0.5, unit: 'h', name: 'Arbeitsleistung',
                    type: PricingItemType.absolute, unitPrice: 50.0
                ),
                new SalesItemPricingItem(
                    quantity: 0, unit: '', name: '',
                    type: PricingItemType.sum
                ),
                new SalesItemPricingItem(
                    quantity: 1, unit: 'Einheit', name: 'Gewinn',
                    type: PricingItemType.relativeToLastSum,
                    unitPercent: 5.0
                ),
                new SalesItemPricingItem(
                    quantity: 1, unit: 'Einheit', name: 'Risiko',
                    type: PricingItemType.relativeToPos, relToPos: 2,
                    unitPercent: 5.0
                ),
                new SalesItemPricingItem(
                    quantity: 1, unit: 'Einheit', name: 'Test',
                    type: PricingItemType.relativeToCurrentSum,
                    unitPercent: 10.0
                )
            ]
        )

    }
}
