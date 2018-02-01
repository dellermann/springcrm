/*
 * WorkSpec.groovy
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


class WorkSpec extends Specification implements DomainUnitTest<Work> {

	//-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty work'
        def work = new Work()

        then: 'the properties are initialized properly'
        'S' == work.type
        null == work.category
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty work'
        def w1 = new Work()

        when: 'I copy the work using the constructor'
        def w2 = new Work(w1)

        then: 'the properties are set properly'
        w1.type == w2.type
        null == w2.category
    }

    def 'Copy a work using constructor'() {
        given: 'a work with various properties'
        def w1 = new Work(category: new WorkCategory())

        when: 'I copy the work using the constructor'
        def w2 = new Work(w1)

        then: 'some properties are the equal'
        w1.type == w2.type

        and: 'some instances are the same'
        w1.category.is w2.category
    }
}
