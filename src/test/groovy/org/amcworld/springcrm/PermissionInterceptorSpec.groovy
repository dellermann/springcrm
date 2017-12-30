/*
 * PermissionInterceptorSpec.groovy
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
import javax.servlet.http.HttpServletResponse
import spock.lang.Specification


class PermissionInterceptorSpec extends Specification
    implements InterceptorUnitTest<PermissionInterceptor>
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
        'call'              | null                  || true
        'organization'      | null                  || true
        'user'              | null                  || true
        'about'             | null                  || false
        'assets'            | null                  || false
        'dataFile'          | null                  || false
        'help'              | null                  || false
        'install'           | null                  || false
        'notification'      | null                  || false
        'overview'          | null                  || false
        'call'              | 'index'               || true
        'organization'      | 'index'               || true
        'user'              | 'index'               || true
        'about'             | 'index'               || false
        'assets'            | 'index'               || false
        'dataFile'          | 'index'               || false
        'help'              | 'index'               || false
        'install'           | 'index'               || false
        'notification'      | 'index'               || false
        'overview'          | 'index'               || false
        'call'              | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'about'             | 'save'                || false
        'assets'            | 'save'                || false
        'dataFile'          | 'save'                || false
        'help'              | 'save'                || false
        'install'           | 'save'                || false
        'notification'      | 'save'                || false
        'overview'          | 'save'                || false
        'call'              | 'login'               || false
        'organization'      | 'login'               || false
        'user'              | 'login'               || false
        'about'             | 'login'               || false
        'assets'            | 'login'               || false
        'dataFile'          | 'login'               || false
        'help'              | 'login'               || false
        'install'           | 'login'               || false
        'notification'      | 'login'               || false
        'overview'          | 'login'               || false
        'call'              | 'authenticate'        || false
        'organization'      | 'authenticate'        || false
        'user'              | 'authenticate'        || false
        'about'             | 'authenticate'        || false
        'assets'            | 'authenticate'        || false
        'dataFile'          | 'authenticate'        || false
        'help'              | 'authenticate'        || false
        'install'           | 'authenticate'        || false
        'notification'      | 'authenticate'        || false
        'overview'          | 'authenticate'        || false
        'call'              | 'frontend'            || false
        'organization'      | 'frontend'            || false
        'user'              | 'frontend'            || false
        'about'             | 'frontend'            || false
        'assets'            | 'frontend'            || false
        'dataFile'          | 'frontend'            || false
        'help'              | 'frontend'            || false
        'install'           | 'frontend'            || false
        'notification'      | 'frontend'            || false
        'overview'          | 'frontend'            || false
        'call'              | 'frontendSave'        || false
        'organization'      | 'frontendSave'        || false
        'user'              | 'frontendSave'        || false
        'about'             | 'frontendSave'        || false
        'assets'            | 'frontendSave'        || false
        'dataFile'          | 'frontendSave'        || false
        'help'              | 'frontendSave'        || false
        'install'           | 'frontendSave'        || false
        'notification'      | 'frontendSave'        || false
        'overview'          | 'frontendSave'        || false
    }

    def 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    def 'Returns true if no credential is not stored'() {
        expect:
        interceptor.before()
    }

    def 'Returns true if credential is stored and controller is allowed'() {
        given: 'a credential'
        session.credential = makeCredential(false)

        and: 'an allowed controller'
        webRequest.controllerName = 'call'

        expect:
        interceptor.before()
    }

    def 'Status forbidden if credential is stored and controller is denied'() {
        given: 'a credential'
        session.credential = makeCredential(false)

        and: 'a denied controller'
        webRequest.controllerName = 'invoice'

        when:
        boolean res = interceptor.before()

        then:
        !res

        and: 'I get status code FORBIDDEN'
        HttpServletResponse.SC_FORBIDDEN == status
    }

    def 'Returns true if credential is stored and administrator'() {
        given: 'a credential'
        session.credential = makeCredential(true)

        and: 'a denied controller'
        webRequest.controllerName = 'invoice'

        expect:
        interceptor.before()
    }


    //-- Non-public methods ---------------------

    private Credential makeCredential(boolean admin) {
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
//            admin: admin
        )
        u.id = 1704L

        new Credential(u)
    }
}
