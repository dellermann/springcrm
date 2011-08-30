package org.amcworld.springcrm

import grails.test.*
import org.springframework.transaction.TransactionStatus

class InvoiceControllerTests extends ControllerUnitTestCase {
	
	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Invoice.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def q1 = new Invoice(number:10000, subject:'Invoice 1')
		def q2 = new Invoice(number:10001, subject:'Invoice 2')
		mockDomain(Invoice, [q1, q2])
		Invoice.metaClass.index = { -> }
		Invoice.metaClass.reindex = { -> }
		
		def seqNumber = new SeqNumber(controllerName:'invoice', nextNumber:10002, prefix:'I', suffix:'')
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
		assertEquals 2, map.invoiceInstanceTotal
		assertEquals 2, map.invoiceInstanceList.size()
		assertEquals 'Invoice 1', map.invoiceInstanceList[0].subject
		assertEquals 'Invoice 2', map.invoiceInstanceList[1].subject
	}
	
	void testCreate() {
		def map = controller.create()
		assertNotNull map.invoiceInstance
		assertNull map.invoiceInstance.subject
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.subject = 'Invoice 3'
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.dueDatePayment = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new InvoiceStage()
		controller.save()
		assertEquals 3, Invoice.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('invoice')
		assertEquals 10003, seqNumber.nextNumber
	}
	
	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.subject = ''
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new InvoiceStage()
		def map = controller.save()
		assertEquals 2, Invoice.count()
		assertEquals 'unique', map.invoiceInstance.errors['number']
		assertEquals 'blank', map.invoiceInstance.errors['subject']
	}
	
	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Invoice 2', map.invoiceInstance.subject
		
		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Invoice 1', map.invoiceInstance.subject

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.subject = 'Invoice 3'
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.dueDatePayment = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new InvoiceStage()
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Invoice.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('invoice')
		assertEquals 10002, seqNumber.nextNumber
		def invoice = Invoice.get(1)
		assertEquals 10000, invoice.number
		assertEquals 'Invoice 3', invoice.subject
		assertEquals 'Test', invoice.headerText
		
		controller.params.subject = ''
		def map = controller.update()
		assertEquals 2, Invoice.count()
		assertEquals 'blank', map.invoiceInstance.errors['subject']
		
		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
	
	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Invoice.count()
		assertNull Invoice.get(1)
		assertNotNull Invoice.get(2)
		
		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
