/*
 * OverviewServiceSpec.groovy
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

import grails.core.GrailsApplication
import grails.test.mixin.TestFor
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import spock.lang.Specification


@TestFor(OverviewService)
class OverviewServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Don\'t show again changelog for a version'() {
        given: 'a mocked application version'
        grailsApplication.metadata['info.app.version'] = '2.0.19'

        and: 'a user with mocked settings'
        User user = makeUser()
        UserSettings settings = Mock()
        user.settings = settings

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        when: 'I set the dont show again version'
        service.dontShowAgain(cred)

        then: 'the user settings are manipulated accordingly'
        1 * settings.put('changelogVersion', '2.0.19')
    }

    def 'An empty changelog returns an empty string'() {
        given: 'a mocked Grails application'
        mockApplication '', ''

        expect:
        '' == service.getChangelog(Locale.default)
        '' == service.getChangelog(Locale.GERMAN)
    }

    def 'A changelog without STOP marker returns all'() {
        given: 'some changelog content'
        String contentDefault = '''## 2.0.19

* Item 1
* Item 2
* Item 3

## 2.0.18

* Item 4
* Item 5

## 2.0.17

* Item 6
* Item 7
'''
        String contentDe = '''## 2.0.19

* Punkt 1
* Punkt 2
* Punkt 3

## 2.0.18

* Punkt 4
* Punkt 5

## 2.0.17

* Punkt 6
* Punkt 7
'''

        and: 'a mocked Grails application'
        mockApplication contentDefault, contentDe

        and: 'a default locale'
        Locale.default = Locale.ENGLISH

        expect:
        contentDefault == service.getChangelog(Locale.default)
        contentDe == service.getChangelog(Locale.GERMAN)
        contentDe == service.getChangelog(Locale.GERMANY)
    }

    def 'A changelog with STOP marker returns partial content'() {
        given: 'some changelog content'
        String contentDefault = '''## 2.0.19

* Item 1
* Item 2
* Item 3

## 2.0.18

* Item 4
* Item 5

[comment]: STOP

## 2.0.17

* Item 6
* Item 7
'''
        String contentDe = '''## 2.0.19

* Punkt 1
* Punkt 2
* Punkt 3

## 2.0.18

* Punkt 4
* Punkt 5

[comment]: STOP

## 2.0.17

* Punkt 6
* Punkt 7
'''

        and: 'a mocked Grails application'
        mockApplication contentDefault, contentDe

        and: 'a default locale'
        Locale.default = Locale.ENGLISH

        expect:
         '''## 2.0.19

* Item 1
* Item 2
* Item 3

## 2.0.18

* Item 4
* Item 5

''' == service.getChangelog(Locale.default)
         '''## 2.0.19

* Punkt 1
* Punkt 2
* Punkt 3

## 2.0.18

* Punkt 4
* Punkt 5

''' == service.getChangelog(Locale.GERMAN)
         '''## 2.0.19

* Punkt 1
* Punkt 2
* Punkt 3

## 2.0.18

* Punkt 4
* Punkt 5

''' == service.getChangelog(Locale.GERMANY)
    }

    def 'Changelog is displayed depending on versions'(String v, boolean b) {
        given: 'a mocked application version'
        grailsApplication.metadata['info.app.version'] = '2.0.19'

        and: 'a user with mocked settings'
        User user = makeUser()
        UserSettings settings = Mock()
        1 * settings.get('changelogVersion') >> v
        user.settings = settings

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        expect:
        b == service.showChangelog(cred)

        where:
        v               || b
        null            || true
        ''              || true
        '1.0.0'         || true
        '1.0.7'         || true
        '1.10.45'       || true
        '2.0.0'         || true
        '2.0.18'        || true
        '2.0.19'        || false
        '2.0.20'        || false
        '2.0.87'        || false
        '2.15.74'       || false
        '3.45.12'       || false
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
        u.id = 1704L

        u
    }

    private void mockApplication(String contentDefault, String contentDe) {
        ApplicationContext ctx = Mock()
        ctx.getResource(_) >> { String path ->
            switch (path) {
            case 'classpath:public/changelog.md':
                Resource res = Mock()
                res.inputStream >> new ByteArrayInputStream(contentDefault.bytes)
                res.exists() >> true
                return res
            case 'classpath:public/changelog_de.md':
                Resource res = Mock()
                res.inputStream >> new ByteArrayInputStream(contentDe.bytes)
                res.exists() >> true
                return res
            default:
                return null
            }
        }

        GrailsApplication app = Mock()
        app.mainContext >> ctx

        service.grailsApplication = app
    }
}
