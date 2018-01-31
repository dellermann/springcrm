/*
 * InvoicingTransactionService.groovy
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

import static java.util.Calendar.YEAR

import grails.core.GrailsApplication
import org.amcworld.springcrm.xml.InvoicingTransactionXML
import org.amcworld.springcrm.xml.InvoicingTransactionXMLFactory


/**
 * The class {@code InvoicingTransactionService} contains methods to work with
 * invoicing transactions such as quotes, sales order, invoices etc.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.2
 */
class InvoicingTransactionService {

    //-- Fields ---------------------------------

    ConfigService configService
    GrailsApplication grailsApplication
    InvoicingTransactionXMLFactory invoicingTransactionXMLFactory
    UserService userService
    UserSettingService userSettingService


    //-- Public methods -------------------------

    /**
     * Computes a list of turnover of all organizations optionally filtered by
     * the given year.
     *
     * @param year  the given year; if zero the total turnover is computed
     * @param sort  the property to sort by; must be either {@code turnover} or
     *              {@code organization}
     * @param order the sort order; either {@code asc} or {@code desc}
     * @return      a sorted map containing the organizations and their
     *              turnover
     * @since 2.1
     */
    Map<Organization, BigDecimal> computeTurnoverList(int year = 0,
                                                      String sort = 'turnover',
                                                      String order = 'desc')
    {
        if (sort != 'turnover') {
            sort = 'ii.invoicingTransaction.organization.name'
        }
        String sql = """
            select
                ii.invoicingTransaction.organization, 
                sum(ii.quantity * ii.unitPrice) - ifnull((
                    select
                        sum(ci.quantity * ci.unitPrice)
                    from
                        InvoicingItem ci
                    where
                        ci.invoicingTransaction.type = 'C' and
                        ci.invoicingTransaction.organization = 
                            ii.invoicingTransaction.organization
                        ${getTurnoverWhere(year, 'ci.invoicingTransaction', 'and ')}
                    group by
                        ci.invoicingTransaction.organization
                ), 0) as turnover
            from InvoicingItem ii
            where
                ii.invoicingTransaction.type = 'I'
                ${getTurnoverWhere(year, 'ii.invoicingTransaction', 'and ')}
            group by ii.invoicingTransaction.organization
            order by ${sort} ${order}
            """
        Map<String, Object> args = year > 0 ? [year: year] : [: ]
        List<Object[]> l = Invoice.executeQuery(sql, args) as List<Object[]>

        Map<Organization, BigDecimal> res = new LinkedHashMap<>(l.size())
        for (Object[] item : l) {
            res[(Organization) item[0]] = (BigDecimal) item[1]
        }

        res
    }

    /**
     * Finds all unpaid bills filtered and ordered using the settings of the
     * currently logged in user.
     *
     * @return  a list of unpaid bills
     */
    List<Invoice> findUnpaidBills() {
        User user = userService.currentUser
        String sort =
            userSettingService.getString(user, 'unpaidBillsSort', 'docDate')
        String ord =
            userSettingService.getString(user, 'unpaidBillsOrder', 'desc')
        int max = userSettingService.getInteger(user, 'unpaidBillsMax', 0i)

        int precision = configService.getInteger('numFractionDigitsExt', 2i)
        String setting =
            userSettingService.getString(user, 'unpaidBillsMinimum')
        BigDecimal minimum = setting ? new BigDecimal(setting)
            : BigDecimal.ONE.movePointLeft(precision)

        List<Object[]> l = Invoice.executeQuery(
            """
            select
                i, 
                -(round(i.paymentAmount, :prec) - round(i.total, :prec)) 
                    as stillUnpaid
            from Invoice as i
            where i.stage.id between :stageStart and :stageEnd
                and round(i.paymentAmount, :prec) - round(i.total, :prec) 
                    <= -:minimum
                and i.dueDatePayment <= current_timestamp()
            order by ${sort} ${ord}
            """.toString(),
            [
                minimum: minimum,
                prec: precision,
                stageStart: 902L,       // sent..
                stageEnd: 905L,         // ..collection
            ],
            [max: max]
        ) as List<Object[]>
        List<Invoice> invoiceInstanceList = new ArrayList<Invoice>(l.size())
        for (Object[] item : l) {
            invoiceInstanceList.add((Invoice) item[0])
        }

        invoiceInstanceList
    }

