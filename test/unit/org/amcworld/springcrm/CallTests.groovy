package org.amcworld.springcrm

import grails.test.*

class CallTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
		Date d = new Date()
		def call = new Call(subject:'foo', notes:'bar', start:d)
		assertEquals 'foo', call.subject
		assertEquals 'bar', call.notes
		assertNull call.organization
		assertNull call.person
		assertNull call.phone
		assertEquals d, call.start
		assertNull call.type
		assertNull call.status
    }
	
	void testOrganization() {
		def call = new Call(subject:'foo')
		def org = new Organization(number:10000, name:'AMC World')
		call.organization = org
		assertNotNull call.organization
		assertEquals 10000, call.organization.number
		assertEquals 'AMC World', call.organization.name
	}
	
	void testPerson() {
		def call = new Call(subject:'foo')
		def p = new Person(
			number:10000, firstName:'Daniel', lastName:'Ellermann'
		)
		call.person = p
		assertNotNull call.person
		assertEquals 10000, call.person.number
		assertEquals 'Daniel', call.person.firstName
		assertEquals 'Ellermann', call.person.lastName
	}
	
	void testBlankConstraints() {
		mockForConstraintsTests Call
		def validationFields = ['subject']
		def call = new Call()
		assertFalse call.validate(validationFields)
		assertEquals 'nullable', call.errors['subject']
		
		call = new Call(subject:'')
		assertFalse call.validate(validationFields)
		assertEquals 'blank', call.errors['subject']
		
		call = new Call(subject:'AMC World')
		assertTrue call.validate(validationFields)
	}
	
	void testSizeConstraints() {
		mockForConstraintsTests Call
		def validationFields = ['phone']
		String s = '1234567890' * 5
		def call = new Call(phone:s)
		assertFalse call.validate(validationFields)
		assertEquals 'maxSize', call.errors['phone']
		
		s = '1234567890' * 4
		call = new Call(phone:s)
		assertTrue call.validate(validationFields)
		
		s = '+49 30 8321475-0'
		call = new Call(phone:s)
		assertTrue call.validate(validationFields)
	}
	
	void testInListConstraints() {
		mockForConstraintsTests Call
		def call = new Call(type:'xxx')
		assertFalse call.validate(['type'])
		assertEquals 'inList', call.errors['type']

		call = new Call(type:'incoming')
		assertTrue call.validate(['type'])

		call = new Call(type:'outgoing')
		assertTrue call.validate(['type'])

		call = new Call(status:'xxx')
		assertFalse call.validate(['status'])
		assertEquals 'inList', call.errors['status']

		call = new Call(status:'planned')
		assertTrue call.validate(['status'])

		call = new Call(status:'completed')
		assertTrue call.validate(['status'])
	}
}
