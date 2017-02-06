/*
 * DepartmentSpec.groovy
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
 */


package org.amcworld.springcrm

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(Department)
@Mock([Department, Staff])
class DepartmentSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Copy using constructor'() {
        given: 'a staff'
        def staff = new Staff(
            number: '4379',
            firstName: 'John',
            lastName: 'Doe'
        )

        and: 'a department'
        def d1 = new Department(
            name: 'Field service',
            costCenter: '2748A',
            manager: staff
        )

        when: 'I copy that department using the constructor'
        def d2 = new Department(d1)

        then: 'I have some properties of the first department in the second one'
        d2.name == d1.name
        d2.costCenter == d1.costCenter
        d2.manager.is d1.manager

        and: 'some properties are unset'
        !d2.id
    }

    def 'Equals is null-safe'() {
        given: 'a department'
        def d = new Department()

        expect:
        null != d
        d != null
        !d.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a department'
        def d = new Department()

        expect:
        d != 'foo'
        d != 45
        d != 45.3
        d != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def d1 = new Department(name: 'Field service')
        def d2 = new Department(name: 'IT')
        def d3 = new Department(name: 'Management')

        expect: 'equals() is reflexive'
        d1 == d1
        d2 == d2
        d3 == d3

        and: 'all instances are equal and equals() is symmetric'
        d1 == d2
        d2 == d1
        d2 == d3
        d3 == d2

        and: 'equals() is transitive'
        d1 == d3
        d3 == d1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with different properties but same IDs'
        def d1 = new Department(name: 'Field service')
        d1.id = 7403L
        def d2 = new Department(name: 'IT')
        d2.id = 7403L
        def d3 = new Department(name: 'Management')
        d3.id = 7403L

        expect: 'equals() is reflexive'
        d1 == d1
        d2 == d2
        d3 == d3

        and: 'all instances are equal and equals() is symmetric'
        d1 == d2
        d2 == d1
        d2 == d3
        d3 == d2

        and: 'equals() is transitive'
        d1 == d3
        d3 == d1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with same properties but different IDs'
        def d1 = new Department(name: 'Field service')
        d1.id = 7403L
        def d2 = new Department(name: 'Field service')
        d2.id = 7404L
        def d3 = new Department(name: 'Field service')
        d3.id = 8473L

        expect: 'equals() is reflexive'
        d1 == d1
        d2 == d2
        d3 == d3

        and: 'all instances are unequal and equals() is symmetric'
        d1 != d2
        d2 != d1
        d2 != d3
        d3 != d2

        and: 'equals() is transitive'
        d1 != d3
        d3 != d1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def d = new Department()

        expect:
        0i == d.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def d = new Department(name: 'Field service')

        expect:
        0i == d.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def d = new Department(name: 'Field service')
        d.id = 7403L

        when: 'I compute the hash code'
        int h = d.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            d = new Department(name: 'IT')
            d.id = 7403L
            h == d.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def d1 = new Department(name: 'Field service')
        d1.id = 7403L
        def d2 = new Department(name: 'IT')
        d2.id = 7403L
        def d3 = new Department(name: 'Management')
        d3.id = 7403L

        expect:
        d1.hashCode() == d2.hashCode()
        d2.hashCode() == d3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def d1 = new Department(name: 'Field service')
        d1.id = 7403L
        def d2 = new Department(name: 'Field service')
        d2.id = 7404L
        def d3 = new Department(name: 'Field service')
        d3.id = 8473L

        expect:
        d1.hashCode() != d2.hashCode()
        d2.hashCode() != d3.hashCode()
    }

    def 'Convert to string'() {
        given: 'an empty department'
        def d = new Department()

        when: 'I set the name'
        d.name = 'Field service'

        then: 'I get a useful string representation'
        'Field service' == d.toString()

        when: 'I empty the name'
        d.name = ''

        then: 'I get an empty string representation'
        '' == d.toString()

        when: 'I unset the name'
        d.name = null

        then: 'I get an empty string representation'
        '' == d.toString()
    }

    def 'Name must not be null or blank'(String name, boolean valid) {
        when:
        def d = new Department(name: name)

        then:
        valid == d.validate()

        where:
        name            || valid
        null            || false
        ''              || false
        ' '             || false
        '      '        || false
        '  \t \n '      || false
        'foo'           || true
        'any name'      || true
    }

    def 'Name must not be too long'(String name, boolean valid) {
        when:
        def d = new Department(name: name)

        then:
        valid == d.validate()

        where:
        name            || valid
        'foo'           || true
        'any name'      || true
        'x' * 50        || true
        'x' * 51        || false
    }

    def 'Cost center must not be too long'(String cc, boolean valid) {
        when:
        def d = new Department(name: 'Field service', costCenter: cc)

        then:
        valid == d.validate()

        where:
        cc              || valid
        null            || true
        ''              || true
        '   '           || true
        '  \t \n '      || true
        'foo'           || true
        'any name'      || true
        'x' * 40        || true
        'x' * 41        || false
    }
}
