/*
 * Payable.groovy
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


/**
 * The class {@code Payable} represents invoicing transactions which are
 * payable, such as invoices, reminders or credit notes.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.0
 */
interface Payable {

    //-- Properties -----------------------------

    /**
     * Gets the date of payment.
     *
     * @return  the date of payment; {@code null} if no payment date has been
     *          specified
     */
    Date getPaymentDate()

    /**
     * Sets the date of payment.
     *
     * @param paymentDate   the date of payment which should be set; {@code null}
     *                      to specify no payment date
     */
    void setPaymentDate(Date paymentDate)

    /**
     * Gets the payment amount.
     *
     * @return  the payment amount
     */
    BigDecimal getPaymentAmount()

    /**
     * Sets the payment amount.
     *
     * @param paymentAmount the payment amount which should be set
     */
    void setPaymentAmount(BigDecimal paymentAmount)

    /**
     * Gets the payment method.
     *
     * @return  the payment method
     */
    PaymentMethod getPaymentMethod()

    /**
     * Sets the payment method.
     *
     * @param paymentMethod the payment method which should be set
     */
    void setPaymentMethod(PaymentMethod paymentMethod)

    /**
     * Gets the total amount.
     *
     * @return  the total
     */
    BigDecimal getTotal()


    //-- Public methods -------------------------

    /**
     * Gets the balance of this transaction without taking any credit notes
     * into account.
     *
     * @return  the invoice balance
     * @since   1.0
     * @see     #getClosingBalance()
     */
    BigDecimal getBalance()

    /**
     * Gets the name of a color indicating the status of the balance of this
     * transaction.  This property is usually used to compute CSS classes in
     * the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor()

    /**
     * Gets the closing balance of this transaction.  The closing balance is
     * calculated from the balance plus the sum of the balances of all credit
     * memos associated to this transaction.  A negative balance indicates a
     * claim to the customer, a positive one indicates a credit of the
     * customer.
     *
     * @return  the closing balance
     * @since   1.0
     * @see     #getBalance()
     */
    BigDecimal getClosingBalance()

    /**
     * Gets the payable amount of this transaction.  It is calculated from the
     * total amount minus the sum of the balances of all credit memos
     * associated to this transaction.  A positive value indicates a claim to
     * the customer, a positive one indicates a credit of the customer.
     *
     * @return  the payable amount
     * @since   2.0
     */
    BigDecimal getPayable()

    /**
     * Gets the name of a color indicating the payment state of this
     * transaction. This property is usually used to compute CSS classes in the
     * views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getPaymentStateColor()
}
