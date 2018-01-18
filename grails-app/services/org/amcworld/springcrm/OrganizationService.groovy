/*
 * OrganizationService.groovy
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
 * The interface {@code OrganizationService} contains general methods to handle
 * organizations in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(Organization)
interface OrganizationService {

    //-- Public methods -----------------------------

    /**
     * Counts all organizations.
     *
     * @return  the number of organizations
     */
    int count()

    /**
     * Counts all organizations that name is alphabetically before the given
     * name.
     *
     * @param name  the given name
     * @return      the number of matching organizations
     */
    int countByNameLessThan(String name)

    /**
     * Counts all organizations that name is alphabetically before the given
     * name and one of the given record types.
     *
     * @param name  the given name
     * @param types the given record types
     * @return      the number of matching organizations
     */
    int countByNameLessThanAndRecTypeInList(String name, Set<Byte> types)

    /**
     * Counts all organizations which have one of the given record types.
     *
     * @param types the given record types
     * @return      the number of matching organizations
     */
    int countByRecTypeInList(Set<Byte> types)

    /**
     * Deletes the organization with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted organization or {@code null} if no organization
     *              has been deleted
     */
    Organization delete(ObjectId id)

    /**
     * Finds all organizations with the given name pattern.
     *
     * @param name  the given name pattern
     * @param args  any arguments for retrieving the organizations
     * @return      the matching organizations
     */
    List<Organization> findAllByNameLike(String name, Map args)

    /**
     * Finds all organizations which have one of the given record types.
     *
     * @param types the given record types
     * @param args  any arguments for retrieving the organizations
     * @return      the matching organizations
     */
    List<Organization> findAllByRecTypeInList(Set<Byte> types, Map args)

    /**
     * Finds all organizations which have one of the given record types and a
     * name matching the given pattern.
     *
     * @param types the given record types
     * @param name  the given name pattern
     * @param args  any arguments for retrieving the organizations
     * @return      the matching organizations
     */
    List<Organization> findAllByRecTypeInListAndNameLike(Set<Byte> types,
                                                         String name,
                                                         Map args)

    /**
     * Gets the organization with the given ID.
     *
     * @param id    the given ID
     * @return      the organization or {@code null} if no such organization
     *              has been found
     */
    Organization get(ObjectId id)

    /**
     * Lists all organizations.
     *
     * @param args  any arguments for retrieving the organizations
     * @return      the organizations
     */
    List<Organization> list(Map args)

    /**
     * Creates or updates the given organization.
     *
     * @param organization  the given organization
     * @return              the saved organization or {@code null} if an error
     *                      occurred
     */
    Organization save(Organization organization)
}
