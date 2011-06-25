package org.amcworld.springcrm

import grails.test.*

class QuoteControllerTests extends ControllerUnitTestCase {
	
	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()
		controller.metaClass.message = { Map map -> return ERROR_MSG }
		Quote.metaClass.static.executeQuery = { String sql -> return [10002] }

		def q1 = new Quote(number:10000, subject:'Quote 1')
		def q2 = new Quote(number:10001, subject:'Quote 2')
		mockDomain(Quote, [q1, q2])
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
		assertEquals 2, map.quoteInstanceTotal
		assertEquals 2, map.quoteInstanceList.size()
		assertEquals 'Quote 1', map.quoteInstanceList[0].subject
		assertEquals 'Quote 2', map.quoteInstanceList[1].subject
	}
	
	void testCreate() {
		def map = controller.create()
		assertNotNull map.quoteInstance
		assertEquals 10002, map.quoteInstance.number
		assertNull map.quoteInstance.subject
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.subject = 'Quote 3'
		controller.params.organization = new Organization()
		controller.params.quoteDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new QuoteStage()
		controller.save()
		assertEquals 3, Quote.count()
		assertEquals 'show', controller.redirectArgs['action']
	}
	
	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.subject = ''
		controller.params.organization = new Organization()
		controller.params.quoteDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new QuoteStage()
		def map = controller.save()
		assertEquals 2, Quote.count()
		assertEquals 'unique', map.quoteInstance.errors['number']
		assertEquals 'blank', map.quoteInstance.errors['subject']
	}
	
	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Quote 2', map.quoteInstance.subject
		
		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Quote 1', map.quoteInstance.subject

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.subject = 'Quote 3'
		controller.params.organization = new Organization()
		controller.params.quoteDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new QuoteStage()
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Quote.count()
		def org = Quote.get(1)
		assertEquals 10000, org.number
		assertEquals 'Quote 3', org.subject
		assertEquals 'Test', org.headerText
		
		controller.params.subject = ''
		def map = controller.update()
		assertEquals 2, Quote.count()
		assertEquals 'blank', map.quoteInstance.errors['subject']
		
		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Quote.count()
		assertNull Quote.get(1)
		assertNotNull Quote.get(2)
		
		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
