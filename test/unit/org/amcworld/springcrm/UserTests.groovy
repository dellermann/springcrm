package org.amcworld.springcrm

import grails.test.*

class UserTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def user = new User(
			userName:'danny', password:'test', firstName:'Daniel',
			lastName:'Ellermann', email:'d.ellermann@amc-world.de'
		)
		assertEquals 'danny', user.userName
		assertEquals 'test', user.password
		assertEquals 'Daniel', user.firstName
		assertEquals 'Ellermann', user.lastName
		assertNull user.phone
		assertNull user.phoneHome
		assertNull user.mobile
		assertNull user.fax
		assertEquals 'd.ellermann@amc-world.de', user.email
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests User
		def validationFields = 
			['userName', 'password', 'firstName', 'lastName', 'email']
		def u = new User()
		assertFalse u.validate(validationFields)
		assertEquals 'nullable', u.errors['userName']
		assertEquals 'nullable', u.errors['password']
		assertEquals 'nullable', u.errors['firstName']
        assertEquals 'nullable', u.errors['lastName']
		assertEquals 'nullable', u.errors['email']
		
		u = new User(userName:'', password:'', firstName:'', lastName:'')
		assertFalse u.validate(validationFields)
		assertEquals 'blank', u.errors['userName']
		assertEquals 'blank', u.errors['password']
		assertEquals 'blank', u.errors['firstName']
		assertEquals 'blank', u.errors['lastName']
		assertEquals 'nullable', u.errors['email']
		
		u = new User(
			userName: 'danny', password:'test', firstName:'Daniel', 
			lastName:'Ellermann', email:'d.ellermann@amc-world.de'
		)
		assertTrue u.validate(validationFields)
	}
	
	void testUniqueConstraints() {
		def jdoe = new User(
			userName:'jdoe', password:'test', firstName:'Peter',
			lastName:'Smith', email:'jdoe@example.com'
		)
		def admin = new User(
			userName:'admin', password:'test', firstName:'Peter',
			lastName:'Smith', email:'admin@example.com'
		)
		mockDomain(User, [jdoe, admin])
		
		def badUser = new User(
			userName:'jdoe', password:'test', firstName:'Peter',
			lastName:'Smith', email:'jdoe@example.com'
		)
		assertNull badUser.save()
		assertEquals 2, User.count()
		assertEquals 'unique', badUser.errors['userName']
		
		def goodUser = new User(
			userName:'good', password:'test', firstName:'Peter',
			lastName:'Smith', email:'good@example.com'
		)
		assertNotNull goodUser.save()
		assertEquals 3, User.count()
		assertNotNull User.findByUserNameAndPassword('good', 'test')
	}
	
	void testSizeConstraints() {
		mockForConstraintsTests User
		def validationFields = [ 'phone', 'phoneHome', 'mobile', 'fax' ]
		String s = '1234567890' * 5
		def u = new User(phone:s, phoneHome:s, mobile:s, fax:s)
		assertFalse u.validate(validationFields)
		assertEquals 'maxSize', u.errors['phone']
		assertEquals 'maxSize', u.errors['phoneHome']
        assertEquals 'maxSize', u.errors['mobile']
		assertEquals 'maxSize', u.errors['fax']
		
		s = '1234567890' * 4
		u = new User(phone:s, phoneHome:s, mobile:s, fax:s)
		assertTrue u.validate(validationFields)
		
		s = '+49 30 8321475-0'
		u = new User(phone:s, phoneHome:s, mobile:s, fax:s)
		assertTrue u.validate(validationFields)
	}
	
	void testEmailConstraints() {
		mockForConstraintsTests User
		def validationFields = ['email']
		User u = new User(email:null)
		assertFalse u.validate(validationFields)
		assertEquals 'nullable', u.errors['email']

		u = new User(email:'foobar')
		assertFalse u.validate(validationFields)
		assertEquals 'email', u.errors['email']
		
		u = new User(email:'foobar@')
		assertFalse u.validate(validationFields)
		assertEquals 'email', u.errors['email']
		
		u = new User(email:'@mydomain.com')
		assertFalse u.validate(validationFields)
		assertEquals 'email', u.errors['email']
		
		u = new User(email:'user@mydomain')
		assertFalse u.validate(validationFields)
		assertEquals 'email', u.errors['email']
		
		u = new User(email:'user@.com')
		assertFalse u.validate(validationFields)
		assertEquals 'email', u.errors['email']
		
		u = new User(email:'')
		assertFalse u.validate(validationFields)
		assertEquals 'blank', u.errors['email']
		
		u = new User(email:'user@mydomain.com')
		assertTrue u.validate(validationFields)
	}
	
	void testFullName() {
		User u = new User(
			userName:'danny', password:'test', firstName:'Daniel',
			lastName:'Ellermann', email:'d.ellermann@amc-world.de'
		)
		assertEquals 'Daniel Ellermann', u.fullName

		u = new User()
		assertEquals ' ', u.fullName
	}
	
	void testToString() {
        User u = new User(
			userName:'danny', password:'test', firstName:'Daniel',
			lastName:'Ellermann', email:'d.ellermann@amc-world.de'
		)
		assertEquals 'danny', u.toString()

		u = new User()
		assertEquals '', u.toString()
	}
}
