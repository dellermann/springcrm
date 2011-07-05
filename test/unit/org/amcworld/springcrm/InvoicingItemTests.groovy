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
            number:'SRV-10000', quantity:4.5, unit:'m', name:'Netzwerkkabel',
			description:'foo bar', unitPrice:0.85, tax:0.19
        )
        assertEquals 'SRV-10000', ii.number
        assertEquals 4.5, ii.quantity
        assertEquals 'm', ii.unit
        assertEquals 'Netzwerkkabel', ii.name
        assertEquals 'foo bar', ii.description
        assertEquals 0.85, ii.unitPrice
        assertEquals 0.19, ii.tax
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = ['number', 'name']
		def ii = new InvoicingItem()
		assertFalse ii.validate(validationFields)
		assertEquals 'nullable', ii.errors['number']
		assertEquals 'nullable', ii.errors['name']
		
		ii = new InvoicingItem(number:'', name:'')
		assertFalse ii.validate(validationFields)
		assertEquals 'blank', ii.errors['number']
		assertEquals 'blank', ii.errors['name']
		
		ii = new InvoicingItem(number:'PRD-10000', name:'TYPO3 Installation')
		assertTrue ii.validate(validationFields)
	}
	
	void testMinConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = ['quantity', 'unitPrice', 'tax']
		def ii = new InvoicingItem(quantity:-1, unitPrice:0, tax:-0.1)
		assertFalse ii.validate(validationFields)
		assertEquals 'min', ii.errors['quantity']
		assertEquals 'min', ii.errors['unitPrice']
		assertEquals 'min', ii.errors['tax']
		
		ii = new InvoicingItem(quantity:0, unitPrice:0.01, tax:0)
		assertTrue ii.validate(validationFields)
	}
}
