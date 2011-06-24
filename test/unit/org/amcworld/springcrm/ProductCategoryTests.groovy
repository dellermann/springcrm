package org.amcworld.springcrm

import grails.test.*

class ProductCategoryTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        ProductCategory cat = 
			new ProductCategory(name:'Hardware', orderId:90)
        assertEquals 'Hardware', cat.name
        assertEquals 90, cat.orderId
    }
}
