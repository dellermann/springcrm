/*
 * HelpdeskSecurityInterceptorSpec.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(HelpdeskSecurityInterceptor)
@Mock([Helpdesk, HelpdeskUser])
class HelpdeskSecurityInterceptorSpec extends Specification {

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
        'helpdesk'          | null                  || false
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'helpdesk'          | 'index'               || false
        'call'              | 'frontend'            || false
        'organization'      | 'frontend'            || false
        'helpdesk'          | 'frontend'            || true
        'call'              | 'frontendSave'        || false
        'organization'      | 'frontendSave'        || false
        'helpdesk'          | 'frontendSave'        || true
    }

    def 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    def 'Not Found if no URL name is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Not Found if an empty URL name is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        when: 'I call the interceptor'
        params.urlName = ''
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Not Found if an invalid URL name is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        and: 'an invalid URL name'
        params.urlName = 'foo'

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Forbidden if an invalid access code is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        and: 'a valid URL name and an invalid access code'
        params.urlName = 'amcworld'
        params.accessCode = 'XXX'

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Forbidden'
        SC_FORBIDDEN == status
    }

    def 'Allowed if an valid URL name and access code is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        and: 'a valid URL name and access code'
        params.urlName = 'amcworld'
        params.accessCode = '47B05XZ'

        expect:
        interceptor.before()
    }


    //-- Non-public methods ---------------------

    private void makeHelpdesk() {
        def h = new Helpdesk(
            name: 'AMCWorld',
            accessCode: '47B05XZ',
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            users: [
                new User(
                    username: 'jsmith',
                    password: 'abcd',
                    firstName: 'John',
                    lastName: 'Smith',
                    phone: '+49 30 1234567',
                    phoneHome: '+49 30 9876543',
                    mobile: '+49 172 3456789',
                    fax: '+49 30 1234568',
                    email: 'j.smith@example.com',
                    admin: true,
                    allowedModules: 'CALL, TICKET, NOTE'
                )
            ] as Set
        )
        h.save failOnError: true
    }
}
