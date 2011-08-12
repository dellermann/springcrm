package org.amcworld.springcrm

import java.util.Date;

class SalesOrder extends InvoicingTransaction {

    static constraints = {
		stage()
		dueDate(nullable:true)
		deliveryDate(nullable:true)
		quote(nullable:true)
    }
    static hasMany = [invoices:Invoice]
	static mapping = {
		stage column:'so_stage_id'
	}
	static searchable = true
	
	SalesOrderStage stage
	Date dueDate
	Date deliveryDate
	Quote quote
	
	{
		type = 'O'
	}

	SalesOrder() {}

	SalesOrder(Quote q) {
		super(q)
		this.quote = q
	}
}
