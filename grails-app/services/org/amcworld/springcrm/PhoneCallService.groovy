/*
 * PhoneCallService.groovy
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

import grails.gorm.services.Service
import org.bson.types.ObjectId


/**
 * The class {@code PhoneCallService} contains methods for working with phone
 * calls.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.3
 */
@Service(PhoneCall)
interface PhoneCallService {

    //-- Public methods -------------------------

    /**
     * Counts all phone calls.
     *
     * @return  the number of phone calls
     */
    int count()

    /**
     * Counts all phone calls which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of matching phone calls
     */
    int countByOrganization(Organization organization)

    /**
     * Counts all phone calls which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of matching phone calls
     */
    int countByPerson(Person person)

    /**
     * Counts all phone calls that subject is alphabetically before the given
     * subject.
     *
     * @param subject   the given subject
     * @return          the number of matching phone calls
     */
    int countBySubjectLessThan(String subject)

    /**
     * Counts the phone calls which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @return          the number of matching phone calls
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the phone phoneCall with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted phone phoneCall or {@code null} if no phone phoneCall has
     *              been deleted
     */
    PhoneCall delete(ObjectId id)

    /**
     * Finds all phone calls which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments for retrieving the phone calls
     * @return              a list of matching phone calls
     */
    List<PhoneCall> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds all phone calls which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments for retrieving the phone calls
     * @return          a list of matching phone calls
     */
    List<PhoneCall> findAllByPerson(Person person, Map args)

    /**
     * Finds all phone calls which subject matches the given pattern.
     *
     * @param subject   the subject pattern
     * @param args      any arguments for retrieving the phone calls
     * @return          a list of matching phone calls
     */
    List<PhoneCall> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the phone phoneCall with the given ID.
     *
     * @param id    the given ID
     * @return      the phone phoneCall or {@code null} if no such phone phoneCall has
     *              been found
     */
    PhoneCall get(ObjectId id)

    /**
     * Lists all phone calls.
     *
     * @param args  any arguments for retrieving the phone calls
     * @return      the phone calls
     */
    List<PhoneCall> list(Map args)

    /**
     * Creates or updates the given phone phoneCall.
     *
     * @param work  the given phone phoneCall
     * @return      the saved phone phoneCall or {@code null} if an error occurred
     */
    PhoneCall save(PhoneCall work)
}
