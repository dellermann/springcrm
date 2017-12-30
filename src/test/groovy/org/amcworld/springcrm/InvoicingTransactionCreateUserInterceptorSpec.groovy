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
        'quote'             | null                  || false
        'salesOrder'        | null                  || false
        'invoice'           | null                  || false
        'creditMemo'        | null                  || false
        'dunning'           | null                  || false
        'call'              | 'index'               || false
        'quote'             | 'index'               || false
        'salesOrder'        | 'index'               || false
        'invoice'           | 'index'               || false
        'creditMemo'        | 'index'               || false
        'dunning'           | 'index'               || false
        'call'              | 'create'              || false
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

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no create user has been set'
        null == instance.createUser
    }

    def 'An invoicing transaction get the create user'() {
        given: 'an instance'
        def instance = new Invoice()

        and: 'a controller name'
        webRequest.controllerName = 'invoice'

        and: 'an invalid instance in model'
        interceptor.model = [invoiceInstance: instance]

        and: 'a credential'
        def user = makeUser()
        session.credential = new Credential(user)

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no create user has been set'
        user == instance.createUser
    }


    //-- Non-public methods ---------------------

    private User makeUser() {
        def u = new User(
            username: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
//            admin: true
        )
        u.save failOnError: true
        u.afterLoad()       // initialize user settings

        u
    }
}
