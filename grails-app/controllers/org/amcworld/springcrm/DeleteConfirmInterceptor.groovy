/*
 * DeleteConfirmInterceptor.groovy
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
 * The class {@code DeleteConfirmInterceptor}
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class DeleteConfirmInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    int order = 20i


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    DeleteConfirmInterceptor() {
        match action: 'delete'
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method does nothing.
     *
     * @return  always {@code true}
     */
    boolean before() {

        /*
         * Normally, no delete action request without confirmed parameter should
         * be received because the JavaScript does not send the request if the
         * user has not confirmed the deletion.  However, crafted URLs or
         * programming errors may cause this situation happen.  If so, we simply
         * redirect to the index view.
         */
        if (!params.confirmed) {
            redirect controller: controllerName, action: 'index'
            return false
        }

        true
    }
}
