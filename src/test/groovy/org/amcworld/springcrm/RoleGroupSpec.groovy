/*
 * RoleGroupSpec.groovy
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
 *
 */


package org.amcworld.springcrm

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class RoleGroupSpec extends Specification implements DomainUnitTest<RoleGroup> {

    //-- Feature methods ------------------------

    def 'Check for administrators'(List<Role> authorities, boolean admin) {
        given: 'a role group'
        def rg = new RoleGroup()

        when: 'I set authorities'
        rg.authorities = authorities as Set<Role>

        then: 'the group is administrators or not'
        admin == rg.administrators

        where:
        authorities                                                         || admin
        null                                                                || false
        []                                                                  || false
        [new Role(authority: 'ROLE_F')]                                     || false
        [new Role(authority: 'ROLE_F'), new Role(authority: 'ROLE_B')]      || false
        [new Role(authority: 'ROLE_ADMIN')]                                 || true
        [new Role(authority: 'ROLE_ADMIN'), new Role(authority: 'ROLE_B')]  || true
    }

    def 'Can convert to string'(String n, String e) {
        expect:
        e == new RoleGroup(name: n).toString()

        where:
        n                   || e
        null                || ''
        ''                  || ''
        '  '                || ''
        'abc'               || 'abc'
        'Administrators'    || 'Administrators'
    }

    def 'Name must not be blank or null'(String n, boolean v) {
        given: 'a quite valid role group'
        def rg = new RoleGroup()

        when: 'I set the name'
        rg.name = n

        then: 'the instance is valid or not'
        v == rg.validate()

        where:
        n       || v
        null    || false
        ''      || false
//        '  '    || false      // TODO is considered as non-blank in Grails 3.3
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }
}
