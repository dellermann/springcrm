package org.amcworld.springcrm

import grails.test.*

import org.springframework.transaction.TransactionStatus

class ServiceControllerTests extends ControllerUnitTestCase {
	
	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Service.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def s1 = new Service(number:10000, name:'Service 1')
		def s2 = new Service(number:10001, name:'Service 2')
		mockDomain(Service, [s1, s2])
		Service.metaClass.index = { -> }
		Service.metaClass.reindex = { -> }
		
		def seqNumber = new SeqNumber(controllerName:'service', nextNumber:10002, prefix:'S', suffix:'')
		mockDomain(SeqNumber, [seqNumber])
		
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
		assertEquals 2, map.serviceInstanceTotal
		assertEquals 2, map.serviceInstanceList.size()
		assertEquals 'Service 1', map.serviceInstanceList[0].name
		assertEquals 'Service 2', map.serviceInstanceList[1].name
	}
	
	void testCreate() {
		def map = controller.create()
		assertNotNull map.serviceInstance
		assertNull map.serviceInstance.name
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.name = 'Service 3'
		controller.params.quantity = 35d
		controller.params.unitPrice = 4.7d
		controller.save()
		assertEquals 3, Service.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('service')
		assertEquals 10003, seqNumber.nextNumber
	}
	
	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.name = ''
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		def map = controller.save()
		assertEquals 2, Service.count()
		assertEquals 'unique', map.serviceInstance.errors['number']
		assertEquals 'blank', map.serviceInstance.errors['name']
	}
	
	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Service 2', map.serviceInstance.name
		
		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Service 1', map.serviceInstance.name

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.name = 'Service 3'
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Service.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('service')
		assertEquals 10002, seqNumber.nextNumber
		def p = Service.get(1)
		assertEquals 10000, p.number
		assertEquals 'Service 3', p.name
		assertEquals 35, p.quantity
		assertEquals 4.7d, p.unitPrice
		
		controller.params.name = ''
		def map = controller.update()
		assertEquals 2, Service.count()
		assertEquals 'blank', map.serviceInstance.errors['name']
		
		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Service.count()
		assertNull Service.get(1)
		assertNotNull Service.get(2)
		
		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
