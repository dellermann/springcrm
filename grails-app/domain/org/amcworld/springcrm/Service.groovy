package org.amcworld.springcrm

class Service {

    static constraints = {
		number(unique: true, widget: 'autonumber')
		name(blank: false)
		category(nullable: true)
		quantity(min: 0.0)
		unit(nullable: true)
		unitPrice(scale: 2, min: 0.0, widget: 'currency')
		taxRate(nullable: true)
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
	ServiceCategory category
	BigDecimal quantity
	Unit unit
	BigDecimal unitPrice
	TaxRate taxRate
	BigDecimal commission
	Date salesStart
	Date salesEnd
	String description
	Date dateCreated
	Date lastUpdated

	Service() {}

	Service(Service s) {
		name = s.name
		category = s.category
		quantity = s.quantity
		unit = s.unit
		unitPrice = s.unitPrice
		taxRate = s.taxRate
		commission = s.commission
		salesStart = s.salesStart
		salesEnd = s.salesEnd
		description = s.description
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
