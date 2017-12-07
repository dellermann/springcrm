/*
 * HelpdeskUserService.groovy
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
 * The interface {@code HelpdeskUserService} represents a service which allows
 * access to the association between helpdesks and users.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(HelpdeskUser)
interface HelpdeskUserService {

    //-- Public methods -------------------------

    /**
     * Counts all helpdesk user associations.
     *
     * @return  the number of all helpdesk user associations
     */
    int count()

    /**
     * Deletes the helpdesk user association with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the helpdesk user associations which belong to the given user.
     *
     * @param user  the given user
     * @return      a list of helpdesk user associations
     */
    List<HelpdeskUser> findAllByUser(User user)

    /**
     * Gets the helpdesk user association with the given ID.
     *
     * @param id    the given ID
     * @return      the helpdesk user association or {@code null} if no such helpdesk user association with the given ID
     *              exists
     */
    HelpdeskUser get(Serializable id)

    /**
     * Gets a list of all helpdesk user associations.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of helpdesk user associations
     */
    List<HelpdeskUser> list(Map args)

    /**
     * Saves the given helpdesk user association.
     *
     * @param instance  the given helpdesk user association
     * @return          the saved helpdesk user association
     */
    HelpdeskUser save(HelpdeskUser instance)
}
