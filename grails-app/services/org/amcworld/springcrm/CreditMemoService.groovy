/*
 * CreditMemoService.groovy
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
 * The interface {@code CreditMemoService} represents a service which
 * allows access to credit memos.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface CreditMemoService {

    //-- Public methods -------------------------

    /**
     * Counts all credit memos.
     *
     * @return  the number of all credit memos
     */
    int count()

    /**
     * Counts the credit memos which belong to the given dunning.
     *
     * @param invoice   the given dunning
     * @return          the number of credit memos
     */
    int countByDunning(Dunning dunning)

    /**
     * Counts the credit memos which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @return          the number of credit memos
     */
    int countByInvoice(Invoice invoice)

    /**
     * Counts the credit memos which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of credit memos
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the credit memos which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of credit memos
     */
    int countByPerson(Person person)

    /**
     * Counts the credit memos with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @return          the number of credit memos
     */
    int countBySubjectLike(String subject)

    /**
     * Deletes the credit memo with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the credit memos which belong to the given dunning.
     *
     * @param dunning   the given dunning
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of credit memos
     */
    List<CreditMemo> findAllByDunning(Dunning dunning, Map args)

    /**
     * Finds the credit memos which belong to the given invoice.
     *
     * @param invoice   the given invoice
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of credit memos
     */
    List<CreditMemo> findAllByInvoice(Invoice invoice, Map args)

    /**
     * Finds the credit memos which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order
     *                      etc.)
     * @return              a list of credit memos
     */
    List<CreditMemo> findAllByOrganization(Organization organization,
                                           Map args)

    /**
     * Finds the credit memos which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of credit memos
     */
    List<CreditMemo> findAllByPerson(Person person, Map args)

    /**
     * Finds the credit memos with a subject matching the given
     * {@code LIKE} pattern.
     *
     * @param subject   the given subject pattern
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of credit memos
     */
    List<CreditMemo> findAllBySubjectLike(String subject, Map args)

    /**
     * Gets the credit memo with the given ID.
     *
     * @param id    the given ID
     * @return      the credit memo or {@code null} if no such credit memo with the given ID
     *              exists
     */
    CreditMemo get(Serializable id)

    /**
     * Gets a list of all credit memos.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of credit memos
     */
    List<CreditMemo> list(Map args)

    /**
     * Creates the given credit memo.
     *
     * @param instance  the given credit memo
     * @return          the saved credit memo
     */
    CreditMemo save(CreditMemo instance)
}


@Service(value = CreditMemo, name = 'creditMemoService')
abstract class CreditMemoServiceImpl implements CreditMemoService {

    /*
     * Implementation Notes: as of version 6.1 of GORM data service do not
     * support properties of super classes in dynamic methods like
     * "countBy..." or "findAllBy..." etc.  As a workaround these methods
     * are implemented the "old" way.  All other methods are meta-programed
     * via the interface.
     */

    //-- Public methods -----------------------------

    int countByOrganization(Organization organization) {
        CreditMemo.countByOrganization organization
    }

    int countByPerson(Person person) {
        CreditMemo.countByPerson person
    }

    int countBySubjectLike(String subject) {
        CreditMemo.countBySubjectLike subject
    }

    List<CreditMemo> findAllByOrganization(Organization organization,
                                           Map args)
    {
        CreditMemo.findAllByOrganization organization, args
    }

    List<CreditMemo> findAllByPerson(Person person, Map args) {
        CreditMemo.findAllByPerson person, args
    }

    List<CreditMemo> findAllBySubjectLike(String subject, Map args) {
        CreditMemo.findAllBySubjectLike subject, args
    }
}
