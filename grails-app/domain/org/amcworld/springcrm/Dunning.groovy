/*
 * Dunning.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import static java.math.BigDecimal.ZERO


/**
 * The class {@code Dunning} represents a reminder which belongs to an invoice.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class Dunning extends InvoicingTransaction implements PayableAndDue {

    //-- Static fields --------------------------

    static constraints = {
        level()
        stage()
        dueDatePayment()
        paymentDate nullable: true
        paymentAmount min: ZERO, widget: 'currency'
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
        'payable', 'paymentStateColor'
    ]


    //-- Fields ---------------------------------

    DunningLevel level
    DunningStage stage
    Date dueDatePayment
    Date paymentDate
    BigDecimal paymentAmount = ZERO
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
        if (i.dunnings == null) {
            i.dunnings = []
        }
        i.dunnings << this
    }

    Dunning(Dunning d) {
        super(d)
        type = d.type
        invoice = d.invoice
    }


    //-- Properties -----------------------------

    /**
     * Gets the balance of this reminder, that is the difference between the
     * payment amount and the reminder total sum.
     * <p>
     * Note that the reminder balance does not take any credit memos into
     * account.  Use method {@code getClosingBalance} for it.
     *
     * @return  the reminder balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance() {
        paymentAmount - total
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * reminder.  This property is usually used to compute CSS classes in the
     * views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor() {
        String color = 'default'
        if (closingBalance < ZERO) {
            color = 'red'
        } else if (closingBalance > ZERO) {
            color = 'green'
        }

        color
    }

    /**
     * Gets the closing balance of this reminder.  The closing balance is
     * calculated from the reminder balance plus the sum of the balances of
     * all credit memos associated to this reminder.  A negative balance
     * indicates a claim to the customer, a positive one indicates a credit of
     * the customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     */
    BigDecimal getClosingBalance() {
        balance + (creditMemos*.balance?.sum() ?: ZERO)
    }

    /**
     * Gets the modified closing balance which is needed in views to compute
     * the still unpaid value dynamically.
     *
     * @return  the modified closing balance
     * @since   1.3
     */
    BigDecimal getModifiedClosingBalance() {
        closingBalance - balance
    }

    /**
     * Gets the payable amount of this reminder.  It is calculated from the
     * total amount minus the sum of the balances of all credit memos
     * associated to this reminder.  A positive value indicates a claim to the
     * customer, a positive one indicates a credit of the customer.
     *
     * @return  the payable amount
     * @since   2.0
     */
    BigDecimal getPayable() {
        total - (creditMemos*.balance?.sum() ?: ZERO)
    }

    /**
     * Sets the payment amount of this reminder.
     *
     * @param paymentAmount the payment amount that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount == null ? ZERO : paymentAmount
    }

    /**
     * Gets the name of a color indicating the payment state of this reminder.
     * This property is usually used to compute CSS classes in the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getPaymentStateColor() {
        String color = 'default'
        switch (stage?.id) {
        case 2206:                      // cancelled
            color = closingBalance >= ZERO ? 'green' : colorIndicatorByDate()
            break
        case 2205:                      // booked out
            color = 'black'
            break
        case 2204:                      // cashing
            color = 'blue'
            break
        case 2203:                      // paid
            color = closingBalance >= ZERO ? 'green' : colorIndicatorByDate()
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
    private String colorIndicatorByDate() {
        String color = 'default'
        if (dueDatePayment != null) {
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
        }

        color
    }
}
