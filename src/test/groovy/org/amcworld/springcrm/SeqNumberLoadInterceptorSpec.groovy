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

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'a particular request is used'
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

    void 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'No model leaves it unmodified'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number service is not used'
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.get(_)
    }

    void 'A non-existing sequence number leaves model unmodified'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'an non-empty model'
        interceptor.model = [foo: 'bar']

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number service has been used but returned null'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('phoneCall') >> null

        and: 'the sequence number service has been set'
        seqNumberService.is interceptor.model.seqNumberService
    }

    void 'No instance does not set number'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

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

        and: 'a controller name'
        webRequest.controllerName = 'note'

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number is obtained'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('note') >> seqNumber
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.nextNumber(_)

        and: 'the full number has not been set'
        null == interceptor.model.fullNumber

        and: 'prefix and suffix are correct'
        seqNumber.prefix == interceptor.model.seqNumberPrefix
        seqNumber.suffix == interceptor.model.seqNumberSuffix

        and: 'the sequence number service has been set'
        seqNumberService.is interceptor.model.seqNumberService
    }

    void 'A non NumberedDomain instance does not set number'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'an non-empty model'
        interceptor.model = [note: new PhoneCall()]

        and: 'a sequence number'
        def seqNumber = new SeqNumber(
            prefix: 'N',
            suffix: 'X',
            startValue: 10_000,
            endValue: 99_999
        )
        seqNumber.id = 'note'

        and: 'a controller name'
        webRequest.controllerName = 'note'

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number is obtained'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('note') >> seqNumber
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.nextNumber(_)

        and: 'the full number has not been set'
        null == interceptor.model.fullNumber

        and: 'prefix and suffix are correct'
        seqNumber.prefix == interceptor.model.seqNumberPrefix
        seqNumber.suffix == interceptor.model.seqNumberSuffix

        and: 'the sequence number service has been set'
        seqNumberService.is interceptor.model.seqNumberService
    }

    void 'An existing sequence number sets prefix and suffix'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'an non-empty model'
        Note note = new Note(number: 12000i, title: 'Test')
        interceptor.model = [note: note]

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

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number has been obtained'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('note') >> seqNumber
        0 * seqNumberService.nextNumber('note')
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(note) >> 'N-12000'

        and: 'prefix and suffix are correct'
        seqNumber.prefix == interceptor.model.seqNumberPrefix
        seqNumber.suffix == interceptor.model.seqNumberSuffix

        and: 'the sequence number service has been set'
        seqNumberService == interceptor.model.seqNumberService

        and: 'the number of the instance is unchanged'
        12000i == interceptor.model.note.number
    }

    void 'Create action sets the next sequence number'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'an non-empty model'
        Note note = new Note(title: 'Test')
        interceptor.model = [note: note]

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

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number is obtained'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('note') >> seqNumber
        1 * seqNumberService.nextNumber('note') >> 15700i

        and: 'prefix and suffix are correct'
        seqNumber.prefix == interceptor.model.seqNumberPrefix
        seqNumber.suffix == interceptor.model.seqNumberSuffix

        and: 'the sequence number service has been set'
        seqNumberService == interceptor.model.seqNumberService

        and: 'the number of the instance has been set'
        15700i == interceptor.model.note.number
    }

    void 'Copy action sets the next sequence number'() {
        given: 'a mocked sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'an non-empty model'
        Note note = new Note(title: 'Test')
        interceptor.model = [note: note]

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

        when: 'the interceptor is called'
        interceptor.after()

        then: 'the sequence number is obtained'
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.get('note') >> seqNumber
        1 * seqNumberService.nextNumber('note') >> 15700i

        and: 'prefix and suffix are correct'
        seqNumber.prefix == interceptor.model.seqNumberPrefix
        seqNumber.suffix == interceptor.model.seqNumberSuffix

        and: 'the sequence number service has been set'
        seqNumberService == interceptor.model.seqNumberService

        and: 'the number of the instance has been set'
        15700i == interceptor.model.note.number
    }
}
