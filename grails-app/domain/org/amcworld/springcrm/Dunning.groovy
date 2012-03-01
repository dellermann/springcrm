/*
 * Dunning.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm


/**
 * The class {@code Dunning} represents a dunning which belongs to an invoice.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class Dunning extends InvoicingTransaction {

    //-- Class variables ------------------------

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


    //-- Instance variables ---------------------

	DunningLevel level
	DunningStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod


    //-- Class initializer ----------------------

	{
		type = 'D'
	}


    //-- Constructors ---------------------------

	Dunning() {}

	Dunning(Invoice i) {
		super(i)
		invoice = i
        headerText = ''
        footerText = ''
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
