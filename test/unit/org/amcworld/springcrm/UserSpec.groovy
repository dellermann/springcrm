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

import spock.lang.Specification


class UserSpec extends Specification {

    //-- Feature methods ------------------------

    // TODO write methods for missing features

    def 'Obtain allowed controllers'(String m, List e) {
        when: 'I create a user with a discrete list of allowed modules'
        def u = new User(allowedModules: m)

        then: 'I get the correct set of controllers'
        e as Set == u.allowedControllers

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
        def u = new User(allowedModulesAsSet: m)

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

    def 'Obtain the allowed modules names'(String m, List<String> e) {
        when: 'I create a user with a discrete list of allowed modules'
        def u = new User(allowedModules: m)

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
        def u = new User(allowedModulesNames: m as Set)

        then: 'I get the correct module list string'
        e == u.allowedModules

        where:
        m                               || e
        null                            || null
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

    def 'Can convert to string'(String fn, String ln, String e) {
        when: 'I create a user with first name and last name'
        def u = new User(firstName: fn, lastName: ln)

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
}
