package org.amcworld.springcrm

import grails.test.*

class CallControllerTests extends ControllerUnitTestCase {
	
	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()
		controller.metaClass.message = { Map map -> return ERROR_MSG }

		def call1 = new Call(subject:'Call 1')
		def call2 = new Call(subject:'Call 2')
		mockDomain(Call, [call1, call2])
        Call.metaClass.index = { -> }
		Call.metaClass.reindex = { -> }
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
		assertEquals 2, map.callInstanceTotal
		assertEquals 2, map.callInstanceList.size()
		assertEquals 'Call 1', map.callInstanceList[0].subject
		assertEquals 'Call 2', map.callInstanceList[1].subject
	}
	
	void testCreate() {
		def map = controller.create()
		assertNotNull map.callInstance
		assertNull map.callInstance.subject
	}

	void testCreateWithPerson() {
		def s = '1234567890'
		controller.params.person = new Person(
			number:10000, firstName:'Daniel', lastName:'Ellermann',
			phone:s
		)
		
		def map = controller.create()
		assertNotNull map.callInstance
		assertNull map.callInstance.subject
		assertEquals s, map.callInstance.phone
	}
	
	void testCreateWithOrganization() {
		def s = '1234567890'
		controller.params.organization = new Organization(
			number:10000, name:'AMC World Technologies GmbH', phone:s
		)
		
		def map = controller.create()
		assertNotNull map.callInstance
		assertNull map.callInstance.subject
		assertEquals s, map.callInstance.phone
	}

	void testSaveSuccessfully() {
		controller.params.subject = 'Call 1'
		controller.params.notes = 'Test'
		controller.params.start = new Date()
		controller.params.status = 'planned'
		controller.params.type = 'incoming'
		controller.save()
		assertEquals 3, Call.count()
		assertEquals 'show', controller.redirectArgs['action']
	}
	
	void testSaveFailed() {
		controller.params.subject = ''
		controller.params.notes = 'Test'
		controller.params.start = new Date()
		controller.params.status = 'foo'
		controller.params.type = 'bar'
		def map = controller.save()
		assertEquals 2, Call.count()
		assertEquals 'blank', map.callInstance.errors['subject']
		assertEquals 'inList', map.callInstance.errors['status']
		assertEquals 'inList', map.callInstance.errors['type']
	}
	
	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Call 2', map.callInstance.subject
		
		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Call 1', map.callInstance.subject

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testUpdate() {
		controller.params.id = 1
		controller.params.subject = 'Call 3'
		controller.params.notes = 'Test'
		controller.params.start = new Date()
		controller.params.status = 'planned'
		controller.params.type = 'incoming'
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Call.count()
		def call = Call.get(1)
		assertEquals 'Call 3', call.subject
		assertEquals 'Test', call.notes
		assertEquals 'planned', call.status
		
		controller.params.subject = ''
		def map = controller.update()
		assertEquals 2, Call.count()
		assertEquals 'blank', map.callInstance.errors['subject']
		
		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Call.count()
		assertNull Call.get(1)
		assertNotNull Call.get(2)
		
		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
