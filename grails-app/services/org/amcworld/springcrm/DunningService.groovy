/*
 * DunningService.groovy
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
 * The interface {@code DunningService} represents a service which allows
 * access to dunnings.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface DunningService {

    //-- Public methods -------------------------

    /**
     * Counts all dunnings.
     *
     * @return  the number of all dunnings
     */
    int count()

    /**
     * Counts the dunnings which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @return          the number of dunnings
     */
    int countByInvoice(Invoice invoice)

    /**
     * Counts the dunnings which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of dunnings
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the dunnings which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of dunnings
     */
    int countByPerson(Person person)

    /**
     * Counts the dunnings with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of dunnings
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the dunning with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the dunnings which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of dunnings
     */
    List<Dunning> findAllByInvoice(Invoice invoice, Map args)

    /**
     * Finds the dunnings which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order
     *                      etc.)
     * @return              a list of dunnings
     */
    List<Dunning> findAllByOrganization(Organization organization,
                                           Map args)

    /**
     * Finds the dunnings which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of dunnings
     */
    List<Dunning> findAllByPerson(Person person, Map args)

    /**
     * Finds the dunnings with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of dunnings
     */
    List<Dunning> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the dunning with the given ID.
     *
     * @param id    the given ID
     * @return      the dunning or {@code null} if no such dunning with the given ID
     *              exists
     */
    Dunning get(Serializable id)

    /**
     * Gets a list of all dunnings.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of dunnings
     */
    List<Dunning> list(Map args)

    /**
     * Creates the given dunning.
     *
     * @param instance  the given dunning
     * @return          the saved dunning
     */
    Dunning save(Dunning instance)
}


@Service(value = Dunning, name = 'dunningService')
abstract class DunningServiceImpl implements DunningService {

    /*
     * Implementation Notes: as of version 6.1 of GORM data service do not
     * support properties of super classes in dynamic methods like
     * "countBy..." or "findAllBy..." etc.  As a workaround these methods
     * are implemented the "old" way.  All other methods are meta-programed
     * via the interface.
     */

    //-- Public methods -----------------------------

    int countByOrganization(Organization organization) {
        Dunning.countByOrganization organization
    }

    int countByPerson(Person person) {
        Dunning.countByPerson person
    }

    int countBySubjectLike(String subject) {
        Dunning.countBySubjectLike subject
    }

    List<Dunning> findAllByOrganization(Organization organization,
                                        Map args)
    {
        Dunning.findAllByOrganization organization, args
    }

    List<Dunning> findAllByPerson(Person person, Map args) {
        Dunning.findAllByPerson person, args
    }

    List<Dunning> findAllBySubjectLike(String subject, Map args) {
        Dunning.findAllBySubjectLike subject, args
    }
}
