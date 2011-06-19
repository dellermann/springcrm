package org.amcworld.springcrm

import java.util.Date;

class Quote extends InvoicingTransaction {

    static constraints = {
		stage()
		quoteDate()
		validUntil(nullable:true)
		carrier(nullable:true)
		shippingDate(nullable:true)
    }
    static hasMany = [termsAndConditions:TermsAndConditions]
	static mapping = {
		items column:"Quote"
	}
	
	QuoteStage stage
	Date quoteDate = new Date()
	Date validUntil
	Carrier carrier
	Date shippingDate

	String getFullNumber() {
		return "A-" + super.fullNumber;
	}
}
