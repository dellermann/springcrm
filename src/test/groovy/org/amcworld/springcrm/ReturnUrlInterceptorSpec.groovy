/*
 * ReturnUrlInterceptorSpec.groovy
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

import static grails.web.mapping.ResponseRedirector.GRAILS_REDIRECT_ISSUED

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(ReturnUrlInterceptor)
class ReturnUrlInterceptorSpec extends Specification {

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
        'call'              | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'call'              | 'update'              || true
        'organization'      | 'update'              || true
        'user'              | 'update'              || true
        'call'              | 'delete'              || true
        'organization'      | 'delete'              || true
        'user'              | 'delete'              || true
        'user'              | 'saveSelValues'       || true
        'user'              | 'saveTaxRates'        || true
        'user'              | 'saveSeqNumbers'      || true
        'invoice'           | 'updatePayment'       || true
    }

    def 'The before interceptor action returns true'() {
        expect:
        interceptor.before()
    }

    def 'Without a return URL nothing is done'() {
        when: 'I call the action method without return URL'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'no redirect has been specified'
        null == request.getAttribute(GRAILS_REDIRECT_ISSUED)
    }

    def 'With a return URL a redirect is set'() {
        given: 'a return URL'
        params.returnUrl = '/organization/show/45'

        when: 'I call the action method without return URL'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'the redirect has been set'
        '/organization/show/45' == request.getAttribute(GRAILS_REDIRECT_ISSUED)
    }

    def 'With a return URL a previous redirect is overwritten'() {
        given: 'a return URL'
        params.returnUrl = '/organization/show/45'

        and: 'a previous redirect'
        request.setAttribute GRAILS_REDIRECT_ISSUED, '/person/show/31'

        when: 'I call the action method without return URL'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'the redirect has been overwritten'
        '/organization/show/45' == request.getAttribute(GRAILS_REDIRECT_ISSUED)
    }
}
