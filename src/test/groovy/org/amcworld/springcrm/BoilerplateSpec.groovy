/*
 * BoilerplateSpec.groovy
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


class BoilerplateSpec extends Specification
    implements DomainUnitTest<Boilerplate>
{

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty boilerplate'
        def b = new Boilerplate()

        then: 'the properties are initialized properly'
        null == b.name
        null == b.content
        null == b.dateCreated
        null == b.lastUpdated
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty boilerplate'
        def b1 = new Boilerplate()

        when: 'I copy the boilerplate using the constructor'
        def b2 = new Boilerplate(b1)

        then: 'the properties are set properly'
        null == b2.name
        null == b2.content
        null == b2.dateCreated
        null == b2.lastUpdated
    }

    def 'Copy a boilerplate using constructor'() {
        given: 'some dates'
        Date dateCreated = new Date()
        Date lastUpdated = dateCreated + 4

        and: 'a boilerplate with various properties'
        def b1 = new Boilerplate(
            name: 'Invoice',
            content: 'for our works we state accounts for the following:',
            dateCreated: dateCreated,
            lastUpdated: lastUpdated
        )

        when: 'I copy the boilerplate using the constructor'
        def b2 = new Boilerplate(b1)

        then: 'some properties are the equal'
        b1.name == b2.name
        b1.content == b2.content

        and: 'some properties are unset'
        null == b2.id
        null == b2.dateCreated
        null == b2.lastUpdated
    }

    def 'Equals is null-safe'() {
        given: 'a boilerplate'
        def b = new Boilerplate()

        expect:
        null != b
        b != null
        !b.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a boilerplate'
        def b = new Boilerplate()

        expect:
        b != 'foo'
        b != 45
        b != 45.3
        b != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def b1 = new Boilerplate(name: 'Invoice')
        def b2 = new Boilerplate(name: 'Quote')
        def b3 = new Boilerplate(name: 'Sales order')

        expect: 'equals() is reflexive'
        b1 == b1
        b2 == b2
        b3 == b3

        and: 'all instances are equal and equals() is symmetric'
        b1 == b2
        b2 == b1
        b2 == b3
        b3 == b2

        and: 'equals() is transitive'
        b1 == b3
        b3 == b1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three boilerplates with same ID'
        def b1 = new Boilerplate(name: 'Invoice')
        b1.id = 7403L
        def b2 = new Boilerplate(name: 'Quote')
        b2.id = 7403L
        def b3 = new Boilerplate(name: 'Sales order')
        b3.id = 7403L

        expect: 'equals() is reflexive'
        b1 == b1
        b2 == b2
        b3 == b3

        and: 'all instances are equal and equals() is symmetric'
        b1 == b2
        b2 == b1
        b2 == b3
        b3 == b2

        and: 'equals() is transitive'
        b1 == b3
        b3 == b1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three boilerplates with different properties'
        def b1 = new Boilerplate(name: 'Invoice')
        b1.id = 7403L
        def b2 = new Boilerplate(name: 'Quote')
        b2.id = 7404L
        def b3 = new Boilerplate(name: 'Sales order')
        b3.id = 8473L

        expect: 'equals() is reflexive'
        b1 == b1
        b2 == b2
        b3 == b3

        and: 'all instances are unequal and equals() is symmetric'
        b1 != b2
        b2 != b1
        b2 != b3
        b3 != b2

        and: 'equals() is transitive'
        b1 != b3
        b3 != b1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def b = new Boilerplate()

        expect:
        0i == b.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def b = new Boilerplate(name: 'Invoice')

        expect:
        0i == b.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance'
        def b = new Boilerplate(name: 'Invoice')
        b.id = 7403L

        when: 'I compute the hash code'
        int h = b.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            b = new Boilerplate(name: 'Invoice')
            b.id = 7403L
            h == b.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three boilerplates with same ID'
        def b1 = new Boilerplate(name: 'Invoice')
        b1.id = 7403L
        def b2 = new Boilerplate(name: 'Quote')
        b2.id = 7403L
        def b3 = new Boilerplate(name: 'Sales order')
        b3.id = 7403L

        expect:
        b1.hashCode() == b2.hashCode()
        b2.hashCode() == b3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three boilerplates with different properties'
        def b1 = new Boilerplate(name: 'Invoice')
        b1.id = 7403L
        def b2 = new Boilerplate(name: 'Quote')
        b2.id = 7404L
        def b3 = new Boilerplate(name: 'Sales order')
        b3.id = 8473L

        expect:
        b1.hashCode() != b2.hashCode()
        b2.hashCode() != b3.hashCode()
    }

    def 'Can convert to string'(String name, String s) {
        given: 'an empty boilerplate'
        def b = new Boilerplate()

        when: 'I set the name'
        b.name = name

        then: 'I get a valid string representation'
        s == b.toString()

        where:
        name            || s
        null            || ''
        ''              || ''
        '   '           || '   '
        'a'             || 'a'
        'abc'           || 'abc'
        '  foo  '       || '  foo  '
        'Invoice'       || 'Invoice'
    }

    def 'Name must not be blank'(String s, boolean v) {
        given: 'a quite valid boilerplate'
        def b = new Boilerplate(
            content: 'for our works we state accounts for the following:'
        )

        when: 'I set the name'
        b.name = s

        then: 'the instance is valid or not'
        v == b.validate()

        where:
        s       || v
        null    || false
        ''      || false
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }

    def 'Text content must not be null'(String s, boolean v) {
        given: 'a quite valid boilerplate'
        def b = new Boilerplate(name: 'Invoice')

        when: 'I set the text content'
        b.content = s

        then: 'the instance is valid or not'
        v == b.validate()

        where:
        s       || v
        null    || false
        ''      || true
        '  \t ' || true
        'a'     || true
        'abc'   || true
        'a  x ' || true
        ' name' || true
    }
}
