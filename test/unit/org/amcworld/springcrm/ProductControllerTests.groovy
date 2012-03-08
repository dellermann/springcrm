package org.amcworld.springcrm

import grails.test.*

import org.springframework.transaction.TransactionStatus

class ProductControllerTests extends ControllerUnitTestCase {

	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Product.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def p1 = new Product(number:10000, name:'Product 1')
		def p2 = new Product(number:10001, name:'Product 2')
		mockDomain(Product, [p1, p2])
		Product.metaClass.index = { -> }
		Product.metaClass.reindex = { -> }

//		def seqNumber = new SeqNumber(controllerName:'product', nextNumber:10002, prefix:'P', suffix:'')
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
		assertEquals 2, map.productInstanceTotal
		assertEquals 2, map.productInstanceList.size()
		assertEquals 'Product 1', map.productInstanceList[0].name
		assertEquals 'Product 2', map.productInstanceList[1].name
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.productInstance
		assertNull map.productInstance.name
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.name = 'Product 3'
		controller.params.quantity = 35d
		controller.params.unitPrice = 4.7d
		controller.params.weight = 3.4
		controller.save()
		assertEquals 3, Product.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('product')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.name = ''
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		controller.params.weight = 3.4
		def map = controller.save()
		assertEquals 2, Product.count()
		assertEquals 'unique', map.productInstance.errors['number']
		assertEquals 'blank', map.productInstance.errors['name']
	}

	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Product 2', map.productInstance.name

		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Product 1', map.productInstance.name

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.name = 'Product 3'
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		controller.params.weight = 3.4
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Product.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('product')
		assertEquals 10002, seqNumber.nextNumber
		def p = Product.get(1)
		assertEquals 10000, p.number
		assertEquals 'Product 3', p.name
		assertEquals 35, p.quantity
		assertEquals 4.7d, p.unitPrice
		assertEquals 3.4, p.weight

		controller.params.name = ''
		def map = controller.update()
		assertEquals 2, Product.count()
		assertEquals 'blank', map.productInstance.errors['name']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Product.count()
		assertNull Product.get(1)
		assertNotNull Product.get(2)

		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
