package org.amcworld.springcrm

import grails.test.*

class CarrierTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Carrier c = new Carrier(name:"DHL", orderId:110)
        assertEquals "DHL", c.name
        assertEquals 110, c.orderId
    }
}
