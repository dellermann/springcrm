/*
 * EncryptPasswordInterceptorSpec.groovy
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


@TestFor(EncryptPasswordInterceptor)
class EncryptPasswordInterceptorSpec extends Specification {

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
        'user'              | null                  || false
        'user'              | 'index'               || false
        'user'              | 'save'                || true
        'user'              | 'update'              || true
        'user'              | 'authenticate'        || true
        'user'              | 'create-admin-save'   || true
        'install'           | null                  || false
        'install'           | 'index'               || false
        'install'           | 'save'                || true
        'install'           | 'update'              || true
        'install'           | 'authenticate'        || true
        'install'           | 'create-admin-save'   || true
        'foo'               | null                  || false
        'foo'               | 'index'               || false
        'foo'               | 'save'                || false
        'foo'               | 'update'              || false
        'foo'               | 'authenticate'        || false
        'foo'               | 'create-admin-save'   || false
    }

    def 'Interceptor methods return true'() {
        expect:
        interceptor.before()
        interceptor.after()
    }

    def 'Unspecified passwords are not encoded'() {
        when: 'I set the password to null and call the interceptor'
        interceptor.params.password = null
        interceptor.before()

        then: 'I get the encoded password'
        null == interceptor.params.password

        when: 'I set the password to an empty string and call the interceptor'
        interceptor.params.password = ''
        interceptor.before()

        then: 'I get the encoded password'
        null == interceptor.params.password
    }

    def 'Given passwords are encoded'() {
        given: 'a mocked security service'
        interceptor.securityService = Mock(SecurityService)
        1 * interceptor.securityService.encryptPassword('foo') >> 'dhfak43hd'

        when: 'I set a particular password and call the interceptor'
        interceptor.params.password = 'foo'
        interceptor.before()

        then: 'I get the encoded password'
        'dhfak43hd' == interceptor.params.password
    }
}
