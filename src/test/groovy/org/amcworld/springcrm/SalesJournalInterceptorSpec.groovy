/*
 * SalesJournalInterceptorSpec.groovy
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


class SalesJournalInterceptorSpec extends Specification
    implements InterceptorUnitTest<SalesJournalInterceptor>
{

    //-- Fields ---------------------------------

    User user = makeUser()


    //-- Fixture methods ------------------------

    void setup() {
        UserService userService = Mock()
        userService.getCurrentUser() >> user
        interceptor.userService = userService

        UserSettingService userSettingService = Mock()
        interceptor.userSettingService = userSettingService
    }


    //-- Feature methods ------------------------

    void 'Interceptor matches the correct controller/action pairs'(
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

    void 'All interceptor methods return true'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'Month and year are initialized from user settings'() {
        given: 'a user setting service methods'
        1 * interceptor.userSettingService.getString(
            user, 'salesJournalYear'
        ) >> '2014'
        1 * interceptor.userSettingService.getString(
            user, 'salesJournalMonth'
        ) >> '5'

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the month and year have been set'
        1 * interceptor.userSettingService.store(
            user, 'salesJournalYear', '2014'
        )
        1 * interceptor.userSettingService.store(
            user, 'salesJournalMonth', '5'
        )
        2014 == params.year
        5 == params.month
    }

    void 'Month and year are stored in user settings'() {
        given: 'a user setting service methods'
        1 * interceptor.userSettingService.getString(
            user, 'salesJournalYear'
        )
        1 * interceptor.userSettingService.getString(
            user, 'salesJournalMonth'
        )

        when: 'the before method of the interceptor is executed'
        params.month = 9
        params.year = 2016
        interceptor.before()

        then: 'the month and year have been set'
        1 * interceptor.userSettingService.store(
            user, 'salesJournalYear', '2016'
        )
        1 * interceptor.userSettingService.store(
            user, 'salesJournalMonth', '9'
        )
        2016 == params.year
        9 == params.month
    }


    //-- Non-public methods ---------------------

    private static User makeUser() {
        new User(
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
    }
}
