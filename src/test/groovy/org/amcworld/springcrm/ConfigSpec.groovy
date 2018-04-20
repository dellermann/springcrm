/*
 * ConfigSpec.groovy
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
import org.bson.types.ObjectId
import spock.lang.Specification


class ConfigSpec extends Specification implements DomainUnitTest<Config> {

    //-- Feature methods ------------------------

    void 'Equals is null-safe'() {
        given: 'an instance'
        def config = new Config()

        expect:
        null != config
        config != null
        !config.equals(null)
    }

    void 'Instances of other types are always unequal'() {
        given: 'an instance'
        def config = new Config()

        expect:
        config != 'foo'
        config != 45
        config != 45.3
        config != new Date()
    }

    void 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def c1 = new Config(value: 'Value 1')
        def c2 = new Config(value: 'Value 2')
        def c3 = new Config(value: 'Value 3')

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

    void 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def c1 = new Config(value: 'Value 1')
        c1.id = id
        def c2 = new Config(value: 'Value 2')
        c2.id = id
        def c3 = new Config(value: 'Value 3')
        c3.id = id

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

    void 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with different IDs'
        def c1 = new Config(value: 'Value 1')
        c1.id = new ObjectId()
        def c2 = new Config(value: 'Value 1')
        c2.id = new ObjectId()
        def c3 = new Config(value: 'Value 1')
        c3.id = new ObjectId()

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

    void 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def config = new Config()

        expect:
        3937i == config.hashCode()
    }

    void 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def config = new Config(value: 'foo')

        expect:
        3937i == config.hashCode()
    }

    void 'Hash codes are consistent'() {
        given: 'an instance'
        def id = new ObjectId()
        def config = new Config(value: 'foo')
        config.id = id

        when: 'I compute the hash code'
        int h = config.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            config = new Config(value: 'foo')
            config.id = id
            h == config.hashCode()
        }
    }

    void 'Equal instances produce the same hash code'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def c1 = new Config(value: 'Value 1')
        c1.id = id
        def c2 = new Config(value: 'Value 2')
        c2.id = id
        def c3 = new Config(value: 'Value 3')
        c3.id = id

        expect:
        c1.hashCode() == c2.hashCode()
        c2.hashCode() == c3.hashCode()
    }

    void 'Different instances produce different hash codes'() {
        given: 'three instances with different properties'
        def c1 = new Config(value: 'Value 1')
        c1.id = new ObjectId()
        def c2 = new Config(value: 'Value 1')
        c2.id = new ObjectId()
        def c3 = new Config(value: 'Value 1')
        c3.id = new ObjectId()

        expect:
        c1.hashCode() != c2.hashCode()
        c2.hashCode() != c3.hashCode()
    }

    void 'Can convert to string'(String value, String e) {
        given: 'an instance'
        def config = new Config(value: value)

        expect:
        e == config.toString()

        where:
        value       || e
        null        || null
        ''          || ''
        '  '        || ''
        ' \t \r'    || ''
        'foo'       || 'foo'
        'BaR'       || 'BaR'
    }
}
