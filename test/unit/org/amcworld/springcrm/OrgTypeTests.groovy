package org.amcworld.springcrm

import grails.test.*

class OrgTypeTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def orgType = new OrgType(name:'Customer', orderId:10)
        assertEquals 'Customer', orgType.name
        assertEquals 10, orgType.orderId
    }
}
