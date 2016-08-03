/*
 * ConfigSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import java.text.ParseException
import spock.lang.Specification


@TestFor(Config)
@Mock(Config)
class ConfigSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Convert to Date'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        and: 'a Date object without milliseconds'
        /*
         * The milliseconds part must be set to zero because otherwise the
         * converted Date is not comparable.  The reason for this is that the
         * formatted date string doesn't contain milliseconds.
         */
        def d = new Date()
        d = new Date(((int) (d.time / 1000L)) * 1000L)

        when: 'I use a pre-converted date string'
        c.value = d.toString()

        then: 'I can convert it to a Date object'
        d == c.toType(Date)

        when: 'I set a valid date string'
        c.value = 'Thu Jan 09 10:52:34 CET 2014'

        then: 'I can convert it to a Date object'
        def cal = new GregorianCalendar(2014, Calendar.JANUARY, 9, 10, 52, 34)
        cal.time == c.toType(Date)

        when: 'I set an empty string'
        c.value = ''

        then: 'I get a null value'
        null == c.toType(Date)

        when: 'I unset the value'
        c.value = null

        then: 'I get a null value'
        null == c.toType(Date)
    }

    def 'Convert to Calendar'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        and: 'a Calendar object without milliseconds'
        /*
         * The milliseconds part must be set to zero because otherwise the
         * converted Calendar is not comparable.  The reason for this is that
         * the formatted date string doesn't contain milliseconds.
         */
        def cal = new GregorianCalendar()
        cal[Calendar.MILLISECOND] = 0

        when: 'I use a pre-converted date string'
        c.value = cal.time.toString()

        then: 'I can convert it to a Calendar object'
        cal == c.toType(Calendar)

        when: 'I set a valid date string'
        c.value = 'Thu Jan 09 10:52:34 CET 2014'

        then: 'I can convert it to a Calendar object'
        new GregorianCalendar(2014, Calendar.JANUARY, 9, 10, 52, 34) == c.toType(Calendar)

        when: 'I set an empty string'
        c.value = ''

        then: 'I get a null value'
        null == c.toType(Calendar)

        when: 'I unset the value'
        c.value = null

        then: 'I get a null value'
        null == c.toType(Calendar)
    }

    def 'Convert to Boolean'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when:
        c.value = v

        then:
        b == c.toType(Boolean)

        where:
        v       | b
        null    | null
        ''      | false
        'false' | false
        'true'  | true
        'xyz'   | false
        'FALSE' | false
        'TRUE'  | true
        'FaLsE' | false
        'TrUe'  | true
    }

    def 'Convert to an integer type'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set a valid numeric string'
        c.value = v

        then: 'I can convert it to an integer type'
        i == c.toType(Integer)
        i as Long == c.toType(Long)
        i as BigInteger == c.toType(BigInteger)

        where:
        v       | i
        null    | null
        '0'     | 0
        '0000'  | 0
        '1'     | 1
        '20474' | 20474
        '-963'  | -963
    }

    def 'Convert an invalid integer string'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set an empty string and convert it'
        c.value = ''
        c.toType(Integer)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set an blank string and convert it'
        c.value = '  '
        c.toType(Integer)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a numeric but decimal string and convert it'
        c.value = '1.5'
        c.toType(Integer)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a non-numeric string and convert it'
        c.value = 'abc'
        c.toType(Integer)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a partially numeric string and convert it'
        c.value = '1ab'
        c.toType(Integer)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)
    }

    def 'Convert to a decimal type'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set a valid numeric string'
        c.value = v

        then: 'I can convert it to an integer type'
        ((Float) d) == c.toType(Float)
        ((Double) d) == c.toType(Double)
        d == c.toType(BigDecimal)

        where:
        v       | d
        null    | null
        '0'     | 0
        '0000'  | 0
        '0.394' | 0.394
        '0.5'   | 0.5
        '1'     | 1
        '1.05'  | 1.05
        '20474' | 20474
        '-963'  | -963
        '-963.1'| -963.1
    }

    def 'Convert an invalid decimal string'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set an empty string and convert it'
        c.value = ''
        c.toType(Double)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set an blank string and convert it'
        c.value = '  '
        c.toType(Double)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a numeric string with wrong notation and convert it'
        c.value = '1,5'
        c.toType(Double)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a non-numeric string and convert it'
        c.value = 'abc'
        c.toType(Double)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)

        when: 'I set a partially numeric string and convert it'
        c.value = '1ab'
        c.toType(Double)

        then: 'I get a NumberFormatException'
        thrown(NumberFormatException)
    }

    def 'Convert to string'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set a valid string'
        c.value = v

        then: 'I can convert it to a string'
        s == c.toString()

        where:
        v           | s
        null        | null
        ''          | ''
        '  '        | '  '
        ' \t \r'    | ' \t \r'
        'foo'       | 'foo'
        'BaR'       | 'BaR'
    }

    def 'Test for equality'() {
        given: 'two equal configuration objects'
        def c1 = new Config(name: 'foo', value: 'bar')
        def c2 = new Config(name: 'foo', value: 'bar')

        when: 'I compare both these objects'
        boolean b = c1 == c2

        then: 'they are equal'
        b

        when: 'I change the value of the second object and compare'
        c2.value = 'whee'
        b = c1 == c2

        then: 'they are equal'
        b
    }

    def 'Test for inequality'() {
        given: 'two inequal configuration objects'
        def c1 = new Config(name: 'foo', value: 'bar')
        def c2 = new Config(name: 'whee', value: 'baz')

        when: 'I compare both these objects'
        boolean b = c1 == c2

        then: 'they are not equal'
        !b

        when: 'I change the value of the second object and compare'
        c2.value = 'bar'
        b = c1 == c2

        then: 'they are not equal'
        !b

        when: 'I compare to null'
        boolean b1 = c1 == null
        boolean b2 = null == c2

        then: 'they are not equal'
        !b1
        !b2

        when: 'I compare to another type'
        b = c1 == 'foo'

        then: 'they are not equal'
        !b
    }

    def 'Compute hash code'() {
        when: 'I use a configuration object'
        def c = new Config(name: n, value: v)

        then: 'I get a valid hash code'
        n.hashCode() == c.hashCode()

        where:
        n           | v
        ''          | null
        ''          | ''
        ''          | 'foo'
        ''          | 'bar'
        ''          | '5'
        ''          | '5.7'
        ''          | 'true'
        'foo'       | ''
        'foo'       | 'foo'
        'foo'       | 'bar'
        'foo'       | '5'
        'foo'       | '5.7'
        'foo'       | 'true'
    }

    def 'Compute hash code of invalid configuration object'() {
        when: 'I use a configuration object without name'
        def c = new Config(value: v)

        and: 'I fixed hash code'
        def h = ''.hashCode()

        then: 'I get a valid hash code'
        h == c.hashCode()

        where:
        v << [null, '', 'foo', 'bar', '5', '5.7', 'true']
    }

    def 'String representation'() {
        given: 'a configuration object'
        def c = new Config(name: 'foo')

        when: 'I set a valid string'
        c.value = v

        then: 'I get a valid string representation'
        s == c.toString()

        where:
        v           | s
        null        | null
        ''          | ''
        '  '        | '  '
        ' \t \r'    | ' \t \r'
        'foo'       | 'foo'
        'BaR'       | 'BaR'
    }

    def 'Name constraints'() {
        when:
        def c = new Config(name: name)
        c.validate()

        then:
        !valid == c.hasErrors()

        where:
        name        | valid
        null        | false
        ''          | false
        ' '         | false
        '      '    | false
        '  \t \n '  | false
        'foo'       | true
        'any name'  | true
    }
}
