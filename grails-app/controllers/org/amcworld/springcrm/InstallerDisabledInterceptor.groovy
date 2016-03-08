/*
 * InstallerDisabledInterceptor.groovy
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


/**
 * The class {@code InstallerDisabledInterceptor} ensures that the user is
 * redirected to the overview page if the installer is disabled.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
class InstallerDisabledInterceptor {

    //-- Fields ---------------------------------

    InstallService installService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    InstallerDisabledInterceptor() {
        match controller: 'install'
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method redirects to the
     * overview page if the application has been initialized and the installer is
     * disabled.
     *
     * @return  {@code true} to call the action of the controller; {@code false}
     *          otherwise
     */
    boolean before() {
        def installStatus = Config.findByName('installStatus')
        if (installStatus?.value && installService.installerDisabled) {
            redirect controller: 'overview', action: 'index'
            return false
        }

        true
    }

    /**
     * Called after the action has been executed.  The method does nothing.
     *
     * @return  always {@code true}
     */
    boolean after() { true }

    /**
     * Called after the view of the action has been generated.  The method does
     * nothing.
     */
    void afterView() { /* no-op */ }
}
