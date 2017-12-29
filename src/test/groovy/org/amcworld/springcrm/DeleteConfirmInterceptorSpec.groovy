/*
 * DeleteConfirmInterceptorSpec.groovy
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


class DeleteConfirmInterceptorSpec extends Specification
    implements InterceptorUnitTest<DeleteConfirmInterceptor>
{

    //-- Feature methods ------------------------

    def 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a                     || b
        'call'              | null                  || false
        'organization'      | null                  || false
        'user'              | null                  || false
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'user'              | 'index'               || false
        'call'              | 'delete'              || true
        'organization'      | 'delete'              || true
        'user'              | 'delete'              || true
    }

    def 'The after interceptor action returns true'() {
        expect:
        interceptor.after()
    }

    def 'Without confirmed parameter the user is redirected'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'the user is redirected'
        '/call/index' == response.redirectedUrl
    }

    def 'With confirmed parameter the action is called'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'the confirmed parameter'
        params.confirmed = 1

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is called'
        res
    }
}
