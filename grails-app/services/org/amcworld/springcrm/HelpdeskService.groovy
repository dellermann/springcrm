/*
 * HelpdeskService.groovy
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

import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod


class HelpdeskService {

    //-- Public methods -------------------------

    boolean saveHelpdesk(Helpdesk helpdesk, Map params, boolean flush = true) {
        BindDynamicMethod bind = new BindDynamicMethod()
        bind.invoke helpdesk, 'bind', [
            helpdesk, params, [exclude: 'users']
        ] as Object[]

        Set<User> users = [] as Set
        String [] userIds = params.users
        if (userIds) {
            for (String userId in userIds) {
                User user = User.get(userId as Long)
                if (user) users << user
            }
        }
        helpdesk.users = users
        helpdesk.save flush: flush
    }
}
