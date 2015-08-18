/*
 * ProjectDocumentSpec.groovy
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

@TestFor(ProjectDocument)
@Mock([ProjectDocument])

class ProjectDocumentSpec extends Specification {
	
	//-- Feature Methods ---------------------------
	
	def 'Check for equality'() {
		given: 'two objects with same properties but different title'
		def p1 = new ProjectDocument(
			phase: 'test', path: 'path', title: 'invoice'
		)
		def p2 = new ProjectDocument(
			phase: 'test', path: 'path', title: 'website'
		)
		
		and: 'the same IDs'
		p1.id = 1002
		p2.id = 1002
		
		expect: 'both objects to be equal'
		p1 == p2
		p2 == p1
	}
	
	def 'Check for inequality'() {
		given: 'two objects with the same properties'
		def p1 = new ProjectDocument(
			phase: 'test', path: 'path', title: 'website'
		)
		def p2 = new ProjectDocument(
			phase: 'test', path: 'path2', title: 'invoice'
		)
		
		when: 'I compare them'
		boolean b1 = (p1 != p2)
		boolean b2 = (p2 != p1)
		
		then: 'they are not equal'
		b1
		b2
	}
	
	def 'Compute hash code'() {
		when:
		def p = new ProjectDocument(phase: ProjectPhase.planning, path: 'path')
		def project = new Project(id: 1003)
		
		then: 
		project.hashCode() == project.id+'-'+p.phase+'-'+p.path.hashCode()
		//TODO
	}
	
	def 'Convert to string'() {
		when: 'I create an object with different properties'
		def p = new ProjectDocument(
			phase: 'test', path: 'path', title: 'invoice'
		)

		then: 'the string should be the title'
		p.title == p.toString()
	}
	
	def 'path constraints'() {
		setup:
		mockForConstraintsTests(ProjectDocument)
		
		when: 'I create a project document with a path and validate it'
		def p = new ProjectDocument(
			phase: ProjectPhase.acceptance, path: path, title: 'title'
		)
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		path			| valid
		null			| false
		''				| false
		' '				| false
		1003			| true
		'Path'			| true
		'Path 1003' 	| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'title constraints'() {
		setup:
		mockForConstraintsTests(ProjectDocument)
		
		when: 'I create a project document with a title and validate it'
		def p = new ProjectDocument(
			phase: ProjectPhase.planning, path: 'path', title: title
		)
		p.validate()
		println p.errors.dump()
		
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
		'abc'*100		| true
		'abc'*1000		| true
	}
}