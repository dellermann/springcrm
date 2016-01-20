/*
 * ServiceSpec.groovy
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

import grails.test.mixin.TestMixin
import spock.lang.Specification


@TestMixin([Service])
class ServiceSpec extends Specification {

	//-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty service'
        def s = new Service()

        then: 'the properties are initialized properly'
        'S' == s.type
        null == s.category
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty service'
        def s1 = new Service()

        when: 'I copy the service using the constructor'
        def s2 = new Service(s1)

        then: 'the properties are set properly'
        s1.type == s2.type
        null == s2.category
    }

    def 'Copy a service using constructor'() {
        given: 'a service with various properties'
        def s1 = new Service(
            category: new ServiceCategory()
        )

        when: 'I copy the service using the constructor'
        def s2 = new Service(s1)

        then: 'some properties are the equal'
        s1.type == s2.type

        and: 'some instances are the same'
        s1.category.is s2.category
    }
}
