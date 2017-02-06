/*
 * UserSpec.groovy
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

import static org.amcworld.springcrm.Module.*

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(User)
@Mock([User, UserSetting])
class UserSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty user'
        def u = new User()

        then: 'the properties are initialized properly'
        null == u.id
        null == u.userName
        null == u.password
        null == u.firstName
        null == u.lastName
        null == u.phone
        null == u.phoneHome
        null == u.mobile
        null == u.fax
        null == u.email
        !u.admin
        null == u.allowedModules
        null == u.dateCreated
        null == u.lastUpdated
        null == u.settings
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty user'
        def u1 = new User()

        when: 'I copy the user using the constructor'
        def u2 = new User(u1)

        then: 'the properties are set properly'
        null == u2.id
        null == u2.userName
        null == u2.password
        null == u2.firstName
        null == u2.lastName
        null == u2.phone
        null == u2.phoneHome
        null == u2.mobile
        null == u2.fax
        null == u2.email
        !u2.admin
        null == u2.allowedModules
        null == u2.dateCreated
        null == u2.lastUpdated
        null == u2.settings
    }

    def 'Copy a user using constructor'() {
        given: 'a user with various properties'
        def u1 = new User(
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

        when: 'I copy the user using the constructor'
        def u2 = new User(u1)

        then: 'some properties are the equal'
        u1.userName == u2.userName
        u1.firstName == u2.firstName
        u1.lastName == u2.lastName
        u1.phone == u2.phone
        u1.phoneHome == u2.phoneHome
        u1.mobile == u2.mobile
        u1.fax == u2.fax
        u1.email == u2.email
        u1.admin == u2.admin
        u1.allowedModules == u2.allowedModules

        and: 'some properties are unset'
        null == u2.id
        null == u2.password
        null == u2.dateCreated
        null == u2.lastUpdated
        null == u2.settings
    }

    def 'Obtain allowed modules as set of enums'(String m, EnumSet<Module> e) {
        when: 'I create a user with a discrete list of allowed modules'
        def u = new User(allowedModules: m)

        then: 'I get the correct set of module enums'
        e == u.allowedModulesAsSet

        where:
        m                           || e
        null                        || EnumSet.noneOf(Module)
        ''                          || EnumSet.noneOf(Module)
        '    '                      || EnumSet.noneOf(Module)
        'CALL'                      || EnumSet.of(CALL)
        'CALL,TICKET'               || EnumSet.of(CALL, TICKET)
        'CALL, TICKET'              || EnumSet.of(CALL, TICKET)
        'CALL, TICKET, NOTE'        || EnumSet.of(CALL, TICKET, NOTE)
        'CALL, TICKET, NOTE, CALL'  || EnumSet.of(CALL, TICKET, NOTE)
        'CONTACT, INVOICE'          || EnumSet.of(CONTACT, INVOICE)
    }

    def 'Set allowed modules as set of enums'(EnumSet<Module> m, String e) {
        when: 'I create a user with a set of allowed modules'
        def u = new User()
        u.allowedModulesAsSet = m

        then: 'I get the correct module list string'
        e == u.allowedModules

        where:
        m                               || e
        null                            || ''
        EnumSet.noneOf(Module)          || ''
        EnumSet.of(CALL)                || 'CALL'
        EnumSet.of(CALL, TICKET)        || 'CALL,TICKET'
        EnumSet.of(CALL, TICKET, NOTE)  || 'CALL,NOTE,TICKET'
    }

    @spock.lang.Unroll
    def 'Obtain the allowed modules names'(String m, List<String> e) {
        when: 'I create a user with a discrete list of allowed modules'
        def u = new User()
        u.allowedModules = m

        then: 'I get the correct set of module names'
        e as Set == u.allowedModulesNames

        where:
        m                           || e
        null                        || []
        ''                          || []
        '    '                      || []
        'CALL'                      || ['CALL']
        'CALL,TICKET'               || ['CALL', 'TICKET']
        'CALL, TICKET'              || ['CALL', 'TICKET']
        'CALL, TICKET, NOTE'        || ['CALL', 'NOTE', 'TICKET']
        'CALL, TICKET, NOTE, CALL'  || ['CALL', 'NOTE', 'TICKET']
        'CONTACT, INVOICE'          || ['CONTACT', 'INVOICE']
    }

    def 'Set the allowed module names'(List<String> m, String e) {
        when: 'I create a user with a set of allowed modules names'
        def u = new User()
        u.allowedModulesNames = m as Set

        then: 'I get the correct module list string'
        e == u.allowedModules

        where:
        m                               || e
        null                            || ''
        []                              || ''
        ['CALL']                        || 'CALL'
        ['CALL', 'TICKET']              || 'CALL,TICKET'
        ['CALL', 'TICKET', 'NOTE']      || 'CALL,NOTE,TICKET'
    }

    def 'Obtain the full name'(String fn, String ln, String e) {
        when: 'I create a user with first name and last name'
        def u = new User(firstName: fn, lastName: ln)

        then: 'the full name is computed correctly'
        e == u.fullName

        where:
        fn          || ln           || e
        null        || null         || ''
        null        || ''           || ''
        null        || '   \t '     || ''
        null        || 'a'          || 'a'
        null        || 'Smith'      || 'Smith'
        null        || ' Smith\t '  || 'Smith'
        ''          || null         || ''
        ''          || ''           || ''
        ''          || '   \t '     || ''
        ''          || 'a'          || 'a'
        ''          || 'Smith'      || 'Smith'
        ''          || ' Smith\t '  || 'Smith'
        '   \t '    || null         || ''
        '   \t '    || ''           || ''
        '   \t '    || '   \t '     || ''
        '   \t '    || 'a'          || 'a'
        '   \t '    || 'Smith'      || 'Smith'
        '   \t '    || ' Smith\t '  || 'Smith'
        'a'         || null         || 'a'
        'a'         || ''           || 'a'
        'a'         || '   \t '     || 'a'
        'a'         || 'a'          || 'a a'
        'a'         || 'Smith'      || 'a Smith'
        'a'         || ' Smith\t '  || 'a Smith'
        'John'      || null         || 'John'
        'John'      || ''           || 'John'
        'John'      || '   \t '     || 'John'
        'John'      || 'a'          || 'John a'
        'John'      || 'Smith'      || 'John Smith'
        'John'      || ' Smith\t '  || 'John Smith'
        ' John \t ' || null         || 'John'
        ' John \t ' || ''           || 'John'
        ' John \t ' || '   \t '     || 'John'
        ' John \t ' || 'a'          || 'John a'
        ' John \t ' || 'Smith'      || 'John Smith'
        ' John \t ' || ' Smith\t '  || 'John Smith'
    }

    def 'Obtain raw user settings'() {
        given: 'a user'
        def u1 = new User(
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
        u1.save()

        and: 'another user'
        def u2 = new User(
            userName: 'bwayne',
            password: 'qxyz',
            firstName: 'Barbra',
            lastName: 'Wayne',
            phone: '+49 30 4040404',
            phoneHome: '+49 30 5050505',
            mobile: '+49 172 7070707',
            fax: '+49 30 4040405',
            email: 'b.wayne@example.com',
            admin: false,
            allowedModules: 'CALL, TICKET, NOTE, CONTACT'
        )
        u2.save()

        and: 'various settings for both the users'
        def us1 = new UserSetting(user: u1, name: 'foo', value: 'bar')
        def us2 = new UserSetting(user: u1, name: 'whee', value: 'buzz')
        def us3 = new UserSetting(user: u2, name: 'order', value: 'asc')
        us1.save()
        us2.save()
        us3.save()

        when: 'I obtain the settings of the first user'
        List<UserSetting> l1 = u1.rawSettings

        then: 'I get two settings'
        null != l1
        2 == l1.size()
        for (UserSetting us : l1) {
            if (us.name == 'foo') {
                'bar' == us.value
            } else {
                'buzz' == us.value
            }
            u1 == us.user
        }

        when: 'I obtain the settings of another user'
        List<UserSetting> l2 = u2.rawSettings

        then: 'I get one setting'
        null != l2
        1 == l2.size()
        'order' == l2[0].name
        'asc' == l2[0].value
        u2 == l2[0].user
    }

    def 'Cannot set raw settings'() {
        given: 'a user'
        def u = new User()

        when: 'I try to set the raw settings'
        u.rawSettings = []

        then: 'I get an exception'
        thrown ReadOnlyPropertyException
    }

    def 'Obtain user settings'() {
        given: 'a user'
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
        u.save()

        and: 'various settings for the user'
        def us1 = new UserSetting(user: u, name: 'foo', value: 'bar')
        def us2 = new UserSetting(user: u, name: 'whee', value: 'buzz')
        us1.save()
        us2.save()

        and: 'I simulate a call to afterLoad()'
        u.afterLoad()

        when: 'I obtain the settings of the first user'
        UserSettings us = u.settings

        then: 'I get the settings'
        null != us
        'bar' == us['foo']
        'buzz' == us['whee']
    }

    def 'Cannot set settings'() {
        given: 'a user'
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
        u.save()

        and: 'various settings for the user'
        def us1 = new UserSetting(user: u, name: 'foo', value: 'bar')
        def us2 = new UserSetting(user: u, name: 'whee', value: 'buzz')
        us1.save()
        us2.save()

        and: 'I simulate a call to afterLoad()'
        u.afterLoad()
        assert null != u.settings

        when: 'I try to set the settings'
        u.settings = new UserSettings(u)

        then: 'the user instance is unchanged'
        null != u.settings
    }

    def 'Equals is null-safe'() {
        given: 'a user'
        def u = new User()

        expect:
        null != u
        u != null
        !u.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a user'
        def u = new User()

        expect:
        u != 'foo'
        u != 45
        u != 45.3
        u != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def u1 = new User(userName: 'jsmith')
        def u2 = new User(userName: 'jsmith')
        def u3 = new User(userName: 'jsmith')

        expect: 'equals() is reflexive'
        u1 == u1
        u2 == u2
        u3 == u3

        and: 'all instances are equal and equals() is symmetric'
        u1 == u2
        u2 == u1
        u2 == u3
        u3 == u2

        and: 'equals() is transitive'
        u1 == u3
        u3 == u1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with different IDs but same name'
        def u1 = new User(userName: 'jsmith')
        u1.id = 7403L
        def u2 = new User(userName: 'jsmith')
        u2.id = 7404L
        def u3 = new User(userName: 'jsmith')
        u3.id = 8473L

        expect: 'equals() is reflexive'
        u1 == u1
        u2 == u2
        u3 == u3

        and: 'all instances are equal and equals() is symmetric'
        u1 == u2
        u2 == u1
        u2 == u3
        u3 == u2

        and: 'equals() is transitive'
        u1 == u3
        u3 == u1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with same IDs but different names'
        def u1 = new User(userName: 'jsmith')
        u1.id = 7403L
        def u2 = new User(userName: 'bwayne')
        u2.id = 7403L
        def u3 = new User(userName: 'mdoe')
        u3.id = 7403L

        expect: 'equals() is reflexive'
        u1 == u1
        u2 == u2
        u3 == u3

        and: 'all instances are unequal and equals() is symmetric'
        u1 != u2
        u2 != u1
        u2 != u3
        u3 != u2

        and: 'equals() is transitive'
        u1 != u3
        u3 != u1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def u = new User()

        expect:
        0i == u.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def u = new User(userName: 'jsmith')

        expect:
        'jsmith'.hashCode() == u.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def u = new User(userName: 'jsmith')
        u.id = 7403L

        when: 'I compute the hash code'
        int h = u.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            u = new User(userName: 'bwayne')
            u.id = 7403L
            h == u.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different IDs but same names'
        def u1 = new User(userName: 'jsmith')
        u1.id = 7403L
        def u2 = new User(userName: 'jsmith')
        u2.id = 7404L
        def u3 = new User(userName: 'jsmith')
        u3.id = 8473L

        expect:
        u1.hashCode() == u2.hashCode()
        u2.hashCode() == u3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same IDs but different names'
        def u1 = new User(userName: 'jsmith')
        u1.id = 7403L
        def u2 = new User(userName: 'bwayne')
        u2.id = 7403L
        def u3 = new User(userName: 'mdoe')
        u3.id = 7403L

        expect:
        u1.hashCode() != u2.hashCode()
        u2.hashCode() != u3.hashCode()
    }

    def 'Convert to string'(String fn, String ln, String e) {
        when: 'I create a user with first name and last name'
        def u = new User(firstName: fn, lastName: ln)

        then: 'the string representation is correct'
        e == u.toString()

        where:
        fn          | ln           || e
        null        | null         || ''
        null        | ''           || ''
        null        | '   \t '     || ''
        null        | 'a'          || 'a'
        null        | 'Smith'      || 'Smith'
        null        | ' Smith\t '  || 'Smith'
        ''          | null         || ''
        ''          | ''           || ''
        ''          | '   \t '     || ''
        ''          | 'a'          || 'a'
        ''          | 'Smith'      || 'Smith'
        ''          | ' Smith\t '  || 'Smith'
        '   \t '    | null         || ''
        '   \t '    | ''           || ''
        '   \t '    | '   \t '     || ''
        '   \t '    | 'a'          || 'a'
        '   \t '    | 'Smith'      || 'Smith'
        '   \t '    | ' Smith\t '  || 'Smith'
        'a'         | null         || 'a'
        'a'         | ''           || 'a'
        'a'         | '   \t '     || 'a'
        'a'         | 'a'          || 'a a'
        'a'         | 'Smith'      || 'a Smith'
        'a'         | ' Smith\t '  || 'a Smith'
        'John'      | null         || 'John'
        'John'      | ''           || 'John'
        'John'      | '   \t '     || 'John'
        'John'      | 'a'          || 'John a'
        'John'      | 'Smith'      || 'John Smith'
        'John'      | ' Smith\t '  || 'John Smith'
        ' John \t ' | null         || 'John'
        ' John \t ' | ''           || 'John'
        ' John \t ' | '   \t '     || 'John'
        ' John \t ' | 'a'          || 'John a'
        ' John \t ' | 'Smith'      || 'John Smith'
        ' John \t ' | ' Smith\t '  || 'John Smith'
    }

    def 'User name must not be blank'(String un, boolean v) {
        given: 'a quite valid user'
        def u = new User(
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

        when: 'I set the user name'
        u.userName = un

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        un      || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'jdoe'  || true
    }

    @spock.lang.Ignore('In Grails 3.1.4, unique constraints are not mocked correctly')
    def 'User name must be unique'() {

        // XXX currently, unique constraints are not mocked correctly in Grails 3.1.4
        given: 'two users with different user names'
        mockDomain(
            User,
            [
                new User(
                    userName: 'jdoe', password: 'test', firstName: 'Peter',
                    lastName: 'Smith', email: 'psmith@example.com'
                ),
                new User(
                    userName: 'admin', password: 'test', firstName: 'Peter',
                    lastName: 'Smith', email: 'admin@example.com'
                )
            ]
        )

        when: 'I create another user with a already used user name'
        def badUser = new User(
            userName: 'jdoe', password: 'abcd', firstName: 'John',
            lastName: 'Doe', email: 'jdoe@example.com'
        )
        mockDomain User, [badUser]

        then: 'the unique constraint has not been fulfilled'
        'unique' == badUser.errors['userName']

        and: 'the user has not been saved'
        2 == User.count()

        when: 'I create another user with a new user name'
        def goodUser = new User(
            userName: 'good', password: 'test', firstName: 'Peter',
            lastName: 'Smith', email: 'good@example.com'
        )
        mockDomain User, [goodUser]

        then: 'that user has been saved'
        3 == User.count()
        User.findByUserNameAndPassword 'good', 'test'
    }

    def 'Password must not be blank'(String p, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
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

        when: 'I set the password'
        u.password = p

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        p       || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'foo!'  || true
    }

    def 'First name must not be blank'(String fn, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the first name'
        u.firstName = fn

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        fn      || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'John'  || true
    }

    def 'Last name must not be blank'(String ln, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the last name'
        u.lastName = ln

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        ln      || v
        null    || false
        ''      || false
        '   '   || false
        ' \t  ' || false
        'a'     || true
        ' abc ' || true
        '    x' || true
        'Doe'   || true
    }

    def 'Office phone number must not exceed length'(String p, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the phone number'
        u.phone = p

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        p           || v
        null        || true
        ''          || true
        '   '       || true
        ' \t  '     || true
        '1'         || true
        '1' * 10    || true
        '1' * 40    || true
        '1' * 41    || false
        '1' * 60    || false
    }

    def 'Home phone number must not exceed length'(String p, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the phone number'
        u.phoneHome = p

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        p           || v
        null        || true
        ''          || true
        '   '       || true
        ' \t  '     || true
        '1'         || true
        '1' * 10    || true
        '1' * 40    || true
        '1' * 41    || false
        '1' * 60    || false
    }

    def 'Mobile phone number must not exceed length'(String m, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the mobile phone number'
        u.mobile = m

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        m           || v
        null        || true
        ''          || true
        '   '       || true
        ' \t  '     || true
        '1'         || true
        '1' * 10    || true
        '1' * 40    || true
        '1' * 41    || false
        '1' * 60    || false
    }

    def 'Fax number must not exceed length'(String f, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the fax number'
        u.fax = f

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        f           || v
        null        || true
        ''          || true
        '   '       || true
        ' \t  '     || true
        '1'         || true
        '1' * 10    || true
        '1' * 40    || true
        '1' * 41    || false
        '1' * 60    || false
    }

    def 'E-mail must not be blank and must be valid'(String e, boolean v) {
        given: 'a quite valid user'
        def u = new User(
            userName: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )

        when: 'I set the e-mail'
        u.email = e

        then: 'the instance is valid or not'
        v == u.validate()

        where:
        e                       || v
        null                    || false
        ''                      || false
        '   '                   || false
        ' \t  '                 || false
        'a'                     || false
        'John'                  || false
        'j.smith'               || false
        'j.smith@'              || false
        '@example.com'          || false
        'j.smith@example.com'   || true
    }
}
