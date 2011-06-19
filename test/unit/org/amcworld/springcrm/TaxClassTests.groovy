package org.amcworld.springcrm

import grails.test.*

class TaxClassTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        TaxClass tc = new TaxClass(name:"19%", orderId:80, taxValue:19)
        assertEquals "19%", tc.name
        assertEquals 80, tc.orderId
		assertEquals 19, tc.taxValue
    }
	
	void testMinConstraints() {
		mockForConstraintsTests TaxClass
		def validationFields = ["taxValue"]
		TaxClass tc = new TaxClass(taxValue:-0.5)
		assertFalse tc.validate(validationFields)
		assertEquals "min", tc.errors["taxValue"]
		
		tc = new TaxClass(taxValue:0)
		assertTrue tc.validate(validationFields)
	}
}
