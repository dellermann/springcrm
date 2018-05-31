/*
 * PaginationInterceptorSpec.groovy
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


class PaginationInterceptorSpec extends Specification
    implements InterceptorUnitTest<PaginationInterceptor>,
        DomainUnitTest<PhoneCall>
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

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a             || b
        'phoneCall'         | null          || false
        'organization'      | null          || false
        'user'              | null          || false
        'phoneCall'         | 'show'        || false
        'organization'      | 'show'        || false
        'user'              | 'show'        || false
        'phoneCall'         | 'index'       || true
        'organization'      | 'index'       || true
        'user'              | 'index'       || true
    }

    void 'All interceptor methods return true'() {
        given: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        expect:
        interceptor.after()
        interceptor.before()
    }

    void 'Offset is initialized from session'() {
        given: 'a controller name'
        webRequest.controllerName = 'xyz'

        and: 'an offset in session'
        session.offsetXyz = 50

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the offset has been set'
        50 == params.offset
        50 == session.offsetXyz
    }

    void 'Offset is stored in session'() {
        given: 'a controller name'
        webRequest.controllerName = 'xyz'

        and: 'an offset in session'
        session.offsetXyz = 50

        and: 'a offset parameter'
        params.offset = 30

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the offset has been set'
        30 == params.offset
        30 == session.offsetXyz
    }

    void 'Cannot obtain count from invalid domain model class'() {
        given: 'a controller name'
        webRequest.controllerName = 'xyz'

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'no offset has been stored in session'
        null == session.offsetXyz
    }

    void 'Offset is limited and stored in session'(Integer o, Integer m, int e)
    {
        given: 'some calls'
        makeCalls()

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'an offset and a page size'
        params.offset = o
        params.max = m

        and: 'an offset in session'
        session.offsetPhoneCall = 500

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the offset has been stored correctly'
        e == params.offset
        e == session.offsetPhoneCall

        where:
        o       | m     || e
        null    | null  || 140
        0       | null  || 0
        1       | null  || 1
        9       | null  || 9
        10      | null  || 10
        100     | null  || 100
        140     | null  || 140
        141     | null  || 140
        150     | null  || 140
        4947500 | null  || 140
        -1      | null  || 0
        -10     | null  || 0
        null    | 5     || 145
        0       | 5     || 0
        1       | 5     || 1
        9       | 5     || 9
        10      | 5     || 10
        100     | 5     || 100
        140     | 5     || 140
        141     | 5     || 141
        150     | 5     || 145
        4947500 | 5     || 145
        -1      | 5     || 0
        -10     | 5     || 0
        null    | 10    || 140
        0       | 10    || 0
        1       | 10    || 1
        9       | 10    || 9
        10      | 10    || 10
        100     | 10    || 100
        140     | 10    || 140
        141     | 10    || 140
        150     | 10    || 140
        4947500 | 10    || 140
        -1      | 10    || 0
        -10     | 10    || 0
        null    | 20    || 140
        0       | 20    || 0
        1       | 20    || 1
        9       | 20    || 9
        10      | 20    || 10
        100     | 20    || 100
        140     | 20    || 140
        141     | 20    || 140
        150     | 20    || 140
        4947500 | 20    || 140
        -1      | 20    || 0
        -10     | 20    || 0
        null    | 50    || 100
        0       | 50    || 0
        1       | 50    || 1
        9       | 50    || 9
        10      | 50    || 10
        100     | 50    || 100
        140     | 50    || 100
        141     | 50    || 100
        150     | 50    || 100
        4947500 | 50    || 100
        -1      | 50    || 0
        -10     | 50    || 0
    }

    void 'Sort criterion is initialized from user settings'() {
        given: 'some calls'
        makeCalls 10

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the sort criterion has been set'
        //noinspection GroovyAssignabilityCheck
        1 * interceptor.userSettingService.getString(user, 'sortPhoneCall') >>
            'type'
        1 * interceptor.userSettingService.store(user, 'sortPhoneCall', 'type')
        'type' == params.sort
    }

    void 'Sort criterion is stored in user settings'() {
        given: 'some calls'
        makeCalls 10

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'the before method of the interceptor is executed'
        params.sort = 'subject'
        interceptor.before()

        then: 'the sort criterion has been set'
        1 * interceptor.userSettingService.getString(user, 'sortPhoneCall')
        1 * interceptor.userSettingService.store(
            user, 'sortPhoneCall', 'subject'
        )
        'subject' == params.sort
    }

    void 'Order is initialized from user settings'() {
        given: 'some calls'
        makeCalls 10

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'the before method of the interceptor is executed'
        interceptor.before()

        then: 'the sort criterion has been set'
        //noinspection GroovyAssignabilityCheck
        1 * interceptor.userSettingService.getString(user, 'orderPhoneCall') >>
            'desc'
        1 * interceptor.userSettingService.store(user, 'orderPhoneCall', 'desc')
        'desc' == params.order
    }

    void 'Order is stored in user settings'() {
        given: 'some calls'
        makeCalls 10

        and: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        when: 'the before method of the interceptor is executed'
        params.order = 'asc'
        interceptor.before()

        then: 'the sort criterion has been set'
        1 * interceptor.userSettingService.getString(user, 'orderPhoneCall')
        1 * interceptor.userSettingService.store(user, 'orderPhoneCall', 'asc')
        'asc' == params.order
    }

    void 'Store offset in session'() {
        given: 'a controller name'
        webRequest.controllerName = 'phoneCall'

        and: 'an offset'
        params.offset = 50

        when: 'the before method of the interceptor is executed'
        interceptor.after()

        then: 'the offset has been stored in session'
        50 == session.offsetPhoneCall
    }


    //-- Non-public methods ---------------------

    private static void makeCalls(int n = 150) {
        for (int i = 0; i < n; i++) {
            def c = new PhoneCall(
                subject: 'My phoneCall ' + i, notes: 'I phoneCall you!',
                type: PhoneCallType.OUTGOING, status: PhoneCallStatus.COMPLETED
            )
            c.save failOnError: true
        }
    }

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
