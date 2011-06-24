package org.amcworld.springcrm

import grails.test.*

class InvoicingItemTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def ii = new InvoicingItem(
            number:10000, quantity:4.5d, unit:'m',
            name:'Netzwerkkabel', description:'foo bar',
            unitPrice:0.85d, tax:0.19d, orderId:2
        )
        assertEquals 10000, ii.number
        assertEquals 4.5d, ii.quantity
        assertEquals 'm', ii.unit
        assertEquals 'Netzwerkkabel', ii.name
        assertEquals 'foo bar', ii.description
        assertEquals 0.85d, ii.unitPrice
        assertEquals 0.0d, ii.discountPercent
        assertEquals 0.0d, ii.discountAmount
        assertEquals 0.19d, ii.tax
        assertEquals 2, ii.orderId
        assertNull ii.quote
        //assertNull ii.salesOrder
        //assertNull ii.invoice
    }

    void testQuote() {
        def ii = new InvoicingItem()
        def q = new Quote(number:20000, subject:'Test')
        ii.quote = q
        assertNotNull ii.quote
        assertEquals 20000, ii.quote.number
        assertEquals 'Test', ii.quote.subject
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = ['name']
		def ii = new InvoicingItem()
		assertFalse ii.validate(validationFields)
		assertEquals 'nullable', ii.errors['name']
		
		ii = new InvoicingItem(name:'')
		assertFalse ii.validate(validationFields)
		assertEquals 'blank', ii.errors['name']
		
		ii = new InvoicingItem(number:'PRD-10000', name:'TYPO3 Installation')
		assertTrue ii.validate(validationFields)
	}
	
	void testUniqueConstraints() {
		def ii1 = new InvoicingItem(number:10000, name:'foo')
		def ii2 = new InvoicingItem(number:10010, name:'bar')
		mockDomain(InvoicingItem, [ii1, ii2])
		
		def badItem = new InvoicingItem(number:10000, name:'whee')
		assertNull badItem.save()
		assertEquals 2, InvoicingItem.count()
		assertEquals 'unique', badItem.errors['number']
		assertNull badItem.errors['name']
		
		def goodItem = new InvoicingItem(
			number:10020, name:'foo2', unit:'m', unitPrice:2.3
		)
		assertNotNull goodItem.save()
		assertEquals 3, InvoicingItem.count()
	}
	
	void testMinConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = [
            'quantity', 'unitPrice', 'discountPercent', 'discountAmount',
            'tax'
        ]
		def ii = new InvoicingItem(
            quantity:-1, unitPrice:0, discountPercent:-0.2, 
			discountAmount:-0.1, tax:-0.1
        )
		assertFalse ii.validate(validationFields)
		assertEquals 'min', ii.errors['quantity']
		assertEquals 'min', ii.errors['unitPrice']
		assertEquals 'min', ii.errors['discountPercent']
		assertEquals 'min', ii.errors['discountAmount']
		assertEquals 'min', ii.errors['tax']
		
		ii = new InvoicingItem(
            quantity:0, unitPrice:0.01, discountPercent:0, discountAmount:0,
            tax:0
        )
		assertTrue ii.validate(validationFields)
	}
}
