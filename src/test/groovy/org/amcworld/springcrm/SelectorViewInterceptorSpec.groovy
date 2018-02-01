/*
 * SelectorViewInterceptorSpec.groovy
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


class SelectorViewInterceptorSpec extends Specification
    implements InterceptorUnitTest<SelectorViewInterceptor>
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
        'phoneCall'         | null                  || false
        'organization'      | null                  || false
        'user'              | null                  || false
        'calendarEvent'     | 'create'              || false
        'phoneCall'         | 'create'              || false
        'creditMemo'        | 'create'              || false
        'dunning'           | 'create'              || false
        'invoice'           | 'create'              || false
        'note'              | 'create'              || false
        'product'           | 'create'              || false
        'purchaseInvoice'   | 'create'              || false
        'quote'             | 'create'              || false
        'salesOrder'        | 'create'              || false
        'work'              | 'create'              || false
        'calendarEvent'     | 'index'               || true
        'phoneCall'         | 'index'               || true
        'creditMemo'        | 'index'               || true
        'dunning'           | 'index'               || true
        'invoice'           | 'index'               || true
        'note'              | 'index'               || true
        'product'           | 'index'               || true
        'purchaseInvoice'   | 'index'               || true
        'quote'             | 'index'               || true
        'salesOrder'        | 'index'               || true
        'work'              | 'index'               || true
    }

    def 'The before event methods return true'() {
        expect:
        interceptor.before()
    }

    @spock.lang.Ignore(
        '''Method render() in the mocked interceptor renders the whole view
        with errors about missing taglibs.'''
    )
    def 'The correct view is rendered'(String c, String v, String e) {
        given: 'a controller name'
        webRequest.controllerName = c

        and: 'a view parameter'
        params.view = v

        and: 'a model'
        interceptor.model = [foo: 'bar']

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the correct view is rendered'
        e == view

        and: 'the model is set'
        'bar' == interceptor.model.foo

        where:
        c               | v                 || e
        'phoneCall'     | null              || '/phoneCall/index'
        'quote'         | null              || '/quote/index'
        'dunning'       | null              || '/dunning/index'
        'work'          | null              || '/work/index'
        'phoneCall'     | 'selector'        || '/phoneCall/selectorList'
        'quote'         | 'selector'        || '/quote/selectorList'
        'dunning'       | 'selector'        || '/dunning/selectorList'
        'work'          | 'selector'        || '/work/selectorList'
        'phoneCall'     | 'foo'             || '/phoneCall/index'
        'quote'         | 'foo'             || '/quote/index'
        'dunning'       | 'foo'             || '/dunning/index'
        'work'          | 'foo'             || '/work/index'
    }
}
