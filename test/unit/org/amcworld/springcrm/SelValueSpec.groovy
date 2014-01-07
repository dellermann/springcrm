/*
 * SelValueSpec.groovy
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


@TestFor(SelValue)
@Mock(SelValue)
class SelValueSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Preset values'() {
        when: 'I create an empty selection value'
        def sv = new SelValue()

        then: 'some values are preset'
        0 == sv.orderId
    }

    def 'Check for equality'() {
        given: 'two selection values with different content'
        def sv1 = new SelValue(name: 'foo', orderId: 10)
        def sv2 = new SelValue(name: 'bar', orderId: 20)

        and: 'the same IDs'
        sv1.id = 2043
        sv2.id = 2043

        expect: 'both these selection values are equal'
        sv2 == sv1
        sv1 == sv2
    }

    def 'Check for inequality'() {
        given: 'two selection values with the same content'
        def sv1 = new SelValue(name: 'foo', orderId: 10)
        def sv2 = new SelValue(name: 'foo', orderId: 10)

        and: 'both the IDs set to different values'
        sv1.id = 2043
        sv2.id = 2044

        when: 'I compare both these selection values'
        boolean b1 = (sv2 != sv1)
        boolean b2 = (sv1 != sv2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        sv2 = null

        then: 'they are not equal'
        sv2 != sv1
        sv1 != sv2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        sv1 != s
    }

    def 'Compute hash code'() {
        when: 'I create a selection value with no ID'
        def sv = new SelValue()

        then: 'I get a valid hash code'
        0 == sv.hashCode()

        when: 'I create a selection value with discrete IDs'
        sv.id = id

        then: 'I get a hash code using this ID'
        e == sv.hashCode()

        where:
           id |     e
            0 |     0
            1 |     1
           10 |    10
          105 |   105
         9404 |  9404
        37603 | 37603
    }

    def 'Convert to string'() {
        given: 'an empty selection value'
        def sv = new SelValue()

        when: 'I set the name'
        sv.name = 'foo'

        then: 'I get a useful string representation'
        'foo' == sv.toString()

        when: 'I empty the name'
        sv.name = ''

        then: 'I get an empty string representation'
        '' == sv.toString()

        when: 'I unset the name'
        sv.name = null

        then: 'I get an empty string representation'
        '' == sv.toString()
    }

    def 'Name constraints'() {
        setup:
        mockForConstraintsTests(SelValue)

        when:
        def sv = new SelValue(name: name, orderId: 10)
        sv.validate()

        then:
        !valid == sv.hasErrors()

        where:
        name            | valid
        null            | false
        ''              | false
        ' '             | false
        '      '        | false
        '  \t \n '      | false
        'foo'           | true
        'any subject'   | true
    }
}
