/*
 * PurchaseInvoiceService.groovy
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
 * The interface {@code PurchaseInvoiceService} represents a service which
 * allows access to purchase invoices.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(PurchaseInvoice)
interface PurchaseInvoiceService {

    //-- Public methods -------------------------

    /**
     * Counts all purchase invoices.
     *
     * @return  the number of all purchase invoices
     */
    int count()

    /**
     * Counts the purchase invoices with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of purchase invoices
     */
    int countBySubjectLike(String subject)

    /**
     * Counts the purchase invoices which belong to the given vendor.
     *
     * @param vendor    the given organization representing the vendor
     * @return          the number of purchase invoices
     */
    int countByVendor(Organization vendor)

    /**
     * Deletes the purchase invoice with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the purchase invoices with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of purchase invoices
     */
    List<PurchaseInvoice> findAllBySubjectLike(String subject, Map args)

    /**
     * Finds the purchase invoices which belong to the given vendor.
     *
     * @param vendor    the given organization representing the vendor
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of purchase invoices
     */
    List<PurchaseInvoice> findAllByVendor(Organization vendor, Map args)

    /**
     * Gets the purchase invoice with the given ID.
     *
     * @param id    the given ID
     * @return      the purchase invoice or {@code null} if no such purchase
     *              invoice with the given ID exists
     */
    PurchaseInvoice get(Serializable id)

    /**
     * Gets a list of all purchase invoices.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of purchase invoices
     */
    List<PurchaseInvoice> list(Map args)

    /**
     * Creates the given purchase invoice.
     *
     * @param instance  the given purchase invoice
     * @return          the saved purchase invoice
     */
    PurchaseInvoice save(PurchaseInvoice instance)
}
