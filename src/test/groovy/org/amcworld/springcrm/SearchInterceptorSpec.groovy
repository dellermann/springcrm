/*
 * SearchInterceptorSpec.groovy
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


@TestFor(SearchInterceptor)
class SearchInterceptorSpec extends Specification {

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
    }

    def 'The before interceptor action returns true'() {
        expect:
        interceptor.before()
    }

    def 'Without a GORM instance nothing is done'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        when: 'I call the action method without a GORM entity'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'save'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'no service method has been called'
        0 * service.index(_)
        0 * service.reindex(_)
        0 * service.removeFromIndex(_)
    }

    def 'Without a valid GORM instance nothing is done'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        and: 'I set an entity which is no GORM instance'
        request['callInstance'] = 'foo'

        when: 'I call the action method'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'save'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'no service method has been called'
        0 * service.index(_)
        0 * service.reindex(_)
        0 * service.removeFromIndex(_)
    }

    def 'A valid GORM instance is indexed at save'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        and: 'I set an entity which is a valid GORM instance'
        request['callInstance'] = new Call()

        when: 'I call the action method'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'save'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'one service method has been called'
        1 * service.index(_ as Call)
        0 * service.reindex(_)
        0 * service.removeFromIndex(_)
    }

    def 'A valid GORM instance is re-indexed at update'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        and: 'I set an entity which is a valid GORM instance'
        request['callInstance'] = new Call()

        when: 'I call the action method'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'update'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'one service method has been called'
        0 * service.index(_)
        1 * service.reindex(_ as Call)
        0 * service.removeFromIndex(_)
    }

    def 'A valid GORM instance is removed from indexed at delete'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        and: 'I set an entity which is a valid GORM instance'
        request['callInstance'] = new Call()

        when: 'I call the action method'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'delete'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'one service method has been called'
        0 * service.index(_)
        0 * service.reindex(_)
        1 * service.removeFromIndex(_ as Call)
    }

    def 'A valid GORM instance is not considered at an invalid action'() {
        given: 'a mocked service'
        SearchService service = Mock()
        interceptor.searchService = service

        and: 'I set an entity which is a valid GORM instance'
        request['callInstance'] = new Call()

        when: 'I call the action method'
        webRequest.controllerName = 'call'
        webRequest.actionName = 'index'
        boolean res = interceptor.after()

        then: 'the method returns true'
        res

        and: 'no service method has been called'
        0 * service.index(_)
        0 * service.reindex(_)
        0 * service.removeFromIndex(_)
    }
}
