/*
 * ReportController.groovy
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

import static java.util.Calendar.*


/**
 * The class {@code ReportController} produces several reports such as sales
 * journals.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class ReportController {

    //-- Public methods -------------------------

    /**
     * Displayes the sales journal for a particular year and optional month.
     *
     * @return  the model for the view
     */
    def salesJournal() {
        def cal = Calendar.instance
        def currentYear = cal[YEAR]
        int year = (params.year as Integer) ?: currentYear
        cal[YEAR] = year
        int month = cal[MONTH] + 1
        if (params.month) {
            month = params.month as Integer
            if (month > 0) {
                cal[MONTH] = month - 1
            }
        }

        def start = cal.updated(
            date: cal.getMinimum(DATE),
            hourOfDay: cal.getMinimum(HOUR_OF_DAY),
            minute: cal.getMinimum(MINUTE),
            second: cal.getMinimum(SECOND)
        )
        def end = cal.updated(
            date: cal.getActualMaximum(DATE),
            hourOfDay: cal.getActualMaximum(HOUR_OF_DAY),
            minute: cal.getActualMaximum(MINUTE),
            second: cal.getActualMaximum(SECOND)
        )
        if (month == 0) {
            start[MONTH] = cal.getMinimum(MONTH)
            end[MONTH] = cal.getActualMaximum(MONTH)
        }

        /*
         * Issue #77: the selection of records below doesn't work for dates at
         * 1st of month, e. g. 2015-12-01.  Records with docDate 2015-12-01
         * 00:00:00 are not selected by criterion docDate >= start.time if
         * start.time = '2015-12-01 00:00:00'.  Maybe this is a bug in mySQL
         * java driver.
         *
         * For now, we subtract one second from start timestamp which causes
         * the above criterion to work.
         */
        start.add SECOND, -1

        /* find first and last year of all customer accounts */
        int yearStart = -1
        int yearEnd = -1
        List<InvoicingTransaction> l = Invoice.list(sort: 'docDate', max: 1)
        if (!l.empty) yearStart = l[0].docDate[YEAR]
        l = Invoice.list(sort: 'docDate', order: 'desc', max: 1)
        if (!l.empty) yearEnd = l[0].docDate[YEAR]
        l = Dunning.list(sort: 'docDate', max: 1)
        if (!l.empty) yearStart = Math.min(yearStart, l[0].docDate[YEAR])
        l = Dunning.list(sort: 'docDate', order: 'desc', max: 1)
        if (!l.empty) yearEnd = Math.max(yearEnd, l[0].docDate[YEAR])
        l = CreditMemo.list(sort: 'docDate', max: 1)
        if (!l.empty) yearStart = Math.min(yearStart, l[0].docDate[YEAR])
        l = CreditMemo.list(sort: 'docDate', order: 'desc', max: 1)
        if (!l.empty) yearEnd = Math.max(yearEnd, l[0].docDate[YEAR])
        yearEnd = Math.max(yearEnd, currentYear)

        /* load customer accounts of the given period */
        def total = 0.0
        def totalPaymentAmount = 0.0
        if ((yearStart < 0) || (yearEnd < 0)) {
            l = null
        } else {
            def query = Invoice.where {
                (docDate >= start.time) && (docDate <= end.time)
            }
            l = query.list(sort: 'number')
            query = Dunning.where {
                (docDate >= start.time) && (docDate <= end.time)
            }
            l.addAll query.list(sort: 'number')
            total = l*.total.sum()
            totalPaymentAmount = l*.paymentAmount.sum { it ?: 0 }

            query = CreditMemo.where {
                (docDate >= start.time) && (docDate <= end.time)
            }
            def creditMemos = query.list(sort: 'number')
            if (creditMemos) {
                l.addAll creditMemos
                total -= creditMemos*.total.sum()
                totalPaymentAmount -= creditMemos*.paymentAmount.sum { it ?: 0 }
            }
        }

        [
            invoicingTransactionInstanceList: l, currentDate: cal.time,
            currentMonth: month, currentYear: year,
            yearStart: yearStart, yearEnd: yearEnd, total: total,
            totalPaymentAmount: totalPaymentAmount
        ]
    }

    /**
     * Displays a list of outstanding items of the given organization.  If no
     * organization is specified, a general overview page is displayed.
     *
     * @param organization  the given organization; {@code null} to display an
     *                      overview page
     * @return              the model for the view
     * @since               2.0
     */
    def outstandingItems(Organization organization) {
        List<PayableAndDue> l = []
        double total = 0
        double totalPaymentAmount = 0
        if (organization) {
            l.addAll Invoice.findAllByOrganization(organization, params)
            l.addAll Dunning.findAllByOrganization(organization, params)

            l = l.findAll { it.closingBalance < 0 }
            if (l) {
                total = l*.payable.sum()
                totalPaymentAmount = l*.paymentAmount.sum { it ?: 0 }
            }
        }

        [
            organizationInstance: organization,
            invoicingTransactionInstanceList: l,
            total: total, totalPaymentAmount: totalPaymentAmount
        ]
    }
}
