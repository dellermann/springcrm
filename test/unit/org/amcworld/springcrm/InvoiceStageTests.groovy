package org.amcworld.springcrm

import grails.test.*

class InvoiceStageTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def is = new InvoiceStage(name:'sent', orderId:500)
        assertEquals 'sent', is.name
        assertEquals 500, is.orderId
    }
}
