/*
 * HelpdeskSpec.groovy
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
import spock.lang.Ignore
import spock.lang.Specification


@TestFor(Helpdesk)
@Mock([Helpdesk, HelpdeskUser])
class HelpdeskSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty helpdesk'
        def h = new Helpdesk()

        then: 'the properties are initialized properly'
        null == h.name
        null == h.urlName
        null == h.accessCode
        [] as Set == h.users
        null == h.organization
        null == h.tickets
        null == h.dateCreated
        null == h.lastUpdated
    }

    def 'Copy a helpdesk using constructor'() {
        given: 'some users'
        Set<User> users = [
            new User(userName: 'User 1'),
            new User(userName: 'User 2')
        ] as Set<User>

        and: 'an organization'
        Organization org = new Organization(name: 'My Company, ltd.')

        and: 'a date'
        Date d = new Date()

        and: 'a helpdesk'
        def h1 = new Helpdesk(
            name: 'My company',
            accessCode: 'ABC123',
            users: users,
            organization: org,
            dateCreated: d,
            lastUpdated: d
        )

        when: 'I copy that helpdesk using the constructor'
        def h2 = new Helpdesk(h1)

        then: 'I have some properties of the first helpdesk in the second one'
        users == h2.users
        org.is h2.organization

        and: 'some properties are unset'
        null == h2.name
        null == h2.urlName
        null == h2.accessCode
        null == h2.tickets
        null == h2.dateCreated
        null == h2.lastUpdated
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty helpdesk'
        def h1 = new Helpdesk()

        when: 'I copy the helpdesk using the constructor'
        def h2 = new Helpdesk(h1)

        then: 'the properties are set properly'
        null == h2.name
        null == h2.urlName
        null == h2.accessCode
        [] as Set == h2.users
        null == h2.organization
        null == h2.tickets
        null == h2.dateCreated
        null == h2.lastUpdated
    }

    void 'Name defaults to empty string'(String name, String n) {
        when: 'I create a helpdesk with a name'
        Helpdesk h = new Helpdesk(name: name)

        then: 'I get an empty name'
        n == h.name

        when: 'I set a name'
        h = new Helpdesk()
        h.name = name

        then: 'I get an empty name'
        n == h.name

        where:
        name            || n
        null            || null
        ''              || ''
        ' '             || ''
        '     '         || ''
        '  \t \t  '     || ''
    }

    void 'Name is trimmed'(String name, String n) {
        when: 'I create a helpdesk with a name'
        Helpdesk h = new Helpdesk(name: name)

        then: 'the name is trimmed'
        n == h.name

        when: 'I set a name'
        h.name = name

        then: 'the name is trimmed'
        n == h.name

        where:
        name            || n
        'abc'           || 'abc'
        'abc  '         || 'abc'
        '  abc'         || 'abc'
        '  abc  '       || 'abc'
        'abc \t '       || 'abc'
        ' \t abc'       || 'abc'
        ' \t abc \t '   || 'abc'
    }

    void 'URL name is set to valid value'(String name, String u) {
        when: 'I create a helpdesk with a given name'
        Helpdesk h = new Helpdesk(name: name)

        then: 'I get a valid URL name'
        u == h.urlName

        when: 'I set a name'
        h = new Helpdesk()
        h.name = name

        then: 'I get a valid URL name'
        u == h.urlName

        where:
        name            || u
        null            || null
        ''              || ''
        '  '            || ''
        'a'             || 'a'
        'abc'           || 'abc'
        'a-c'           || 'a-c'
        'a c'           || 'a-c'
        'a+c'           || 'a-c'
        'a/c'           || 'a-c'
        'a.c'           || 'a-c'
        'a,c'           || 'a-c'
        'a;c'           || 'a-c'
        'a*c'           || 'a-c'
        'abü'           || 'ab-'
        'abc  '         || 'abc'
        '  abc'         || 'abc'
        '  abc  '       || 'abc'
    }

    void 'Create user associations at insert'() {
        given: 'some users'
        User u1 = new User(userName: 'User 1')
        User u2 = new User(userName: 'User 2')

        and: 'a helpdesk'
        Helpdesk h = new Helpdesk(
            name: 'My test helpdesk', accessCode: 'ABC123',
            organization: new Organization(name: 'My Company, ltd.'),
            users: [u1, u2] as Set<User>
        ).save failOnError: true

        when: 'I save this helpdesk'
        h.afterInsert()

        then: 'the helpdesk user associations have been created'
        2 == HelpdeskUser.count()
        h == HelpdeskUser.findByUser(u1)?.helpdesk
        h == HelpdeskUser.findByUser(u2)?.helpdesk
    }

    @Ignore('ExecuteUpdate currently not supported in GORM')
    void 'Update user associations at update'() {
        given: 'some users'
        User u1 = new User(userName: 'User 1')
        User u2 = new User(userName: 'User 2')
        User u3 = new User(userName: 'User 3')

        and: 'a helpdesk'
        Helpdesk h = new Helpdesk(
            name: 'Test', accessCode: 'ABC123',
            organization: new Organization(name: 'My Company, ltd.'),
            users: [u1, u2] as Set<User>
        ).save failOnError: true
        h.afterInsert()

        when: 'I update this helpdesk'
        h.users = [u3, u2] as Set<User>
        h.afterUpdate()

        then: 'the helpdesk user associations have been updated'
        2 == HelpdeskUser.count()
        h == HelpdeskUser.findByUser(u2)?.helpdesk
        h == HelpdeskUser.findByUser(u3)?.helpdesk
    }

    @Ignore('ExecuteUpdate currently not supported in GORM')
    void 'Remove user associations at delete'() {
        given: 'some users'
        User u1 = new User(userName: 'User 1')
        User u2 = new User(userName: 'User 2')

        and: 'a helpdesk'
        Helpdesk h = new Helpdesk(
            name: 'Test', accessCode: 'ABC123',
            organization: new Organization(name: 'My Company, ltd.'),
            users: [u1, u2] as Set<User>
        ).save failOnError: true
        h.afterInsert()

        when: 'I delete this helpdesk'
        h.beforeDelete()

        then: 'the helpdesk user associations have been removed'
        0 == HelpdeskUser.count()
    }

    void 'Get users of helpdesk'() {
        given: 'some users'
        User u1 = new User(userName: 'User 1')
        User u2 = new User(userName: 'User 2')

        and: 'a helpdesk'
        Helpdesk h = new Helpdesk(
            name: 'Test', accessCode: 'ABC123', users: [u1, u2] as Set<User>
        )

        when: 'I get the users'
        Set<User> users = h.users

        then: 'I get the correct set'
        [u1, u2] as Set == users
    }

    def 'Equals is null-safe'() {
        given: 'a helpdesk'
        def h = new Helpdesk()

        expect:
        null != h
        h != null
        !h.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a helpdesk'
        def h = new Helpdesk()

        expect:
        h != 'foo'
        h != 45
        h != 45.3
        h != new Date()
    }

    def 'Instances are equal if they have the same URL name'() {
        given: 'two helpdesks with different properties'
        def h1 = new Helpdesk(name: 'Customer 1')
        def h2 = new Helpdesk(name: 'Customer 1')
        def h3 = new Helpdesk(name: 'Customer 1')

        expect: 'equals() is reflexive'
        h1 == h1
        h2 == h2
        h3 == h3

        and: 'all instances are equal and equals() is symmetric'
        h1 == h2
        h2 == h1
        h2 == h3
        h3 == h2

        and: 'equals() is transitive'
        h1 == h3
        h3 == h1
    }

    def 'Instances are unequal if they have the different URL names'() {
        given: 'two helpdesks with different properties'
        def h1 = new Helpdesk(name: 'Customer 1')
        def h2 = new Helpdesk(name: 'Customer 2')
        def h3 = new Helpdesk(name: 'Customer 3')

        expect: 'equals() is reflexive'
        h1 == h1
        h2 == h2
        h3 == h3

        and: 'all instances are unequal and equals() is symmetric'
        h1 != h2
        h2 != h1
        h2 != h3
        h3 != h2

        and: 'equals() is transitive'
        h1 != h3
        h3 != h1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def h = new Helpdesk()

        expect:
        0i == h.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def h = new Helpdesk(name: 'Customer')
        h.id = 7403L

        when: 'I compute the hash code'
        int hc = h.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            h = new Helpdesk(name: 'Customer')
            h.id = 7403L
            hc == h.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'two helpdesks with different properties'
        def h1 = new Helpdesk(name: 'Customer 1')
        def h2 = new Helpdesk(name: 'Customer 1')
        def h3 = new Helpdesk(name: 'Customer 1')

        expect:
        h1.hashCode() == h2.hashCode()
        h2.hashCode() == h3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'two helpdesks with different properties'
        def h1 = new Helpdesk(name: 'Customer 1')
        def h2 = new Helpdesk(name: 'Customer 2')
        def h3 = new Helpdesk(name: 'Customer 3')

        expect:
        h1.hashCode() != h2.hashCode()
        h2.hashCode() != h3.hashCode()
    }

    def 'Can convert to string'(String name, String s) {
        given: 'an empty helpdesk'
        def h = new Helpdesk()

        when: 'I set the name'
        h.name = name

        then: 'I get a valid string representation'
        s == h.toString()

        where:
        name            || s
        null            || ''
        ''              || ''
        '   '           || ''
        'a'             || 'a'
        'abc'           || 'abc'
        '  foo  '       || 'foo'
        'Services'      || 'Services'
    }

    def 'Name must not be blank'(String n, boolean v) {
        given: 'a user'
        User u = new User(userName: 'jsmith')

        and: 'an organization'
        Organization o = new Organization(name: 'My company, ltd.')

        and: 'a quite valid helpdesk'
        Helpdesk h = new Helpdesk(
            accessCode: 'ABC123',
            users: [u] as Set<User>,
            organization: o
        )

        when: 'I set the name'
        h.name = n

        then: 'the instance is valid or not'
        v == h.validate()

        where:
        n       || v
        null    || false
        ''      || false
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    def 'Organization must not be null'() {
        given: 'a quite valid helpdesk'
        Helpdesk h = new Helpdesk(
            name: 'My helpdesk',
            accessCode: 'ABC123',
            users: [new User(userName: 'jsmith')] as Set<User>,
        )

        when: 'I set the organization'
        h.organization = new Organization(name: 'My company, ltd.')

        then: 'the instance is valid'
        h.validate()

        when: 'I unset the organization'
        h.organization = null

        then: 'the instance is not valid'
        !h.validate()
    }
}
