/*
 * TaxRateSpec.groovy
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

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(TaxRate)
class TaxRateSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty tax rate'
        def t = new TaxRate()

        then: 'the properties are initialized properly'
        null == t.name
        0 == t.orderId
        0.0 == t.taxValue
    }

    def 'Set decimal values to null converts them to zero'() {
        given: 'an empty tax rate'
        def t = new TaxRate()

        when: 'I set the decimal values to null'
        t.taxValue = null

        then: 'all decimal values are never null'
        0.0 == t.taxValue

        when: 'I create a tax rate with null values'
        t = new TaxRate(taxValue: null)

        then: 'all decimal values are never null'
        0.0 == t.taxValue
    }

    def 'Tax value must be positive or zero'(BigDecimal tv, boolean v) {
        given: 'a quite valid tax rate'
        def t = new TaxRate(name: '19 %', orderId: 10)

        when: 'I set the tax value'
        t.taxValue = tv

        then: 'the instance is valid or not'
        v == t.validate()

        where:
        tv      || v
        null    || true
        0.00    || true
        0.00001 || true
        1       || true
        1.27543 || true
        4750.79 || true
        -0.0001 || false
        -1      || false
        -1750.7 || false
    }
}
