/*
 * ProjectSpec.groovy
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
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification


@TestFor(Project)
@Mock([Project])
class ProjectSpec extends Specification {

	//-- Feature Methods -----------------

	def 'Copy using constructor'() {
		given: 'a project with various properties'
		def p1 = new Project(
			number: 1993,
			title: 'title',
			description: 'description',
			organization: new Organization(),
			person: new Person(),
			phase: ProjectPhase.planning,
			//status: ProjectStatus
		)

		when:
		def p2 = new Project(p1)

		then: 'some properties should be copied'
		p2.title == p1.title
		p2.description == p1.description
		p2.organization == p1.organization
		p2.person == p1.person

		and: 'some properties shoud be unset'
		0 == p2.number
		null == p2.dateCreated
		null == p2.lastUpdated
	}

	def 'Get the full number'() {
		given: 'a project with mocked sequence number service'
		def p = new Project()
		p.seqNumberService = Mock(SeqNumberService)
		p.seqNumberService.format(_, _) >> '0-11332'

		expect:
		'0-11332' == p.fullNumber
	}

	def 'Simulate the save method in insert mode and check number'() {
		given: 'a project without number'
		def p = new Project()
		p.seqNumberService = Mock(SeqNumberService)
		p.seqNumberService.nextNumber(_) >> 92283

		when: 'I simulate calling save() in insert mode'
		p.beforeInsert()

		then: 'the sequence number must be set'
		92283 == p.number
	}

	def 'Check for equality'() {
		given: 'two objects with different properties'
		def p1 = new Project(title: 'Website', description: 'programming')
		def p2 = new Project(title: 'Book', description: 'writing')

		when: 'the IDs are the same'
		p1.id = 1001
		p2.id = 1001

		then: 'the objects are equal'
		p1 == p2
		p2 == p1
	}

	def 'Check for inequelity'() {
		given: 'two objects with same properties'
		def p1 = new Project(title: 'title', description: 'content')
		def p2 = new Project(title: 'title', description: 'content')

		and: 'different IDs'
		p1.id = 1001
		p2.id = 1002

		when: 'I compare both these objects'
		def b1 = (p1 != p2)
		def b2 = (p2 != p1)

		then: 'they are not equal'
		b1
		b2

		when: 'I compare with null'
		p1 = null

		then: 'they are not equal'
		p2 != p1
		p1 != p2

		when: 'I compare with another type'
		int i = 3

		then: 'they are not equal'
		p2 != i
	}

	def 'Compute hash code'() {
		when: 'I create a person with no ID'
		def p = new Project()

		then: 'I get a valid hash code'
		0 == p.hashCode()

		when: 'I create a person with an ID'
		p.id = id

		then: 'I get a hash code using this ID'
		e == p.hashCode()

		where:
				id 	|   	   e
				 0 	|   	   0
				 1 	|   	   1
				10 	|  		  10
			   123 	|  		 123
			  5324 	|  		5324
			 12344 	| 	   12344
		1023991929	| 1023991929
	}

	def 'Convert to string'() {
		given: 'an empty project'
		def p = new Project()

		expect: 'the string to be empty'
		p.toString() == ''

		when: 'I set a title'
		p.title = 'title'

		then:
		p.toString() == 'title'
	}

	def 'Title constraints'() {
		when: 'I create a project with a title and validate it'
		def p = new Project(
			title: title,
			organization: new Organization(
				recType: 1, name: 'foo', billingAddr: new Address(),
				shippingAddr: new Address()
			),
			status: new ProjectStatus(name: 'in progress')
		)
		p.validate()

		then:
		!valid == p.hasErrors()

		where:
		title			| valid
		null			| false
		''				| false
		' '				| false
		1003			| true
		'Title'			| true
		'Title 1003'	| true
		'a'*100			| true
		'a'*200			| true
		'a'*1000		| true
	}

	def 'Description contraints'() {
		when: 'I create a project with a description and validate it'
		def p = new Project(
			title: 'title',
			description: description,
			organization: new Organization(
				recType: 1, name: 'foo', billingAddr: new Address(),
				shippingAddr: new Address()
			),
			status: new ProjectStatus(name: 'in progress')
		)
		p.validate()

		then:
		!valid == p.hasErrors()

		where:
		description		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'Title'			| true
		'Title 1003'	| true
		'a'*100			| true
		'a'*200			| true
		'a'*1000		| true
	}
}
