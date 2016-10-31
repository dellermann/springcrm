/*
 * SearchDataSpec.groovy
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

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SearchData)
class SearchDataSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Creating an empty instance initializes the properties'() {
        when: 'I create an empty instance'
        def sd = new SearchData()

        then: 'the properties are initialized properly'
        null == sd.content
        null == sd.contentStructured
        0i == sd.orderId
        0L == sd.recordId
        null == sd.recordTitle
        null == sd.type
    }

    def 'Get structured content'() {
        given: 'an instance'
        SearchData sd = new SearchData()

        when: 'no JSON content is set'
        sd.contentStructured = null

        then: 'the structured content is unset'
        null == sd.structuredContent

        when: 'JSON content is set'
        sd.contentStructured = '{"foo":"bar","xyz":"whee"}'

        then: 'the structured content is unset'
        null != sd.structuredContent
        2 == sd.structuredContent.size()
        'bar' == sd.structuredContent.foo
        'whee' == sd.structuredContent.xyz
    }

    def 'Set structured content'() {
        given: 'an instance'
        SearchData sd = new SearchData()

        when: 'I set a null value'
        sd.structuredContent = null

        then: 'content and JSON content is unset'
        null == sd.content
        null == sd.contentStructured

        when: 'I set some values'
        sd.structuredContent = [foo: 'bar', xyz: 'whee']

        then: 'content and JSON content is set correctly'
        'bar\nwhee' == sd.content
        '{"foo":"bar","xyz":"whee"}' == sd.contentStructured
    }

    def 'Equals is null-safe'() {
        given: 'a note'
        def sd = new SearchData()

        expect:
        null != sd
        sd != null
        !sd.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a note'
        def sd = new SearchData()

        expect:
        sd != 'foo'
        sd != 45
        sd != 45.3
        sd != new Date()
    }

    def 'Instances are equal if they have the same type and record ID'() {
        given: 'three instances with same type and record ID'
        def sd1 = new SearchData(type: 'call', recordId: 56L, content: 'Network configuration')
        def sd2 = new SearchData(type: 'call', recordId: 56L, content: 'Customer assessment')
        def sd3 = new SearchData(type: 'call', recordId: 56L, content: 'Staff')

        expect: 'equals() is reflexive'
        sd1 == sd1
        sd2 == sd2
        sd3 == sd3

        and: 'all instances are equal and equals() is symmetric'
        sd1 == sd2
        sd2 == sd1
        sd2 == sd3
        sd3 == sd2

        and: 'equals() is transitive'
        sd1 == sd3
        sd3 == sd1
    }

    def 'Instances are unequal if they have the different type and record ID'()
    {
        given: 'three instances with different types and record IDs'
        def sd1 = new SearchData(type: 'call', recordId: 67L, content: 'Network configuration')
        def sd2 = new SearchData(type: 'call', recordId: 146L, content: 'Network configuration')
        def sd3 = new SearchData(type: 'note', recordId: 146L, content: 'Network configuration')

        expect: 'equals() is reflexive'
        sd1 == sd1
        sd2 == sd2
        sd3 == sd3

        and: 'all instances are unequal and equals() is symmetric'
        sd1 != sd2
        sd2 != sd1
        sd2 != sd3
        sd3 != sd2

        and: 'equals() is transitive'
        sd1 != sd3
        sd3 != sd1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def sd = new SearchData()

        expect:
        'null:0'.hashCode() == sd.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def sd = new SearchData(type: 'call', recordId: 42L, content: 'Network configuration')

        when: 'I compute the hash code'
        int h = sd.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            sd = new SearchData(type: 'call', recordId: 42L, content: 'Network configuration')
            h == sd.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with same type and record ID'
        def sd1 = new SearchData(type: 'call', recordId: 56L, content: 'Network configuration')
        def sd2 = new SearchData(type: 'call', recordId: 56L, content: 'Customer assessment')
        def sd3 = new SearchData(type: 'call', recordId: 56L, content: 'Staff')

        expect:
        sd1.hashCode() == sd2.hashCode()
        sd2.hashCode() == sd3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with different types and record IDs'
        def sd1 = new SearchData(type: 'call', recordId: 67L, content: 'Network configuration')
        def sd2 = new SearchData(type: 'call', recordId: 146L, content: 'Network configuration')
        def sd3 = new SearchData(type: 'note', recordId: 146L, content: 'Network configuration')

        expect:
        sd1.hashCode() != sd2.hashCode()
        sd2.hashCode() != sd3.hashCode()
    }

    def 'Can convert to string'(String type, long recordId) {
        given: 'an empty instance'
        def sd = new SearchData()

        when: 'I set the type and record ID'
        sd.type = type
        sd.recordId = recordId

        then: 'I get a valid string representation'
        "${type}:${recordId}".toString() == sd.toString()

        where:
        type            | recordId  || _
        null            | 0L        || _
        null            | 567L      || _
        'call'          | 0L        || _
        'call'          | 573L      || _
    }

    def 'Type must not be blank and max 100 char long'(String t, boolean v) {
        given: 'a quite valid instance'
        def sd = new SearchData(
            content: 'foo', contentStructured: 'foo', recordTitle: 'foo'
        )

        when: 'I set the type'
        sd.type = t

        then: 'the instance is valid or not'
        v == sd.validate()

        where:
        t       	|| v
        null    	|| false
        ''      	|| false
        '  \t ' 	|| false
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 100	|| true
        'a' * 101	|| false
    }

    def 'Content must not be blank and has limited size'(String c, boolean v) {
        given: 'a quite valid instance'
        def sd = new SearchData(
            type: 'call', contentStructured: 'foo', recordTitle: 'foo'
        )

        when: 'I set the content'
        sd.content = c

        then: 'the instance is valid or not'
        v == sd.validate()

        where:
        c       	                    || v
        null    	                    || false
        ''      	                    || false
        '  \t ' 	                    || false
        'a'     	                    || true
        'S'     	                    || true
        'abc'   	                    || true
        'a  x ' 	                    || true
        ' name' 	                    || true
        // XXX the following lines cause an OutOfMemoryError
//        'a' * Integer.MAX_VALUE	        || true
//        'a' * (Integer.MAX_VALUE + 1)	|| false
    }

    def 'JSON content must not be blank and has limited size'(String c,
                                                              boolean v)
    {
        given: 'a quite valid instance'
        def sd = new SearchData(
            type: 'call', content: 'foo', recordTitle: 'foo'
        )

        when: 'I set the JSON content'
        sd.contentStructured = c

        then: 'the instance is valid or not'
        v == sd.validate()

        where:
        c       	                    || v
        null    	                    || false
        ''      	                    || false
        '  \t ' 	                    || false
        'a'     	                    || true
        'S'     	                    || true
        'abc'   	                    || true
        'a  x ' 	                    || true
        ' name' 	                    || true
        // XXX the following lines cause an OutOfMemoryError
//        'a' * Integer.MAX_VALUE	        || true
//        'a' * (Integer.MAX_VALUE + 1)	|| false
    }

    def 'Record title must not be null'(String rt, boolean v) {
        given: 'a quite valid instance'
        def sd = new SearchData(
            type: 'call', content: 'foo', contentStructured: 'foo'
        )

        when: 'I set the record title'
        sd.recordTitle = rt

        then: 'the instance is valid or not'
        v == sd.validate()

        where:
        rt       	|| v
        null    	|| false
        ''      	|| true
        '  \t ' 	|| true
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 100	|| true
        'a' * 101	|| true
    }
}
