package org.amcworld.springcrm

import grails.test.*

class SalesOrderStageTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def sos = new SalesOrderStage(name:'sent', orderId:100)
        assertEquals 'sent', sos.name
        assertEquals 100, sos.orderId
    }
}
