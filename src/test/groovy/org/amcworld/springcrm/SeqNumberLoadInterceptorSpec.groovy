/*
 * SeqNumberLoadInterceptorSpec.groovy
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


class SeqNumberLoadInterceptorSpec extends Specification
    implements InterceptorUnitTest<SeqNumberLoadInterceptor>
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
        'phoneCall'         | 'create'              || true
        'organization'      | 'create'              || true
        'user'              | 'create'              || true
        'phoneCall'         | 'copy'                || true
        'organization'      | 'copy'                || true
        'user'              | 'copy'                || true
        'phoneCall'         | 'edit'                || true
        'organization'      | 'edit'                || true
        'user'              | 'edit'                || true
        'phoneCall'         | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'phoneCall'         | 'update'              || true
        'organization'      | 'update'              || true
        'user'              | 'update'              || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.get(_)
    }

    def 'An empty model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.get(_)
    }

    def 'A non-existing sequence number leaves model unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [foo: 'bar']

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.get(_)
    }

    def 'An existing sequence number model sets prefix and suffix'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [noteInstance: new Note(title: 'Test')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'edit'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('note') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('note')

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix

        and: 'the number of the instance is unset'
        0i == interceptor.model.noteInstance.number
    }

    def 'No instance does not set number in create mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [foo: 'bar']

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('note') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('note')

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix
    }

    def 'No instance does not set number in copy mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [foo: 'bar']

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('note') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('note')

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix
    }

    def 'No numbered instance does not set number in create mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [callInstance: new PhoneCall(subject: 'Phone call')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'T',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'phoneCall'

        and: 'a controller and action name'
        webRequest.controllerName = 'phoneCall'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('phoneCall') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('phoneCall')

        and: 'prefix and suffix are correct'
        'T' == interceptor.model.seqNumberPrefix
        'Y' == interceptor.model.seqNumberSuffix
    }

    def 'No numbered instance does not set number in copy mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [callInstance: new PhoneCall(subject: 'Phone call')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'T',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'phoneCall'

        and: 'a controller and action name'
        webRequest.controllerName = 'phoneCall'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('phoneCall') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('phoneCall')

        and: 'prefix and suffix are correct'
        'T' == interceptor.model.seqNumberPrefix
        'Y' == interceptor.model.seqNumberSuffix
    }

    def 'An existing sequence number sets number in create mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [noteInstance: new Note(title: 'Test')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('note') >> seqNumber
        1 * interceptor.seqNumberService.nextNumber('note') >> 15700i

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix

        and: 'the number of the instance is set'
        15700i == interceptor.model.noteInstance.number
    }

    def 'An existing sequence number sets number in copy mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [noteInstance: new Note(title: 'Test')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.get('note') >> seqNumber
        1 * interceptor.seqNumberService.nextNumber('note') >> 15700i

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix

        and: 'the number of the instance is set'
        15700i == interceptor.model.noteInstance.number
    }
}
