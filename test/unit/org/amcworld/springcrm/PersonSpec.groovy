/*
 * PersonSpec.groovy
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


@TestFor(Person)
@Mock([Person])
class PersonSpec extends Specification {

    //-- Feature methods ------------------------
	
	def 'Copy using constructor'() {
		given: 
		def p1 = new Person(
			number: 10003,
			organization: new Organization (recType: 1, name : 'YourOrganizationLtd'),
			salutation: new Salutation (name: 'Herr', orderId: 10),
			title: 'test',
			firstName: 'foo',
			lastName: 'bar',
			mailingAddr: new Address(),
			otherAddr: new Address(),
			phone: '3030303',
			phoneHome: '030330303',
			mobile: '1944392',
			fax: '8008088',
			phoneAssistant: '2002002',
			phoneOther: '10012100',
			email1: 'info@yourorganization.example',
			email2: 'office@yourorganization.example',
			jobTitle: 'test title',
			department: 'test department',
			assistant: 'test assistant',
			birthday: new Date(),
			picture: 'foobar'.getBytes(),
			notes: 'some notes'
		)
		
		when:
		def p2 = new Person(p1)
		
		then: 
		p2.organization == p1.organization
		p2.salutation == p1.salutation
		p2.title == p1.title
		p2.firstName == p1.firstName
		p2.lastName == p1.lastName
		p2.mailingAddr == p1.mailingAddr
		p2.otherAddr == p1.otherAddr
		p2.phone == p1.phone
		p2.phoneHome == p1.phoneHome
		p2.mobile == p1.mobile
		p2.fax == p1.fax
		p2.phoneAssistant == p1.phoneAssistant
		p2.phoneOther == p1.phoneOther
		p2.email1 == p1.email1
		p2.email2 == p1.email2
		p2.jobTitle == p1.jobTitle
		p2.department == p1.department
		p2.assistant == p1.assistant
		p2.birthday == p1.birthday
		p2.picture == p1.picture
		p2.notes == p1.notes	
		
		and: 'some properties are unset'
		0 == p2.number
		null == p2.dateCreated
		null == p2.lastUpdated
	}
	
	def 'Get full name'() {
		given: 
		def p = new Person()
		
		when:
		p.firstName = firstName
		p.lastName = lastName
		def fullName = p.getFullName()
		
		then:
		result == fullName
		
		where:
		firstName		| lastName		| result
		null			| null			| ''
		null			| 'Smith'		| 'Smith'
		'John'			| null			| 'John'
		'John'			| 'Smith'		| 'John Smith'
		' '				| null			| ''
		null			| ' '			| ''
		'abc'*1000		| ' '			| 'abc'*1000
		'abc'*1000		| 'abc'*1000	| 'abc'*1000+' '+'abc'*1000
		',.-'			| ',.-'			| ',.- ,.-'
		'10001'			| '202022'		| '10001 202022'
		'user@mydomain' | ''			| 'user@mydomain'
		'string123'		| '321string'	| 'string123 321string'
	}
	
	def 'Get the full number'() {
		given: 'a person with mocked sequence number service'
		def p = new Person()
		p.seqNumberService = Mock(SeqNumberService)
		p.seqNumberService.format(_, _) >> 'O-11332'
		
		expect:
		'O-11332' == p.fullNumber
	}
	
	def 'Simulate the save method in insert mode and check number'() {
		given: 'a person without number'
		def p = new Person()
		p.seqNumberService = Mock(SeqNumberService)
		p.seqNumberService.nextNumber(_) >> 92283

		when: 'I simulate calling save() in insert mode'
		p.beforeInsert()

		then: 'the sequence number must be set'
		92283 == p.number
	}
	
	def 'Check for equality'() {
		given: 'two objects with different properties'
		def p1 = new Person (firstName: 'Ben', lastName: 'Rider')
		def p2 = new Person (firstName: 'Tom', lastName: 'Riggins')
		
		and: 'the same IDs'
		p1.id = 10010
		p2.id = 10010
		
		expect: 'both persons to be equal'
		p1 == p2
		p2 == p1 
	}
	
	def 'Check for inequality'() {
		given: 'two objects with same properties'
		def p1 = new Person (firstName: 'Ben', lastName: 'Rider')
		def p2 = new Person (firstName: 'Ben', lastName: 'Rider')
		
		and: 'different IDs'
		p1.id = 944
		p2.id = 1003
		
		when: 'I compare both these persons'
		boolean b1 = (p1 != p2)
		boolean b2 = (p2 != p1)
		
		then: 'they should not be equal'
		b1
		b2
		
		when:  'I compare with null'
		p2 = null
		
		then: 'they are not equal'
		p1 != p2
		p2 != p1
		
		when: 'I compare to another type'
		int i = 3
		
		then: 'they are not equal'
		p1 != i
	}
	
	def 'Compute hash code'() {
		when: 'I create a person with no ID'
		def p = new Person()

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
		given: 'an empty person'
		def p = new Person()
		
		when: 'I set the first name'
		p.firstName = 'Johan'
		
		and: 'do not set the last name'
		p.lastName = ''
		
		then: 
		'Johan' == p.toString()
		
		when: 'I set the last name'
		p.lastName = 'Doug'
		
		and: 'do not set the first name'
		p.firstName = ''
		
		then: 
		'Doug' == p.toString()
		
		when: 'I set the first name and the last name'
		p.firstName = 'Tim'
		p.lastName = 'Statter'
		
		then: 
		'Statter, Tim' == p.toString()
	}
	
	def 'title constraints'() {
		setup: 
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a title and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo', 
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  title: title, firstName: 'John', lastName: 'Doe', 
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		title			| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'Title'			| true
		'Title 1003'	| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'firstName constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a first name and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo', 
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: firstName, lastName: 'Doe', 
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then: 
		!valid == p.hasErrors()
		
		where:
		firstName		| valid
		null			| false
		''				| false
		' '				| false
		'    '			| false
		' \t \n'		| false
		1003			| true
		'John'			| true
		'Derk 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'lastName constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a last name and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: lastName,
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		lastName		| valid
		null			| false
		''				| false
		' '				| false
		'    '			| false
		' \t \n'		| false
		1003			| true
		'Doe'			| true
		'Derk 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'Phone constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a phone number and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe', phone: phone,
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then: 
		!valid == p.hasErrors()
		
		where:
		phone		| valid
		null		| true
		''			| true
		' '			| true
		'abc'		| true
		'abc'*999	| false
		0			| true
		0133022		| true
		'1'*40		| true
		'1'*41		| false
		'1'*999		| false
	}
	
	def 'Fax constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a fax number and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe', fax: fax,
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		fax 		| valid
		null		| true
		''			| true
		' '			| true
		'abc'		| true
		'abc'*999	| false
		0			| true
		0133022		| true
		'1'*40		| true
		'1'*41		| false
		'1'*999		| false
	}
	
	def 'phoneAssistant constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a phone assistant number and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe', 
						  phoneAssistant: phoneAssistant, 
						  mailingAddr: new Address(), otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		phoneAssistant	| valid
		null			| true
		''				| true
		' '				| true
		'abc'			| true
		'abc'*999		| false
		0				| true
		0133022			| true
		'1'*40			| true
		'1'*41			| false
		'1'*999			| false
	}
	
	def 'phoneOther constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with an other phone number and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  phoneOther: phoneOther, mailingAddr: new Address(), 
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		phoneOther		| valid
		null			| true
		''				| true
		' '				| true
		'abc'			| true
		'abc'*999		| false
		0				| true
		0133022			| true
		'1'*40			| true
		'1'*41			| false
		'1'*999			| false
	}
	
	def 'Email1 constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with an email1 and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  email1: email, mailingAddr: new Address(),
						  otherAddr: new Address())
		p.validate()
		
		then: 
		!valid == p.hasErrors()
		
		where: 
		email               | valid
        null                | true
        ''                  | true
        ' '                 | true
        'foo'               | false
        'any name'          | false
        'foobar@'           | false
        '@mydomain.com'     | false
        'user@mydomain'     | false
        'user@.com'         | false
        'user@mydomain.com' | true
        'user@härbört.com'  | false     // XXX currently no IDN support
	}
	
	def 'Email2 constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with an email2 and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  email2: email, mailingAddr: new Address(),
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		email               | valid
		null                | true
		''                  | true
		' '                 | true
		'foo'               | false
		'any name'          | false
		'foobar@'           | false
		'@mydomain.com'     | false
		'user@mydomain'     | false
		'user@.com'         | false
		'user@mydomain.com' | true
		'user@härbört.com'  | false     // XXX currently no IDN support
	}
	
	def 'Job title constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a job title and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  jobTitle: jobTitle, mailingAddr: new Address(), 
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		jobTitle		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'Title'			| true
		'Title 1003'	| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'Department constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with a department and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  department: department, mailingAddr: new Address(), 
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		department		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'test'			| true
		'test 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'Assistant constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with an assistant and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  assistant: assistant, mailingAddr: new Address(),
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		assistant		| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'test'			| true
		'test 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
	
	def 'Notes constraints'() {
		setup:
		mockForConstraintsTests(Person)
		
		when: 'I create a person with an assistant and validate it'
		def p = new Person(
			organization: new Organization(recType: 1, name: 'foo',
										   billingAddr: new Address(),
										   shippingAddr: new Address()),
						  firstName: 'John', lastName: 'Doe',
						  notes: notes, mailingAddr: new Address(),
						  otherAddr: new Address())
		p.validate()
		
		then:
		!valid == p.hasErrors()
		
		where:
		notes			| valid
		null			| true
		''				| true
		' '				| true
		1003			| true
		'test'			| true
		'test 1003'		| true
		'abc'*100		| true
		'abc'*1000		| true
	}
}
