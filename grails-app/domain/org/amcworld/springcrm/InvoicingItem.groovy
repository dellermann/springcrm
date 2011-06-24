package org.amcworld.springcrm

class InvoicingItem {

    static constraints = {
		number(unique:true)
		quantity(min:0.0d)
		unit()
		name(blank:false)
		description(nullable:true)
		unitPrice(scale:2, min:0.01d)
		discountPercent(scale:2, min:0.0d)
		discountAmount(scale:2, min:0.0d)
		tax(scale:1, min:0.0d)
		orderId()
		quote(nullable:true)
		//salesOrder(nullable:true)
		//invoice(nullable:true)
    }
	static mapping = {
		sort 'orderId'
	}
	static transients = ['total']
	
	int number
	double quantity
	String unit
	String name
	String description
	double unitPrice
	double discountPercent
	double discountAmount
	double tax
	int orderId
	Quote quote
	//SalesOrder salesOrder
	//Invoice invoice
	
	double getTotal() {
		return quantity * unitPrice
	}
	
	String toString() {
		return name
	}
}
