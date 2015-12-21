package org.amcworld.springcrm

import grails.test.*

import org.springframework.transaction.TransactionStatus

class QuoteControllerTests extends ControllerUnitTestCase {

	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Quote.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def q1 = new Quote(number:10000, subject:'Quote 1')
		def q2 = new Quote(number:10001, subject:'Quote 2')
		mockDomain(Quote, [q1, q2])

//		def seqNumber = new SeqNumber(controllerName:'quote', nextNumber:10002, prefix:'Q', suffix:'')
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
		assertEquals 2, map.quoteInstanceTotal
		assertEquals 2, map.quoteInstanceList.size()
		assertEquals 'Quote 1', map.quoteInstanceList[0].subject
		assertEquals 'Quote 2', map.quoteInstanceList[1].subject
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.quoteInstance
		assertNull map.quoteInstance.subject
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.subject = 'Quote 3'
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new QuoteStage()
		controller.save()
		assertEquals 3, Quote.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('quote')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.subject = ''
		controller.params.organization = new Organization()
		controller.params.docDate = new Date()
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
		controller.params.docDate = new Date()
		controller.params.headerText = 'Test'
		controller.params.stage = new QuoteStage()
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Quote.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('quote')
		assertEquals 10002, seqNumber.nextNumber
		def quote = Quote.get(1)
		assertEquals 10000, quote.number
		assertEquals 'Quote 3', quote.subject
		assertEquals 'Test', quote.headerText

		controller.params.subject = ''
		def map = controller.update()
		assertEquals 2, Quote.count()
		assertEquals 'blank', map.quoteInstance.errors['subject']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

//	void testUpdateItems() {
//		controller.params.id = 1
//		controller.params.number = 10000
//		controller.params.subject = 'Quote 3'
//		controller.params.organization = new Organization()
//		controller.params.docDate = new Date()
//		controller.params.headerText = 'Test'
//		controller.params.stage = new QuoteStage()
//		controller.params.'items.size' = 1
//		controller.params['items[0].number'] = 'S-10002'
//		controller.params['items[0].amount'] = 3
//		controller.params['items[0].unit'] = 'Pieces'
//		controller.params['items[0].name'] = 'Test entry'
//		controller.params['items[0].unitPrice'] = 60
//		controller.params['items[0].tax'] = 19
//		controller.update()
//		assertEquals 'show', controller.redirectArgs['action']
//		assertEquals 2, Quote.count()
//		def quote = Quote.get(1)
//		assertNotNull quote.items
//		assertEquals 1, quote.items.length
//		assertEquals 'S-10002', quote.items[0].number
//		assertEquals 3, quote.items[0].amount
//		assertEquals 'Test entry', quote.items[0].name
//		assertEquals 60, quote.items[0].unitPrice
//		assertEquals 180, quote.items[0].total
//	}

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
