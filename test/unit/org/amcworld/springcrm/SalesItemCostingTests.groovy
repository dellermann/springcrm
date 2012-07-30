/*
 * SalesItemCostingTests.groovy
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

import grails.test.mixin.*
import org.junit.*


/**
 * The class {@code SalesItemCostingTests} contains the unit test cases for
 * {@code SalesItemCosting}.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
@TestFor(SalesItemCosting)
@Mock([SalesItemCosting, SalesItemCostingItem])
class SalesItemCostingTests {

    //-- Public methods -------------------------

    void testGetLastSumPos() {
        def costing = mockDomain()
        assert 2 == costing.getLastSumPos()
        assert 2 == costing.getLastSumPos(5)
        assert 2 == costing.getLastSumPos(4)
        assert 2 == costing.getLastSumPos(3)
        assert 2 == costing.getLastSumPos(2)
        assert -1 == costing.getLastSumPos(1)
        assert -1 == costing.getLastSumPos(0)
    }

    void testComputeUnitPriceOfItem() {
        def costing = mockDomain()
        assert 0.1 == costing.computeUnitPriceOfItem(0)
        assert 50.0 == costing.computeUnitPriceOfItem(1)
        assert null == costing.computeUnitPriceOfItem(2)
        assert 16.25 == costing.computeUnitPriceOfItem(3)
        assert 16.25 == costing.computeUnitPriceOfItem(4)
        assert 35.75 == costing.computeUnitPriceOfItem(5)
    }

    void testComputeTotalOfItem() {
        def costing = mockDomain()
        assert 300 == costing.computeTotalOfItem(0)
        assert 25.0 == costing.computeTotalOfItem(1)
        assert 325 == costing.computeTotalOfItem(2)
        assert 16.25 == costing.computeTotalOfItem(3)
        assert 16.25 == costing.computeTotalOfItem(4)
        assert 35.75 == costing.computeTotalOfItem(5)
    }

    void testGetCurrentSum() {
        def costing = mockDomain()
        assert 393.25 == costing.getCurrentSum()
        assert 393.25 == costing.getCurrentSum(5)
        assert 357.5 == costing.getCurrentSum(4)
        assert 341.25 == costing.getCurrentSum(3)
        assert 325 == costing.getCurrentSum(2)
        assert 325 == costing.getCurrentSum(1)
        assert 300 == costing.getCurrentSum(0)
    }


    //-- Non-public methods ---------------------

    protected SalesItemCosting mockDomain() {
        return new SalesItemCosting(
            name: 'Netzwerkkabel', quantity: 3000.0, unit: 'm',
            items: [
                new SalesItemCostingItem(
                    quantity: 3000.0, unit: 'm', name: 'Netzwerkkabel',
                    type: CostingItemType.ABSOLUTE, unitPrice: 0.1
                ),
                new SalesItemCostingItem(
                    quantity: 0.5, unit: 'h', name: 'Arbeitsleistung',
                    type: CostingItemType.ABSOLUTE, unitPrice: 50.0
                ),
                new SalesItemCostingItem(
                    quantity: 0, unit: '', name: '',
                    type: CostingItemType.SUM
                ),
                new SalesItemCostingItem(
                    quantity: 1, unit: 'Einheit', name: 'Gewinn',
                    type: CostingItemType.RELATIVE_TO_LAST_SUM,
                    unitPercent: 5.0
                ),
                new SalesItemCostingItem(
                    quantity: 1, unit: 'Einheit', name: 'Risiko',
                    type: CostingItemType.RELATIVE_TO_POS, relToPos: 2,
                    unitPercent: 5.0
                ),
                new SalesItemCostingItem(
                    quantity: 1, unit: 'Einheit', name: 'Test',
                    type: CostingItemType.RELATIVE_TO_CURRENT_SUM,
                    unitPercent: 10.0
                )
            ]
        )

    }
}
