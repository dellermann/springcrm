/*
 * LoginFilters.groovy
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
 * The class {@code LoginFilters} contains filters which check login and
 * access permissions.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class LoginFilters {

    //-- Public methods -------------------------

    def filters = {
        login(controller: '*', controllerExclude: 'i18n|install',
              action: '*', actionExclude: 'login|authenticate')
        {
            before = {
				if (!session?.user) {
					redirect(controller: 'user', action: 'login')
					return false
				}
            }
        }

		permission(controller: '*',
                   controllerExclude: 'about|i18n|install|notification|overview|searchable',
                   action: '*',
                   actionExclude: 'login|authenticate|logout|settings*')
        {
			before = {
				User user = session?.user
				if (user && controllerName) {
					if (!user.checkAllowedControllers([controllerName])) {
						render(status: 403)
						return false
					}
				}
			}
		}
    }
}
