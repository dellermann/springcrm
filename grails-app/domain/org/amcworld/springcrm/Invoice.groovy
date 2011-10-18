package org.amcworld.springcrm

class Invoice extends InvoicingTransaction {

    static constraints = {
		stage()
		dueDatePayment()
		paymentDate(nullable:true)
		paymentAmount(nullable:true, min:0.0, scale:2)
		paymentMethod(nullable:true)
		quote(nullable:true)
		salesOrder(nullable:true)
    }
    static belongsTo = [quote:Quote, salesOrder:SalesOrder]
	static mapping = {
		stage column:'invoice_stage_id'
	}
	static searchable = true
	
	InvoiceStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod

	{
		type = 'I'
	}

	Invoice() {}

	Invoice(Quote q) {
		super(q)
		quote = q
	}

	Invoice(SalesOrder so) {
		super(so)
		salesOrder = so
	}

	Invoice(Invoice i) {
		super(i)
		quote = i.quote
		salesOrder = i.salesOrder
	}
}
