/*
 * SalesOrderService.groovy
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
 *
 */


package org.amcworld.springcrm

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import grails.gorm.services.Service
import java.util.regex.Pattern
import org.bson.conversions.Bson
import org.bson.types.ObjectId


/**
 * The interface {@code ISalesOrderService} declares methods for working with
 * sales orders.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface ISalesOrderService {

    //-- Public methods -------------------------

    /**
     * Counts all sales orders.
     *
     * @return  the number of sales orders
     */
    int count()

    /**
     * Counts all sales orders which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of matching sales orders
     */
    int countByOrganization(Organization organization)

    /**
     * Counts all sales orders which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of matching sales orders
     */
    int countByPerson(Person person)

    /**
     * Counts all sales orders which belong to the given quote.
     *
     * @param quote the given quote
     * @return      the number of matching sales orders
     */
    int countByQuote(Quote quote)

    /**
     * Counts the sales orders which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @return          the number of matching sales orders
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the sales order with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted sales order or {@code null} if no sales order
     *              has been deleted
     */
    SalesOrder delete(ObjectId id)

    /**
     * Finds all sales orders which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments for retrieving the sales orders
     * @return              a list of matching sales orders
     */
    List<SalesOrder> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds all sales orders which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments for retrieving the sales orders
     * @return          a list of matching sales orders
     */
    List<SalesOrder> findAllByPerson(Person person, Map args)

    /**
     * Finds all sales orders which belong to the given quote.
     *
     * @param quote the given quote
     * @param args  any arguments for retrieving the sales orders
     * @return      a list of matching sales orders
     */
    List<SalesOrder> findAllByQuote(Quote quote, Map args)

    /**
     * Finds all sales orders which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @param args      any arguments for retrieving the sales orders
     * @return          a list of matching sales orders
     */
    List<SalesOrder> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the sales order with the given ID.
     *
     * @param id    the given ID
     * @return      the sales order or {@code null} if no such sales order has
     *              been found
     */
    SalesOrder get(ObjectId id)

    /**
     * Lists all sales orders.
     *
     * @param args  any arguments for retrieving the sales orders
     * @return      the sales orders
     */
    List<SalesOrder> list(Map args)

    /**
     * Creates or updates the given sales order.
     *
     * @param salesOrder    the given sales order
     * @return              the saved sales order or {@code null} if an error
     *                      occurred
     */
    SalesOrder save(SalesOrder salesOrder)
}


/**
 * The class {@code SalesOrderService} implements methods for working with
 * sales orders.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(SalesOrder)
abstract class SalesOrderService implements ISalesOrderService {

    //-- Public methods -------------------------

    /**
     * Finds all sales order with either the given number or name and optionally
     * belonging to the given organization.
     *
     * @param number        the given sales order number; may be {@code null}
     * @param name          a term which is searched in the subject of the sales
     *                      order; may be {@code null} or empty
     * @param organization  an optional organization the sales order must belong
     *                      to; may be {@code null}
     * @return              a list of matching sales orders
     */
    List<SalesOrder> find(Integer number, String name,
                          Organization organization)
    {
        List<Bson> orFilters = []
        if (number != null) {
            orFilters << Filters.eq('number', number)
        }
        if (name) {
            orFilters << Filters.regex('subject', Pattern.quote(name))
        }

        List<Bson> andFilters = [Filters.or(orFilters)]
        if (organization != null) {
            andFilters << Filters.eq('organization', organization.id)
        }

        SalesOrder.find(Filters.and(andFilters))
            .sort(Sorts.orderBy(Sorts.descending('number')))
            .toList()
    }
}
