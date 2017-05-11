/*
 * ListInterceptorSpec.groovy
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

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(ListInterceptor)
class ListInterceptorSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Interceptor matches the correct controller/action pairs'(
        String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: 'Call', action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        a               || b
        null            || false
        'create'        || false
        'show'          || false
        'update'        || false
        'index'         || true
        'listEmbedded'  || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.afterView()
        interceptor.before()
    }

    def 'A default maximum value is set'() {
        when: 'I call the interceptor method'
        interceptor.before()

        then: 'a default value has been set'
        10 == params.max
    }

    def 'The maximum value is set correctly'(int m, int e) {
        given: 'a maximum value'
        params.max = m

        when: 'I call the interceptor method'
        interceptor.before()

        then: 'a value has been set correctly'
        e == params.max

        where:
        m       || e
        0       || 10
        1       || 10
        5       || 10
        10      || 10
        15      || 15
        20      || 20
        50      || 50
        90      || 90
        100     || 100
        101     || 100
        110     || 100
        3485049 || 100
    }
}
