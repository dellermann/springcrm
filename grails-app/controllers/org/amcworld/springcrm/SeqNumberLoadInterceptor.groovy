/*
 * SeqNumberLoadInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic


/**
 * The class {@code SeqNumberLoadInterceptor} makes information about the next
 * sequence number available when the form of a domain model class is
 * displayed.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class SeqNumberLoadInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    SeqNumberService seqNumberService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    SeqNumberLoadInterceptor() {
        match action: ~/(create|copy|edit|editPayment|find|get|save|update)/
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method stores the next
     * available sequence number to the instance which is stored in the model
     * and adds the following data to the model:
     * <ul>
     *   <li>{@code fullNumber}. the full number of the domain model
     *   instance</li>
     *   <li>{@code seqNumberService}. the instance of the
     *   {@code SeqNumberService} class</li>
     *   <li>{@code seqNumberPrefix}. the prefix of the sequence number</li>
     *   <li>{@code seqNumberSuffix}. the suffix of the sequence number</li>
     * </ul>
     *
     * @return  always {@code true}
     */
    boolean after() {
        Map<String, Object> model = getModel()
        if (model) {
            SeqNumber seqNumber = seqNumberService.get(controllerName)
            if (seqNumber) {
                def inst = model[controllerName]
                NumberedDomain nd = inst instanceof NumberedDomain \
                    ? (NumberedDomain) inst
                    : null
                if (nd != null) {
                    if (actionName == 'create' || actionName == 'copy') {
                        nd.number = seqNumberService.nextNumber(controllerName)
                    }
                    model.fullNumber = seqNumberService.getFullNumber(nd)
                }

                // XXX we must add the instance of SeqNumberService to the
                // model because GSON files don't support dependency injection,
                // yet.
                model.seqNumberService = seqNumberService
                model.seqNumberPrefix = seqNumber.prefix
                model.seqNumberSuffix = seqNumber.suffix
            }
        }

        true
    }
}
