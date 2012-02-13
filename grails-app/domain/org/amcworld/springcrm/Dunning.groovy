package org.amcworld.springcrm

class Dunning extends InvoicingTransaction {

    static constraints = {
		level()
		stage()
		dueDatePayment()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, min: 0.0, scale: 2, widget: 'currency')
		paymentMethod(nullable: true)
		invoice()
    }
	static belongsTo = [invoice: Invoice]
	static mapping = {
		stage column: 'dunning_stage_id'
	}
	static searchable = true

	DunningLevel level
	DunningStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod

	{
		type = 'D'
	}

	Dunning() {}

	Dunning(Invoice i) {
		super(i)
		invoice = i
	}

	Dunning(Dunning d) {
		super(d)
		level = d.level
		stage = d.stage
		dueDatePayment = d.dueDatePayment
		paymentDate = d.paymentDate
		paymentAmount = d.paymentAmount
		paymentMethod = d.paymentMethod
		invoice = d.invoice
	}
}
