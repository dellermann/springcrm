package org.amcworld.springcrm

class Product {

    static constraints = {
		number(unique: true, widget: 'autonumber')
		name(blank: false)
		category(nullable: true)
		manufacturer(nullable: true)
		retailer(nullable: true)
		quantity(min: 0.0)
		unit(nullable: true)
		unitPrice(scale: 2, min: 0.0, widget: 'currency')
		taxRate(nullable: true)
		purchasePrice(nullable: true, scale: 2, min: 0.0, widget: 'currency')
		weight(nullable: true, min: 0.0)
		commission(nullable: true, min: 0.0, widget: 'percent')
		salesStart(nullable: true)
		salesEnd(nullable: true)
        description(nullable: true, widget: 'textarea')
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		sort 'number'
		description type: 'text'
    }
	static searchable = true
	static transients = ['fullNumber']

	def seqNumberService

	int number
	String name
	ProductCategory category
	String manufacturer
	String retailer
	BigDecimal quantity
	Unit unit
	BigDecimal unitPrice
	TaxRate taxRate
	BigDecimal purchasePrice
	BigDecimal weight
	BigDecimal commission
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
		taxRate = p.taxRate
		purchasePrice = p.purchasePrice
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

	def beforeInsert() {
		if (number == 0) {
			number = seqNumberService.nextNumber(getClass())
		}
	}
}
