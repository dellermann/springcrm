package org.amcworld.springcrm

import grails.test.*

class DunningLevelTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def dl = new DunningLevel(name:'1st dunning', orderId:2300)
        assertEquals '1st dunning', dl.name
        assertEquals 2300, dl.orderId
    }
}
