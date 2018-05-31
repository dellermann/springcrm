/*
 * ListInterceptorSpec.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class ListInterceptorSpec extends Specification
    implements InterceptorUnitTest<ListInterceptor>
{

    //-- Feature methods ------------------------

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Interceptor matches the correct controller/action pairs'(
        String a, boolean b
    ) {
        when: 'a particular request is used'
        withRequest controller: 'PhoneCall', action: a

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

    void 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.afterView()
        interceptor.before()
    }

    void 'The character encoding is set at action listEmbedded'() {
        when: 'the listEmbedded action has been called'
        webRequest.actionName = 'listEmbedded'
        interceptor.after()

        then: 'the character encoding has been set'
        'UTF-8' == response.characterEncoding

        when: 'another action has been called'
        response.reset()
        webRequest.actionName = 'index'
        interceptor.after()

        then: 'the character encoding has not been changed'
        null == response.characterEncoding
    }

    void 'A default maximum value is set'() {
        when: 'the interceptor method is called'
        interceptor.before()

        then: 'a default value has been set'
        10 == params.max
    }

    void 'The maximum value is set correctly'(int m, int e) {
        given: 'a maximum value'
        params.max = m

        when: 'the interceptor method is called'
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
