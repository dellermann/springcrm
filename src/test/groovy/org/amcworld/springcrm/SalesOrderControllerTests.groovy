package org.amcworld.springcrm

import grails.test.mixin.TestFor
import org.springframework.transaction.TransactionStatus


@TestFor(SalesOrderController)
class SalesOrderControllerTests {

	private static final String ERROR_MSG = 'error message'

    void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		SalesOrder.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def q1 = new SalesOrder(number:10000, subject:'Sales order 1')
		def q2 = new SalesOrder(number:10001, subject:'Sales order 2')
		mockDomain(SalesOrder, [q1, q2])

//		def seqNumber = new SeqNumber(controllerName:'salesOrder', nextNumber:10002, prefix:'S', suffix:'')
//		mockDomain(SeqNumber, [seqNumber])

		controller.seqNumberService = new SeqNumberService()
    }

    void testIndex() {
		controller.index()
		assertEquals 'list', controller.redirectArgs['action']
    }

	void testList() {
		def map = controller.list()
		assertEquals 2, map.salesOrderInstanceTotal
		assertEquals 2, map.salesOrderInstanceList.size()
		assertEquals 'Sales order 1', map.salesOrderInstanceList[0].subject
		assertEquals 'Sales order 2', map.salesOrderInstanceList[1].subject
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.salesOrderInstance
		assertNull map.salesOrderInstance.subject
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.subject = 'Sales order 3'
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new SalesOrderStage()
		controller.save()
		assertEquals 3, SalesOrder.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('salesOrder')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.subject = ''
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new SalesOrderStage()
		def map = controller.save()
		assertEquals 2, SalesOrder.count()
		assertEquals 'unique', map.salesOrderInstance.errors['number']
		assertEquals 'blank', map.salesOrderInstance.errors['subject']
	}

	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Sales order 2', map.salesOrderInstance.subject

		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Sales order 1', map.salesOrderInstance.subject

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.subject = 'Sales order 3'
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new SalesOrderStage()
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, SalesOrder.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('salesOrder')
		assertEquals 10002, seqNumber.nextNumber
		def so = SalesOrder.get(1)
		assertEquals 10000, so.number
		assertEquals 'Sales order 3', so.subject
		assertEquals 'Test', so.headerText

		controller.params.subject = ''
		def map = controller.update()
		assertEquals 2, SalesOrder.count()
		assertEquals 'blank', map.salesOrderInstance.errors['subject']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, SalesOrder.count()
		assertNull SalesOrder.get(1)
		assertNotNull SalesOrder.get(2)

		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
