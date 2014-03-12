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


/**
 * The class {@code HelpdeskUser} represents a third-party relation class
 * between {@code Helpdesk} and {@code User}.  You must not save or delete
 * instances of this class directly.  Use the methods in {@code Helpdesk}
 * instead.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
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
        (obj instanceof HelpdeskUser) \
            ? helpdesk == obj.helpdesk && user == obj.user \
            : false
    }

    @Override
    int hashCode() {
        "${helpdesk?.id}/${user?.id}".hashCode()
    }
}
