/*
 * HelpdeskUser.groovy
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


class HelpdeskUser implements Serializable {

    //-- Class variables ------------------------

    static mapping = {
        id composite: ['helpdesk', 'user']
        table 'helpdesk_users'
        version false
    }


    //-- Instance variables ---------------------

    Helpdesk helpdesk
    User user


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        (obj instanceof HelpdeskUser) ? helpdesk == obj.helpdesk && user == obj.user : false
    }

    @Override
    int hashCode() {
        "${helpdesk?.id}/${user?.id}".hashCode()
    }

    static HelpdeskUser create(Helpdesk helpdesk, User user,
                               boolean flush = false)
    {
        HelpdeskUser helpdeskUser = new HelpdeskUser(
            helpdesk: helpdesk, user: user
        )
        helpdeskUser.save flush: flush, insert: true
    }

    static boolean remove(Helpdesk helpdesk, User user, boolean flush = false)
    {
        HelpdeskUser helpdeskUser = HelpdeskUser.findByHelpdeskAndUser(
            helpdesk, user
        )
        helpdeskUser ? helpdeskUser.delete(flush: flush) : false
    }

    static void removeAll(Helpdesk helpdesk) {
        executeUpdate(
            'delete from HelpdeskUser where helpdesk=:helpdesk',
            [helpdesk: helpdesk]
        )
    }
}
