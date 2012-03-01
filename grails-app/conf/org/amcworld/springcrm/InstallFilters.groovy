/*
 * InstallFilters.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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
 * The class {@code InstallFilters} catches requests to the installer and
 * redirects them to the login page if the installer is disabled.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class InstallFilters {

    //-- Instance variables ---------------------

    def installService


    //-- Public methods -------------------------

    def filters = {
        security(controller: 'install', action: '*') {
            before = {
                if (installService.isInstallerDisabled()) {
                    redirect(uri: '/')
                    return false
                }

                return true
            }
        }
    }
}
