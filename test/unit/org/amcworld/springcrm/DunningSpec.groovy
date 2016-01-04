/*
 * DunningSpec.groovy
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

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification


@TestMixin(GrailsUnitTestMixin)
class DunningSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Decimal values are never null'() {
        when: 'I create an empty reminder'
        def d = new Dunning()

        then: 'all decimal values are never null'
        0.0 == d.adjustment
        0.0 == d.discountAmount
        0.0 == d.discountPercent
        0.0 == d.paymentAmount
        0.0 == d.shippingCosts
        19.00 == d.shippingTax
        0.0 == d.total
    }

    def 'Cannot set decimal values to null'() {
        given: 'an empty reminder'
        def d = new Dunning()

        when: 'I set the decimal values to null'
        d.adjustment = null
        d.discountAmount = null
        d.discountPercent = null
        d.paymentAmount = null
        d.shippingCosts = null
        d.shippingTax = null
        d.total = null

        then: 'all decimal values are never null'
        0.0 == d.adjustment
        0.0 == d.discountAmount
        0.0 == d.discountPercent
        0.0 == d.paymentAmount
        0.0 == d.shippingCosts
        0.0 == d.shippingTax
        0.0 == d.total

        when: 'I create a reminder with null values'
        d = new Dunning(
            adjustment: null, discountAmount: null, discountPercent: null,
            paymentAmount: null, shippingCosts: null, shippingTax: null,
            total: null
        )

        then: 'all decimal values are never null'
        0.0 == d.adjustment
        0.0 == d.discountAmount
        0.0 == d.discountPercent
        0.0 == d.paymentAmount
        0.0 == d.shippingCosts
        0.0 == d.shippingTax
        0.0 == d.total
    }
}
