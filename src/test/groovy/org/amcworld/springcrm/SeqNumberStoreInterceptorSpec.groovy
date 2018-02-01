/*
 * SeqNumberStoreInterceptorSpec.groovy
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


class SeqNumberStoreInterceptorSpec extends Specification
    implements InterceptorUnitTest<SeqNumberStoreInterceptor>
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
        'phoneCall'         | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'Do not change number if number is not set'() {
        when: 'I call the interceptor'
        interceptor.before()

        then: 'no number has been set'
        null == params.number
    }

    def 'Do not change number if autoNumber is not set'() {
        given: 'a number'
        params.number = 34

        when: 'I call the interceptor'
        interceptor.before()

        then: 'no number has been set'
        34 == params.number
    }

    def 'Set number to zero if autoNumber is set'() {
        given: 'a number'
        params.number = 34
        params.autoNumber = true

        when: 'I call the interceptor'
        interceptor.before()

        then: 'the number has been reset'
        0 == params.number
    }

    def 'No model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.nextNumber(_)
    }

    def 'An empty model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.nextNumber(_)
    }

    def 'No instance leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'a model'
        interceptor.model = [foo: 'bar']

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.nextNumber(_)
    }

    def 'No numbered instance leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'a model'
        interceptor.model = [callInstance: new PhoneCall()]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.nextNumber(_)
    }

    def 'A numbered instance with sequence number leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'a model'
        interceptor.model = [noteInstance: new Note(number: 10474)]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.nextNumber(_)

        and: 'the number is unchanged'
        10474 == interceptor.model.noteInstance.number
    }

    def 'A numbered instance with no sequence number sets the next one'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'a model'
        interceptor.model = [noteInstance: new Note()]

        and: 'a controller name'
        webRequest.controllerName = 'note'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        1 * interceptor.seqNumberService.nextNumber('note') >> 12074

        and: 'the number is unchanged'
        12074 == interceptor.model.noteInstance.number
    }
}
