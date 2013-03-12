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


class InvoicingTransactionService {

    //-- Instance variables ---------------------

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

        return invoicingTransaction.save(flush: true)
    }
}
