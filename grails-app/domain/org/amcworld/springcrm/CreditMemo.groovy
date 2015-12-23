/*
 * CreditMemo.groovy
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
 * The class {@code CreditMemo} represents a credit note.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class CreditMemo extends InvoicingTransaction implements Payable {

    //-- Static fields --------------------------

    static constraints = {
        stage()
        paymentDate nullable: true
        paymentAmount min: 0.0d, widget: 'currency'
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
        'payable', 'paymentStateColor'
    ]

    //-- Fields ---------------------------------

    def userService

    CreditMemoStage stage
    Date paymentDate
    double paymentAmount
    PaymentMethod paymentMethod


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
        i.creditMemos << this
    }

    CreditMemo(Dunning d) {
        super(d)
        type = 'C'
        headerText = ''
        footerText = ''
        dunning = d
        d.creditMemos << this
    }

    CreditMemo(CreditMemo cm) {
        super(cm)
        type = 'C'
        invoice = cm.invoice
        dunning = cm.dunning
    }


    //-- Properties -----------------------------

    /**
     * Gets the balance of this credit note, that is the difference between the
     * credit note total sum and the payment amount.
     *
     * @return  the credit memo balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    double getBalance() {
        int d = userService.numFractionDigitsExt
        total.round(d) - paymentAmount.round(d)
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
    double getClosingBalance() {
        ((invoice ? invoice : dunning)?.closingBalance ?: 0.0d).round(userService.numFractionDigitsExt)
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
        if (closingBalance > 0.0d) {
            color = 'red'
        } else if (closingBalance < 0.0d) {
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
        (balance - closingBalance).round(userService.numFractionDigitsExt)
    }

    /**
     * Gets the payable amount of this credit note.  It is the same as the
     * total value.
     *
     * @return  the payable amount
     * @since   2.0
     */
    double getPayable() {
        total
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
            color = (closingBalance >= 0.0d) ? 'green' : 'red'
        }

        color
    }
}
