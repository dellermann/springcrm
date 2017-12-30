/*
 * Credential.groovy
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

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.bson.types.ObjectId


/**
 * The class {@code Credential} represents an immutable representation of most
 * of the user data.  It should be used instead of {@code User} in order to be
 * stored in the user session.
 *
 * @author	Daniel Ellermann
 * @version 3.0
 * @see     User
 * @since   2.0
 */
@CompileStatic
final class Credential {

    //-- Fields ---------------------------------

    /**
     * The ID of the user with this credential.
     */
    final ObjectId id

    /**
     * The user name of this credential for authentication.
     */
    final String username

    /**
     * The first name of the user with this credential.
     */
    final String firstName

    /**
     * The last name of the user with this credential.
     */
    final String lastName

    /**
     * The office phone number of the user with this credential.
     */
    final String phone

    /**
     * The home phone number of the user with this credential.
     */
    final String phoneHome

    /**
     * The mobile phone number of the user with this credential.
     */
    final String mobile

    /**
     * The fax number of the user with this credential.
     */
    final String fax

    /**
     * The e-mail address of the user with this credential.
     */
    final String email

    /**
     * Whether or not the user with this credential is administrator and has
     * access permissions to all components of this software.
     */
    final boolean admin

    /**
     * A set of allowed modules of the user with this credential.
     */
    final EnumSet<Module> allowedModules

    /**
     * A set of all controllers the user with this credential has access to.
     */
    final Set<String> allowedControllers

    /**
     * The full name of this user.  The full name consists of the first and the
     * last name of the user separated by a space character.
     */
    final String fullName

    /**
     * The settings of this user.
     */
    Map<String, Object> settings


    //-- Constructors ---------------------------

    Credential(User user) {
        ObjectId id = user.id
        if (!id) {
            throw new IllegalArgumentException('User has no ID set.')
        }

        this.id = id
        username = user.username
        firstName = user.firstName
        lastName = user.lastName
        phone = user.phone
        phoneHome = user.phoneHome
        mobile = user.mobile
        fax = user.fax
        email = user.email
        fullName = computeFullName()
        settings = user.settings
    }


    //-- Properties -----------------------------

    /**
     * Gets set of all controllers the user with this credential has access to.
     *
     * @return  the set of allowed controllers; the method returns a copy of
     *          the internal set
     */
    Set<String> getAllowedControllers() {
        new HashSet<String>(allowedControllers)
    }

    /**
     * Gets a set of allowed modules this credential permits access.
     *
     * @return  the set of allowed modules; the method returns a copy of the
     *          internal set
     */
    EnumSet<Module> getAllowedModules() {
        EnumSet.copyOf(allowedModules)
    }


    //-- Public methods -------------------------

    /**
     * Checks whether or not the user has permission to access at least one of
     * the given controllers.
     *
     * @param controllers   the names of the controllers to check; may be
     *                      {@code null}
     * @return              {@code true} if the user may access at least one of
     *                      the given controllers; {@code false} otherwise
     */
    boolean checkAllowedControllers(Set<String> controllers) {
        admin || allowedControllers.intersect(controllers ?: [] as Set)
    }

    /**
     * Checks whether or not the user has permission to access at least one of
     * the given modules.
     *
     * @param modules   the names of the modules to check; may be {@code null}
     * @return          {@code true} if the user may access at least one of the
     *                  given modules; {@code false} otherwise
     */
    boolean checkAllowedModules(Set<Module> modules) {
        admin || allowedModules.intersect(modules ?: EnumSet.noneOf(Module))
    }

    @Override
    boolean equals(Object o) {
        o instanceof Credential && o.id == id
    }

    @Override
    int hashCode() {
        id as int
    }

    /**
     * Loads the user instance belonging to this credential.
     * <p>
     * For performance reasons, you should save the return value in a variable
     * because it was obtained from data source via {@code loadUser()}.
     * Furthermore, this method may be used within a Hibernate session, only.
     *
     * @return  the user instance
     */
    @CompileDynamic
    User loadUser() {
        User.get id
    }

    @Override
    String toString() {
        fullName
    }


    //-- Non-public methods ---------------------

    /**
     * Computes the full name of this user.  The full name consists of the
     * first and the last name of the user separated by a space character.
     *
     * @return  the full name of the user
     */
    private String computeFullName() {
        String fn = firstName?.trim()
        String ln = lastName?.trim()
        StringBuilder buf = new StringBuilder()
        if (fn) buf << fn
        if (fn && ln) buf << ' '
        if (ln) buf << ln

        buf.toString()
    }
}
