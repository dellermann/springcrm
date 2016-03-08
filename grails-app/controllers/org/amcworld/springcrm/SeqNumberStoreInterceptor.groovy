/*
 * SeqNumberStoreInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic


@CompileStatic
class SeqNumberStoreInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    SeqNumberService seqNumberService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    SeqNumberStoreInterceptor() {
        match action: 'save'
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method stores the next
     * available sequence number to the instance which is stored in the model.
     *
     * @return  always {@code true}
     */
    boolean after() {
        Map<String, Object> model = getModel()
        if (model) {
            def inst = model["${controllerName}Instance".toString()]
            if (inst instanceof NumberedDomain) {
                NumberedDomain nd = (NumberedDomain) inst
                if (nd.number == 0i) {
                    nd.number = seqNumberService.nextNumber(controllerName)
                }
            }
        }

        true
    }

    /**
     * Called before the action is executed.  The method sets the number
     * parameter to zero if the {@code autoNumber} flag is submitted.
     *
     * @return  always {@code true}
     */
    boolean before() {
        if (params.number != null && params.autoNumber) {
            params.number = 0i
        }

        true
    }
}
