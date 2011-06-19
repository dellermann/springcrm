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
        InvoicingItem ii = new InvoicingItem(
            number:"SRV-10000", quantity:4.5d, unit:"m",
            name:"Netzwerkkabel", description:"foo bar",
            unitPrice:0.85d, tax:0.19d, orderId:2
        )
        assertEquals "SRV-10000", ii.number
        assertEquals 4.5d, ii.quantity
        assertEquals "m", ii.unit
        assertEquals "Netzwerkkabel", ii.name
        assertEquals "foo bar", ii.description
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
        InvoicingItem ii = new InvoicingItem()
        Quote q = new Quote(number:20000, subject:"Test")
        ii.quote = q
        assertNotNull ii.quote
        assertEquals 20000, ii.quote.number
        assertEquals "Test", ii.quote.subject
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = ["number", "name"]
		InvoicingItem ii = new InvoicingItem()
		assertFalse ii.validate(validationFields)
		assertEquals "nullable", ii.errors["number"]
		assertEquals "nullable", ii.errors["name"]
		
		ii = new InvoicingItem(number:"", name:"")
		assertFalse ii.validate(validationFields)
		assertEquals "blank", ii.errors["number"]
		assertEquals "blank", ii.errors["name"]
		
		ii = new InvoicingItem(number:"PRD-10000", name:"TYPO3 Installation")
		assertTrue ii.validate(validationFields)
	}
	
	void testMinConstraints() {
		mockForConstraintsTests InvoicingItem
		def validationFields = [
            "quantity", "unitPrice", "discountPercent", "discountAmount",
            "tax"
        ]
		InvoicingItem ii = new InvoicingItem(
            quantity:-1, unitPrice:0, discountPercent:-0.2, discountAmount:-0.1,
            tax:-0.1
        )
		assertFalse ii.validate(validationFields)
		assertEquals "min", ii.errors["quantity"]
		assertEquals "min", ii.errors["unitPrice"]
		assertEquals "min", ii.errors["discountPercent"]
		assertEquals "min", ii.errors["discountAmount"]
		assertEquals "min", ii.errors["tax"]
		
		ii = new InvoicingItem(
            quantity:0, unitPrice:0.01, discountPercent:0, discountAmount:0,
            tax:0
        )
		assertTrue ii.validate(validationFields)
	}
}
