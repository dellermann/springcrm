package org.amcworld.springcrm

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
		description type:'text'
    }
	static searchable = true
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

	Product() {}

	Product(Product p) {
		name = p.name
		category = p.category
		manufacturer = p.manufacturer
		retailer = p.retailer
		quantity = p.quantity
		unit = p.unit
		unitPrice = p.unitPrice
		taxClass = p.taxClass
		weight = p.weight
		commission = p.commission
		salesStart = p.salesStart
		salesEnd = p.salesEnd
		description = p.description
	}

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String toString() {
		return name ?: ''
	}
}
