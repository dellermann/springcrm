/*
 * Dunning.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
 * @author  Daniel Ellermann
 * @version 2.0
 */
class Dunning extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
        level()
        stage()
        dueDatePayment()
        paymentDate nullable: true
        paymentAmount min: 0.0d, widget: 'currency'
        paymentMethod nullable: true
        invoice()
    }
    static belongsTo = [invoice: Invoice]
    static hasMany = [creditMemos: CreditMemo]
    static mapping = {
        stage column: 'dunning_stage_id'
    }
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'modifiedClosingBalance',
        'paymentStateColor'
    ]


    //-- Instance variables ---------------------

    def userService

    DunningLevel level
    DunningStage stage
    Date dueDatePayment
    Date paymentDate
    double paymentAmount
    PaymentMethod paymentMethod


    //-- Constructors ---------------------------

    Dunning() {
        type = 'D'
    }

    Dunning(Invoice i) {
        super(i)
        type = 'D'
        invoice = i
        headerText = ''
        footerText = ''
    }

    Dunning(Dunning d) {
        super(d)
        type = 'D'
        level = d.level
        stage = d.stage
        dueDatePayment = d.dueDatePayment
        paymentDate = d.paymentDate
        paymentAmount = d.paymentAmount
        paymentMethod = d.paymentMethod
        invoice = d.invoice
    }


    //-- Properties -----------------------------

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
    double getBalance() {
        int d = userService.numFractionDigitsExt
        paymentAmount.round(d) - total.round(d)
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
    double getClosingBalance() {
        (balance + (creditMemos ? creditMemos*.balance.sum() : 0.0d)).round(userService.numFractionDigitsExt)
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
        if (closingBalance < 0.0d) {
            color = 'red'
        } else if (closingBalance > 0.0d) {
            color = 'green'
        }
        color
    }

    /**
     * Gets the modified closing balance which is needed in views to compute
     * the still unpaid value dynamically.
     *
     * @return  the modified closing balance
     * @since   1.3
     */
    double getModifiedClosingBalance() {
        (closingBalance - balance).round(userService.numFractionDigitsExt)
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
        color
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
        color
    }
}
