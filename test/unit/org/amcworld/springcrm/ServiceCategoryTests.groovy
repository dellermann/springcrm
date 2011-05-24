package org.amcworld.springcrm

import grails.test.*

class ServiceCategoryTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        ServiceCategory cat = 
			new ServiceCategory(name:"Development", orderId:60)
        assertEquals "Development", cat.name
        assertEquals 60, cat.orderId
    }
}
