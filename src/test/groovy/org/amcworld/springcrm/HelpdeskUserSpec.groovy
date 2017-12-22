/*
 * HelpdeskUserSpec.groovy
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


@TestFor(HelpdeskUser)
class HelpdeskUserSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty helpdesk user'
        def hu = new HelpdeskUser()

        then: 'the properties are initialized properly'
        null == hu.helpdesk
        null == hu.user
    }

    def 'Equals is null-safe'() {
        given: 'a helpdesk user'
        def hu = new HelpdeskUser()

        expect:
        null != hu
        hu != null
        !hu.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a helpdesk user'
        def hu = new HelpdeskUser()

        expect:
        hu != 'foo'
        hu != 45
        hu != 45.3
        hu != new Date()
    }

    def 'Instances are equal if they have the same properties'() {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: 'Test helpdesk')

        and: 'a user'
        User u = new User(username: 'jsmith')

        and: 'three helpdesk users'
        def hu1 = new HelpdeskUser(helpdesk: h, user: u)
        def hu2 = new HelpdeskUser(helpdesk: h, user: u)
        def hu3 = new HelpdeskUser(helpdesk: h, user: u)

        expect: 'equals() is reflexive'
        hu1 == hu1
        hu2 == hu2
        hu3 == hu3

        and: 'all instances are equal and equals() is symmetric'
        hu1 == hu2
        hu2 == hu1
        hu2 == hu3
        hu3 == hu2

        and: 'equals() is transitive'
        hu1 == hu3
        hu3 == hu1
    }

    def 'Instances are unequal if they have the different helpdesks'() {
        given: 'some helpdesks'
        Helpdesk h1 = new Helpdesk(name: 'Test helpdesk')
        Helpdesk h2 = new Helpdesk(name: 'Another helpdesk')
        Helpdesk h3 = new Helpdesk(name: 'A third helpdesk')

        and: 'a user'
        User u = new User(username: 'jsmith')

        and: 'three helpdesk users with properties'
        def hu1 = new HelpdeskUser(helpdesk: h1, user: u)
        def hu2 = new HelpdeskUser(helpdesk: h2, user: u)
        def hu3 = new HelpdeskUser(helpdesk: h3, user: u)

        expect: 'equals() is reflexive'
        hu1 == hu1
        hu2 == hu2
        hu3 == hu3

        and: 'all instances are unequal and equals() is symmetric'
        hu1 != hu2
        hu2 != hu1
        hu2 != hu3
        hu3 != hu2

        and: 'equals() is transitive'
        hu1 != hu3
        hu3 != hu1
    }

    def 'Instances are unequal if they have the different users'() {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: 'Test helpdesk')

        and: 'some users'
        User u1 = new User(username: 'jsmith')
        User u2 = new User(username: 'jdoe')
        User u3 = new User(username: 'bwayne')

        and: 'two helpdesk users with properties'
        def hu1 = new HelpdeskUser(helpdesk: h, user: u1)
        def hu2 = new HelpdeskUser(helpdesk: h, user: u2)
        def hu3 = new HelpdeskUser(helpdesk: h, user: u3)

        expect: 'equals() is reflexive'
        hu1 == hu1
        hu2 == hu2
        hu3 == hu3

        and: 'all instances are unequal and equals() is symmetric'
        hu1 != hu2
        hu2 != hu1
        hu2 != hu3
        hu3 != hu2

        and: 'equals() is transitive'
        hu1 != hu3
        hu3 != hu1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def hu = new HelpdeskUser()

        expect:
        'null/null'.hashCode() == hu.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: 'Test helpdesk')

        and: 'a user'
        User u = new User(username: 'jsmith')

        and: 'an instance'
        def hu = new HelpdeskUser(helpdesk: h, user: u)

        when: 'I compute the hash code'
        int hc = hu.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            hu = new HelpdeskUser(helpdesk: h, user: u)
            hc == hu.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: 'Test helpdesk')

        and: 'a user'
        User u = new User(username: 'jsmith')

        and: 'three helpdesk users'
        def hu1 = new HelpdeskUser(helpdesk: h, user: u)
        def hu2 = new HelpdeskUser(helpdesk: h, user: u)
        def hu3 = new HelpdeskUser(helpdesk: h, user: u)

        expect:
        hu1.hashCode() == hu2.hashCode()
        hu2.hashCode() == hu3.hashCode()
    }

    def 'Instances with different helpdesks produce different hash codes'() {
        given: 'some helpdesks'
        Helpdesk h1 = new Helpdesk(name: 'Test helpdesk')
        Helpdesk h2 = new Helpdesk(name: 'Another helpdesk')
        Helpdesk h3 = new Helpdesk(name: 'A third helpdesk')

        and: 'a user'
        User u = new User(username: 'jsmith')

        and: 'three helpdesk users with properties'
        def hu1 = new HelpdeskUser(helpdesk: h1, user: u)
        def hu2 = new HelpdeskUser(helpdesk: h2, user: u)
        def hu3 = new HelpdeskUser(helpdesk: h3, user: u)

        expect:
        hu1.hashCode() != hu2.hashCode()
        hu2.hashCode() != hu3.hashCode()
    }

    def 'Instances with different users produce different hash codes'() {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: 'Test helpdesk')

        and: 'some users'
        User u1 = new User(username: 'jsmith')
        User u2 = new User(username: 'jdoe')
        User u3 = new User(username: 'bwayne')

        and: 'two helpdesk users with properties'
        def hu1 = new HelpdeskUser(helpdesk: h, user: u1)
        def hu2 = new HelpdeskUser(helpdesk: h, user: u2)
        def hu3 = new HelpdeskUser(helpdesk: h, user: u3)

        expect:
        hu1.hashCode() != hu2.hashCode()
        hu2.hashCode() != hu3.hashCode()
    }

    def 'Can convert to string'(String name, String username) {
        given: 'a helpdesk'
        Helpdesk h = new Helpdesk(name: name)

        and: 'a user'
        User u = new User(username: username)

        when: 'I create a helpdesk user'
        def hu = new HelpdeskUser(helpdesk: h, user: u)

        then: 'I get a valid string representation'
        ('Helpdesk ' + (name ?: '').trim() + ' -> user ' + username?.trim()) == hu.toString()

        where:
        name            | username          || _
        null            | null              || _
        ''              | null              || _
        '   '           | null              || _
        'a'             | null              || _
        'abc'           | null              || _
        '  foo  '       | null              || _
        'Services'      | null              || _
        null            | ''                || _
        ''              | ''                || _
        '   '           | ''                || _
        'a'             | ''                || _
        'abc'           | ''                || _
        '  foo  '       | ''                || _
        'Services'      | ''                || _
        null            | '    '            || _
        ''              | '    '            || _
        '   '           | '    '            || _
        'a'             | '    '            || _
        'abc'           | '    '            || _
        '  foo  '       | '    '            || _
        'Services'      | '    '            || _
        null            | 'a'               || _
        ''              | 'a'               || _
        '   '           | 'a'               || _
        'a'             | 'a'               || _
        'abc'           | 'a'               || _
        '  foo  '       | 'a'               || _
        'Services'      | 'a'               || _
        null            | 'abc'             || _
        ''              | 'abc'             || _
        '   '           | 'abc'             || _
        'a'             | 'abc'             || _
        'abc'           | 'abc'             || _
        '  foo  '       | 'abc'             || _
        'Services'      | 'abc'             || _
        null            | 'jsmith'          || _
        ''              | 'jsmith'          || _
        '   '           | 'jsmith'          || _
        'a'             | 'jsmith'          || _
        'abc'           | 'jsmith'          || _
        '  foo  '       | 'jsmith'          || _
        'Services'      | 'jsmith'          || _
    }

    def 'Helpdesk must not be null'() {
        given: 'a quite valid helpdesk user'
        def hu = new HelpdeskUser(
            user: new User(username: 'jsmith')
        )

        when: 'I set the helpdesk'
        hu.helpdesk = new Helpdesk()

        then: 'the instance is valid'
        hu.validate()

        when: 'I unset the helpdesk'
        hu.helpdesk = null

        then: 'the instance is not valid'
        !hu.validate()
    }

    def 'User must not be null'() {
        given: 'a quite valid helpdesk user'
        def hu = new HelpdeskUser(
            helpdesk: new Helpdesk(name: 'Test')
        )

        when: 'I set the user'
        hu.user = new User()

        then: 'the instance is valid'
        hu.validate()

        when: 'I unset the user'
        hu.user = null

        then: 'the instance is not valid'
        !hu.validate()
    }
}
