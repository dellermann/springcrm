package org.amcworld.springcrm

class Invoice extends InvoicingTransaction {

    static constraints = {
		stage()
		dueDatePayment()
		paymentDate(nullable:true)
		paymentAmount(nullable:true, min:0.0, scale:2)
		quote(nullable:true)
		salesOrder(nullable:true)
    }
	static mapping = {
		stage column:'invoice_stage_id'
	}

	InvoiceStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	Quote quote
	SalesOrder salesOrder
	
	{
		type = 'I'
	}

	Invoice() {}

	Invoice(Quote q) {
		super(q)
		this.quote = q
	}

	Invoice(SalesOrder so) {
		super(so)
		this.salesOrder = so
	}
}
