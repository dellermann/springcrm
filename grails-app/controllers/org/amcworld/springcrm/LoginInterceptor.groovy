/*
 * LoginInterceptor.groovy
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
 * The class {@code LoginInterceptor} ensures that the installer is called if
 * the application has not been initialized yet.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class LoginInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    ConfigService configService
    InstallService installService
    int order = 30


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    LoginInterceptor() {
        matchAll()
            .excludes(controller: ~/(assets|help|install)/)
            .excludes(action: ~/frontend.*/)
    }

    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method redirects to the
     * installer if the application has not been initialized yet and redirects
     * to the login page if the user has no session.
     *
     * @return  {@code true} to call the action of the controller;
     *          {@code false} otherwise
     */
    boolean before() {
        if (configService.getInteger('installStatus')) {
            return true
        }

        installService.enableInstaller()
        redirect controller: 'install', action: 'index'

        false
    }
}
