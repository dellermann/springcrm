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
}
