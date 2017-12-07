/*
 * WorkService.groovy
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
 * The interface {@code WorkService} represents a service which allows access
 * to works.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface WorkService {

    //-- Public methods -------------------------

    /**
     * Counts all works.
     *
     * @return  the number of all works
     */
    int count()

    /**
     * Counts the work with a name alphabetically before the given name.
     *
     * @param name  the given name
     * @return      the number of works
     */
    int countByNameLessThan(String name)

    int countByNameLessThanAndNameLike(String name, String pattern)

    /**
     * Counts the works with a name matching the given {@code LIKE} pattern.
     *
     * @param name  the given name pattern
     * @return      the number of works
     */
    int countByNameLike(String name)

    /**
     * Deletes the work with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the works with a name matching the given {@code LIKE} pattern.
     *
     * @param name  the given name pattern
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of works
     */
    List<Work> findAllByNameLike(String name, Map args)

    List<Work> findAllByNumberOrNameIlike(Integer number, String name, Map args)

    /**
     * Gets the work with the given ID.
     *
     * @param id    the given ID
     * @return      the work or {@code null} if no such work with the given ID
     *              exists
     */
    Work get(Serializable id)

    /**
     * Gets a list of all works.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of works
     */
    List<Work> list(Map args)

    /**
     * Saves the given work.
     *
     * @param instance  the given work
     * @return          the saved work
     */
    Work save(Work instance)
}


/**
 * The class {@code WorkServiceImpl} represents an implementation of the
 * service which allows access to services (work).
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(value = Work, name = 'workService')
abstract class WorkServiceImpl implements WorkService {

    //-- Public methods -----------------------------

    int countByNameLessThan(String name) {
        Work.countByNameLessThan name
    }

    int countByNameLessThanAndNameLike(String name, String pattern) {
        Work.countByNameLessThanAndNameLike name, pattern
    }

    int countByNameLike(String name) {
        Work.countByNameLike name
    }

    List<Work> findAllByNameLike(String name, Map args) {
        Work.findAllByNameLike name, args
    }

    List<Work> findAllByNumberOrNameIlike(Integer number, String name, Map args)
    {
        Work.findAllByNumberOrNameIlike number, name, args
    }
}
