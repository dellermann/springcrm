/*
 * NoteSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Note)
@Mock([Note])

class NoteSpec extends Specification {

	//-- Feature Methods --------------------
	
	def 'Copy using constructor'() {
		given: 'a note with various properties'
		def n1 = new Note(
			number: 100302,
			title: 'note title',
			content: 'note content',
			dateCreated: new Date(),
			lastUpdated: new Date()
		)
		
		when: 'I copy the object'
		def n2 = new Note(n1)
		
		then: 'the properties should be the same'
		n2.title == n1.title
		n2.content == n1.content
		
		and: 'some properties are unset'
		n2.number == 0
		n2.dateCreated == null
		n2.lastUpdated == null
	}
	
	def 'Get the full number'() {
		given: 'a note with mocked sequence number service'
		def n = new Note()
		n.seqNumberService = Mock(SeqNumberService)
		n.seqNumberService.format(_, _) >> 'O-11332'
		
		expect:
		'O-11332' == n.fullNumber
	}
	
	def 'Check for equality'() {
		given: 'two objects with different properties'
		def n1 = new Note(title: 'Invoice', content: 'Open')
		def n2 = new Note(title: 'Open', content: 'Invoice')
		
		and: 'the same IDs'
		n1.id = 1001
		n2.id = 1001
		
		expect: 'the objects to be equal'
		n1 == n2
		n2 == n1
	}
	
	def 'Check for inequality'() {
		given: 'two objects with the same properties'
		def n1 = new Note(title: 'title', content: 'content')
		def n2 = new Note(title: 'title', content: 'content')
		
		and: 'different IDs'
		n1.id = 1
		n2.id = 2
		
		when: 'I compare these objects'
		def b1 = (n1 != n2)
		def b2 = (n2 != n1)
		
		then: 'they should not be equal'
		b1
		b2
		
		when: 'I compare with null'
		n1 == null
		
		then: 'they should not be equal'
		n2 != n1
		n1 != n2
		
		when: 'I compare with another type'
		n1 == 'c'
		
		then: 'they should not be equal'
		n1 != n2
		n2 != n1
	}
	
	def 'Compute hash code'() {
		when: 'I create a note with no ID'
		def n = new Note()
		
		then: 'I get a valid hash code'
		0 == n.hashCode()
		
		when: 'I create a note with an ID'
		n.id = id
		
		then: 'I get a hash code using this ID'
		e == n.hashCode()
		
		where:
				id 	|   	   e
				 0 	|   	   0
				 1 	|   	   1
				10 	|  		  10
			   123 	|  		 123
			  5324 	|  		5324
			 12344  | 	   12344
		1023991929	| 1023991929
		
	}
	
	def 'Simulate the save method in insert mode and check number'() {
		given: 'a note without number'
		def n = new Note()
		n.seqNumberService = Mock(SeqNumberService)
		n.seqNumberService.nextNumber(_) >> 92283

		when: 'I simulate calling save() in insert mode'
		n.beforeInsert()

		then: 'the sequence number must be set'
		92283 == n.number
	}
	
	def 'title constraints'() {
		setup:
		mockForConstraintsTests(Note)
		
		when: 'I create a note with a title and validate it'
		def n = new Note(
			title: title,
			content: 'Test content'
		)
		n.validate()
		
		then: 
		!valid == n.hasErrors()
		
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
		'a'*1000		| false
	}
	
	def 'content constraints'() {
		setup:
		mockForConstraintsTests(Note)
		
		when: 'I create a note with content and validate it'
		def n = new Note(title: 'title', content: content)
		n.validate()
		
		then:
		!valid == n.hasErrors()
		
		where:
		content			| valid
		null			| false
		''				| false
		' '				| false
		1003			| true
		'Content'		| true
		'Content 1003'	| true
		'a'*100			| true
		'a'*200			| true
		'a'*1000		| true
	}
}
