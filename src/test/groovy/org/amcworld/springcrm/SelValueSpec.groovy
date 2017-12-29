/*
 * SelValueSpec.groovy
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
 */


package org.amcworld.springcrm

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class SelValueSpec extends Specification implements DomainUnitTest<SelValue> {

    //-- Feature methods ------------------------

    def 'Creating an empty selection value initializes the properties'() {
        when: 'I create an empty selection value'
        def sv = new SelValue()

        then: 'the properties are initialized properly'
        null == sv.name
        0 == sv.orderId
    }

    def 'Equals is null-safe'() {
        given: 'a selection value'
        def s = new SelValue()

        expect:
        null != s
        s != null
        !s.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a selection value'
        def s = new SelValue()

        expect:
        s != 'foo'
        s != 45
        s != 45.3
        s != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def s1 = new SelValue(name: 'Mr.')
        def s2 = new SelValue(name: 'Mrs.')
        def s3 = new SelValue(name: 'Ms.')

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new SelValue(name: 'Mr.')
        s1.id = 7403L
        def s2 = new SelValue(name: 'Mrs.')
        s2.id = 7403L
        def s3 = new SelValue(name: 'Ms.')
        s3.id = 7403L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SelValue(name: 'Mr.')
        s1.id = 7403L
        def s2 = new SelValue(name: 'Mr.')
        s2.id = 7404L
        def s3 = new SelValue(name: 'Mr.')
        s3.id = 8473L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are unequal and equals() is symmetric'
        s1 != s2
        s2 != s1
        s2 != s3
        s3 != s2

        and: 'equals() is transitive'
        s1 != s3
        s3 != s1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def s = new SelValue()

        expect:
        0i == s.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def s = new SelValue(name: 'Mr.')

        expect:
        0i == s.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def s = new SelValue(name: 'Mr.')
        s.id = 7403L

        when: 'I compute the hash code'
        int h = s.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            s = new SelValue(name: 'Mrs.')
            s.id = 7403L
            h == s.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new SelValue(name: 'Mr.')
        s1.id = 7403L
        def s2 = new SelValue(name: 'Mrs.')
        s2.id = 7403L
        def s3 = new SelValue(name: 'Ms.')
        s3.id = 7403L

        expect:
        s1.hashCode() == s2.hashCode()
        s2.hashCode() == s3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new SelValue(name: 'Mr.')
        s1.id = 7403L
        def s2 = new SelValue(name: 'Mr.')
        s2.id = 7404L
        def s3 = new SelValue(name: 'Mr.')
        s3.id = 8473L

        expect:
        s1.hashCode() != s2.hashCode()
        s2.hashCode() != s3.hashCode()
    }

    def 'Can convert to string'(String name) {
        given: 'an empty selection value'
        def s = new SelValue()

        when: 'I set the name'
        s.name = name

        then: 'I get a valid string representation'
        (name ?: '') == s.toString()

        where:
        name << [null, '', '   ', 'a', 'abc', '  foo  ', 'Services']
    }

    def 'Name must not be blank'(String n, boolean v) {
        given: 'a quite valid selection value'
        def s = new SelValue()

        when: 'I set the name'
        s.name = n

        then: 'the instance is valid or not'
        v == s.validate()

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
