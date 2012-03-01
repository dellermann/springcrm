/*
 * Invoice.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm


/**
 * The class {@code Invoice} represents an invoice.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class Invoice extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
		stage()
		dueDatePayment()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, min: 0.0, scale: 2, widget: 'currency')
		paymentMethod(nullable: true)
		quote(nullable: true)
		salesOrder(nullable: true)
    }
    static belongsTo = [quote: Quote, salesOrder: SalesOrder]
	static mapping = {
		stage column: 'invoice_stage_id'
	}
	static searchable = true


    //-- Instance variables ---------------------

	InvoiceStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod


    //-- Instance initializer -------------------

	{
		type = 'I'
	}


    //-- Constructors ---------------------------

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
