package org.amcworld.springcrm

import grails.test.*

import org.springframework.transaction.TransactionStatus

class PersonControllerTests extends ControllerUnitTestCase {

	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Person.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def p1 = new Person(number:10000, firstName:'Daniel', lastName:'Ellermann')
		def p2 = new Person(number:10001, firstName:'Robert', lastName:'Smith')
		mockDomain(Person, [p1, p2])
		Person.metaClass.index = { -> }
		Person.metaClass.reindex = { -> }

//		def seqNumber = new SeqNumber(controllerName:'person', nextNumber:10002, prefix:'E', suffix:'')
//		mockDomain(SeqNumber, [seqNumber])

		controller.seqNumberService = new SeqNumberService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex() {
		controller.index()
		assertEquals 'list', controller.redirectArgs['action']
    }

	void testList() {
		def map = controller.list()
		assertEquals 2, map.personInstanceTotal
		assertEquals 2, map.personInstanceList.size()
		assertEquals 'Daniel', map.personInstanceList[0].firstName
		assertEquals 'Ellermann', map.personInstanceList[0].lastName
		assertEquals 'Robert', map.personInstanceList[1].firstName
		assertEquals 'Smith', map.personInstanceList[1].lastName
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.personInstance
		assertNull map.personInstance.firstName
		assertNull map.personInstance.lastName
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.organization = new Organization()
		controller.params.firstName = 'John'
		controller.params.lastName = 'Doe'
		controller.params.phone = '030 1234567'
		controller.params.email = 'jdoe@example.com'
		controller.save()
		assertEquals 3, Person.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('person')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.firstName = ''
		controller.params.lastName = ''
		controller.params.phone = '030 1234567'
		controller.params.email = 'jdoe@example.com'
		def map = controller.save()
		assertEquals 2, Person.count()
		assertEquals 'unique', map.personInstance.errors['number']
        assertEquals 'blank', map.personInstance.errors['firstName']
		assertEquals 'blank', map.personInstance.errors['lastName']
	}

	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Robert', map.personInstance.firstName
		assertEquals 'Smith', map.personInstance.lastName

		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Daniel', map.personInstance.firstName
		assertEquals 'Ellermann', map.personInstance.lastName

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.organization = new Organization()
		controller.params.firstName = 'Erika'
		controller.params.lastName = 'Mustermann'
		controller.params.phone = '030 7654321'
		controller.params.email1 = 'emustermann@example.com'
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Person.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('person')
		assertEquals 10002, seqNumber.nextNumber
		def p = Person.get(1)
		assertEquals 10000, p.number
		assertEquals 'Erika', p.firstName
		assertEquals 'Mustermann', p.lastName
		assertEquals '030 7654321', p.phone
		assertEquals 'emustermann@example.com', p.email1

		controller.params.firstName = ''
		controller.params.lastName = ''
		def map = controller.update()
		assertEquals 2, Person.count()
		assertEquals 'blank', map.personInstance.errors['firstName']
		assertEquals 'blank', map.personInstance.errors['lastName']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Person.count()
		assertNull Person.get(1)
		assertNotNull Person.get(2)

		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
