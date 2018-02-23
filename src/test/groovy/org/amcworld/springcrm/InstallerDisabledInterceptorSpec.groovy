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

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class InstallerDisabledInterceptorSpec extends Specification
    implements InterceptorUnitTest<InstallerDisabledInterceptor>,
        DomainUnitTest<Config>
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
        'phoneCall'         | null                  || false
        'organization'      | null                  || false
        'install'           | null                  || true
        'phoneCall'         | 'index'               || false
        'organization'      | 'index'               || false
        'install'           | 'index'               || true
        'phoneCall'         | 'save'                || false
        'organization'      | 'save'                || false
        'install'           | 'save'                || true
    }

    void 'Other interceptor methods return true'() {
        expect:
        interceptor.after()
    }

    void 'No redirect if application is uninitialized'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        interceptor.configService = configService

        and: 'a install service instance'
        InstallService installService = Mock()
        interceptor.installService = installService

        when: 'the interceptor is called'
        boolean res = interceptor.before()

        then: 'the action is called'
        res

        and: 'it is not checked whether the installer is disabled'
        1 * configService.getInteger('installStatus') >> null
        0 * installService.isInstallerDisabled()

        when: 'the interceptor is called'
        res = interceptor.before()

        then: 'the action is called'
        res

        and: 'it is not checked whether the installer is disabled'
        1 * configService.getInteger('installStatus') >> 0
        0 * installService.isInstallerDisabled()
    }

    void 'No redirect if application is initialized but installer enabled'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        interceptor.configService = configService

        and: 'a install service instance'
        InstallService installService = Mock()
        interceptor.installService = installService

        when: 'the interceptor is called'
        boolean res = interceptor.before()

        then: 'the action is called'
        res

        and: 'it is checked whether the installer is disabled'
        1 * configService.getInteger('installStatus') >> 1
        //noinspection GroovyAssignabilityCheck
        1 * installService.isInstallerDisabled() >> false
    }

    void 'Redirect if application is initialized and installer disabled'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        interceptor.configService = configService

        and: 'a install service instance'
        InstallService installService = Mock()
        interceptor.installService = installService

        when: 'the interceptor is called'
        boolean res = interceptor.before()

        then: 'the action is not called'
        !res

        and: 'a redirect to overview page is issued'
        1 * configService.getInteger('installStatus') >> 1
        //noinspection GroovyAssignabilityCheck
        1 * installService.isInstallerDisabled() >> true
        '/' == response.redirectedUrl   // see UrlMappings.groovy
    }
}
