package org.amcworld.springcrm

import grails.test.*

class QuoteStageTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        QuoteStage qs = new QuoteStage(name:'sent', orderId:100)
        assertEquals 'sent', qs.name
        assertEquals 100, qs.orderId
    }
}
