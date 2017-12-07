/*
 * BoilerplateService.groovy
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
 * The interface {@code BoilerplateService} represents a service which allows
 * access to boilerplates.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Boilerplate)
interface BoilerplateService {

    //-- Public methods -------------------------

    /**
     * Counts all boilerplates.
     *
     * @return  the number of all boilerplates
     */
    int count()

    /**
     * Counts the boilerplates with a name alphabetically before the given
     * name.
     *
     * @param name  the given name
     * @return      the number of boilerplates
     */
    int countByNameLessThan(String name)

    /**
     * Deletes the boilerplate with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the boilerplates with a name matching the given {@code LIKE}
     * pattern.
     *
     * @param name  the given name pattern
     * @return      a list of boilerplates
     */
    List<Boilerplate> findAllByNameIlike(String name)

    /**
     * Gets the boilerplate with the given ID.
     *
     * @param id    the given ID
     * @return      the boilerplate or {@code null} if no such boilerplate with
     *              the given ID exists
     */
    Boilerplate get(Serializable id)

    /**
     * Gets a list of all boilerplates.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of boilerplates
     */
    List<Boilerplate> list(Map args)

    /**
     * Creates the given boilerplate.
     *
     * @param instance  the given boilerplate
     * @return          the saved boilerplate
     */
    Boilerplate save(Boilerplate instance)
}
