/*
 * Invoice.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import com.mongodb.client.model.Filters
import java.math.RoundingMode


/**
 * The class {@code Invoice} represents an invoice.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class Invoice extends InvoicingTransaction implements PayableAndDue {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'subject', 'billingAddr.street', 'billingAddr.poBox',
        'billingAddr.postalCode', 'billingAddr.location', 'billingAddr.state',
        'billingAddr.country', 'shippingAddr.street', 'shippingAddr.poBox',
        'shippingAddr.postalCode', 'shippingAddr.location',
        'shippingAddr.state', 'shippingAddr.country', 'headerText',
        'items.*name', 'items.*description', 'footerText', 'notes'
    ].asImmutable()
    public static final String TYPE = 'I'


    //-- Class fields ---------------------------

    static constraints = {
        paymentDate nullable: true
        paymentAmount min: ZERO, scale: 6, widget: 'currency'
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
    static nextNumberFilters = [Filters.eq('type', TYPE)]
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'modifiedClosingBalance',
        'payable', 'paymentStateColor', 'turnover', 'turnoverOtherSalesItems',
        'turnoverProducts', 'turnoverWorks'
    ]


    //-- Fields ---------------------------------

    def userService

    /**
     * The due date of payment.
     */
    Date dueDatePayment

    /**
     * The amount of payment.
     */
    BigDecimal paymentAmount = ZERO

    /**
     * The date of payment.
     */
    Date paymentDate

    /**
     * The payment method.
     */
    PaymentMethod paymentMethod

    /**
     * The stage of this invoice.
     */
    InvoiceStage stage


    //-- Constructors ---------------------------

    /**
     * Creates an empty invoice.
     */
    Invoice() {
        super()
        type = TYPE
    }

    /**
     * Create an invoice using the data of the given one (copy constructor).
     *
     * @param i the given invoice
     */
    Invoice(Invoice i) {
        super(i)
        userService = i.userService
        type = i.type
        quote = i.quote
        salesOrder = i.salesOrder
    }

    /**
     * Creates an invoice associated to the given quote.
     *
     * @param q the given quote
     */
    Invoice(Quote q) {
        super(q)
        type = TYPE
        quote = q
        if (q.invoices == null) {
            q.invoices = []
        }
        q.invoices << this
    }

    /**
     * Creates an invoice associated to the given sales order.
     *
     * @param so    the given sales order
     */
    Invoice(SalesOrder so) {
        super(so)
        type = TYPE
        salesOrder = so
        if (so.invoices == null) {
            so.invoices = []
        }
        so.invoices << this
    }


    //-- Properties -----------------------------

    /**
     * Gets the balance of this invoice, that is the difference between the
     * payment amount and the invoice total sum.  A positive value indicates
     * debts to the client, a negative value indicates debts owned by you.
     * <p>
     * Note that the invoice balance does not take any credit memos into
     * account.  Use method {@code getClosingBalance} for it.
     *
     * @return  the invoice balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance() {
        int n = userService.numFractionDigitsExt
        RoundingMode rm = RoundingMode.HALF_UP

        paymentAmount.setScale(n, rm) - total.setScale(n, rm)
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
        if (closingBalance < ZERO) {
            color = 'red'
        } else if (closingBalance > ZERO) {
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
     * Gets the payable amount of this invoice.  It is calculated from the
     * total amount minus the sum of the balances of all credit memos
     * associated to this invoice.  A positive value indicates a claim to the
     * customer, a positive one indicates a credit of the customer.
     *
     * @return  the payable amount
     * @since   2.0
     */
    BigDecimal getPayable() {
        total - (creditMemos*.balance?.sum() ?: ZERO)
    }

    /**
     * Sets the payment amount of this invoice.
     *
     * @param paymentAmount the payment amount that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount == null ? ZERO : paymentAmount
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
        case 903:                       // paid
            color = (closingBalance <=> ZERO) >= 0 \
                ? 'green'
                : colorIndicatorByDate()
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
    BigDecimal getTurnover() {
        turnoverProducts + turnoverWorks + turnoverOtherSalesItems
    }

    /**
     * Gets the turnover of all items of this invoice which are neither
     * products nor works.
     *
     * @return  the turnover of all other items
     * @since   2.0
     */
    BigDecimal getTurnoverOtherSalesItems() {
        BigDecimal value = itemsOfType(null)*.totalNet.sum ZERO
        if (creditMemos) {
            value -= creditMemos*.turnoverOtherSalesItems.sum ZERO
        }

        value
    }

    /**
     * Gets the turnover of all products of this invoice.
     *
     * @return  the turnover of all products
     * @since   2.0
     */
    BigDecimal getTurnoverProducts() {
        BigDecimal value = itemsOfType('P')*.totalNet.sum ZERO
        if (creditMemos) {
            value -= creditMemos*.turnoverProducts.sum ZERO
        }

        value
    }

    /**
     * Gets the turnover of all works of this invoice.
     *
     * @return  the turnover of all works
     * @since   2.0
     */
    BigDecimal getTurnoverWorks() {
        BigDecimal value = itemsOfType('S')*.totalNet.sum ZERO
        if (creditMemos) {
            value -= creditMemos*.turnoverWorks.sum ZERO
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
