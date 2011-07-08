package org.amcworld.springcrm

import java.util.Date;

class Product {

    static constraints = {
		number(unique:true)
		name(blank:false)
		category(nullable:true)
		manufacturer(nullable:true)
		retailer(nullable:true)
		quantity(min:0.0d)
		unit(nullable:true)
		unitPrice(scale:2, min:0.01d)
		taxClass(nullable:true)
		weight(nullable:true, min:0.0)
		commission(min:0.0d)
		salesStart(nullable:true)
		salesEnd(nullable:true)
        description(widget:'textarea', nullable:true)
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		sort 'number'
    }
	static transients = ['fullNumber']
	
	def seqNumberService
	
	int number
	String name
	ProductCategory category
	String manufacturer
	String retailer
	double quantity
	Unit unit
	double unitPrice
	TaxClass taxClass
	BigDecimal weight
	double commission
	Date salesStart
	Date salesEnd
	String description
	Date dateCreated
	Date lastUpdated
	
	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}
	
	String toString() {
		return name ?: ''
	}
}
