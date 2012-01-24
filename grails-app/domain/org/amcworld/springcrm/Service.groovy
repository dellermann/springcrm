package org.amcworld.springcrm

class Service {

    static constraints = {
		number(unique: true)
		name(blank: false)
		category(nullable: true)
		quantity(min: 0.0d)
		unit(nullable: true)
		unitPrice(scale: 2, min: 0.0d)
		taxRate(nullable: true)
		commission(min: 0.0d)
		salesStart(nullable: true)
		salesEnd(nullable: true)
        description(widget: 'textarea', nullable: true)
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
	double quantity
	Unit unit
	double unitPrice
	TaxRate taxRate
	double commission
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
