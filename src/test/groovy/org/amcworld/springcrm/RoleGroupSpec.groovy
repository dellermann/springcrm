/*
 * RoleGroupSpec.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

    void 'Name must not be blank or null'() {
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
        '  \t ' || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }
}
