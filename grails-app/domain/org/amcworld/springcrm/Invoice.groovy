/*
 * Invoice.groovy
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

import groovy.transform.CompileStatic


/**
 * The class {@code Invoice} represents an invoice.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class Invoice extends InvoicingTransaction implements PayableAndDue {

    //-- Static fields --------------------------

    static constraints = {
        stage()
        dueDatePayment()
        paymentDate nullable: true
        paymentAmount min: 0.0d, widget: 'currency'
        paymentMethod nullable: true
        quote nullable: true
        salesOrder nullable: true
    }
    static belongsTo = [quote: Quote, salesOrder: SalesOrder]
    static hasMany = [creditMemos: CreditMemo, dunnings: Dunning]
    static mapping = {
        creditMemos lazy: false
        stage column: 'invoice_stage_id'
    }
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'modifiedClosingBalance',
        'payable', 'paymentStateColor', 'turnover', 'turnoverOtherSalesItems',
        'turnoverProducts', 'turnoverServices'
    ]


    //-- Fields ---------------------------------

    def userService

    InvoiceStage stage
    Date dueDatePayment
    Date paymentDate
    double paymentAmount
    PaymentMethod paymentMethod


    //-- Constructors ---------------------------

    Invoice() {
        super()
        type = 'I'
    }

    Invoice(Quote q) {
        super(q)
        type = 'I'
        quote = q
    }

    Invoice(SalesOrder so) {
        super(so)
        type = 'I'
        salesOrder = so
    }

    Invoice(Invoice i) {
        super(i)
        type = 'I'
        quote = i.quote
        salesOrder = i.salesOrder
    }


    //-- Properties -----------------------------

    /**
     * Gets the balance of this invoice, that is the difference between the
     * payment amount and the invoice total sum.
     * <p>
     * Note that the invoice balance does not take any credit memos into
     * account.  Use method {@code getClosingBalance} for it.
     *
     * @return  the invoice balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    double getBalance() {
        int d = userService.numFractionDigitsExt
        paymentAmount.round(d) - total.round(d)
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * invoice.  This property is usually used to compute CSS classes in the
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
     * Gets the closing balance of this invoice.  The closing balance is
     * calculated from the invoice balance plus the sum of the balances of
     * all credit memos associated to this invoice.  A negative balance
     * indicates a claim to the customer, a positive one indicates a credit of
     * the customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     */
    double getClosingBalance() {
        (balance + (creditMemos ? creditMemos*.balance.sum(0) : 0.0d))
            .round(userService.numFractionDigitsExt)
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
     * Gets the payable amount of this invoice.  It is calculated from the
     * total amount minus the sum of the balances of all credit memos
     * associated to this invoice.  A positive value indicates a claim to the
     * customer, a positive one indicates a credit of the customer.
     *
     * @return  the payable amount
     * @since   2.0
     */
    double getPayable() {
        (total - (creditMemos ? creditMemos*.balance.sum(0) : 0.0d))
            .round(userService.numFractionDigitsExt)
    }

    /**
     * Gets the name of a color indicating the payment state of this invoice.
     * This property is usually used to compute CSS classes in the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getPaymentStateColor() {
        String color = 'default'
        switch (stage?.id) {
        case 907:                       // cancelled
            color = (closingBalance >= 0) ? 'green' : colorIndicatorByDate()
            break
        case 906:                       // booked out
            color = 'black'
            break
        case 905:                       // cashing
            color = 'blue'
            break
        case 904:                       // reminded
            color = 'purple'
            break
        case 903:                       // paid
            color = (closingBalance >= 0) ? 'green' : colorIndicatorByDate()
            break
        case 902:                       // delivered
            color = colorIndicatorByDate()
            break
        }

        color
    }

    /**
     * Gets the turnover of this invoice, that is the total minus the total of
     * all credit memos associated to this invoice.
     *
     * @return  the turnover
     * @since   2.0
     */
    double getTurnover() {
        turnoverProducts + turnoverServices + turnoverOtherSalesItems
    }

    /**
     * Gets the turnover of all items of this invoice which are neither
     * products nor services.
     *
     * @return  the turnover of all other items
     * @since   2.0
     */
    double getTurnoverOtherSalesItems() {
        double value = itemsOfType(null)*.total.sum 0
        if (creditMemos) {
            value -= creditMemos*.turnoverOtherSalesItems.sum 0
        }

        value
    }

    /**
     * Gets the turnover of all products of this invoice.
     *
     * @return  the turnover of all products
     * @since   2.0
     */
    double getTurnoverProducts() {
        double value = itemsOfType('P')*.total.sum 0
        if (creditMemos) {
            value -= creditMemos*.turnoverProducts.sum 0
        }

        value
    }

    /**
     * Gets the turnover of all services of this invoice.
     *
     * @return  the turnover of all services
     * @since   2.0
     */
    double getTurnoverServices() {
        double value = itemsOfType('S')*.total.sum 0
        if (creditMemos) {
            value -= creditMemos*.turnoverServices.sum 0
        }

        value
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
