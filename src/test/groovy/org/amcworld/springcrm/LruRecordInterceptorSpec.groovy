/*
 * LruRecordInterceptorSpec.groovy
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


class LruRecordInterceptorSpec extends Specification
    implements InterceptorUnitTest<LruRecordInterceptor>
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
        'phoneCall'         | 'index'               || false
        'organization'      | 'index'               || false
        'user'              | 'index'               || false
        'phoneCall'         | 'show'                || true
        'organization'      | 'show'                || true
        'user'              | 'show'                || true
        'phoneCall'         | 'edit'                || true
        'organization'      | 'edit'                || true
        'user'              | 'edit'                || true
        'phoneCall'         | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'phoneCall'         | 'update'              || true
        'organization'      | 'update'              || true
        'user'              | 'update'              || true
        'phoneCall'         | 'updatePayment'       || true
        'organization'      | 'updatePayment'       || true
        'user'              | 'updatePayment'       || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No model causes no LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)
    }

    def 'An empty model causes no LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)
    }

    def 'A model with an invalid instance name causes no LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a model with an invalid instance name'
        interceptor.model = [foo: 'bar']

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)

        when: 'I use a wrong instance name and call the interceptor'
        webRequest.controllerName = 'phoneCall'
        interceptor.model = [noteInstance: new Note()]
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)
    }

    def 'An instance without ID causes no LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'a model with an instance without ID'
        interceptor.model = [callInstance: new PhoneCall()]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)
    }

    def 'An instance with errors causes no LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'an instance with errors'
        def call = new PhoneCall()
        call.validate()

        and: 'a model with an invalid instance name'
        interceptor.model = [callInstance: call]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        0 * interceptor.lruService.recordItem(_, _)
    }

    def 'A valid instance causes an LRU item'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'an instance with errors'
        def call = new PhoneCall(
            subject: 'Phone phoneCall',
            type: PhoneCallType.INCOMING,
            status: PhoneCallStatus.COMPLETED
        )
        call.save failOnError: true

        and: 'a model with an invalid instance name'
        interceptor.model = [callInstance: call]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no LRU item is recorded'
        1 * interceptor.lruService.recordItem('phoneCall', call)
    }
}
