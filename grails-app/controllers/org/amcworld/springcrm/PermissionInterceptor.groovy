/*
 * PermissionInterceptor.groovy
 *
 * Copyright (c) 2011-2019, Daniel Ellermann
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
import javax.servlet.http.HttpServletResponse


/**
 * The class {@code PermissionInterceptor} controls access to controllers
 * depending on the user access permissions.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class PermissionInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    PermissionInterceptor() {
        matchAll()
            .excludes(
                controller: ~/(about|assets|dataFile|help|install|notification|overview|search)/
            )
            .excludes(
                action: ~/(login|authenticate|logout|settings.*|frontend.*)/
            )
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method redirects to the
     * installer if the application has not been initialized yet and redirects to
     * the login page if the user has no session.
     *
     * @return  {@code true} to call the action of the controller; {@code false}
     *          otherwise
     */
    boolean before() {
        Credential credential = (Credential) session?.getAttribute('credential')
        if (credential && controllerName) {
            Set<String> controllerNames = [controllerName] as Set
            if (!credential.checkAllowedControllers(controllerNames)) {
                render status: HttpServletResponse.SC_FORBIDDEN
                return false
            }
        }

        true
    }
}
