/*
 * DataFileFrontendSecurityInterceptorSpec.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class DataFileFrontendSecurityInterceptorSpec extends Specification
    implements InterceptorUnitTest<DataFileFrontendSecurityInterceptor>
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
        'organization'      | null                  || false
        'helpdesk'          | null                  || false
        'ticket'            | null                  || false
        'dataFile'          | null                  || false
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'helpdesk'          | 'index'               || false
        'ticket'            | 'index'               || false
        'dataFile'          | 'index'               || false
        'call'              | 'frontend-load-file'  || false
        'organization'      | 'frontend-load-file'  || false
        'helpdesk'          | 'frontend-load-file'  || false
        'ticket'            | 'frontend-load-file'  || false
        'dataFile'          | 'frontend-load-file'  || true
    }

    def 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    def 'Not Found if no helpdesk is submitted'() {
        given: 'a helpdesk'
        makeHelpdesk()

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Not Found if an invalid helpdesk is submitted'() {
        given: 'a helpdesk'
        Helpdesk h = makeHelpdesk()

        and: 'I set the helpdesk ID'
        params.helpdesk = h.id + 1L

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Forbidden if an invalid access code is submitted'() {
        given: 'a helpdesk'
        Helpdesk h = makeHelpdesk()

        and: 'I set the helpdesk ID and an invalid access code'
        params.helpdesk = h.id
        params.accessCode = 'XXX'

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Forbidden'
        SC_FORBIDDEN == status
    }

    def 'Allowed if a valid helpdesk ID and access code is submitted'() {
        given: 'a helpdesk'
        Helpdesk h = makeHelpdesk()

        and: 'I set the helpdesk ID and an invalid access code'
        params.helpdesk = h.id
        params.accessCode = '47B05XZ'

        expect:
        interceptor.before()
    }


    //-- Non-public methods ---------------------

    private Helpdesk makeHelpdesk() {
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
