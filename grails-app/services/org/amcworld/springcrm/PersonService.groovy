/*
 * PersonService.groovy
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
import grails.gorm.services.Where


/**
 * The interface {@code PersonService} represents a service which allows
 * access to persons.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Person)
interface PersonService {

    //-- Public methods -------------------------

    /**
     * Counts all persons.
     *
     * @return  the number of all persons
     */
    int count()

    /**
     * Counts the persons which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of persons
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the persons with a last name alphabetically before the given
     * last name.
     *
     * @param lastName  the given last name
     * @return          the number of persons
     */
    int countByLastNameLessThan(String lastName)

    /**
     * Deletes the person with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the persons which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of persons
     */
    List<Person> findAllByOrganization(Organization organization, Map args)

    /**
     * Gets the person with the given ID.
     *
     * @param id    the given ID
     * @return      the person or {@code null} if no such person with the
     *              given ID exists
     */
    Person get(Serializable id)

    /**
     * Gets a list of all persons.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of persons
     */
    List<Person> list(Map args)

    /**
     * Creates the given person.
     *
     * @param instance  the given person
     * @return          the saved person
     */
    Person save(Person instance)

    /**
     * Searches all persons which belong to the given organization and whose
     * name matches the given name {@code LIKE} pattern.
     *
     * @param organizationInstance  the given organization
     * @param name                  the name pattern
     * @param args                  any arguments used for retrieval (sort,
     *                              order etc.)
     * @return                      a list of persons
     */
    @Where({
        organization == organizationInstance &&
        (lastName =~ name || firstName =~ name)
    })
    List<Person> searchPersons(Organization organizationInstance,
                               String name, Map args)
}
