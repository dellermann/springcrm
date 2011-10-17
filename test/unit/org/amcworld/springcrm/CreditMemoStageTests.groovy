package org.amcworld.springcrm

import grails.test.*

class CreditMemoStageTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def cms = new CreditMemoStage(name:'sent', orderId:2502)
        assertEquals 'sent', cms.name
        assertEquals 2502, cms.orderId
    }
}
