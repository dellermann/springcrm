/*
 * LoginInterceptorSpec.groovy
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


class LoginInterceptorSpec extends Specification
    implements InterceptorUnitTest<LoginInterceptor>
{

    //-- Feature methods ------------------------

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'a particular request is used'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a                     || b
        'phoneCall'         | null                  || true
        'organization'      | null                  || true
        'user'              | null                  || true
        'assets'            | null                  || false
        'help'              | null                  || false
        'install'           | null                  || false
        'phoneCall'         | 'index'               || true
        'organization'      | 'index'               || true
        'user'              | 'index'               || true
        'assets'            | 'index'               || false
        'help'              | 'index'               || false
        'install'           | 'index'               || false
        'phoneCall'         | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
        'assets'            | 'save'                || false
        'help'              | 'save'                || false
        'install'           | 'save'                || false
        'phoneCall'         | 'frontend'            || false
        'organization'      | 'frontend'            || false
        'user'              | 'frontend'            || false
        'assets'            | 'frontend'            || false
        'help'              | 'frontend'            || false
        'install'           | 'frontend'            || false
        'phoneCall'         | 'frontendSave'        || false
        'organization'      | 'frontendSave'        || false
        'user'              | 'frontendSave'        || false
        'assets'            | 'frontendSave'        || false
        'help'              | 'frontendSave'        || false
        'install'           | 'frontendSave'        || false
    }

    void 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    void 'Redirect if application is uninitialized'() {
        given: 'an install service instance'
        InstallService installService = Mock()
        interceptor.installService = installService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.getInteger('installStatus') >> 0
        interceptor.configService = configService

        when: 'the interceptor is called'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'the installer is enabled'
        1 * installService.enableInstaller()

        and: 'I am redirected to installer'
        '/install/index' == response.redirectedUrl
    }

    void 'No redirect if application is already initialized'() {
        given: 'an install service instance'
        InstallService installService = Mock()
        interceptor.installService = installService

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.getInteger('installStatus') >> 1
        interceptor.configService = configService

        when: 'the interceptor is called'
        boolean res = interceptor.before()

        then: 'the action is called'
        res

        and: 'the installer is not enabled'
        0 * installService.enableInstaller()
    }
}
