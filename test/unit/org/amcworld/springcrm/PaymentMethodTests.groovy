package org.amcworld.springcrm

import grails.test.*

class PaymentMethodTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def pm = new PaymentMethod(name:'cash', orderId:2400)
        assertEquals 'cash', pm.name
        assertEquals 2400, pm.orderId
    }
}
