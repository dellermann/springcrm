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

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class SeqNumberStoreInterceptorSpec extends Specification
    implements InterceptorUnitTest<SeqNumberStoreInterceptor>,
        DomainUnitTest<Note>
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
        'phoneCall'         | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
    }

    void 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'Do not change number if number is set and autoNumber is off'() {
        given: 'a sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        when: 'the interceptor is called'
        params.number = 12345i
        interceptor.before()

        then: 'the number remains unchanged'
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.nextNumber(_)
        12345i == params.number

        when: 'the interceptor is called'
        params.autoNumber = null
        params.number = 12345i
        interceptor.before()

        then: 'the number remains unchanged'
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.nextNumber(_)
        12345i == params.number

        when: 'the interceptor is called'
        params.autoNumber = ''
        params.number = 12345i
        interceptor.before()

        then: 'the number remains unchanged'
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.nextNumber(_)
        12345i == params.number
    }

    void 'Change number if number is unset or zero'() {
        given: 'a sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'a controller name'
        webRequest.controllerName = 'note'

        when: 'the interceptor is called'
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number

        when: 'the interceptor is called'
        params.number = 0
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number

        when: 'the interceptor is called'
        params.number = '0'
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number
    }

    void 'Change number if autoNumber is on'() {
        given: 'a sequence number service'
        SeqNumberService seqNumberService = Mock()
        interceptor.seqNumberService = seqNumberService

        and: 'a controller name'
        webRequest.controllerName = 'note'

        when: 'the interceptor is called'
        params.number = '34'
        params.autoNumber = 1
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number

        when: 'the interceptor is called'
        params.number = '34'
        params.autoNumber = '1'
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number

        when: 'the interceptor is called'
        params.number = '34'
        params.autoNumber = 'true'
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number

        when: 'the interceptor is called'
        params.number = '34'
        params.autoNumber = 'X'
        interceptor.before()

        then: 'the number has been changed'
        1 * seqNumberService.nextNumber('note') >> 12002i
        12002i == params.number
    }
}
