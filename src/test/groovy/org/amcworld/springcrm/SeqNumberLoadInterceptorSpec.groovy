/*
 * SeqNumberLoadInterceptorSpec.groovy
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


@TestFor(SeqNumberLoadInterceptor)
class SeqNumberLoadInterceptorSpec extends Specification {

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
        'call'              | 'create'              || true
        'organization'      | 'create'              || true
        'user'              | 'create'              || true
        'call'              | 'copy'                || true
        'organization'      | 'copy'                || true
        'user'              | 'copy'                || true
        'call'              | 'edit'                || true
        'organization'      | 'edit'                || true
        'user'              | 'edit'                || true
        'call'              | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'call'              | 'update'              || true
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
        0 * interceptor.seqNumberService.loadSeqNumber()
    }

    def 'An empty model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.loadSeqNumber()
    }

    def 'A non-existing sequence number leaves model unmodified'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [foo: 'bar']

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            controllerName: 'note',
            prefix: 'N',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller name'
        webRequest.controllerName = 'call'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number service is not used'
        0 * interceptor.seqNumberService.loadSeqNumber()
    }

    def 'An existing sequence number model sets prefix and suffix'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [noteInstance: new Note(title: 'Test')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            controllerName: 'note',
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'edit'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('note') >> seqNumber
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
            controllerName: 'note',
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('note') >> seqNumber
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
            controllerName: 'note',
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('note') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('note')

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix
    }

    def 'No numbered instance does not set number in create mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [callInstance: new Call(subject: 'Phone call')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            controllerName: 'call',
            prefix: 'T',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('call') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('call')

        and: 'prefix and suffix are correct'
        'T' == interceptor.model.seqNumberPrefix
        'Y' == interceptor.model.seqNumberSuffix
    }

    def 'No numbered instance does not set number in copy mode'() {
        given: 'a mocked sequence number service'
        interceptor.seqNumberService = Mock(SeqNumberService)

        and: 'an non-empty model'
        interceptor.model = [callInstance: new Call(subject: 'Phone call')]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            controllerName: 'call',
            prefix: 'T',
            suffix: 'Y',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('call') >> seqNumber
        0 * interceptor.seqNumberService.nextNumber('call')

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
            controllerName: 'note',
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'create'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('note') >> seqNumber
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
            controllerName: 'note',
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )

        and: 'a controller and action name'
        webRequest.controllerName = 'note'
        webRequest.actionName = 'copy'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the sequence number is obtained'
        1 * interceptor.seqNumberService.loadSeqNumber('note') >> seqNumber
        1 * interceptor.seqNumberService.nextNumber('note') >> 15700i

        and: 'prefix and suffix are correct'
        'N' == interceptor.model.seqNumberPrefix
        'X' == interceptor.model.seqNumberSuffix

        and: 'the number of the instance is set'
        15700i == interceptor.model.noteInstance.number
    }
}
