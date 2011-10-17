package org.amcworld.springcrm

class CreditMemo extends InvoicingTransaction {

    static constraints = {
		stage()
		paymentDate(nullable:true)
		paymentAmount(nullable:true, min:0.0, scale:2)
		paymentMethod(nullable:true)
		invoice(nullable:true)
		dunning(nullable:true)
    }
	static belongsTo = [ invoice:Invoice, dunning:Dunning ]
	static mapping = {
		stage column:'credit_memo_stage_id'
	}
	static searchable = true

	CreditMemoStage stage	
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod
	
	{
		type = 'C'
	}

	CreditMemo() {}

	CreditMemo(Invoice i) {
		super(i)
		invoice = i
	}

	CreditMemo(Dunning d) {
		super(d)
		dunning = d
	}

	CreditMemo(CreditMemo cm) {
		super(cm)
		invoice = cm.invoice
		dunning = cm.dunning
	}
}
