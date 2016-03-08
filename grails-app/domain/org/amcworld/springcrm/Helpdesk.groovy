/*
 * Helpdesk.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
 * @version 2.1
 * @since   2.0
 */
class Helpdesk {

    //-- Class fields ---------------------------

    static belongsTo = [organization: Organization]
    static constraints = {
        name blank: false, unique: true
        urlName blank: false, unique: true
        accessCode blank: false, matches: /[A-Z0-9]+/, maxSize: 10
        users minSize: 1
    }
    static hasMany = [tickets: Ticket]
    static transients = ['users']


    //-- Fields ---------------------------------

    String name
    String urlName
    String accessCode
    Set<User> users
    Date dateCreated
    Date lastUpdated


    //-- Properties -----------------------------

    String getName() {
        name
    }

    Set<User> getUsers() {
        if (users == null) {
            users = (Helpdesk.exists(id) ? HelpdeskUser.findAllByHelpdesk(this).collect { it.user } : []) as Set
        }
        users
    }

    void setName(String name) {
        this.name = name
        this.urlName = java.net.URLEncoder.encode(name).toLowerCase()
    }

    void setUsers(Set<User> users) {
        this.users = users
    }


    //-- Public methods -------------------------

    def afterInsert() {
        saveUsers()
    }

    def afterUpdate() {
        removeAllUsers()
        saveUsers()
    }

    def beforeDelete() {
        removeAllUsers()
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof Helpdesk && urlName == obj.urlName
    }

    @Override
    int hashCode() {
        urlName ? urlName.hashCode() : 0i
    }

    boolean hasUser(User user) {
        HelpdeskUser.countByHelpdeskAndUser(this, user) > 0
    }

    @Override
    String toString() {
        name
    }


    //-- Non-public methods ---------------------

    protected void removeAllUsers() {
        executeUpdate(
            'delete from HelpdeskUser where helpdesk=:helpdesk',
            [helpdesk: this]
        )
    }

    protected void saveUsers() {
        for (User user in users) {
            HelpdeskUser helpdeskUser =
                new HelpdeskUser(helpdesk: this, user: user)
            helpdeskUser.save flush: false, insert: true, failOnError: true
        }
    }
}
