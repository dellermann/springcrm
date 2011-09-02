package org.amcworld.springcrm

class Service {

    static constraints = {
		number(unique:true)
		name(blank:false)
		category(nullable:true)
		quantity(min:0.0d)
		unit(nullable:true)
		unitPrice(scale:2, min:0.01d)
		taxClass(nullable:true)
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
	ServiceCategory category
	double quantity
	Unit unit
	double unitPrice
	TaxClass taxClass
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
