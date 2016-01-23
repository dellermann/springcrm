/*
 * LoginFilters.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import javax.servlet.http.HttpServletResponse


/**
 * The class {@code LoginFilters} contains filters which check login and
 * access permissions.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class LoginFilters {

    //-- Instance variables ---------------------

    InstallService installService
    SecurityService securityService


    //-- Public methods -------------------------

    def filters = {
        encryptPassword(controller: 'user|install',
                        action: 'save|update|authenticate|create-admin-save')
        {
            before = {
                params.password = params.password \
                    ? securityService.encryptPassword(params.password)
                    : null
            }
        }

        login(controller: '*', controllerExclude: 'assets|help|install',
              action: '*', actionExclude: 'login|authenticate|frontend*')
        {
            before = {
                def installStatus = Config.findByName('installStatus')
                if (!installStatus?.value) {
                    installService.enableInstaller()
                    redirect controller: 'install', action: 'index'
                    return false
                }
                if (!session?.credential) {
                    redirect controller: 'user', action: 'login'
                    return false
                }
            }
        }

        install(controller: 'install', action: '*') {
            before = {
                def installStatus = Config.findByName('installStatus')
                if (installStatus?.value && installService.installerDisabled) {
                    redirect controller: 'overview', action: 'index'
                    return false
                }

                true
            }
        }

        permission(controller: '*',
                   controllerExclude: 'about|assets|dataFile|help|install|notification|overview',
                   action: '*',
                   actionExclude: 'login|authenticate|logout|settings*|frontend*')
        {
            before = {
                Credential credential = session?.credential
                if (credential && controllerName) {
                    Set<String> controllerNames = [controllerName] as Set
                    if (!credential.checkAllowedControllers(controllerNames)) {
                        render status: HttpServletResponse.SC_FORBIDDEN
                        return false
                    }
                }
            }
        }
    }
}
