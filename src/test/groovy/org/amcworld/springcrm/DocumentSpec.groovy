/*
 * DocumentSpec.groovy
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


class DocumentSpec extends Specification implements DomainUnitTest<Document> {

    //-- Feature methods ------------------------

    def 'Creating a document initializes the properties'() {
        given: 'a file'
        def f = new File('test.dat')

        when: 'I create a document'
        def d = new Document('test', f)

        then: 'the properties are initialized correctly'
        'test' == d.id
        'test.dat' == d.name
        0L == d.size
        null != d.lastModified
    }

    def 'Last modification date does not change class interna'() {
        given: 'a document'
        def d = new Document('test', new File('test.dat'))

        when: 'I obtain twice the last modification date'
        Date d1 = d.lastModified
        Date d2 = d.lastModified

        then: 'both the dates are own instances'
        !d1.is(d2)
    }

    def 'Format file size'(long l, String e) {
        given: 'a mocked file'
        File f = Mock()
        f.getName() >> 'test.dat'
        f.length() >> l
        f.lastModified() >> new Date().time

        and: 'locale Germany'
        Locale.default = Locale.GERMANY

        when: 'I create a document'
        def d = new Document('test', f)

        then: 'I get the correct formatted file size'
        e == d.sizeAsString

        where:
        l               || e
        0               || '0 B'
        1               || '1 B'
        1_000           || '1.000 B'
        1_023           || '1.023 B'
        1_024           || '1 KB'
        1_025           || '1 KB'
        1_125           || '1,1 KB'
        10_240          || '10 KB'
        75_241          || '73,5 KB'
        984_404         || '961,3 KB'
        1_048_576       || '1 MB'
        1_073_741_824   || '1 GB'
        4_471_082_391   || '4,2 GB'
    }
}
