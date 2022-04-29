/*
 * PurchaseInvoiceService.groovy
 *
 * Copyright (c) 2011-2022, Daniel Ellermann
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

import static java.util.Calendar.YEAR
import grails.transaction.Transactional


@Transactional
class PurchaseInvoiceService {

    //-- Public methods -------------------------

    /**
     * Finds the last year in which purchase invoices have been written.
     *
     * @return  the last year with purchase invoices; -1 if no such elements
     *          exist
     * @since   2.1
     */
    int findYearEnd() {
        PurchaseInvoice invoice = PurchaseInvoice.find(
            'from PurchaseInvoice order by docDate desc',
            [: ],
            [max: 1]
        )

        ((Integer) invoice?.docDate[YEAR]) ?: -1
    }

    /**
     * Finds the first year in which purchase invoices have been written.
     *
     * @return  the first year with purchase invoices; -1 if no such elements
     *          exist
     * @since   2.1
     */
    int findYearStart() {
        PurchaseInvoice invoice = PurchaseInvoice.find(
            'from PurchaseInvoice order by docDate asc',
            [: ],
            [max: 1]
        )

        ((Integer) invoice?.docDate[YEAR]) ?: -1
    }
}
