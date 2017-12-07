/*
 * QuoteService.groovy
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
 * The interface {@code QuoteService} represents a service which allows access
 * to quotes.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface QuoteService {

    //-- Public methods -------------------------

    /**
     * Counts all quotes.
     *
     * @return  the number of all quotes
     */
    int count()

    /**
     * Counts the quotes which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of quotes
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the quotes which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of quotes
     */
    int countByPerson(Person person)

    /**
     * Counts the quotes with a subject matching the given {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of quotes
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the quote with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the quotes which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of quotes
     */
    List<Quote> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds the quotes which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of quotes
     */
    List<Quote> findAllByPerson(Person person, Map args)

    /**
     * Finds the quotes with a subject matching the given {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of quotes
     */
    List<Quote> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the quote with the given ID.
     *
     * @param id    the given ID
     * @return      the quote or {@code null} if no such quote with the given ID
     *              exists
     */
    Quote get(Serializable id)

    /**
     * Gets a list of all quotes.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of quotes
     */
    List<Quote> list(Map args)

    /**
     * Saves the given quote.
     *
     * @param instance  the given quote
     * @return          the saved quote
     */
    Quote save(Quote instance)
}


@Service(value = Quote, name = 'quoteService')
abstract class QuoteServiceImpl implements QuoteService {

    //-- Public methods -----------------------------

    int countByOrganization(Organization organization) {
        Quote.countByOrganization organization
    }

    int countByPerson(Person person) {
        Quote.countByPerson person
    }

    int countBySubjectLike(String subject) {
        Quote.countBySubjectLike subject
    }

    List<Quote> findAllByOrganization(Organization organization, Map args) {
        Quote.findAllByOrganization organization, args
    }

    List<Quote> findAllByPerson(Person person, Map args) {
        Quote.findAllByPerson person, args
    }

    List<Quote> findAllBySubjectLike(String subject, Map args) {
        Quote.findAllBySubjectLike subject, args
    }
}
