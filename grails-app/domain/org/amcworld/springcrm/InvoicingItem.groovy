package org.amcworld.springcrm

class InvoicingItem {

	static belongsTo = [invoicingTransaction:InvoicingTransaction]
    static constraints = {
		number(blank:false)
		quantity(min:0.0)
		unit()
		name(blank:false)
		description(nullable:true)
		unitPrice(scale:2, min:0.01)
		tax(scale:1, min:0.0)
    }
	static mapping = {
    	total formula:'quantity * unit_price'
	}
	
	String number
	BigDecimal quantity
	String unit
	String name
	String description
	BigDecimal unitPrice
	BigDecimal total
	BigDecimal tax
	
	String toString() {
		return name
	}
}
