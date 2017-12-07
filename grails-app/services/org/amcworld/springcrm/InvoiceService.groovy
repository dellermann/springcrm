/*
 * InvoiceService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.gorm.services.Service


/**
 * The interface {@code InvoiceService} represents a service which allows
 * access to invoice.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface InvoiceService {

    //-- Public methods -------------------------

    /**
     * Counts all invoices.
     *
     * @return  the number of all invoices
     */
    int count()

    /**
     * Counts the invoices which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @return          the number of invoices
     */
    int countByQuote(Quote quote)

    /**
     * Counts the invoices which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of invoices
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the invoices which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of invoices
     */
    int countByPerson(Person person)

    /**
     * Counts the invoices which belong to the given sales order.
     *
     * @param salesOrder    the given sales order
     * @return              the number of invoices
     */
    int countBySalesOrder(SalesOrder salesOrder)

    /**
     * Counts the invoices with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of invoices
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the invoice with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the invoices which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of invoices
     */
    List<Invoice> findAllByQuote(Quote quote, Map args)

    /**
     * Finds the invoices which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order
     *                      etc.)
     * @return              a list of invoices
     */
    List<Invoice> findAllByOrganization(Organization organization,
                                        Map args)

    /**
     * Finds the invoices which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of invoices
     */
    List<Invoice> findAllByPerson(Person person, Map args)

    /**
     * Finds the invoices which belong to the given sales order.
     *
     * @param salesOrder    the given sales order
     * @param args          any arguments used for retrieval (sort, order
     *                      etc.)
     * @return              a list of invoices
     */
    List<Invoice> findAllBySalesOrder(SalesOrder salesOrder, Map args)

    /**
     * Finds the invoices with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of invoices
     */
    List<Invoice> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the invoice with the given ID.
     *
     * @param id    the given ID
     * @return      the invoice or {@code null} if no such invoice with the
     *              given ID exists
     */
    Invoice get(Serializable id)

    /**
     * Gets a list of all invoices.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of invoices
     */
    List<Invoice> list(Map args)

    /**
     * Creates the given invoice.
     *
     * @param instance  the given invoice
     * @return          the saved invoice
     */
    Invoice save(Invoice instance)
}


@Service(value = Invoice, name = 'invoiceService')
abstract class InvoiceServiceImpl implements InvoiceService {

    /*
     * Implementation Notes: as of version 6.1 of GORM data service do not
     * support properties of super classes in dynamic methods like
     * "countBy..." or "findAllBy..." etc.  As a workaround these methods
     * are implemented the "old" way.  All other methods are meta-programed
     * via the interface.
     */

    //-- Public methods -----------------------------

    int countByOrganization(Organization organization) {
        Invoice.countByOrganization organization
    }

    int countByPerson(Person person) {
        Invoice.countByPerson person
    }

    int countByQuote(Quote quote) {
        Invoice.countByQuote quote
    }

    int countBySalesOrder(SalesOrder salesOrder) {
        Invoice.countBySalesOrder salesOrder
    }

    int countBySubjectLike(String subject) {
        Invoice.countBySubjectLike subject
    }

    List<Invoice> findAllByOrganization(Organization organization,
                                        Map args) {
        Invoice.findAllByOrganization organization, args
    }

    List<Invoice> findAllByPerson(Person person, Map args) {
        Invoice.findAllByPerson person, args
    }

    List<Invoice> findAllByQuote(Quote quote, Map args) {
        Invoice.findAllByQuote quote, args
    }

    List<Invoice> findAllBySalesOrder(SalesOrder salesOrder, Map args) {
        Invoice.findAllBySalesOrder salesOrder, args
    }

    List<Invoice> findAllBySubjectLike(String subject, Map args) {
        Invoice.findAllBySubjectLike subject, args
    }
}
