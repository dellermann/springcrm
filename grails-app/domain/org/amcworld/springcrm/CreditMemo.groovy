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
 * @version 1.3
 */
class CreditMemo extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
		stage()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, min: 0.0, scale: 10, widget: 'currency')
		paymentMethod(nullable: true)
		invoice(nullable: true)
		dunning(nullable: true)
    }
	static belongsTo = [invoice: Invoice, dunning: Dunning]
	static mapping = {
		stage column: 'credit_memo_stage_id'
	}
	static searchable = true
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'paymentStateColor'
    ]


    //-- Instance variables ---------------------

	CreditMemoStage stage
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod; /* leave semicolon here! */


    //-- Constructors ---------------------------

	CreditMemo() {
        type = 'C'
    }

	CreditMemo(Invoice i) {
		super(i)
        type = 'C'
        headerText = ''
        footerText = ''
		invoice = i
	}

	CreditMemo(Dunning d) {
		super(d)
        type = 'C'
        headerText = ''
        footerText = ''
		dunning = d
	}

	CreditMemo(CreditMemo cm) {
		super(cm)
        type = 'C'
		invoice = cm.invoice
		dunning = cm.dunning
	}


    //-- Public methods -------------------------

    /**
     * Gets the balance of this credit memo, that is the difference between the
     * credit memo total sum and the payment amount.
     *
     * @return  the credit memo balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance() {
        return (total ?: 0) - (paymentAmount ?: 0)
    }

    /**
     * Gets the closing balance of the invoice or dunning associated to this
     * credit memo.  The closing balance is calculated from the balance of the
     * associated invoice or dunning minus the sum of the balances of all its
     * credit memos.  A negative balance indicates a claim to the customer, a
     * positive one indicates a credit of the customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     * @see     Invoice#getClosingBalance()
     * @see     Dunning#getClosingBalance()
     */
    BigDecimal getClosingBalance() {
        return (invoice ? invoice : dunning).closingBalance
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * invoice.  This property is usually use to compute CSS classes in the
     * views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor() {
        String color = 'default'
        if (closingBalance > 0) {
            color = 'red'
        } else if (closingBalance < 0) {
            color = 'green'
        }
        return color
    }

    /**
     * Gets the name of a color indicating the payment state of this credit
     * memo.
     *
     * @return  the indicator color
     */
    String getPaymentStateColor() {
        String color = 'default'
        def id = stage?.id ?: 0
        if ((id >= 2502) && (id <= 2504)) {     // cancelled, paid, delivered
            color = (closingBalance >= 0) ? 'green' : 'red'
        }
        return color
    }
}
