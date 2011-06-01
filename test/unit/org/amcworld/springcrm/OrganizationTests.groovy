package org.amcworld.springcrm

import grails.test.*

class OrganizationTests extends GrailsUnitTestCase {
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Organization org = new Organization(
            number:10000, name:"AMC World", email1:"info@amc-world.de",
            website:"http://www.amc-world.de"
        )
        assertEquals 10000, org.number
        assertEquals "AMC World", org.name
        assertNull org.billingAddrStreet
        assertNull org.billingAddrPoBox
        assertNull org.billingAddrLocation
        assertNull org.billingAddrPostalCode
        assertNull org.billingAddrState
        assertNull org.billingAddrCountry
        assertNull org.shippingAddrStreet
        assertNull org.shippingAddrPoBox
        assertNull org.shippingAddrLocation
        assertNull org.shippingAddrPostalCode
        assertNull org.shippingAddrState
        assertNull org.shippingAddrCountry
        assertNull org.phone
        assertNull org.fax
        assertNull org.phoneOther
        assertEquals "info@amc-world.de", org.email1
        assertNull org.email2
        assertEquals "http://www.amc-world.de", org.website
        assertNull org.legalForm
        assertNull org.type
        assertNull org.industry
        assertNull org.owner
        assertNull org.numEmployees
        assertNull org.notes
        assertNull org.dateCreated
		assertNull org.lastUpdated
    }

    void testSetWebsite() {
        Organization org = new Organization(website:"www.amc-world.de")
        assertEquals "http://www.amc-world.de", org.website
        org.website = "http://www.amc-world.de"
        assertEquals "http://www.amc-world.de", org.website
        org.website = "https://www.amc-world.de"
        assertEquals "https://www.amc-world.de", org.website
        org.website = "www.amc-world[http://].de"
        assertEquals "http://www.amc-world[http://].de", org.website
    }

    void testOrgType() {
        Organization org = new Organization()
        org.type = new OrgType(name:"Customer")
        assertEquals "Customer", org.type.name
        assertEquals 0, org.type.orderId
    }

    void testIndustry() {
        Organization org = new Organization()
        org.industry = new Industry(name:"Banking")
        assertEquals "Banking", org.industry.name
        assertEquals 0, org.industry.orderId
    }

    void testRating() {
        Organization org = new Organization()
        org.rating = new Rating(name:"Market Failed")
        assertEquals "Market Failed", org.rating.name
        assertEquals 0, org.rating.orderId
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests Organization
		def validationFields = ["name"]
		Organization org = new Organization()
		assertFalse org.validate(validationFields)
		assertEquals "nullable", org.errors["name"]
		
		org = new Organization(name:"")
		assertFalse org.validate(validationFields)
		assertEquals "blank", org.errors["name"]
		
		org = new Organization(name:"AMC World")
		assertTrue org.validate(validationFields)
	}
	
	void testSizeConstraints() {
		mockForConstraintsTests Organization
		def validationFields = ["phone", "fax", "phoneOther"]
		String s = "123456789012345678901234567890123456789012345"
		Organization org = new Organization(phone:s, fax:s, phoneOther:s)
		assertFalse org.validate(validationFields)
		assertEquals "maxSize", org.errors["phone"]
		assertEquals "maxSize", org.errors["fax"]
		assertEquals "maxSize", org.errors["phoneOther"]
		
		s = "1234567890123456789012345678901234567890"
		org = new Organization(phone:s, fax:s, phoneOther:s)
		assertTrue org.validate(validationFields)
		
		s = "+49 30 8321475-0"
		org = new Organization(phone:s, fax:s, phoneOther:s)
		assertTrue org.validate(validationFields)
	}
	
	void testEmailConstraints() {
		mockForConstraintsTests Organization
		def validationFields = ["email1", "email2"]
		String s = null
		Organization org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "nullable", org.errors["email1"]
		assertEquals "nullable", org.errors["email2"]

		s = "foobar"
		org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "email", org.errors["email1"]
		assertEquals "email", org.errors["email2"]
		
		s = "foobar@"
		org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "email", org.errors["email1"]
		assertEquals "email", org.errors["email2"]
		
		s = "@mydomain.com"
		org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "email", org.errors["email1"]
		assertEquals "email", org.errors["email2"]
		
		s = "user@mydomain"
		org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "email", org.errors["email1"]
		assertEquals "email", org.errors["email2"]
		
		s = "user@.com"
		org = new Organization(email1:s, email2:s)
		assertFalse org.validate(validationFields)
		assertEquals "email", org.errors["email1"]
		assertEquals "email", org.errors["email2"]
		
		s = ""
		org = new Organization(email1:s, email2:s)
		assertTrue org.validate(validationFields)

		s = "user@mydomain.com"
		org = new Organization(email1:s, email2:s)
		assertTrue org.validate(validationFields)
	}
	
	void testUrlConstraints() {
		mockForConstraintsTests Organization
		def validationFields = ["website"]
		Organization org = new Organization(website:null)
		assertFalse org.validate(validationFields)
		assertEquals "nullable", org.errors["website"]

		org = new Organization(website:"foobar")
		assertFalse org.validate(validationFields)
		assertEquals "url", org.errors["website"]

		org = new Organization(website:"mydomain.com")
		assertTrue org.validate(validationFields)

		org = new Organization(website:"www.mydomain.com")
		assertTrue org.validate(validationFields)

		org = new Organization(website:"http://www.mydomain.com")
		assertTrue org.validate(validationFields)
		
		org = new Organization(website:"")
		assertTrue org.validate(validationFields)

		org = new Organization(
			website:"http://www.mydomain.com/foo/bar.html?id=5"
		)
		assertTrue org.validate(validationFields)
	}
	
	void testFullNumber() {
		Organization org = new Organization(number:10000)
		assertEquals "ORG-10000", org.fullNumber
		org = new Organization(number:10)
		assertEquals "ORG-10", org.fullNumber
		org = new Organization(number:100000)
		assertEquals "ORG-100000", org.fullNumber
	}
	
	void testBillingAddr() {
		Organization org = new Organization(
			billingAddrStreet:"Fischerinsel 1", billingAddrPoBox: "12345",
			billingAddrPostalCode:"10179", billingAddrLocation:"Berlin",
			billingAddrState:"BLN", billingAddrCountry:"Deutschland"
		)
		assertEquals "Fischerinsel 1, 10179 Berlin", org.billingAddr
		org = new Organization(
			billingAddrStreet:"Fischerinsel 1", billingAddrPostalCode:"10179", 
			billingAddrLocation:"Berlin"
		)
		assertEquals "Fischerinsel 1, 10179 Berlin", org.billingAddr
		org = new Organization(
			billingAddrStreet:"Fischerinsel 1", billingAddrPostalCode:"10179"
		)
		assertEquals "Fischerinsel 1", org.billingAddr
		org = new Organization(
			billingAddrStreet:"Fischerinsel 1", billingAddrLocation:"Berlin"
		)
		assertEquals "Fischerinsel 1, Berlin", org.billingAddr
		org = new Organization(
			billingAddrStreet:"Fischerinsel 1"
		)
		assertEquals "Fischerinsel 1", org.billingAddr
		org = new Organization(
			billingAddrPostalCode:"10179", billingAddrLocation:"Berlin"
		)
		assertEquals "10179 Berlin", org.billingAddr
		org = new Organization(billingAddrPostalCode:"10179")
		assertEquals "", org.billingAddr
		org = new Organization(billingAddrLocation:"Berlin")
		assertEquals "Berlin", org.billingAddr
		org = new Organization(billingAddrStreet:"Fischerinsel 1")
		assertEquals "Fischerinsel 1", org.billingAddr
		org = new Organization()
		assertEquals "", org.billingAddr
	}
	
	void testShippingAddr() {
		Organization org = new Organization(
			shippingAddrStreet:"Fischerinsel 1", shippingAddrPoBox: "12345",
			shippingAddrPostalCode:"10179", shippingAddrLocation:"Berlin",
			shippingAddrState:"BLN", shippingAddrCountry:"Deutschland"
		)
		assertEquals "Fischerinsel 1, 10179 Berlin", org.shippingAddr
		org = new Organization(
			shippingAddrStreet:"Fischerinsel 1", shippingAddrPostalCode:"10179", 
			shippingAddrLocation:"Berlin"
		)
		assertEquals "Fischerinsel 1, 10179 Berlin", org.shippingAddr
		org = new Organization(
			shippingAddrStreet:"Fischerinsel 1", shippingAddrPostalCode:"10179"
		)
		assertEquals "Fischerinsel 1", org.shippingAddr
		org = new Organization(
			shippingAddrStreet:"Fischerinsel 1", shippingAddrLocation:"Berlin"
		)
		assertEquals "Fischerinsel 1, Berlin", org.shippingAddr
		org = new Organization(
			shippingAddrStreet:"Fischerinsel 1"
		)
		assertEquals "Fischerinsel 1", org.shippingAddr
		org = new Organization(
			shippingAddrPostalCode:"10179", shippingAddrLocation:"Berlin"
		)
		assertEquals "10179 Berlin", org.shippingAddr
		org = new Organization(shippingAddrPostalCode:"10179")
		assertEquals "", org.shippingAddr
		org = new Organization(shippingAddrLocation:"Berlin")
		assertEquals "Berlin", org.shippingAddr
		org = new Organization(shippingAddrStreet:"Fischerinsel 1")
		assertEquals "Fischerinsel 1", org.shippingAddr
		org = new Organization()
		assertEquals "", org.shippingAddr
	}
	
	void testToString() {
        Organization org = new Organization(
            number:10000, name:"AMC World", email1:"info@amc-world.de",
            website:"http://www.amc-world.de"
        )
		assertEquals "AMC World", org.toString()
		
		org = new Organization()
		assertEquals "", org.toString()
	}
}
