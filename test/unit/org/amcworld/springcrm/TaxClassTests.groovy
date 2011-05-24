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
        TaxClass tc = new TaxClass(name:"19%", orderId:80)
        assertEquals "19%", tc.name
        assertEquals 80, tc.orderId
    }
}
