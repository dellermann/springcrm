/*
 * SalesJournalInterceptorSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SalesJournalInterceptor)
@Mock([User, UserSetting])
class SalesJournalInterceptorSpec extends Specification {

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
        'report'            | null                  || false
        'call'              | 'show'                || false
        'organization'      | 'show'                || false
        'report'            | 'show'                || false
        'call'              | 'sales-journal'       || false
        'organization'      | 'sales-journal'       || false
        'report'            | 'sales-journal'       || true
    }

    def 'All interceptor methods return true'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'Month and year are initialized from user settings'() {
        given: 'a user and a credential'
        session.credential = new Credential(makeUser())
        session.credential.settings.salesJournalYear = '2014'
        session.credential.settings.salesJournalMonth = '5'

        when: 'I call the interceptor'
        interceptor.before()

        then: 'the month and year have been set'
        2014 == params.year
        '2014' == session.credential.settings.salesJournalYear
        5 == params.month
        '5' == session.credential.settings.salesJournalMonth
    }

    def 'Month and year are stored in user settings'() {
        given: 'a user and a credential'
        session.credential = new Credential(makeUser())

        and: 'month and year'
        params.month = 5
        params.year = 2014

        when: 'I call the interceptor'
        interceptor.before()

        then: 'the month and year have been set'
        2014 == params.year
        '2014' == session.credential.settings.salesJournalYear
        5 == params.month
        '5' == session.credential.settings.salesJournalMonth
    }


    //-- Non-public methods ---------------------

    private User makeUser() {
        def u = new User(
            userName: 'jsmith',
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
        u.save failOnError: true
        u.afterLoad()       // initialize user settings

        u
    }
}
