/*
 * ServiceSpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
import grails.test.mixin.Mock
import spock.lang.Specification


//@TestFor(Service)
//@Mock([Service])
class ServiceSpec extends Specification {

	//-- Feature Methods --------------------

	// TODO
	//Cannot add Service class [class org.amcworld.springcrm.Service].
	//It is not a Service!

	def 'Copy using constructor'() {
		given:
		def s1 = new Service(category: new ServiceCategory())

		when:
		def s2 = new Service(s1)

		then:
		s2.category == s1.category
	}

	def 'Category constraint'() {
		setup:
		mockForConstraintsTests(Service)

		when:
		def s = new Service(category: null)

		then:
		!s.hasErrors()
	}
}

