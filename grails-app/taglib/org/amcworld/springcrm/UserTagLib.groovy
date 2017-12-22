/*
 * UserTagLib.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.plugin.springsecurity.SpringSecurityService


/**
 * The class {@code UserTagLib} represents tags for login.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class UserTagLib {

    //-- Fields ---------------------------------

    SpringSecurityService springSecurityService


    //-- Public methods -------------------------

    /**
     * Renders an area to display the currently logged in user.
     */
    def loginControl = {
        User user = springSecurityService.currentUser as User
        if (user != null) {
            out << '<small>' << user.fullName << ' [' << user.username <<
                ']</small>'
            out << form(controller: 'logout', method: 'post') {
                """
                <button type="submit" class="btn btn-warning btn-xs">
                  <i class="fa fa-sign-out"></i>
                  ${message(code: 'default.logout')}
                </button>
                """
            }
        }
    }

    /**
     * Renders the value of a user setting.
     *
     * @attr key REQUIRED   the name of the user setting
     */
    def userSetting = { attrs, body ->
        Map<String, Object> settings =
            ((User) springSecurityService.currentUser)?.settings
        if (settings) {
            out << settings[attrs.key.toString()]
        }
    }
}
