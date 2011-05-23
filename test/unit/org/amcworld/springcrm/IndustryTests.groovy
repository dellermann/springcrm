package org.amcworld.springcrm

import grails.test.*

class IndustryTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Industry industry = new Industry(name:"Energy", orderId:20)
        assertEquals "Energy", industry.name
        assertEquals 20, industry.orderId
    }
}
