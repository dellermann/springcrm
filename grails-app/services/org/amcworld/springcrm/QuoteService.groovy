/*
 * QuoteService.groovy
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

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import grails.gorm.services.Service
import java.util.regex.Pattern
import org.bson.conversions.Bson
import org.bson.types.ObjectId


/**
 * The interface {@code IQuoteService} declares methods for working with quotes.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface IQuoteService {

    //-- Public methods -------------------------

    /**
     * Counts all quotes.
     *
     * @return  the number of quotes
     */
    int count()

    /**
     * Counts all quotes which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of matching quotes
     */
    int countByOrganization(Organization organization)

    /**
     * Counts all quotes which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of matching quotes
     */
    int countByPerson(Person person)

    /**
     * Counts the quotes which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @return          the number of matching quotes
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the quote with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted quote or {@code null} if no quote has been
     *              deleted
     */
    Quote delete(ObjectId id)

    /**
     * Finds all quotes which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments for retrieving the quotes
     * @return              a list of matching quotes
     */
    List<Quote> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds all quotes which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments for retrieving the quotes
     * @return          a list of matching quotes
     */
    List<Quote> findAllByPerson(Person person, Map args)

    /**
     * Finds all quotes which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @param args      any arguments for retrieving the quotes
     * @return          a list of matching quotes
     */
    List<Quote> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the quote with the given ID.
     *
     * @param id    the given ID
     * @return      the quote or {@code null} if no such quote has been found
     */
    Quote get(ObjectId id)

    /**
     * Lists all quotes.
     *
     * @param args  any arguments for retrieving the quotes
     * @return      the quotes
     */
    List<Quote> list(Map args)

    /**
     * Creates or updates the given quote.
     *
     * @param quote the given quote
     * @return      the saved quote or {@code null} if an error occurred
     */
    Quote save(Quote quote)
}


/**
 * The class {@code QuoteService} implements methods for working with quotes.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(Quote)
abstract class QuoteService implements IQuoteService {

    //-- Public methods -------------------------

    /**
     * Finds all quotes with either the given number or name and optionally
     * belonging to the given organization.
     *
     * @param number        the given quote number; may be {@code null}
     * @param name          a term which is searched in the subject of the
     *                      quote; may be {@code null} or empty
     * @param organization  an optional organization the quotes must belong to;
     *                      may be {@code null}
     * @return              a list of matching quotes
     */
    List<Quote> find(Integer number, String name, Organization organization) {
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

        Quote.find(Filters.and(andFilters))
            .sort(Sorts.orderBy(Sorts.descending('number')))
            .toList()
    }
}