    /**
     * Finds the first year in which invoices, credit notes or reminders have
     * been written.
     *
     * @return  the first year with invoices, credit notes or reminders; -1 if
     *          no such elements exist
     * @since   2.0
     */
    int findYearEnd() {
        InvoicingTransaction transaction = InvoicingTransaction.find(
            '''
            from InvoicingTransaction as i 
            where i.type in ('I', 'D', 'C') 
            order by docDate desc
            ''',
            [: ],
            [max: 1]
        )

        ((Integer) transaction?.docDate[YEAR]) ?: -1
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
    int findYearEndByOrganization(Organization organization) {
        Invoice invoice = Invoice.findByOrganization(
            organization, [sort: 'docDate', order: 'desc', max: 1]
        )

        invoice?.docDate[YEAR] ?: -1
    }

    /**
     * Finds the first year in which invoices, credit notes or reminders have
     * been written.
     *
     * @return  the first year with invoices, credit notes or reminders; -1 if
     *          no such elements exist
     * @since   2.0
     */
    int findYearStart() {
        InvoicingTransaction transaction = InvoicingTransaction.find(
            '''
            from InvoicingTransaction as i 
            where i.type in ('I', 'D', 'C') 
            order by docDate asc
            ''',
            [: ],
            [max: 1]
        )

        ((Integer) transaction?.docDate[YEAR]) ?: -1
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
    int findYearStartByOrganization(Organization organization) {
        Invoice invoice = Invoice.findByOrganization(
            organization, [sort: 'docDate', max: 1]
        )

        invoice?.docDate[YEAR] ?: -1
    }

    /**
     * Generates the XML data structure which is used in XSL transformation
     * using the given invoicing transaction.
     *
     * @param transaction       the given invoicing transaction
     * @param user              the currently logged in user
     * @param duplicate         whether or not the data structure of a
     *                          duplicate document is to create; if
     *                          {@code true} the XSL transformation renders a
     *                          watermark
     * @param additionalData    any additional data which are added to the
     *                          generated data structure; all entries in this
     *                          table overwrite possible existing entries in
     *                          the generated data structure
     * @return                  the generated XML data structure as string
     * @since                   1.4
     */
    String generateXML(InvoicingTransaction transaction, User user,
                       boolean duplicate = false, Map additionalData = null)
    {
        InvoicingTransactionXML xml =
            invoicingTransactionXMLFactory.newConverter(transaction, user)
        xml.duplicate = duplicate
        if (additionalData) {
            xml << additionalData
        }

        xml.toString()
    }

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
    boolean save(InvoicingTransaction invoicingTransaction, params) {
        boolean create = !invoicingTransaction.id
        if (params.autoNumber) {
            params.number = invoicingTransaction.number
        }
        invoicingTransaction.properties = params
//        invoicingTransaction.items?.retainAll { it != null }

        /*
         * XXX  This code is necessary because the default implementation
         *      in Grails does not work.  The above lines worked in Grails
         *      2.0.0.  Now, either data binding or saving does not work
         *      correctly if items were deleted and gaps in the indices
         *      occurred (e. g. 0, 1, null, null, 4) or the items were
         *      re-ordered.  Then I observed cluttering in saved data
         *      columns.
         *      The following lines do not make me happy but they work.
         *      In future, this problem hopefully will be fixed in Grails
         *      so we can remove these lines.
         */
        if (invoicingTransaction.items == null) {
            invoicingTransaction.items = []
        } else {
            invoicingTransaction.items.clear()
        }
        boolean itemErrors = false
        for (int i = 0; params."items[${i}]"; i++) {
            if (create || params."items[${i}]".id != 'null') {
                InvoicingItem item = params."items[${i}]"
                itemErrors |= item.hasErrors()
                invoicingTransaction.addToItems item
            }
        }

        if (itemErrors || !invoicingTransaction.validate()) {
            invoicingTransaction.discard()
            return false
        }

        invoicingTransaction.save flush: true, failOnError: true
    }


    //-- Non-public methods ---------------------

    /**
     * Computes a WHERE clause for method {@code computeTurnoverList}.
     *
     * @param year      the given year as criterion
     * @param alias     the name of the SQL alias
     * @param prefix    any string which is used as prefix, such as {@code and}
     * @return          the computed WHERE clause
     * @see #computeTurnoverList(int)
     */
    private static String getTurnoverWhere(int year, String alias,
                                           String prefix)
    {
        year > 0 ? "${prefix}year(${alias}.docDate) = :year" : ''
    }
}
