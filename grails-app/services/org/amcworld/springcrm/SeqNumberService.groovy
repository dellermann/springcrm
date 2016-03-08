/*
 * SeqNumberService.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
import org.grails.core.artefact.DomainClassArtefactHandler
import org.springframework.transaction.annotation.Transactional


/**
 * The class {@code SeqNumberService} cares about a sequential numbering of
 * content items.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class SeqNumberService {

    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Retrieves the next available sequence number for the given controller
     * name.
     *
     * @param controllerName    the given controller name
     * @return                  the next available sequence number
     */
    @Transactional(readOnly = true)
    int nextNumber(String controllerName) {
        SeqNumber seq = loadSeqNumber(controllerName)
        GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
            DomainClassArtefactHandler.TYPE, controllerName
        )
        Integer num
        try {
            num = cls.clazz.'maxNumber'(seq)
        } catch (e) {
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
    @Transactional(readOnly = true)
    int nextNumber(Class cls) {
        nextNumber classToControllerName(cls)
    }

    /**
     * Returns the next sequence number for the given controller formatted with
     * prefix and suffix, if any.
     *
     * @param controllerName    the given controller name
     * @return                  the formatted next sequence number
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    String nextFullNumber(Class cls) {
        nextFullNumber classToControllerName(cls)
    }

    /**
     * Loads the sequence number data for the given controller.
     *
     * @param controllerName    the given controller name
     * @return                  the sequence number data; {@code null} if no
     *                          such data are stored for the given controller
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    SeqNumber loadSeqNumber(Class cls) {
        loadSeqNumber classToControllerName(cls)
    }

    /**
     * Formats the given sequence number as specified in the number schema for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    String format(Class cls, int number) {
        format classToControllerName(cls), number
    }

    /**
     * Formats the given sequence number with the prefix which is defined for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    @Transactional(readOnly = true)
    String formatWithPrefix(String controllerName, int number) {
        formatNumber controllerName: controllerName, number: number, withSuffix: false
    }

    /**
     * Formats the given sequence number with the prefix which is defined for
     * the controller which is associated to the given class.
     *
     * @param cls       the given class
     * @param number    the given number
     * @return          the formatted number
     */
    @Transactional(readOnly = true)
    String formatWithPrefix(Class cls, int number) {
        formatWithPrefix classToControllerName(cls), number
    }

    /**
     * Formats the given sequence number with the suffix which is defined for
     * the given controller.
     *
     * @param controllerName    the given controller name
     * @param number            the given number
     * @return                  the formatted number
     */
    @Transactional(readOnly = true)
    String formatWithSuffix(String controllerName, int number) {
        formatNumber controllerName: controllerName, number: number, withPrefix: false
    }

    /**
     * Formats the given sequence number with the suffix which is defined for
     * the controller which is associated to the given class.
     *
     * @param cls       the given class
     * @param number    the given number
     * @return          the formatted number
     */
    @Transactional(readOnly = true)
    String formatWithSuffix(Class cls, int number) {
        formatWithSuffix classToControllerName(cls), number
    }


    //-- Non-public methods ---------------------

    /**
     * Obtains the name of the controller which is associated to the given
     * class.
     *
     * @param cls   the given class
     * @return      the associated controller name
     */
    protected String classToControllerName(Class cls) {
        ArtefactHandler handler = grailsApplication.getArtefactType(cls)
        GrailsClass gc = grailsApplication.getArtefact(handler.type, cls.name)
        gc?.logicalPropertyName
    }

    /**
     * Formats a sequence number as specified in the given arguments.
     *
     * @param controllerName    the name of the controller that sequence number
     *                          should be returned
     * @param number            the number to format; if not specified the next
     *                          available sequence number for the given
     *                          controller is used
     * @param withPrefix        if {@code true} or not specified the prefix is
     *                          added to the returned string
     * @param withSuffix        if {@code true} or not specified the suffix is
     *                          added to the returned string
     * @return                  the formatted sequence number
     */
    protected String formatNumber(Map args) {
        String controllerName = args.controllerName
        Integer number = args.number
        boolean withPrefix = (args.withPrefix == null) ? true : args.withPrefix
        boolean withSuffix = (args.withSuffix == null) ? true : args.withSuffix

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
