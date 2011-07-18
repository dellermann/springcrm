package org.amcworld.springcrm

import java.util.Date;

class Quote extends InvoicingTransaction {

    static constraints = {
		stage()
		validUntil(nullable:true)
    }
	static mapping = {
		stage column:'quote_stage_id'
	}
	static searchable = true
	
	QuoteStage stage
	Date validUntil
	
	{
		type = 'Q'
	}
}
