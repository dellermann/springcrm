/*
 * TicketSecurityInterceptorSpec.groovy
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


class TicketSecurityInterceptorSpec extends Specification
    implements InterceptorUnitTest<TicketSecurityInterceptor>
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
        'helpdesk'          | null                  || false
        'ticket'            | null                  || false
        'phoneCall'         | 'index'               || false
        'organization'      | 'index'               || false
        'helpdesk'          | 'index'               || false
        'ticket'            | 'index'               || false
        'phoneCall'         | 'frontend'            || false
        'organization'      | 'frontend'            || false
        'helpdesk'          | 'frontend'            || false
        'ticket'            | 'frontend'            || true
        'phoneCall'         | 'frontendSave'        || false
        'organization'      | 'frontendSave'        || false
        'helpdesk'          | 'frontendSave'        || false
        'ticket'            | 'frontendSave'        || true
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

    def 'Not Found if an invalid ticket ID is submitted'() {
        given: 'a helpdesk and a ticket'
        Helpdesk h = makeHelpdesk()
        Ticket t = makeTicket(h)

        and: 'I set the helpdesk ID'
        params.helpdesk = h.id
        params.accessCode = '47B05XZ'
        params.id = t.id + 1

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Not Found'
        SC_NOT_FOUND == status
    }

    def 'Forbidden if an ticket is assigned to wrong helpdesk'() {
        given: 'a helpdesk and a ticket'
        Helpdesk h1 = makeHelpdesk()
        Ticket t = makeTicket(h1)
        Helpdesk h2 = makeAnotherHelpdesk()

        and: 'I set the helpdesk ID'
        params.helpdesk = h2.id
        params.accessCode = '357B7Y4'
        params.id = t.id

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I get status code Forbidden'
        SC_FORBIDDEN == status
    }

    def 'Allowed if all parameters are correct'() {
        given: 'a helpdesk and a ticket'
        Helpdesk h = makeHelpdesk()
        Ticket t = makeTicket(h)

        and: 'I set the helpdesk ID'
        params.helpdesk = h.id
        params.accessCode = '47B05XZ'
        params.id = t.id

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
//                    admin: true
                )
            ] as Set
        )
        h.save failOnError: true
    }

    private Helpdesk makeAnotherHelpdesk() {
        def h = new Helpdesk(
            name: 'YourCom',
            accessCode: '357B7Y4',
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
//                    admin: true
                )
            ] as Set
        )
        h.save failOnError: true
    }

    private Ticket makeTicket(Helpdesk helpdesk) {
        def t = new Ticket(
            subject: 'Printer not working',
            firstName: 'John',
            lastName: 'Smith',
            address: new Address(),
            email1: 'j.smith@example.com',
            helpdesk: helpdesk,
            logEntries: []
        )
        t.logEntries << new TicketLogEntry(action: TicketLogAction.create)
        t.save failOnError: true
    }
}
