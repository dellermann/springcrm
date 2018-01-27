/*
 * NumberedDomainSpec.groovy
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

import spock.lang.Specification


class NumberedDomainSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Compute full numbers without sequence number'(int n, String e) {
        given: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(null)

        where:
        n           || e
        0i          || '0'
        1i          || '1'
        4503i       || '4503'
    }

    def 'Compute full numbers with sequence number'(int n, String e) {
        given: 'a sequence number'
        def seqNumber = new SeqNumber(prefix: 'X', suffix: 'Y')

        and: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(seqNumber)
        e == note.computeFullNumber(seqNumber, withPrefix: true, withSuffix: true)
        e == note.computeFullNumber(seqNumber, withPrefix: null, withSuffix: null)

        where:
        n           || e
        0i          || 'X-0-Y'
        1i          || 'X-1-Y'
        4503i       || 'X-4503-Y'
    }

    def 'Compute full numbers with sequence number without prefix'(int n,
                                                                   String e)
    {
        given: 'a sequence number'
        def seqNumber = new SeqNumber(prefix: 'X', suffix: 'Y')

        and: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(seqNumber, withPrefix: false)
        e == note.computeFullNumber(seqNumber, withPrefix: false, withSuffix: true)
        e == note.computeFullNumber(seqNumber, withPrefix: false, withSuffix: null)

        where:
        n           || e
        0i          || '0-Y'
        1i          || '1-Y'
        4503i       || '4503-Y'
    }

    def 'Compute full numbers with sequence number without suffix'(int n,
                                                                   String e)
    {
        given: 'a sequence number'
        def seqNumber = new SeqNumber(prefix: 'X', suffix: 'Y')

        and: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(seqNumber, withSuffix: false)
        e == note.computeFullNumber(seqNumber, withPrefix: true, withSuffix: false)
        e == note.computeFullNumber(seqNumber, withPrefix: null, withSuffix: false)

        where:
        n           || e
        0i          || 'X-0'
        1i          || 'X-1'
        4503i       || 'X-4503'
    }

    def 'Compute full numbers with empty prefix and suffix'(int n, String e) {
        given: 'a sequence number'
        def seqNumber = new SeqNumber(prefix: '', suffix: '')

        and: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(seqNumber)
        e == note.computeFullNumber(seqNumber, withPrefix: true, withSuffix: true)
        e == note.computeFullNumber(seqNumber, withPrefix: null, withSuffix: null)

        where:
        n           || e
        0i          || '0'
        1i          || '1'
        4503i       || '4503'
    }

    def 'Compute full numbers with null prefix and suffix'(int n, String e) {
        given: 'a sequence number'
        def seqNumber = new SeqNumber(prefix: null, suffix: null)

        and: 'a note which is a NumberedDomain'
        def note = new Note(number: n)

        expect:
        e == note.computeFullNumber(seqNumber)
        e == note.computeFullNumber(seqNumber, withPrefix: true, withSuffix: true)
        e == note.computeFullNumber(seqNumber, withPrefix: null, withSuffix: null)

        where:
        n           || e
        0i          || '0'
        1i          || '1'
        4503i       || '4503'
    }
}
