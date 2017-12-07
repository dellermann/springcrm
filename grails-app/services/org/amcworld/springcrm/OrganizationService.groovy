/*
 * OrganizationService.groovy
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
 * The interface {@code OrganizationService} represents a service which
 * allows access to organization.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Organization)
interface OrganizationService {

    //-- Public methods -------------------------

    /**
     * Counts all organizations.
     *
     * @return  the number of all organizations
     */
    int count()

    /**
     * Counts the organizations with the given record types (client, vendor
     * or both).
     *
     * @param types the given record types
     * @return      the number of organizations
     */
    int countByRecTypeInList(List<Byte> types)

    /**
     * Counts the organizations with the given record types (client, vendor
     * or both) and a name alphabetically before the given name.
     *
     * @param types the given record types
     * @param name  the given name
     * @return      the number of organizations
     */
    int countByRecTypeInListAndNameLessThan(List<Byte> types,
                                            String name)

    /**
     * Counts the organizations with a name alphabetically before the given
     * name.
     *
     * @param name  the given name
     * @return      the number of organizations
     */
    int countByNameLessThan(String name)

    /**
     * Deletes the organization with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the organizations with a name matching the given {@code LIKE}
     * pattern.
     *
     * @param name  the given name pattern
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of organizations
     */
    List<Organization> findAllByNameLike(String name, Map args)

    /**
     * Finds the organizations with the given record types (client, vendor
     * or both).
     *
     * @param types the given record types
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of organizations
     */
    List<Organization> findAllByRecTypeInList(List<Byte> types, Map args)

    /**
     * Finds the organizations with the given record types (client, vendor
     * or both) and a name matching the given {@code LIKE} pattern.
     *
     * @param types the given record types
     * @param name  the given name pattern
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of organizations
     */
    List<Organization> findAllByRecTypeInListAndNameLike(List<Byte> types,
                                                         String name,
                                                         Map args)

    /**
     * Gets the organization with the given ID.
     *
     * @param id    the given ID
     * @return      the organization or {@code null} if no such organization
     *              with the given ID
     *              exists
     */
    Organization get(Serializable id)

    /**
     * Gets a list of all organizations.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of organizations
     */
    List<Organization> list(Map args)

    /**
     * Creates the given organization.
     *
     * @param instance  the given organization
     * @return          the saved organization
     */
    Organization save(Organization instance)
}
