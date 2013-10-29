/*
 * Helpdesk.groovy
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
 * The class {@code Helpdesk} represents an area where tickets for a particular
 * organization can be submitted.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class Helpdesk {

    //-- Class variables ------------------------

    static belongsTo = [organization: Organization]
    static constraints = {
        name blank: false, unique: true
        urlName blank: false, unique: true
        accessCode blank: false, matches: /[A-Z0-9]+/, maxSize: 10
        users minSize: 1
        dateCreated()
        lastUpdated()
    }
    static hasMany = [tickets: Ticket]
    static searchable = [only: ['name']]


    //-- Instance variables ---------------------

    String name
    String urlName
    String accessCode
    Date dateCreated
    Date lastUpdated


    //-- Properties -----------------------------

    String getName() {
        name
    }

    Set<User> getUsers() {
        HelpdeskUser.findAllByHelpdesk(this).collect { it.user } as Set
    }

    void setName(String name) {
        this.name = name
        this.urlName = name.encodeAsUrlPart()
    }

    void setUsers(Set<User> users) {
        HelpdeskUser.removeAll this
        for (User user in users) {
            HelpdeskUser.create(this, user)
        }
    }


    //-- Public methods -------------------------

    void addToUsers(User user) {
        if (!hasUser(user)) {
            HelpdeskUser.create(this, user)
        }
    }

    @Override
    boolean equals(Object obj) {
        (obj instanceof Helpdesk) ? urlName == obj.urlName : false
    }

    @Override
    int hashCode() {
        urlName ? urlName.hashCode() : 0i
    }

    boolean hasUser(User user) {
        HelpdeskUser.countByHelpdeskAndUser(this, user) > 0
    }

    void removeFromUsers(User user) {
        HelpdeskUser.remove this, user
    }

    @Override
    String toString() {
        name
    }
}

