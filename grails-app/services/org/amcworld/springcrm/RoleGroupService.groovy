/*
 * RoleGroupService.groovy
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
import org.bson.types.ObjectId


/**
 * The class {@code RoleGroupService} handles the persistence of user groups in
 * the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(RoleGroup)
interface RoleGroupService {

    //-- Public methods -------------------------

    /**
     * Counts the user groups.
     *
     * @return  the number of user groups
     */
    Long count()

    /**
     * Deletes the user group with the given ID.
     *
     * @param id    the ID of the user group
     */
    RoleGroup delete(ObjectId id)

    /**
     * Finds the user group which belongs to the given role.
     *
     * @param role  the given role
     * @return      the user group or {@code null} if no such user group exists
     */
    RoleGroup findByAuthorities(Role role)

    /**
     * Gets the user group with the given ID.
     *
     * @param id    the ID of the user group
     * @return      the user group or {@code null} if no such user group exists
     */
    RoleGroup get(ObjectId id)

    /**
     * Retrieves a list of all user groups.
     *
     * @param args  any arguments used for retrieving the user groups
     * @return      a list of user groups
     */
    List<RoleGroup> list(Map args)

    /**
     * Saves the given user group.
     *
     * @param user  the user group which should be saved
     * @return      the saved user group
     */
    RoleGroup save(RoleGroup roleGroup)
}
