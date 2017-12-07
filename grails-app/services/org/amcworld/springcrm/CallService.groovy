/*
 * CallService.groovy
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
 * The interface {@code CallService} represents a service which allows access
 * to phone calls.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Call)
interface CallService {

    //-- Public methods -------------------------

    /**
     * Counts all phone calls.
     *
     * @return  the number of all phone calls
     */
    int count()

    /**
     * Counts the phone calls which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of phone calls
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the phone calls which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of phone calls
     */
    int countByPerson(Person person)

    /**
     * Counts the phone calls with a subject alphabetically before the given
     * subject.
     *
     * @param subject   the given subject
     * @return          the number of phone calls
     */
    int countBySubjectLessThan(String subject)

    /**
     * Counts the phone calls with a subject matching the given {@code LIKE}
     * pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of phone calls
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the phone call with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the phone calls which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of phone calls
     */
    List<Call> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds the phone calls which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of phone calls
     */
    List<Call> findAllByPerson(Person person, Map args)

    /**
     * Finds the phone calls with a subject matching the given {@code LIKE}
     * pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of phone calls
     */
    List<Call> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the phone call with the given ID.
     *
     * @param id    the given ID
     * @return      the phone call or {@code null} if no such phone call with
     *              the given ID exists
     */
    Call get(Serializable id)

    /**
     * Gets a list of all phone calls.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of phone calls
     */
    List<Call> list(Map args)

    /**
     * Creates the given phone call.
     *
     * @param instance  the given phone call
     * @return          the saved phone call
     */
    Call save(Call instance)
}
