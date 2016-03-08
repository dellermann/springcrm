/*
 * CredentialSpec.groovy
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
import spock.lang.Specification


@Mock([User])
class CredentialSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Cannot create a credential from non-persisted user'() {
        when: 'I try to create a credential from a non-persisted user'
        new Credential(new User())

        then: 'I get an exception'
        thrown IllegalArgumentException
    }

    def 'Create an instance from a user'() {
        given: 'a user'
        def u = makeUser()

        when: 'I create a credential'
        def c = new Credential(u)

        then: 'the properties are set correctly'
        1704L == c.id
        u.userName == c.userName
        u.firstName == c.firstName
        u.lastName == c.lastName
        u.phone == c.phone
        u.phoneHome == c.phoneHome
        u.mobile == c.mobile
        u.fax == c.fax
        u.email == c.email
        u.admin == c.admin
        u.settings == c.settings
        EnumSet.of(CALL, TICKET, NOTE) == c.allowedModules
        ['call', 'ticket', 'note'] as Set == c.allowedControllers
    }

    def 'Class is immutable'() {
        given: 'a credential'
        def c = new Credential(makeUser())

        when: 'I set a property'
        c.id = 45L

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.userName = 'bwayne'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.firstName = 'Barbra'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.lastName = 'Wayne'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.phone = '073054'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.phoneHome = '546446'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.mobile = '6456412'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.fax = '89713146'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.email = 'b.wayne@example.com'

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.admin = true

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.settings = null

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.allowedModules = EnumSet.of(CALL)

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException

        when: 'I set a property'
        c.allowedControllers = ['CALL', 'TICKET'] as Set

        then: 'an exception is thrown'
        thrown ReadOnlyPropertyException
    }

    def 'Obtain allowed controllers'(String m, List e) {
        given: 'a user'
        def user = new User(allowedModules: m)
        user.id = 5

        when: 'I create a credential with a discrete list of allowed modules'
        def c = new Credential(user)

        then: 'I get the correct set of controllers'
        e as Set == c.allowedControllers

        where:
        m                           || e
        null                        || []
        ''                          || []
        '    '                      || []
        'CALL'                      || ['call']
        'CALL,TICKET'               || ['call', 'ticket']
        'CALL, TICKET'              || ['call', 'ticket']
        'CALL, TICKET, NOTE'        || ['call', 'ticket', 'note']
        'CONTACT, INVOICE'          || ['organization', 'person', 'invoice']
    }

    def 'Obtaining allowed controllers set does not change internal'() {
        given: 'a user'
        def user = new User(allowedModules: 'CALL,TICKET,NOTE')
        user.id = 5

        and: 'a credential'
        def c = new Credential(user)

        when: 'I obtain the allowed controllers'
        def s1 = c.allowedControllers
        def s2 = c.allowedControllers

        then: 'the objects are not the same'
        !s1.is(s2)

        and: 'they are equal'
        s1 == s2
    }

    def 'Obtain allowed modules as set of enums'(String m, EnumSet<Module> e) {
        given: 'a user'
        def user = new User(allowedModules: m)
        user.id = 5

        when: 'I create a credential with a discrete list of allowed modules'
        def c = new Credential(user)

        then: 'I get the correct set of module enums'
        e == c.allowedModules

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

    def 'Obtaining allowed modules set does not change internal'() {
        given: 'a user'
        def user = new User(allowedModules: 'CALL,TICKET,NOTE')
        user.id = 5

        and: 'a credential'
        def c = new Credential(user)

        when: 'I obtain the allowed modules'
        def s1 = c.allowedModules
        def s2 = c.allowedModules

        then: 'the objects are not the same'
        !s1.is(s2)

        and: 'they are equal'
        s1 == s2
    }

    def 'Obtain the full name'(String fn, String ln, String e) {
        given: 'a user with first name and last name'
        def u = new User(firstName: fn, lastName: ln)
        u.id = 5

        when: 'I create a credential'
        def c = new Credential(u)

        then: 'the full name is computed correctly'
        e == c.fullName

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

    def 'Check allowed controllers for admins'(List<String> controllers) {
        given: 'a credential'
        def c = new Credential(makeUser())

        expect:
        c.checkAllowedControllers(controllers as Set)

        where:
        controllers                 || _
        null                        || _
        []                          || _
        ['person']                  || _
        ['person', 'invoice']       || _
        ['call']                    || _
        ['note']                    || _
        ['call', 'note']            || _
    }

    def 'Check allowed controllers for non-admins'(List<String> controllers,
                                                   boolean b)
    {
        given:
        def u = makeUser()
        u.admin = false

        and: 'a credential'
        def c = new Credential(u)

        expect:
        b == c.checkAllowedControllers(controllers as Set)

        where:
        controllers                     || b
        null                            || false
        []                              || false
        ['person']                      || false
        ['person', 'invoice']           || false
        ['call']                        || true
        ['note']                        || true
        ['call', 'note']                || true
        ['call', 'note', 'ticket']      || true
        ['call', 'note', 'person']      || true
        ['person', 'invoice', 'call']   || true
    }

    def 'Check allowed modules for admins'(EnumSet<Module> modules) {
        given: 'a credential'
        def c = new Credential(makeUser())

        expect:
        c.checkAllowedModules(modules)

        where:
        modules                         || _
        null                            || _
        EnumSet.noneOf(Module)          || _
        EnumSet.of(CONTACT)             || _
        EnumSet.of(CONTACT, INVOICE)    || _
        EnumSet.of(CALL)                || _
        EnumSet.of(NOTE)                || _
        EnumSet.of(CALL, NOTE)          || _
    }

    def 'Check allowed modules for non-admins'(EnumSet<Module> modules,
                                               boolean b)
    {
        given:
        def u = makeUser()
        u.admin = false

        and: 'a credential'
        def c = new Credential(u)

        expect:
        b == c.checkAllowedModules(modules)

        where:
        modules                             || b
        null                                || false
        EnumSet.noneOf(Module)              || false
        EnumSet.of(CONTACT)                 || false
        EnumSet.of(CONTACT, INVOICE)        || false
        EnumSet.of(CALL)                    || true
        EnumSet.of(NOTE)                    || true
        EnumSet.of(CALL, NOTE)              || true
        EnumSet.of(CALL, NOTE, TICKET)      || true
        EnumSet.of(CALL, NOTE, CONTACT)     || true
        EnumSet.of(CONTACT, INVOICE, CALL)  || true
    }

    def 'Equals is null-safe'() {
        given: 'a credential'
        def c = new Credential(makeUser())

        expect:
        null != c
        c != null
        !c.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a credential'
        def c = new Credential(makeUser())

        expect:
        c != 'foo'
        c != 45
        c != 45.3
        c != new Date()
    }

    def 'Instances are equal if they have the same ID'() {
        given: 'three instances with different properties but same IDs'
        def c1 = new Credential(makeUser())
        def c2 = new Credential(makeUser())
        def c3 = new Credential(makeUser())

        expect: 'equals() is reflexive'
        c1 == c1
        c2 == c2
        c3 == c3

        and: 'all instances are equal and equals() is symmetric'
        c1 == c2
        c2 == c1
        c2 == c3
        c3 == c2

        and: 'equals() is transitive'
        c1 == c3
        c3 == c1
    }

    def 'Instances are unequal if they have the different ID'() {
        given: 'three instances with same properties but different IDs'
        def u1 = makeUser()
        u1.id = 7403L
        def u2 = makeUser()
        u2.id = 7404L
        def u3 = makeUser()
        u3.id = 8473L

        and: 'three credentials'
        def c1 = new Credential(u1)
        def c2 = new Credential(u2)
        def c3 = new Credential(u3)

        expect: 'equals() is reflexive'
        c1 == c1
        c2 == c2
        c3 == c3

        and: 'all instances are unequal and equals() is symmetric'
        c1 != c2
        c2 != c1
        c2 != c3
        c3 != c2

        and: 'equals() is transitive'
        c1 != c3
        c3 != c1
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def c = new Credential(makeUser())

        when: 'I compute the hash code'
        int h = c.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            c = new Credential(makeUser())
            h == c.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def c1 = new Credential(makeUser())
        def c2 = new Credential(makeUser())
        def c3 = new Credential(makeUser())

        expect:
        c1.hashCode() == c2.hashCode()
        c2.hashCode() == c3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def u1 = makeUser()
        u1.id = 7403L
        def u2 = makeUser()
        u2.id = 7404L
        def u3 = makeUser()
        u3.id = 8473L

        and: 'three credentials'
        def c1 = new Credential(u1)
        def c2 = new Credential(u2)
        def c3 = new Credential(u3)

        expect:
        c1.hashCode() != c2.hashCode()
        c2.hashCode() != c3.hashCode()
    }

    def 'Convert to string'(String fn, String ln, String e) {
        given: 'a user with first name and last name'
        def u = new User(firstName: fn, lastName: ln)
        u.id = 5

        when: 'I create a credential'
        def c = new Credential(u)

        then: 'the string representation is correct'
        e == u.toString()

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

    def 'Load user of credential'() {
        given: 'a user'
        def u = makeUser()
        u.save()

        and: 'a credential'
        def c = new Credential(u)

        when: 'I obtain the user'
        def user = c.loadUser()

        then: 'I get a valid user object'
        u == user
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
}
