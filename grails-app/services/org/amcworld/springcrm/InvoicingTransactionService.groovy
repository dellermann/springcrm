/*
 * InvoicingTransactionService.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * The class {@code InvoicingTransactionService} contains methods to work with
 * invoicing transactions such as quotes, sales order, invoices etc.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.2
 */
class InvoicingTransactionService {

    //-- Instance variables ---------------------

    /**
     * Populates the given invoicing transaction with the given request
     * parameters, validates, and saves it.
     *
     * @param invoicingTransaction  the invoicing transaction that should be
     *                              saved
     * @param params                the request parameters that should be used
     *                              to populate the invoicing transaction
     * @return                      {@code true} if validating and saving was
     *                              successful; {@code false} otherwise
     */
    boolean saveInvoicingTransaction(InvoicingTransaction invoicingTransaction,
                                     def params)
    {
        if (params.autoNumber) {
            params.number = invoicingTransaction.number
        }
        invoicingTransaction.properties = params

        if (invoicingTransaction.items == null) {
            invoicingTransaction.items = []
        } else {
            invoicingTransaction.items.clear()
        }
        for (int i = 0; params."items[${i}]"; i++) {
            invoicingTransaction.addToItems params."items[${i}]"
        }

        if (!invoicingTransaction.validate()) {
            invoicingTransaction.discard()
            return false
        }

        invoicingTransaction.save flush: true
    }
}
