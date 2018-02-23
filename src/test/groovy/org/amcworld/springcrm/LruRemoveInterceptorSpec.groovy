/*
 * LruRemoveInterceptorSpec.groovy
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
import org.bson.types.ObjectId
import spock.lang.Specification


class LruRemoveInterceptorSpec extends Specification
    implements InterceptorUnitTest<LruRemoveInterceptor>
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
        'document'          | null                  || false
        'phoneCall'         | 'index'               || false
        'organization'      | 'index'               || false
        'document'          | 'index'               || false
        'phoneCall'         | 'delete'              || true
        'organization'      | 'delete'              || true
        'document'          | 'delete'              || false
    }

    void 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'No removal without confirmation'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        when: 'the interceptor is called'
        interceptor.before()

        then: 'the remove method has not been called'
        //noinspection GroovyAssignabilityCheck
        0 * interceptor.lruService.removeItem(String, long)
    }

    void 'Removal with confirmation'() {
        given: 'a mocked LRU service'
        interceptor.lruService = Mock(LruService)

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'an ID'
        ObjectId id = new ObjectId()

        and: 'some parameters'
        interceptor.params.confirmed = '1'
        interceptor.params.id = id.toString()

        when: 'the interceptor is called'
        interceptor.before()

        then: 'the remove method has not been called'
        1 * interceptor.lruService.removeItem('phoneCall', id)
    }
}
