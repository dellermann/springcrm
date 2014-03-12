/*
 * HelpdeskService.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


class HelpdeskService {

    //-- Public methods -------------------------

    def saveHelpdesk(Helpdesk helpdesk, Map params, boolean flush = true) {
        Map props = params

        String [] userIds = props.remove('users')
        Set<User> users = [] as Set
        if (userIds) {
            for (String userId in userIds) {
                users << User.get(userId as Long)
            }
        }
        helpdesk.users = users

        helpdesk.properties = props
        helpdesk.save flush: flush
    }
}
