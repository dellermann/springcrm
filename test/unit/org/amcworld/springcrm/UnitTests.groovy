package org.amcworld.springcrm

import grails.test.*

class UnitTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Unit unit = new Unit(name:'Meter', orderId:70)
        assertEquals 'Meter', unit.name
        assertEquals 70, unit.orderId
    }
}
