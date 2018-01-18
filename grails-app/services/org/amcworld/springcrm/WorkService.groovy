/*
 * WorkService.groovy
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


@Service(Work)
interface WorkService {

    //-- Public methods -----------------------------

    /**
     * Counts all services.
     *
     * @return  the number of services
     */
    int count()

    /**
     * Deletes the service with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted service or {@code null} if no service has been
     *              deleted
     */
    Work delete(ObjectId id)

    /**
     * Gets the service with the given ID.
     *
     * @param id    the given ID
     * @return      the service or {@code null} if no such service has been
     *              found
     */
    Work get(ObjectId id)

    /**
     * Lists all services.
     *
     * @param args  any arguments for retrieving the services
     * @return      the services
     */
    List<Work> list(Map args)

    /**
     * Creates or updates the given service.
     *
     * @param work  the given service
     * @return      the saved service or {@code null} if an error occurred
     */
    Work save(Work work)
}
