/*
 * SalesOrderService.groovy
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
 * The interface {@code SalesOrderService} represents a service which allows
 * access to sales orders.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface SalesOrderService {

    //-- Public methods -------------------------

    /**
     * Counts all sales orders.
     *
     * @return  the number of all sales orders
     */
    int count()

    /**
     * Counts the sales orders which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of sales orders
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the sales orders which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of sales orders
     */
    int countByPerson(Person person)

    /**
     * Counts the sales orders which belong to the given quote.
     *
     * @param quote the given quote
     * @return      the number of sales orders
     */
    int countByQuote(Quote quote)

    /**
     * Counts the sales orders with a subject matching the given {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of sales orders
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the sales order with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the sales orders which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of sales orders
     */
    List<SalesOrder> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds the sales orders which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of sales orders
     */
    List<SalesOrder> findAllByPerson(Person person, Map args)

    /**
     * Finds the sales orders which belong to the given quote.
     *
     * @param quote the given quote
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of sales orders
     */
    List<SalesOrder> findAllByQuote(Quote quote, Map args)

    /**
     * Finds the sales orders with a subject matching the given {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of sales orders
     */
    List<SalesOrder> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the sales order with the given ID.
     *
     * @param id    the given ID
     * @return      the sales order or {@code null} if no such sales order with the given ID
     *              exists
     */
    SalesOrder get(Serializable id)

    /**
     * Gets a list of all sales orders.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of sales orders
     */
    List<SalesOrder> list(Map args)

    /**
     * Saves the given sales order.
     *
     * @param instance  the given sales order
     * @return          the saved sales order
     */
    SalesOrder save(SalesOrder instance)
}


@Service(value = SalesOrder, name = 'salesOrderService')
abstract class SalesOrderServiceImpl implements SalesOrderService {

    //-- Public methods -----------------------------

    int countByOrganization(Organization organization) {
        SalesOrder.countByOrganization organization
    }

    int countByPerson(Person person) {
        SalesOrder.countByPerson person
    }

    int countBySubjectLike(String subject) {
        SalesOrder.countBySubjectLike subject
    }

    List<SalesOrder> findAllByOrganization(Organization organization, Map args)
    {
        SalesOrder.findAllByOrganization organization, args
    }

    List<SalesOrder> findAllByPerson(Person person, Map args) {
        SalesOrder.findAllByPerson person, args
    }

    List<SalesOrder> findAllBySubjectLike(String subject, Map args) {
        SalesOrder.findAllBySubjectLike subject, args
    }
}
