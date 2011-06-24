package org.amcworld.springcrm

import grails.test.*

class SalutationTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Salutation salutation = new Salutation(name:'Mr.', orderId:40)
        assertEquals 'Mr.', salutation.name
        assertEquals 40, salutation.orderId
    }
}
