/*
 * InvoicingTransactionCreateUserInterceptorSpec.groovy
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


class InvoicingTransactionCreateUserInterceptorSpec extends Specification
    implements InterceptorUnitTest<InvoicingTransactionCreateUserInterceptor>
{

    //-- Feature methods ------------------------

    @SuppressWarnings("GroovyPointlessBoolean")
    def 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'a particular request is used'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a                     || b
        'phoneCall'         | null                  || false
        'quote'             | null                  || false
        'salesOrder'        | null                  || false
        'invoice'           | null                  || false
        'creditMemo'        | null                  || false
        'dunning'           | null                  || false
        'phoneCall'         | 'index'               || false
        'quote'             | 'index'               || false
        'salesOrder'        | 'index'               || false
        'invoice'           | 'index'               || false
        'creditMemo'        | 'index'               || false
        'dunning'           | 'index'               || false
        'phoneCall'         | 'create'              || false
        'quote'             | 'create'              || true
        'salesOrder'        | 'create'              || true
        'invoice'           | 'create'              || true
        'creditMemo'        | 'create'              || true
        'dunning'           | 'create'              || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No instance does not set create user'() {
        given: 'an instance'
        def instance = new Invoice()

        and: 'a user service instance'
        UserService userService = Mock()
        interceptor.userService = userService

        when: 'the interceptor is called'
        interceptor.after()

        then: 'no create user has been set'
        0 * userService.getCurrentUser()
        null == instance.createUser
    }

    def 'An invoicing transaction get the create user'() {
        given: 'an instance'
        def instance = new Invoice()

        and: 'a user service instance'
        UserService userService = Mock()
        interceptor.userService = userService

        and: 'a controller name'
        webRequest.controllerName = 'invoice'

        and: 'an invalid instance in model'
        interceptor.model = [invoice: instance]

        and: 'a user'
        User user = new User(
            username: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com'
        )

        when: 'the interceptor is called'
        interceptor.after()

        then: 'no create user has been set'
        1 * userService.getCurrentUser() >> user
        user == instance.createUser
    }
}
