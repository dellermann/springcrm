/*
 * LoginFilters.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * @version 1.4
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
                params.password = params.password ? securityService.encryptPassword(params.password) : null
            }
        }

        login(controller: '*', controllerExclude: 'help|i18n|install',
              action: '*', actionExclude: 'login|authenticate|frontend*')
        {
            before = {
                def installStatus = Config.findByName('installStatus')
                if (!installStatus?.value) {
                    installService.enableInstaller()
                    redirect controller: 'install', action: 'index'
                    return false
                }
                if (!session?.user) {
                    redirect controller: 'user', action: 'login'
                    return false
                }
            }
        }

        install(controller: 'install', action: '*') {
            before = {
                def installStatus = Config.findByName('installStatus')
                if (installStatus?.value && installService.installerDisabled) {
                    redirect uri: '/'
                    return false
                }

                true
            }
        }

        permission(controller: '*',
                   controllerExclude: 'about|dataFile|help|i18n|install|notification|overview|searchable',
                   action: '*',
                   actionExclude: 'login|authenticate|logout|settings*|frontend*')
        {
            before = {
                User user = session?.user
                if (user && controllerName) {
                    if (!user.checkAllowedControllers([controllerName])) {
                        render status: HttpServletResponse.SC_FORBIDDEN
                        return false
                    }
                }
            }
        }
    }
}
