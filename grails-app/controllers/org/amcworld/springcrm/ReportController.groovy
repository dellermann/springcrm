/*
 * ReportController.groovy
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
     * Displays the sales journal for a particular year and optional month.
     *
     * @return  the model for the view
     */
    def salesJournal() {
        Map<String, Object> model = [: ]

        Calendar cal = Calendar.instance
        int currentYear = cal[YEAR]
        int year = model.activeYear = (params.year as Integer) ?: currentYear
        int month = model.activeMonth = (params.month as Integer) ?: 0
        Calendar start = computeStart(year, month)
        Calendar end = computeEnd(year, month)

        /* find first and last year of all customer accounts */
        int yearStart = model.yearStart = findYearStart()
        int yearEnd = model.yearEnd = Math.max(findYearEnd(), currentYear)

        /* load customer accounts of the given period */
        if (yearStart >= 0 && yearEnd >= 0) {

            /* load invoices and reminders with given sort criterion */
            String sort = params.sort ?: 'number'
            String sortOrder = params.order ?: 'asc'
            Closure crit = {
                between 'docDate', start.time, end.time
                order sort, sortOrder
            }
            List<InvoicingTransaction> l = Invoice.withCriteria crit
            l.addAll Dunning.withCriteria(crit)
            BigDecimal total = l*.total.sum 0.0
            BigDecimal totalPaymentAmount = l*.paymentAmount.sum(0.0) { it ?: 0.0 }

            /* load credit notes with modified sort criterion */
            if (sort == 'dueDatePayment') {
                sort = 'docDate'
            }
            List<CreditMemo> creditMemos = CreditMemo.withCriteria crit
            if (creditMemos) {
                l.addAll creditMemos
                total -= creditMemos*.total.sum 0.0
                totalPaymentAmount -= creditMemos*.paymentAmount.sum { it ?: 0.0 }
            }

            model.invoicingTransactionInstanceList = l
            model.total = total
            model.totalPaymentAmount = totalPaymentAmount
        }

        model
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
        BigDecimal total = 0.0
        BigDecimal totalPaymentAmount = 0.0
        if (organization) {
            l.addAll Invoice.findAllByOrganization(organization, params)
            l.addAll Dunning.findAllByOrganization(organization, params)

            l = l.findAll { it.closingBalance < 0 }
            if (l) {
                total = l*.payable.sum 0.0
                totalPaymentAmount = l*.paymentAmount.sum { it ?: 0.0 }
            }
        }

        [
            organizationInstance: organization,
            invoicingTransactionInstanceList: l,
            total: total, totalPaymentAmount: totalPaymentAmount
        ]
    }

    /**
     * Displays the turnover of the given organization.  If no organization is
     * specified, a general overview page is displayed.
     *
     * @param organization  the given organization; {@code null} to display an
     *                      overview page
     * @return              2.0
     */
    def turnoverReport(Organization organization) {
        Map<String, Object> model = [organizationInstance: organization]

        if (organization) {

            /* store current year to allow month selection */
            int currentYear = model.currentYear = Calendar.instance[YEAR]

            /* find first and last year of all invoices of the organization */
            model.yearStart = findYearStartByOrganization(organization)
            model.yearEnd = Math.max(
                findYearEndByOrganization(organization), currentYear
            )

            List<Invoice> l
            String sort = params.sort ?: 'number'
            String order = params.order ?: 'asc'
            Integer year = model.activeYear = params.year as Integer
            if (year == null) {

                /* display all invoices of the organization */
                l = Invoice.findAllByOrganization(
                    organization, [sort: sort, order: order]
                )
            } else {
                int month = model.activeMonth = params.month as Integer

                /* compute start and end of period */
                Calendar start = computeStart(year, month)
                Calendar end = computeEnd(year, month)

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

                def c = Invoice.createCriteria()
                l = c {
                    eq 'organization', organization
                    between 'docDate', start.time, end.time
                    delegate.order sort, order
                }
            }

            model.invoiceInstanceList = l

            BigDecimal totalProducts = model.totalProducts =
                l*.turnoverProducts.sum 0.0
            BigDecimal totalServices = model.totalServices =
                l*.turnoverServices.sum 0.0
            BigDecimal totalOtherItems = model.totalOtherItems =
                l*.turnoverOtherSalesItems.sum 0.0
            model.total = totalProducts + totalServices + totalOtherItems
        }

        model
    }


    //-- Non-public methods ---------------------

    /**
     * Computes the end of a period within the given year.  The period is
     * either the whole year (if parameter {@code month} is zero) or the given
     * month of that year.
     *
     * @param year  the given year
     * @param month the one-based number of the month; zero to use the last
     *              month of the year
     * @return      the end of the period
     * @since       2.0
     */
    private Calendar computeEnd(int year, int month) {
        Calendar cal = Calendar.instance
        cal[YEAR] = year
        cal[MONTH] = month > 0 ? month - 1 : cal.getActualMaximum(MONTH)
        cal[DATE] = cal.getActualMaximum(DATE)
        cal[HOUR_OF_DAY] = cal.getActualMaximum(HOUR_OF_DAY)
        cal[MINUTE] = cal.getActualMaximum(MINUTE)
        cal[SECOND] = cal.getActualMaximum(SECOND)
        cal[MILLISECOND] = cal.getActualMaximum(MILLISECOND)

        cal
    }

    /**
     * Computes the start of a period within the given year.  The period is
     * either the whole year (if parameter {@code month} is zero) or the given
     * month of that year.
     *
     * @param year  the given year
     * @param month the one-based number of the month; zero to use the first
     *              month of the year
     * @return      the start of the period
     * @since       2.0
     */
    private Calendar computeStart(int year, int month) {
        Calendar cal = Calendar.instance
        cal[YEAR] = year
        cal[MONTH] = month > 0 ? month - 1 : cal.getMinimum(MONTH)
        cal[DATE] = cal.getMinimum(DATE)
        cal[HOUR_OF_DAY] = cal.getMinimum(HOUR_OF_DAY)
        cal[MINUTE] = cal.getMinimum(MINUTE)
        cal[SECOND] = cal.getMinimum(SECOND)
        cal[MILLISECOND] = cal.getMinimum(MILLISECOND)

        cal
    }

    /**
     * Finds the first year in which invoices, credit notes or reminders have
     * been written.
     *
     * @return  the first year with invoices, credit notes or reminders; -1 if
     *          no such elements exist
     * @since   2.0
     */
    private int findYearEnd() {
        List<Integer> years = new ArrayList<Integer>(3)
        List<InvoicingTransaction> l = Invoice.list(
            sort: 'docDate', order: 'desc', max: 1
        )
        if (l) years << l[0].docDate[YEAR]
        l = Dunning.list(sort: 'docDate', order: 'desc', max: 1)
        if (l) years << l[0].docDate[YEAR]
        l = CreditMemo.list(sort: 'docDate', order: 'desc', max: 1)
        if (l) years << l[0].docDate[YEAR]

        years ? years.min() : -1
    }

    /**
     * Finds the last year in which invoices for the given organization have
     * been written.
     *
     * @param organization  the given organization
     * @return              the last year with invoices; -1 if no invoices for
     *                      the given organization have been found
     * @since               2.0
     */
    private int findYearEndByOrganization(Organization organization) {
        List<Invoice> l = Invoice.findAllByOrganization(
            organization, [sort: 'docDate', order: 'desc', max: 1]
        )

        l ? l[0].docDate[YEAR] : -1
    }

    /**
     * Finds the first year in which invoices, credit notes or reminders have
     * been written.
     *
     * @return  the first year with invoices, credit notes or reminders; -1 if
     *          no such elements exist
     * @since   2.0
     */
    private int findYearStart() {
        List<Integer> years = new ArrayList<Integer>(3)
        List<InvoicingTransaction> l = Invoice.list(sort: 'docDate', max: 1)
        if (l) years << l[0].docDate[YEAR]
        l = Dunning.list(sort: 'docDate', max: 1)
        if (l) years << l[0].docDate[YEAR]
        l = CreditMemo.list(sort: 'docDate', max: 1)
        if (l) years << l[0].docDate[YEAR]

        years ? years.max() : -1
    }

    /**
     * Finds the first year in which invoices for the given organization have
     * been written.
     *
     * @param organization  the given organization
     * @return              the first year with invoices; -1 if no invoices for
     *                      the given organization have been found
     * @since               2.0
     */
    private int findYearStartByOrganization(Organization organization) {
        List<Invoice> l = Invoice.findAllByOrganization(
            organization, [sort: 'docDate', max: 1]
        )

        l ? l[0].docDate[YEAR] : -1
    }
}
