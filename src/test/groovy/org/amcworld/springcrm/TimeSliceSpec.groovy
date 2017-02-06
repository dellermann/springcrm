/*
 * TimeSliceSpec.groovy
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
import java.time.LocalDateTime
import spock.lang.Specification


@TestFor(TimeSlice)
@Mock([Staff, TimeSlice])
class TimeSliceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Preset values'() {
        when: 'I create an empty time slice'
        def t = new TimeSlice()

        then: 'some values are preset'
        null != t.type
    }

    def 'Start must not be null'() {
        given: 'a time slice'
        def t = new TimeSlice(staff: new Staff())

        when: 'I set the start'
        t.start = LocalDateTime.now()

        then: 'the instance is valid'
        t.validate()

        when: 'I unset the start'
        t.start = null

        then: 'the instance is not valid'
        !t.validate()
    }

    def 'Type must not be null'() {
        given: 'a time slice'
        def t = new TimeSlice(
            start: LocalDateTime.now(),
            staff: new Staff()
        )

        when: 'I set the type'
        t.type = TimeSliceType.PAUSE

        then: 'the instance is valid'
        t.validate()

        when: 'I unset the type'
        t.type = null

        then: 'the instance is not valid'
        !t.validate()
    }

    def 'Staff must not be null'() {
        given: 'a time slice'
        def t = new TimeSlice(start: LocalDateTime.now())

        when: 'I set the staff'
        t.staff = new Staff()

        then: 'the instance is valid'
        t.validate()

        when: 'I unset the staff'
        t.staff = null

        then: 'the instance is not valid'
        !t.validate()
    }
}
