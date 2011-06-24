package org.amcworld.springcrm

import grails.test.*

class PersonTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Organization org = new Organization(number:20000, name:'AMC World')
        Person p = new Person(
            number:10000, organization:org,
            salutation:new Salutation(name:'Mr.'),
            firstName:'Daniel', lastName:'Ellermann',
            phone:'030 8321475-0'
        )
        assertEquals 10000, p.number
        assertNotNull p.organization
        assertEquals 20000, p.organization.number
        assertEquals 'AMC World', p.organization.name
        assertNotNull p.salutation
        assertEquals 'Mr.', p.salutation.name
        assertEquals 'Daniel', p.firstName
        assertEquals 'Ellermann', p.lastName
        assertNull p.mailingAddrStreet
        assertNull p.mailingAddrPoBox
        assertNull p.mailingAddrLocation
        assertNull p.mailingAddrPostalCode
        assertNull p.mailingAddrState
        assertNull p.mailingAddrCountry
        assertNull p.otherAddrStreet
        assertNull p.otherAddrPoBox
        assertNull p.otherAddrLocation
        assertNull p.otherAddrPostalCode
        assertNull p.otherAddrState
        assertNull p.otherAddrCountry
        assertEquals '030 8321475-0', p.phone
        assertNull p.phoneHome
        assertNull p.notes
        assertNull p.dateCreated
		assertNull p.lastUpdated
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests Person
		def validationFields = ['firstName', 'lastName']
		Person p = new Person()
		assertFalse p.validate(validationFields)
		assertEquals 'nullable', p.errors['firstName']
		assertEquals 'nullable', p.errors['lastName']
		
		p = new Person(firstName:'', lastName:'')
		assertFalse p.validate(validationFields)
		assertEquals 'blank', p.errors['firstName']
		assertEquals 'blank', p.errors['lastName']
		
		p = new Person(firstName:'Daniel', lastName:'Ellermann')
		assertTrue p.validate(validationFields)
	}
	
	void testUniqueConstraints() {
		def p1 = new Person(
			number:10000, firstName:'Daniel', lastName:'Ellermann'
		)
		def p2 = new Person(
			number:10001, firstName:'Robert', lastName:'Kirchner'
		)
		mockDomain(Person, [p1, p2])
		
		def badPerson = new Person(
			number:10000, firstName:'George W.', lastName:'Bush'
		)
		assertNull badPerson.save()
		assertEquals 2, Person.count()
		assertEquals 'unique', badPerson.errors['number']
		assertNull badPerson.errors['firstName']
		assertNull badPerson.errors['lastName']
		
		def org = new Organization(number:10020, name:'Organization 4')
		def goodPerson = new Person(
			number:10020, firstName:'Jesus', lastName:'Christus',
			organization:org
		)
		assertNotNull goodPerson.save()
		assertEquals 3, Person.count()
	}

	void testSizeConstraints() {
		mockForConstraintsTests Person
		def validationFields = [
			'phone', 'phoneHome', 'mobile', 'fax', 'phoneAssistant', 
			'phoneOther'
		]
		String s = '1234567890' * 5
		Person p = new Person(
			phone:s, phoneHome:s, mobile:s, fax:s, phoneAssistant:s,
			phoneOther:s
		)
		assertFalse p.validate(validationFields)
		assertEquals 'maxSize', p.errors['phone']
		assertEquals 'maxSize', p.errors['phoneHome']
        assertEquals 'maxSize', p.errors['mobile']
		assertEquals 'maxSize', p.errors['fax']
        assertEquals 'maxSize', p.errors['phoneAssistant']
		assertEquals 'maxSize', p.errors['phoneOther']
		
		s = '1234567890' * 4
		p = new Person(
			phone:s, phoneHome:s, mobile:s, fax:s, phoneAssistant:s,
			phoneOther:s
		)
		assertTrue p.validate(validationFields)
		
		s = '+49 30 8321475-0'
		p = new Person(
			phone:s, phoneHome:s, mobile:s, fax:s, phoneAssistant:s,
			phoneOther:s
		)
		assertTrue p.validate(validationFields)
	}
	
	void testEmailConstraints() {
		mockForConstraintsTests Person
		def validationFields = ['email1', 'email2']
		String s = 'foobar'
		def p = new Person(email1:s, email2:s)
		assertFalse p.validate(validationFields)
		assertEquals 'email', p.errors['email1']
		assertEquals 'email', p.errors['email2']
		
		s = 'foobar@'
		p = new Person(email1:s, email2:s)
		assertFalse p.validate(validationFields)
		assertEquals 'email', p.errors['email1']
		assertEquals 'email', p.errors['email2']
		
		s = '@mydomain.com'
		p = new Person(email1:s, email2:s)
		assertFalse p.validate(validationFields)
		assertEquals 'email', p.errors['email1']
		assertEquals 'email', p.errors['email2']
		
		s = 'user@mydomain'
		p = new Person(email1:s, email2:s)
		assertFalse p.validate(validationFields)
		assertEquals 'email', p.errors['email1']
		assertEquals 'email', p.errors['email2']
		
		s = 'user@.com'
		p = new Person(email1:s, email2:s)
		assertFalse p.validate(validationFields)
		assertEquals 'email', p.errors['email1']
		assertEquals 'email', p.errors['email2']
		
		s = ''
		p = new Person(email1:s, email2:s)
		assertTrue p.validate(validationFields)

		s = 'user@mydomain.com'
		p = new Person(email1:s, email2:s)
		assertTrue p.validate(validationFields)
	}
	
	void testMailingAddr() {
		Person p = new Person(
			mailingAddrStreet:'Fischerinsel 1', mailingAddrPoBox: '12345',
			mailingAddrPostalCode:'10179', mailingAddrLocation:'Berlin',
			mailingAddrState:'BLN', mailingAddrCountry:'Deutschland'
		)
		assertEquals 'Fischerinsel 1, 10179 Berlin', p.mailingAddr
		p = new Person(
			mailingAddrStreet:'Fischerinsel 1', mailingAddrPostalCode:'10179', 
			mailingAddrLocation:'Berlin'
		)
		assertEquals 'Fischerinsel 1, 10179 Berlin', p.mailingAddr
		p = new Person(
			mailingAddrStreet:'Fischerinsel 1', mailingAddrPostalCode:'10179'
		)
		assertEquals 'Fischerinsel 1', p.mailingAddr
		p = new Person(
			mailingAddrStreet:'Fischerinsel 1', mailingAddrLocation:'Berlin'
		)
		assertEquals 'Fischerinsel 1, Berlin', p.mailingAddr
		p = new Person(
			mailingAddrStreet:'Fischerinsel 1'
		)
		assertEquals 'Fischerinsel 1', p.mailingAddr
		p = new Person(
			mailingAddrPostalCode:'10179', mailingAddrLocation:'Berlin'
		)
		assertEquals '10179 Berlin', p.mailingAddr
		p = new Person(mailingAddrPostalCode:'10179')
		assertEquals '', p.mailingAddr
		p = new Person(mailingAddrLocation:'Berlin')
		assertEquals 'Berlin', p.mailingAddr
		p = new Person(mailingAddrStreet:'Fischerinsel 1')
		assertEquals 'Fischerinsel 1', p.mailingAddr
		p = new Person()
		assertEquals '', p.mailingAddr
	}
	
	void testOtherAddr() {
		Person p = new Person(
			otherAddrStreet:'Fischerinsel 1', otherAddrPoBox: '12345',
			otherAddrPostalCode:'10179', otherAddrLocation:'Berlin',
			otherAddrState:'BLN', otherAddrCountry:'Deutschland'
		)
		assertEquals 'Fischerinsel 1, 10179 Berlin', p.otherAddr
		p = new Person(
			otherAddrStreet:'Fischerinsel 1', otherAddrPostalCode:'10179', 
			otherAddrLocation:'Berlin'
		)
		assertEquals 'Fischerinsel 1, 10179 Berlin', p.otherAddr
		p = new Person(
			otherAddrStreet:'Fischerinsel 1', otherAddrPostalCode:'10179'
		)
		assertEquals 'Fischerinsel 1', p.otherAddr
		p = new Person(
			otherAddrStreet:'Fischerinsel 1', otherAddrLocation:'Berlin'
		)
		assertEquals 'Fischerinsel 1, Berlin', p.otherAddr
		p = new Person(
			otherAddrStreet:'Fischerinsel 1'
		)
		assertEquals 'Fischerinsel 1', p.otherAddr
		p = new Person(
			otherAddrPostalCode:'10179', otherAddrLocation:'Berlin'
		)
		assertEquals '10179 Berlin', p.otherAddr
		p = new Person(otherAddrPostalCode:'10179')
		assertEquals '', p.otherAddr
		p = new Person(otherAddrLocation:'Berlin')
		assertEquals 'Berlin', p.otherAddr
		p = new Person(otherAddrStreet:'Fischerinsel 1')
		assertEquals 'Fischerinsel 1', p.otherAddr
		p = new Person()
		assertEquals '', p.otherAddr
	}
	
	void testToString() {
        Organization org = new Organization(number:20000, name:'AMC World')
        Person p = new Person(
            number:10000, organization:org,
            salutation:new Salutation(name:'Mr.'),
            firstName:'Daniel', lastName:'Ellermann',
            phone:'030 8321475-0'
        )
		assertEquals 'Ellermann, Daniel', p.toString()
		
		p = new Person(lastName:'Ellermann')
		assertEquals 'Ellermann', p.toString()
		
		p = new Person(firstName:'Daniel')
		assertEquals 'Daniel', p.toString()

		p = new Person()
		assertEquals '', p.toString()
	}
}
