package org.amcworld.springcrm

import grails.test.*

class ProductTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        Product p = new Product(
            number:10000, name:'Netzwerkkabel', quantity:12.5, unitPrice:0.8,
			weight:1.2
        )
        assertEquals 10000, p.number
        assertEquals 'Netzwerkkabel', p.name
        assertNull p.category
		assertNull p.manufacturer
		assertNull p.retailer
        assertEquals 12.5, p.quantity
        assertNull p.unit
        assertEquals 0.8, p.unitPrice
        assertNull p.taxClass
		assertEquals 1.2, p.weight
        assertEquals 0, p.commission
        assertNull p.salesStart
        assertNull p.salesEnd
		assertNull p.description
    }

    void testCategory() {
        Product p = new Product()
        p.category = new ProductCategory(name:'Hardware')
        assertEquals 'Hardware', p.category.name
        assertEquals 0, p.category.orderId
    }

    void testUnit() {
        Product p = new Product()
        p.unit = new Unit(name:'m')
        assertEquals 'm', p.unit.name
        assertEquals 0, p.unit.orderId
    }

    void testTaxClass() {
        Product p = new Product()
        p.taxClass = new TaxClass(name:'19%')
        assertEquals '19%', p.taxClass.name
        assertEquals 0, p.taxClass.orderId
    }
	
	void testBlankConstraints() {
		mockForConstraintsTests Product
		def validationFields = ['name']
        Product p = new Product()
		assertFalse p.validate(validationFields)
		assertEquals 'nullable', p.errors['name']
		
		p = new Product(name:'')
		assertFalse p.validate(validationFields)
		assertEquals 'blank', p.errors['name']
	}
	
	void testUniqueConstraints() {
		def p1 = new Product(number:10000, name:'foo')
		def p2 = new Product(number:10010, name:'bar')
		mockDomain(Product, [p1, p2])
		
		def badProd = new Product(number:10000, name:'whee')
		assertNull badProd.save()
		assertEquals 2, Product.count()
		assertEquals 'unique', badProd.errors['number']
		assertNull badProd.errors['name']
		
		def goodProd = new Product(
			number:10020, name:'foo2', unit:'m', unitPrice:2.3
		)
		assertNotNull goodProd.save()
		assertEquals 3, Product.count()
	}

	void testMinConstraints() {
		mockForConstraintsTests Product
		def validationFields = ['quantity', 'unitPrice', 'weight', 'commission']
		Product p = 
			new Product(quantity:-1, unitPrice:0, weight:-0.8, commission:-0.5)
		assertFalse p.validate(validationFields)
		assertEquals 'min', p.errors['quantity']
		assertEquals 'min', p.errors['unitPrice']
		assertEquals 'min', p.errors['weight']
		assertEquals 'min', p.errors['commission']
		
		p = new Product(quantity:0, unitPrice:0.01, weight:0, commission:0)
		assertTrue p.validate(validationFields)
		
		p = new Product(quantity:0, unitPrice:0.01, weight:null, commission:0)
		assertTrue p.validate(validationFields)
	}
	
	void testFullNumber() {
		def seqNumber = new SeqNumber(className:Product.class.name, nextNumber:10002, prefix:'P', suffix:'')
		mockDomain(SeqNumber, [seqNumber])
		
        Product p = new Product(number:10000, name:'Netzwerkkabel')
		p.seqNumberService = new SeqNumberService()
		assertEquals 'P-10000', p.fullNumber
	}
	
	void testToString() {
        Product p = new Product(number:10000, name:'Netzwerkkabel')
		assertToString p, 'Netzwerkkabel'
		
		p = new Product()
		assertToString p, ''
	}
}
