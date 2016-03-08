/*
 * PayableAndDue.groovy
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
 * The class {@code PayableAndDue} represents invoicing transactions which are
 * payable and have a due date such as invoices or reminders.
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @since   1.0
 */
interface PayableAndDue extends Payable {

    //-- Properties -----------------------------

    /**
     * Gets the due date of the payment.
     *
     * @return  the due date; {@code null} if no due date has been specified
     */
    Date getDueDatePayment()

    /**
     * Sets the due date of the payment.
     *
     * @param dueDatePayment    the due date; {@code null} to specify no date
     */
    void setDueDatePayment(Date dueDatePayment)
}
