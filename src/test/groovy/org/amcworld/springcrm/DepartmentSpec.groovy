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

    def 'Check for equality'() {
        given: 'two department with different content'
        def d1 = new Department(name: 'foo')
        def d2 = new Department(name: 'bar')

        and: 'the same IDs'
        d1.id = 4903
        d2.id = 4903

        expect: 'both these departments are equal'
        d2 == d1
        d1 == d2
    }

    def 'Check for inequality'() {
        given: 'two departments with the same content'
        def d1 = new Department(name: 'foo')
        def d2 = new Department(name: 'foo')

        and: 'both the IDs set to different values'
        d1.id = 4903
        d2.id = 4904

        when: 'I compare both these departments'
        boolean b1 = (d2 != d1)
        boolean b2 = (d1 != d2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        d2 = null

        then: 'they are not equal'
        d2 != d1
        d1 != d2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        d1 != s
    }

    def 'Compute hash code'() {
        when: 'I create a department with no ID'
        def d = new Department()

        then: 'I get a valid hash code'
        0 == d.hashCode()

        when: 'I create a department with discrete IDs'
        d.id = id

        then: 'I get a hash code using this ID'
        e == d.hashCode()

        where:
        id      ||     e
        0       ||     0
        1       ||     1
        10      ||    10
        105     ||   105
        9404    ||  9404
        37603   || 37603
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
        d.validate()

        then:
        !valid == d.hasErrors()

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
        d.validate()

        then:
        !valid == d.hasErrors()

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
        d.validate()

        then:
        !valid == d.hasErrors()

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
