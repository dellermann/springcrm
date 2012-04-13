/*
 * CreditMemo.groovy
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
 * The class {@code CreditMemo} represents a credit memo.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class CreditMemo extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
		stage()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, min: 0.0, scale: 2, widget: 'currency')
		paymentMethod(nullable: true)
		invoice(nullable: true)
		dunning(nullable: true)
    }
	static belongsTo = [invoice: Invoice, dunning: Dunning]
	static mapping = {
		stage column: 'credit_memo_stage_id'
	}
	static searchable = true


    //-- Instance variables ---------------------

	CreditMemoStage stage
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod


    //-- Instance initializer -------------------

	{
		type = 'C'
	}


    //-- Constructors ---------------------------

	CreditMemo() {}

	CreditMemo(Invoice i) {
		super(i)
        headerText = ''
        footerText = ''
		invoice = i
	}

	CreditMemo(Dunning d) {
		super(d)
        headerText = ''
        footerText = ''
		dunning = d
	}

	CreditMemo(CreditMemo cm) {
		super(cm)
		invoice = cm.invoice
		dunning = cm.dunning
	}
}
