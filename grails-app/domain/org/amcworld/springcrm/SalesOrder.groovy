package org.amcworld.springcrm

class SalesOrder extends InvoicingTransaction {

    static constraints = {
		stage()
		dueDate(nullable: true)
		deliveryDate(nullable: true)
		quote(nullable: true)
    }
    static belongsTo = [quote: Quote]
    static hasMany = [invoices: Invoice]
	static mapping = {
		stage column: 'so_stage_id'
	}
	static searchable = true

	SalesOrderStage stage
	Date dueDate
	Date deliveryDate

	{
		type = 'O'
	}

	SalesOrder() {}

	SalesOrder(Quote q) {
		super(q)
		quote = q
	}

	SalesOrder(SalesOrder so) {
		super(so)
		quote = so.quote
	}
}
