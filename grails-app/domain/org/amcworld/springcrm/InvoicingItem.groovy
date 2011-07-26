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
	static searchable = [only:['number', 'name', 'description']]
	static transients = ['total']
		
	String number
	BigDecimal quantity
	String unit
	String name
	String description
	BigDecimal unitPrice
	BigDecimal tax
	
	InvoicingItem() {}
	
	InvoicingItem(InvoicingItem i) {
		number = i.number
		quantity = i.quantity
		unit = i.unit
		name = i.name
		description = i.description
		unitPrice = i.unitPrice
		tax = i.tax
	}
	
	BigDecimal getQuantity() {
		return quantity ?: 0
	}
	
	BigDecimal getUnitPrice() {
		return unitPrice ?: 0
	}
	
	BigDecimal getTotal() {
		return getQuantity() * getUnitPrice()
	}
	
	String toString() {
		return name
	}
}
