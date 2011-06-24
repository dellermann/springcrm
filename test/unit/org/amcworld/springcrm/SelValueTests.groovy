package org.amcworld.springcrm

import grails.test.*

class SelValueTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        SelValue sv = new SelValue(name:'foo', orderId:10)
        assertEquals 'foo', sv.name
        assertEquals 10, sv.orderId
		
		sv = new SelValue(name:'bar')
		assertEquals 'bar', sv.name
		assertEquals 0, sv.orderId
    }
	
	void testConstraints() {
		mockForConstraintsTests SelValue
        SelValue sv = new SelValue(name:'', orderId:0)
		assertFalse sv.validate()
		assertEquals 'blank', sv.errors['name']
		assertNull sv.errors['orderId']
		
		sv = new SelValue()
		assertFalse sv.validate()
		assertEquals 'nullable', sv.errors['name']
		assertNull sv.errors['orderId']
		
		sv = new SelValue(name:'foo')
		assertTrue sv.validate()
	}
	
	void testToString() {
        SelValue sv = new SelValue(name:'foo', orderId:10)
		assertEquals 'foo', sv.toString()
		
		sv = new SelValue()
		assertEquals '', sv.toString()
	}
}
