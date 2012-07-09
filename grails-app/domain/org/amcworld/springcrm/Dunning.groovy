/*
 * Dunning.groovy
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
 * The class {@code Dunning} represents a dunning which belongs to an invoice.
 *
 * @author	Daniel Ellermann
 * @version 1.0
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
    static hasMany = [creditMemos: CreditMemo]
	static mapping = {
		stage column: 'dunning_stage_id'
	}
	static searchable = true
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'paymentStateColor'
    ]


    //-- Instance variables ---------------------

	DunningLevel level
	DunningStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod; /* leave semicolon here! */


    //-- Instance initializer -------------------

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


    //-- Public methods -------------------------

    /**
     * Gets the balance of this dunning, that is the difference between the
     * payment amount and the dunning total sum.
     * <p>
     * Note that the dunning balance does not take any credit memos into
     * account.  Use method {@code getClosingBalance} for it.
     *
     * @return  the dunning balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance() {
        return (paymentAmount ?: 0) - (total ?: 0)
    }

    /**
     * Gets the closing balance of this dunning.  The closing balance is
     * calculated from the dunning balance plus the sum of the balances of
     * all credit memos associated to this dunning.  A negative balance
     * indicates a claim to the customer, a positive one indicates a credit of
     * the customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     */
    BigDecimal getClosingBalance() {
        return balance + (creditMemos ? creditMemos*.balance.sum() : 0)
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * dunning.  This property is usually use to compute CSS classes in the
     * views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor() {
        String color = 'default'
        if (closingBalance < 0) {
            color = 'red'
        } else if (closingBalance > 0) {
            color = 'green'
        }
        return color
    }

    /**
     * Gets the name of a color indicating the payment state of this dunning.
     * This property is usually use to compute CSS classes in the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getPaymentStateColor() {
        String color = 'default'
        switch (stage?.id) {
        case 2206:                      // cancelled
            color = (closingBalance >= 0) ? 'green' : colorIndicatorByDate()
            break
        case 2205:                      // booked out
            color = 'black'
            break
        case 2204:                      // cashing
            color = 'blue'
            break
        case 2203:                      // paid
            color = (closingBalance >= 0) ? 'green' : colorIndicatorByDate()
            break
        case 2202:                      // delivered
            color = colorIndicatorByDate()
            break
        }
        return color
    }


    //-- Non-public methods ---------------------

    /**
     * Returns the color indicator for the payment state depending on the
     * current date and its relation to the due date of payment.
     *
     * @return  the indicator color
     * @since   1.0
     */
    protected String colorIndicatorByDate() {
        String color = 'default'
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
        return color
    }
}
