/*
 * Helpdesk.groovy
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


/**
 * The class {@code Helpdesk} represents an area where tickets for a particular
 * organization can be submitted.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.0
 */
class Helpdesk {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = ['name'].asImmutable()


    //-- Class fields ---------------------------

    static belongsTo = [organization: Organization]
    static constraints = {
        name blank: false, unique: true
        urlName blank: false, unique: true
        accessCode blank: false, matches: /[A-Z0-9]+/, maxSize: 10
        organization nullable: true
        users minSize: 1
    }
    static hasMany = [tickets: Ticket]
    static transients = ['users']


    //-- Fields ---------------------------------

    /**
     * The name of the helpdesk.
     */
    String name

    /**
     * The name of the helpdesk used in URLs to access the frontend view.  The
     * URL name is computed automatically from property {@code name}.
     */
    String urlName

    /**
     * An access code which improves security when accessing the frontend view.
     */
    String accessCode

    /**
     * The users which handle tickets of this helpdesk.
     */
    Set<User> users

    /**
     * The timestamp when this helpdesk has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this helpdesk has been modified.
     */
    Date lastUpdated


    //-- Constructors ---------------------------

    /**
     * Creates an empty helpdesk.
     */
    Helpdesk() {}

    /**
     * Creates a copy of the given helpdesk.
     *
     * @param h the given helpdesk
     */
    Helpdesk(Helpdesk h) {
        users = new HashSet<>(h.users)
        organization = h.organization
    }


    //-- Properties -----------------------------

    String getName() {
        name
    }

    Set<User> getUsers() {
        if (users == null) {
            users = (
                exists(id) \
                    ? HelpdeskUser.findAllByHelpdesk(this).collect { it.user }
                    : []
            ) as Set
        }

        users
    }

    void setName(String name) {
        this.name = name = name?.trim()
        this.urlName = name == null ? null
            : URLEncoder.encode(
                name.replaceAll(/[^0-9a-zA-Z]/, '-'), 'utf-8'
            ).toLowerCase()
    }

    void setUsers(Set<User> users) {
        this.users = users
    }


    //-- Public methods -------------------------

    /**
     * Called after this helpdesk is inserted.  The method saves all user
     * associations.
     *
     * @return  no return value
     */
    def afterInsert() {
        saveUsers()
    }

    /**
     * Called after this helpdesk is updated.  The method removes all user
     * associations and sets them anew.
     *
     * @return  no return value
     */
    def afterUpdate() {
        removeAllUsers()
        saveUsers()
    }

    /**
     * Called before this helpdesk is deleted.  The method removes all user
     * associations.
     *
     * @return  no return value
     */
    def beforeDelete() {
        removeAllUsers()
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof Helpdesk && urlName == obj.urlName
    }

    /**
     * Gets all helpdesk the given user is assigned to.
     *
     * @param user  the given user
     * @return      a list of helpdesks
     * @since 2.2
     */
    static List<Helpdesk> findByUser(User user) {
        HelpdeskUser.findByUser(user)*.helpdesk.unique()
    }

    @Override
    int hashCode() {
        urlName ? urlName.hashCode() : 0i
    }

    /**
     * Checks whether or not this helpdesk is associated to the given user.
     *
     * @param user  the given user
     * @return      {@code true} if the given user has been associated to this
     *              helpdesk; {@code false} otherwise
     */
    boolean hasUser(User user) {
        HelpdeskUser.countByHelpdeskAndUser(this, user) > 0
    }

    /**
     * Checks whether or not this helpdesk is dedicated to end users.
     *
     * @return  {@code true} if this helpdesk is dedicated to end users;
     *          {@code false} otherwise
     * @since   2.1
     */
    boolean isForEndUsers() {
        organization == null
    }

    @Override
    String toString() {
        name ?: ''
    }


    //-- Non-public methods ---------------------

    /**
     * Removes all users associated to the given helpdesk.
     */
    private void removeAllUsers() {
        executeUpdate(
            'delete from HelpdeskUser where helpdesk = :helpdesk',
            [helpdesk: this]
        )
    }

    /**
     * Saves the users of this helpdesk as {@code HelpdeskUser} objects.
     */
    private void saveUsers() {
        for (User user in users) {
            HelpdeskUser helpdeskUser =
                new HelpdeskUser(helpdesk: this, user: user)
            helpdeskUser.save flush: false, insert: true, failOnError: true
        }
    }
}
