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
    static hasMany = [creditMemos: CreditMemo, dunnings: Dunning]
	static mapping = {
		stage column: 'invoice_stage_id'
	}
	static searchable = true
    static transients = ['stateColor']


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


    //-- Public methods -------------------------

    String getStateColor() {
        String color = 'white'
        switch (stage?.id) {
        case 907:
            color = 'green'
            break
        case 906:
            color = 'black'
            break
        case 905:
            color = 'blue'
            break
        case 904:
            color = 'purple'
            break
        case 903:
            color = 'green'
            break
        case 902:
            Date d = new Date()
            if (d >= dueDatePayment - 3) {
                if (d <= dueDatePayment) {
                    color = 'yellow'
                } else if (d <= dueDatePayment + 3) {
                    color = 'orange'
                } else {
                    color = 'red'
                }
            }
            break
        }
        return color
    }
}
