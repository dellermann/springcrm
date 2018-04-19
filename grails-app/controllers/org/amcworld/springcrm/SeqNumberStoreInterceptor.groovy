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
import grails.core.GrailsClass
import groovy.transform.CompileStatic
import org.grails.core.artefact.DomainClassArtefactHandler


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
     * Called before the action is executed.  The method obtains the next
     * sequence number if the {@code autoNumber} flag has been submitted or the
     * submitted number is zero.
     *
     * @return  always {@code true}
     */
    boolean before() {
        if (!params.int('number') || params.autoNumber) {
            GrailsClass gc = grailsApplication.getArtefactByLogicalPropertyName(
                DomainClassArtefactHandler.TYPE, controllerName
            )
            Class<?> clazz = gc?.clazz
            if (clazz != null && NumberedDomain.isAssignableFrom(clazz)) {
                params.number = seqNumberService.nextNumber(controllerName)
            }
        }

        true
    }
}
