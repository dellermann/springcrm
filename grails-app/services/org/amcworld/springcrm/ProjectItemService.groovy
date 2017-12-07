/*
 * ProjectItemService.groovy
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
 * The interface {@code ProjectItemService} represents a service which allows
 * access to project items.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(ProjectItem)
interface ProjectItemService {

    //-- Public methods -------------------------

    /**
     * Counts all project items.
     *
     * @return  the number of all project items
     */
    int count()

    /**
     * Deletes the project item with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Gets the project item with the given ID.
     *
     * @param id    the given ID
     * @return      the project item or {@code null} if no such project item
     *              with the given ID exists
     */
    ProjectItem get(Serializable id)

    /**
     * Gets a list of all project items.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of project items
     */
    List<ProjectItem> list(Map args)

    /**
     * Creates the given project item.
     *
     * @param instance  the given project item
     * @return          the saved project item
     */
    ProjectItem save(ProjectItem instance)
}
