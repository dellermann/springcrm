/*
 * HelpdeskService.groovy
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
 * The interface {@code HelpdeskService} represents a service which allows
 * access to helpdesks.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Helpdesk)
interface HelpdeskService {

    //-- Public methods -------------------------

    /**
     * Counts all helpdesks.
     *
     * @return  the number of all helpdesks
     * @since 2.2
     */
    int count()

    /**
     * Counts the helpdesks which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of helpdesks
     * @since 2.2
     */
    int countByOrganization(Organization organization)

    /**
     * Deletes the helpdesk with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the helpdesks which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order
     *                      etc.)
     * @return              a list of helpdesks
     * @since 2.2
     */
    List<Helpdesk> findAllByOrganization(Organization organization,
                                         Map args)

    /**
     * Finds the helpdesk with the given URL name.
     *
     * @param urlName   the given URL name
     * @return          the matching helpdesk or {@code null} if no such
     *                  helpdesk exists
     * @since 2.2
     */
    Helpdesk findByUrlName(String urlName)

    /**
     * Gets the helpdesk with the given ID.
     *
     * @param id    the given ID
     * @return      the helpdesk or {@code null} if no such helpdesk with
     *              the given ID exists
     * @since 2.2
     */
    Helpdesk get(Serializable id)

    /**
     * Gets a list of all helpdesks.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of helpdesks
     * @since 2.2
     */
    List<Helpdesk> list(Map args)

    /**
     * Creates the given helpdesk.
     *
     * @param instance  the given helpdesk
     * @return          the saved helpdesk
     * @since 2.2
     */
    Helpdesk save(Helpdesk instance)
}
