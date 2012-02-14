package org.amcworld.springcrm

class Quote extends InvoicingTransaction {

    static constraints = {
		stage()
		validUntil(nullable: true)
    }
    static hasMany = [salesOrders: SalesOrder, invoices: Invoice]
	static mapping = {
		stage column: 'quote_stage_id'
	}
	static searchable = true

	QuoteStage stage
	Date validUntil

	{
		type = 'Q'
	}

	Quote() {}

	Quote(Quote q) {
		super(q)
		validUntil = q.validUntil
	}
}
