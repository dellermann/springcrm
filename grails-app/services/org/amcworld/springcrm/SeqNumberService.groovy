/*
 * SeqNumberService.groovy
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

import grails.core.ArtefactHandler
import grails.core.GrailsApplication
import grails.core.GrailsClass
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.grails.core.artefact.DomainClassArtefactHandler
import org.springframework.transaction.annotation.Transactional

/**
 * The class {@code SeqNumberService} cares about a sequential numbering of
 * content items.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
@CompileStatic
class SeqNumberService {

    //-- Constants ------------------------------

    /**
     * The controllers which should be checked for suitable sequence numbers.
     *
     * @since 2.1
     */
    private static final List<Class<? extends InvoicingTransaction>> \
        CONTROLLERS_TO_CHECK = [Quote, SalesOrder, Invoice, Dunning, CreditMemo]


    //-- Class fields ---------------------------

    static transactional = false


    //-- Fields ---------------------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Checks whether the start values of the sequence numbers of invoicing
     * transactions such as quotes, sales order, invoices etc. matches the
     * current year.
     *
     * @return  {@code true} if all sequence numbers are suitable;
     *          {@code false} otherwise
     * @since   2.1
     */
    @Transactional(readOnly = true)
    boolean checkNumberScheme() {
        String year = new Date().format('YY')

        boolean res = true
        for (Class<? extends InvoicingTransaction> c : CONTROLLERS_TO_CHECK) {
            res &= checkControllerNumberScheme(c, year)
        }

        res
    }

    /**
     * Gets a list of sequence numbers where the numbers of invoicing
     * transactions such as quotes, sales order, invoices etc. have been fixed
     * to match the current year.
     *
     * @return  a list of sequence numbers with partially fixed start values
     * @since   2.1
     */
    List<SeqNumber> getFixedSeqNumbers() {
        String year = new Date().format('YY')
        int num = year.toInteger() * 1000

        List<SeqNumber> res = SeqNumber.list(readOnly: true)
        for (SeqNumber sn : res) {
            Class<?> cls = domainNameToClass(sn.controllerName)
            if (InvoicingTransaction.isAssignableFrom(cls)) {
                Class<? extends InvoicingTransaction> c =
                    (Class<? extends InvoicingTransaction>) cls
                if (c in CONTROLLERS_TO_CHECK && sn.startValue < num) {
                    sn.startValue = num
                }
            }
        }

        res
    }

    /**
     * Checks whether or not to display the hint about changing the sequence
     * numbers for the user with the given credential.  The hint is displayed
     * for administrators, only.
     *
     * @param credential    the given credential of the user
     * @return              {@code true} if the hint should be displayed;
     *                      {@code false} otherwise
     * @since 2.1
     */
    @Transactional(readOnly = true)
    boolean getShowHint(Credential credential) {
        if (checkNumberScheme() || !credential.admin) return false

        String year = credential.settings['seqNumberHintYear']

        !year || new Date().format('YYYY').toInteger() > year.toInteger()
    }

    /**
     * Gets the full name of the given domain model instance.
     *
     * @param domain    the given domain model
     * @return          the full name
     * @since 2.1
     */
    String getFullName(NumberedDomain domain) {
        domain.computeFullName loadSeqNumber(domain.getClass())
    }

    /**
     * Computes the full number of the given domain model instance.
     *
     * @param domain    the given domain model
     * @return          the computed number
     */
    String getFullNumber(NumberedDomain domain) {
        domain.computeFullNumber loadSeqNumber(domain.getClass())
    }

    /**
     * Loads the sequence number data for the controller which is associated to
     * the given class.
     *
     * @param cls   the given class
     * @return      the sequence number data; {@code null} if no such sequence
     *              number exists for the class
     */
    SeqNumber loadSeqNumber(Class cls) {
        loadSeqNumber classToDomainName(cls)
    }

    /**
     * Loads the sequence number data for the given controller.
     *
     * @param controllerName    the given controller name
     * @return                  the sequence number data; {@code null} if no
     *                          such sequence number exists for the controller
     */
    @CompileDynamic
    SeqNumber loadSeqNumber(String controllerName) {
        SeqNumber.findByControllerName controllerName
    }

    /**
     * Retrieves the next available sequence number for the given controller
     * name.
     *
     * @param controllerName    the given controller name
     * @return                  the next available sequence number
     */
    @CompileDynamic
    int nextNumber(String controllerName) {
        SeqNumber seq = loadSeqNumber(controllerName)
        GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
            DomainClassArtefactHandler.TYPE, controllerName
        )
        Integer num
        try {
            num = cls.clazz.'maxNumber'(seq)
        } catch (Exception ignore) {
            def c = cls.clazz.'createCriteria'()
            num = c.get {
                projections {
                    max 'number'
                }
                lte 'number', seq.endValue
            }
        }

        (num == null || num < seq.startValue) ? seq.startValue : num + 1
    }

    /**
     * Retrieves the next available sequence number for the controller which is
     * associated to the given class.
     *
     * @param cls   the given class
     * @return      the next available sequence number
     */
    int nextNumber(Class cls) {
        nextNumber classToDomainName(cls)
    }

    /**
     * Stores the current year in the user settings to prevent display of the
     * hint about changing the sequence number scheme for this year.
     *
     * @param credential    the credential representing the currently logged in
     *                      user
     */
    void setDontShowAgain(Credential credential) {
        credential.settings['seqNumberHintYear'] = new Date().format('YYYY')
    }

    /**
     * Stores in the user settings that the hint about changing the sequence
     * number scheme should no longer be displayed.
     *
     * @param credential    the credential representing the currently logged in
     *                      user
     */
    void setNeverShowAgain(Credential credential) {
        credential.settings['seqNumberHintYear'] = '9999'
    }


    //-- Non-public methods ---------------------

    /**
     * Checks whether the start value of the sequence number of the given
     * controller matches the stated year.
     *
     * @param controller    the controller to check
     * @param year          the two digit year number (for example, {@code 17}
     *                      for 2017)
     * @return              {@code true} if the sequence number is suitable;
     *                      {@code false} otherwise
     * @since 2.1
     */
    private boolean checkControllerNumberScheme(
        Class<? extends InvoicingTransaction> controller, String year
    ) {
        SeqNumber seqNumber = loadSeqNumber(controller)

        seqNumber != null && (seqNumber.startValue as String).startsWith(year)
    }

    /**
     * Obtains the name of the domain model which is associated to the given
     * class.
     *
     * @param cls   the given class
     * @return      the associated domain model name; {@code null} if no domain
     *              model with the given class exists
     * @since 2.1
     */
    private String classToDomainName(Class cls) {
        ArtefactHandler handler = grailsApplication.getArtefactType(cls)
        if (handler == null) {

            /*
             * This occurs in some cases where subclasses of a domain class
             * have been meta programed.  E. g. class "Quote_$$_jvst322_51"
             * will be used instead of "Quote".
             */
            cls = cls.superclass
            handler = grailsApplication.getArtefactType(cls)
        }
        GrailsClass gc = grailsApplication.getArtefact(handler.type, cls.name)

        gc?.logicalPropertyName
    }

    /**
     * Obtains the class of the domain model with the given name.
     *
     * @param domainName    the given domain name such as {@code organization}
     * @return              the associated domain model class; {@code null} if
     *                      no domain model with the given name exists
     * @since 2.1
     */
    private Class<?> domainNameToClass(String domainName) {
        grailsApplication.getArtefactByLogicalPropertyName(
                DomainClassArtefactHandler.TYPE, domainName
            )?.clazz
    }
}
