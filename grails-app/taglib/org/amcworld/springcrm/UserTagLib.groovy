/*
 * UserTagLib.groovy
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
 * The class {@code UserTagLib} represents tags for login.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class UserTagLib {

    //-- Public methods -------------------------

	/**
	 * Renders an area to display the currently logged in user.
	 */
	def loginControl = {
		if (request.getSession(false) && session.user) {
			out << "${message(code: 'default.welcome', args: [session.user.fullName])} "
			out << "${link(controller: 'user', action: 'logout'){message(code: 'default.logout')}}"
		}
	}

	/**
	 * Renders the value of a user setting.
	 *
	 * @attr key REQUIRED	the name of the user setting
	 */
	def userSetting = { attrs, body ->
        def settings = session.user?.settings
        if (settings) {
            out << settings[attrs.key]
        }
	}
}
