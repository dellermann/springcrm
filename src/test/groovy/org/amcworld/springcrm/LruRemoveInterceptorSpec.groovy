/*
 * LruRemoveInterceptorSpec.groovy
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


@TestFor(LruRemoveInterceptor)
class LruRemoveInterceptorSpec extends Specification {

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
        'document'          | null                  || false
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'document'          | 'index'               || false
        'call'              | 'delete'              || true
        'organization'      | 'delete'              || true
        'document'          | 'delete'              || false
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No removal without confirmation'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        when: 'I call the interceptor'
        interceptor.before()

        then: 'the remove method has not been called'
        0 * interceptor.lruService.removeItem(String, long)
    }

    def 'Removal with confirmation'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'some parameters'
        interceptor.params.confirmed = '1'
        interceptor.params.id = '25'

        when: 'I call the interceptor'
        interceptor.before()

        then: 'the remove method has not been called'
        1 * interceptor.lruService.removeItem('call', 25L)
    }
}
