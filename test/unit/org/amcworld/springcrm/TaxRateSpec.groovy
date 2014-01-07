/*
 * TaxRateSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import spock.lang.Specification


@TestFor(TaxRate)
@Mock(TaxRate)
class TaxRateSpec extends Specification {

    //-- Feature methods ------------------------

    def 'TaxValue constraints'() {
        setup:
        mockForConstraintsTests(TaxRate)

        when:
        def t = new TaxRate(name: 'foo', orderId: 10, taxValue: tv)
        t.validate()

        then:
        !valid == t.hasErrors()

        where:
        tv          | valid
        -259.34     | false
         -33.47     | false
          -3.14     | false
          -0.01     | false
           0        | true
           0.01     | true
           3.14     | true
          33.47     | true
         100.00     | true
         259.34     | true
    }
}
