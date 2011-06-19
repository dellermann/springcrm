package org.amcworld.springcrm

import grails.test.*

class TermsAndConditionsTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        TermsAndConditions tac =
            new TermsAndConditions(name:"wares", orderId:120)
        assertEquals "wares", tac.name
        assertEquals 120, tac.orderId
    }
}
