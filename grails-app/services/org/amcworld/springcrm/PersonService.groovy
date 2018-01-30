/*
 * PersonService.groovy
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
import groovy.transform.CompileStatic
import java.util.regex.Pattern
import org.bson.types.ObjectId


/**
 * The interface {@code IPersonService} contains general methods to handle
 * persons in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface IPersonService {

    //-- Public methods -----------------------------

    /**
     * Counts all persons.
     *
     * @return  the number of persons
     */
    int count()

    /**
     * Counts all persons that last name is alphabetically before the given
     * name.
     *
     * @param name  the given name
     * @return      the number of matching persons
     */
    int countByLastNameLessThan(String name)

    /**
     * Count all persons which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of matching persons
     */
    int countByOrganization(Organization organization)

    /**
     * Deletes the person with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted person or {@code null} if no person has been
     *              deleted
     */
    Person delete(ObjectId id)

    /**
     * Finds all persons which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments for retrieving the persons
     * @return              the matching persons
     */
    List<Person> findAllByOrganization(Organization organization, Map args)

    /**
     * Gets the person with the given ID.
     *
     * @param id    the given ID
     * @return      the person or {@code null} if no such person has been found
     */
    Person get(ObjectId id)

    /**
     * Lists all persons.
     *
     * @param args  any arguments for retrieving the persons
     * @return      the persons
     */
    List<Person> list(Map args)

    /**
     * Creates or updates the given person.
     *
     * @param person    the given person
     * @return          the saved person or {@code null} if an error occurred
     */
    Person save(Person person)
}

/**
 * The class {@code PersonService} implements general methods to handle
 * persons in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(Person)
@CompileStatic
abstract class PersonService implements IPersonService {

    //-- Public methods -----------------------------

    /**
     * Searches all persons which belong to the given organization and contain
     * the given search term in either the first or the last name.
     *
     * @param organization  the given organization
     * @param term          the given search term
     * @return              a list of matching persons or {@code null} if no
     *                      organization has been specified
     */
    List<Person> search(Organization organization, String term) {
        if (organization == null) {
            return null
        }

        String pattern = term ? Pattern.quote(term) : '.*'

        Person.find(
                Filters.and(
                    Filters.eq('organization', organization.id),
                    Filters.or(
                        Filters.regex('lastName', pattern),
                        Filters.regex('firstName', pattern)
                    )
                )
            )
            .sort(Sorts.orderBy(Sorts.ascending('lastName')))
            .toList()
    }
}
