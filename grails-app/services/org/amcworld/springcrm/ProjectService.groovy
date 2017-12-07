/*
 * ProjectService.groovy
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

import grails.gorm.services.Query
import grails.gorm.services.Service


/**
 * The interface {@code ProjectService} represents a service which allows
 * access to projects.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Project)
interface ProjectService {

    //-- Public methods -------------------------

    /**
     * Counts all projects.
     *
     * @return  the number of all projects
     */
    int count()

    /**
     * Counts the projects which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of projects
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the projects which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of projects
     */
    int countByPerson(Person person)

    /**
     * Deletes the project with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the projects which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of projects
     */
    List<Project> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds the projects which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of projects
     */
    List<Project> findAllByPerson(Person person, Map args)

    /**
     * Finds the projects which are currently active.
     *
     * @return  a list of project
     */
    @Query("""
        from ${Project p} where ${p.status}.id between 2600 and 2603 
        order by ${p.status}.orderId
    """)
    List<Project> findAllCurrentProjects()

    /**
     * Gets the project with the given ID.
     *
     * @param id    the given ID
     * @return      the project or {@code null} if no such project with the
     *              given ID exists
     */
    Project get(Serializable id)

    /**
     * Gets a list of all projects.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of projects
     */
    List<Project> list(Map args)

    /**
     * Creates the given project.
     *
     * @param instance  the given project
     * @return          the saved project
     */
    Project save(Project instance)
}
