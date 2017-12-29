/*
 * InstallerDisabledInterceptorSpec.groovy
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


class InstallerDisabledInterceptorSpec extends Specification
    implements InterceptorUnitTest<InstallerDisabledInterceptor>
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
        'install'           | null                  || true
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'install'           | 'index'               || true
        'call'              | 'save'                || false
        'organization'      | 'save'                || false
        'install'           | 'save'                || true
    }

    def 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    def 'No redirect if application is uninitialized'() {
        given: 'a mocked install service'
        interceptor.installService = Mock(InstallService)

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is called'
        res

        and: 'it is not checked whether the installer is disabled'
        0 * interceptor.installService.installerDisabled
    }

    def 'No redirect if application is initialized but installer enabled'() {
        given: 'a configuration that the application has been initialized'
        new Config(name: 'installStatus', value: '1').save()

        and: 'a mocked install service'
        interceptor.installService = Mock(InstallService)
        1 * interceptor.installService.installerDisabled >> false

        expect:
        interceptor.before()
    }

    def 'Redirect if application is initialized and installer disabled'() {
        given: 'a configuration that the application has been initialized'
        new Config(name: 'installStatus', value: '1').save()

        and: 'a mocked install service'
        interceptor.installService = Mock(InstallService)
        1 * interceptor.installService.installerDisabled >> true

        when: 'I call the interceptor'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'I am redirected to overview page'
        response.redirectedUrl == '/'   // see UrlMappings.groovy
    }
}
