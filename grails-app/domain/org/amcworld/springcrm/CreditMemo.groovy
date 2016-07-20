/*
 * CreditMemo.groovy
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

import java.math.RoundingMode


/**
 * The class {@code CreditMemo} represents a credit note.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class CreditMemo extends InvoicingTransaction implements Payable {

    //-- Class fields ---------------------------

    static constraints = {
        paymentDate nullable: true
        paymentAmount min: ZERO, scale: 6, widget: 'currency'
        paymentMethod nullable: true
        invoice nullable: true
        dunning nullable: true
    }
    static belongsTo = [invoice: Invoice, dunning: Dunning]
    static mapping = {
        stage column: 'credit_memo_stage_id'
    }
    static transients = [
        'balance', 'balanceColor', 'closingBalance', 'modifiedClosingBalance',
        'payable', 'paymentStateColor', 'turnoverOtherSalesItems',
        'turnoverProducts', 'turnoverWorks'
    ]

    //-- Fields ---------------------------------

    def userService

    /**
     * The stage of this credit note.
     */
    CreditMemoStage stage

    /**
     * The date of payment.
     */
    Date paymentDate

    /**
     * The payment amount.
     */
    BigDecimal paymentAmount = ZERO

    /**
     * The payment method.
     */
    PaymentMethod paymentMethod


    //-- Constructors ---------------------------

    /**
     * Creates an empty credit note.
     */
    CreditMemo() {
        super()
        type = 'C'
    }

    /**
     * Creates a credit note associated to the given invoice.
     *
     * @param i the given invoice
     */
    CreditMemo(Invoice i) {
        super(i)
        type = 'C'
        headerText = ''
        footerText = ''
        invoice = i
        if (i.creditMemos == null) {
            i.creditMemos = []
        }
        i.creditMemos << this
    }

    /**
     * Creates a credit note associated to the given reminder.
     *
     * @param d the given reminder
     */
    CreditMemo(Dunning d) {
        super(d)
        type = 'C'
        headerText = ''
        footerText = ''
        dunning = d
        if (d.creditMemos == null) {
            d.creditMemos = []
        }
        d.creditMemos << this
    }

    /**
     * Creates a credit note using the data of the given one (copy
     * constructor).
     *
     * @param cm    the given credit note
     */
    CreditMemo(CreditMemo cm) {
        super(cm)
        type = cm.type
        invoice = cm.invoice
        dunning = cm.dunning
    }


    //-- Properties -----------------------------

    /**
     * Gets the balance of this credit note, that is the difference between the
     * credit note total sum and the payment amount.  A positive value indicates
     * debts to the client, a negative value indicates debts owned by you.
     *
     * @return  the credit memo balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance() {
        (total - paymentAmount).setScale(
                userService.numFractionDigitsExt, RoundingMode.HALF_UP
            )
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * credit note.  This property is usually used to compute CSS classes in
     * the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor() {
        String color = 'default'
        if (closingBalance > ZERO) {
            color = 'red'
        } else if (closingBalance < ZERO) {
            color = 'green'
        }

        color
    }

    /**
     * Gets the closing balance of the invoice or reminder associated to this
     * credit note.  The closing balance is calculated from the balance of the
     * associated invoice or reminder minus the sum of the balances of all its
     * credit notes.  A negative balance indicates a claim to the customer, a
     * positive one indicates a credit of the customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     * @see     Invoice#getClosingBalance()
     * @see     Dunning#getClosingBalance()
     */
    BigDecimal getClosingBalance() {
        (invoice ? invoice : dunning)?.closingBalance ?: ZERO
    }

    /**
     * Gets the modified closing balance which is needed in views to compute
     * the still unpaid value dynamically.
     *
     * @return  the modified closing balance
     * @since   1.3
     */
    BigDecimal getModifiedClosingBalance() {
        balance - closingBalance
    }

    /**
     * Gets the payable amount of this credit note.  It is the same as the
     * total value.
     *
     * @return  the payable amount
     * @since   2.0
     */
    BigDecimal getPayable() {
        total
    }

    /**
     * Sets the payment amount of this credit note.
     *
     * @param paymentAmount the payment amount that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount == null ? ZERO : paymentAmount
    }

    /**
     * Gets the name of a color indicating the payment state of this credit
     * note.
     *
     * @return  the indicator color
     */
    String getPaymentStateColor() {
        String color = 'default'
        def id = stage?.id ?: 0
        if ((id >= 2502) && (id <= 2504)) {     // cancelled, paid, delivered
            color = (closingBalance >= ZERO) ? 'green' : 'red'
        }

        color
    }

    /**
     * Gets the turnover of all items of this credit memo which are neither
     * products nor works.
     *
     * @return  the turnover of all other items
     * @since   2.0
     */
    BigDecimal getTurnoverOtherSalesItems() {
        itemsOfType(null)*.total.sum ZERO
    }

    /**
     * Gets the turnover of all products of this credit memo.
     *
     * @return  the turnover of all products
     * @since   2.0
     */
    BigDecimal getTurnoverProducts() {
        itemsOfType('P')*.total.sum ZERO
    }

    /**
     * Gets the turnover of all works of this credit memo.
     *
     * @return  the turnover of all works
     * @since   2.0
     */
    BigDecimal getTurnoverWorks() {
        itemsOfType('S')*.total.sum ZERO
    }
}
