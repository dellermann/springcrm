package org.amcworld.springcrm

import grails.test.*

class DunningStageTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def ds = new DunningStage(name:'sent', orderId:2200)
        assertEquals 'sent', ds.name
        assertEquals 2200, ds.orderId
    }
}
