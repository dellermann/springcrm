/*
 * SeqNumberService.groovy
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
 * @version 3.0
 */
@CompileStatic
@Transactional(readOnly = true)
class SeqNumberService {

    //-- Constants ------------------------------

    /**
     * The controllers which should be checked for suitable sequence numbers.
     *
     * @since 2.1
     */
    private static final List<Class<? extends InvoicingTransaction>> \
        CONTROLLERS_TO_CHECK = [
            Quote, SalesOrder, Invoice, Dunning, CreditMemo
        ].asImmutable()


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
    boolean getShowHint(Credential credential) {
        if (checkNumberScheme() || !credential.admin) return false

        String year = credential.settings['seqNumberHintYear']

        !year || new Date().format('YYYY').toInteger() > year.toInteger()
    }

    /**
     * Formats the given sequence number as specified in the number schema for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    String format(String controllerName, int number) {
        formatNumber controllerName: controllerName, number: number
    }

    /**
     * Formats the given sequence number as specified in the number schema for
     * the controller which is associated to the given class.
     *
     * @param cls       the given class
     * @param number    the given number
     * @return          the formatted number
     */
    String format(Class cls, int number) {
        format classToDomainName(cls), number
    }

    /**
     * Formats the given sequence number with the prefix which is defined for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    String formatWithPrefix(String controllerName, int number) {
        formatNumber(
            controllerName: controllerName, number: number, withSuffix: false
        )
    }

    /**
     * Formats the given sequence number with the prefix which is defined for
     * the controller which is associated to the given class.
     *
     * @param cls       the given class
     * @param number    the given number
     * @return          the formatted number
     */
    String formatWithPrefix(Class cls, int number) {
        formatWithPrefix classToDomainName(cls), number
    }

    /**
     * Formats the given sequence number with the suffix which is defined for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    String formatWithSuffix(String controllerName, int number) {
        formatNumber(
            controllerName: controllerName, number: number, withPrefix: false
        )
    }

    /**
     * Formats the given sequence number with the suffix which is defined for
     * the controller which is associated to the given class.
     *
     * @param cls       the given class
     * @param number    the given number
     * @return          the formatted number
     */
    String formatWithSuffix(Class cls, int number) {
        formatWithSuffix classToDomainName(cls), number
    }

    /**
     * Loads the sequence number data for the given controller.
     *
     * @param controllerName    the given controller name
     * @return                  the sequence number data; {@code null} if no
     *                          such data are stored for the given controller
     */
    @CompileDynamic
    SeqNumber loadSeqNumber(String controllerName) {
        SeqNumber.findByControllerName controllerName
    }

    /**
     * Loads the sequence number data for the controller which is associated to
     * the given class.
     *
     * @param controllerName    the given class
     * @return                  the sequence number data; {@code null} if no
     *                          such data are stored for the controller
     */
    SeqNumber loadSeqNumber(Class cls) {
        loadSeqNumber classToDomainName(cls)
    }

    /**
     * Returns the next sequence number for the given controller formatted with
     * prefix and suffix, if any.
     *
     * @param controllerName    the given controller name
     * @return                  the formatted next sequence number
     */
    String nextFullNumber(String controllerName) {
        formatNumber controllerName: controllerName
    }

    /**
     * Returns the next sequence number for the controller which is associated
     * to the given class formatted with prefix and suffix, if any.
     *
     * @param cls   the given class
     * @return      the formatted next sequence number
     */
    String nextFullNumber(Class cls) {
        nextFullNumber classToDomainName(cls)
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
                between 'number', seq.startValue, seq.endValue
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
     * @param user  the currently logged in user
     */
    @Transactional
    void setDontShowAgain(User user) {
        user.settings['seqNumberHintYear'] = new Date().format('YYYY')
    }

    /**
     * Stores in the user settings that the hint about changing the sequence
     * number scheme should no longer be displayed.
     *
     * @param user  the currently logged in user
     */
    @Transactional
    void setNeverShowAgain(User user) {
        user.settings['seqNumberHintYear'] = '9999'
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
        GrailsClass gc = grailsApplication.getArtefactByLogicalPropertyName(
            'Domain', domainName
        )

        gc?.clazz
    }

    /**
     * Formats a sequence number as specified in the given arguments.  The
     * given argument map may contain the following keys:
     * <ul>
     *   <li>{@code controllerName}.  The name of the controller that sequence
     *   number should be returned.</li>
     *   <li>{@code number}.  The number to format; if not specified the next
     *   available sequence number for the given controller is used.</li>
     *   <li>{@code withPrefix}.  If {@code true} or not specified the prefix
     *   is added to the returned string.</li>
     *   <li>{@code withSuffix}. If {@code true} or not specified the suffix is
     *   added to the returned string.</li>
     * </ul>
     *
     * @param args  any arguments as described above
     * @return      the formatted sequence number
     */
    private String formatNumber(Map args) {
        String controllerName = args.controllerName.toString()
        Integer number = (Integer) args.number
        boolean withPrefix = (args.withPrefix == null) ? true
            : (boolean) args.withPrefix
        boolean withSuffix = (args.withSuffix == null) ? true
            : (boolean) args.withSuffix

        def seqNumberInstance = loadSeqNumber(controllerName)
        if (seqNumberInstance) {
            def s = new StringBuilder()
            if (withPrefix) s << seqNumberInstance.prefix
            if (s) s << '-'
            s << ((number == null) ? nextNumber(controllerName) : number)
            if (withSuffix && (seqNumberInstance.suffix != '')) {
                s << '-' << seqNumberInstance.suffix
            }
            s.toString()
        } else {
            ((number == null) ? 1 : number).toString()
        }
    }
}
