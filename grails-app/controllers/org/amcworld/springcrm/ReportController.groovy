/*
 * ReportController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


class ReportController {

    //-- Public methods -------------------------

    def index() {}

    def salesJournal() {
        def cal = Calendar.instance
        int year = (params.year as Integer) ?: cal[YEAR]
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

        def l = Invoice.list(sort: 'docDate', max: 1)
        int yearStart = l[0].docDate[YEAR]
        l = Invoice.list(sort: 'docDate', order: 'desc', max: 1)
        int yearEnd = l[0].docDate[YEAR]
        l = Dunning.list(sort: 'docDate', max: 1)
        yearStart = Math.min(yearStart, l[0].docDate[YEAR])
        l = Dunning.list(sort: 'docDate', order: 'desc', max: 1)
        yearEnd = Math.max(yearEnd, l[0].docDate[YEAR])
        l = CreditMemo.list(sort: 'docDate', max: 1)
        yearStart = Math.min(yearStart, l[0].docDate[YEAR])
        l = CreditMemo.list(sort: 'docDate', order: 'desc', max: 1)
        yearEnd = Math.max(yearEnd, l[0].docDate[YEAR])

        def query = Invoice.where {
            (docDate >= start.time) && (docDate <= end.time)
        }
        l = query.list(sort: 'number')
        query = Dunning.where {
            (docDate >= start.time) && (docDate <= end.time)
        }
        l.addAll(query.list(sort: 'number'))
        def total = l*.total.sum()
        def totalPaymentAmount = l*.paymentAmount.sum { it ?: 0 }

        query = CreditMemo.where {
            (docDate >= start.time) && (docDate <= end.time)
        }
        def creditMemos = query.list(sort: 'number')
        if (creditMemos) {
            l.addAll(creditMemos)
            total -= creditMemos*.total.sum()
            totalPaymentAmount -= creditMemos*.paymentAmount.sum { it ?: 0 }
        }

        return [
            invoicingTransactionInstanceList: l, currentDate: cal.time,
            currentMonth: month, currentYear: year,
            yearStart: yearStart, yearEnd: yearEnd, total: total,
            totalPaymentAmount: totalPaymentAmount
        ]
    }
}
