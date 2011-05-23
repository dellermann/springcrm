package org.amcworld.springcrm

import grails.test.*

class RatingTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Rating rating = new Rating(name:"Active", orderId:30)
        assertEquals "Active", rating.name
        assertEquals 30, rating.orderId
    }
}
