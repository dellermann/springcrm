/*
 * TaxRateTests.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code TaxRateTests} contains the unit test cases for
 * {@code TaxRate}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(TaxRate)
@Mock(TaxRate)
class TaxRateTests {

    //-- Public methods -------------------------

	void testMinConstraints() {
		mockForConstraintsTests(TaxRate)
		def validationFields = ['taxValue']
		def taxRate = new TaxRate(taxValue:-0.5)
		assert !taxRate.validate(validationFields)
        assert 'min' == taxRate.errors['taxValue']

		taxRate = new TaxRate(taxValue:0)
		assert taxRate.validate(validationFields)
	}
}
