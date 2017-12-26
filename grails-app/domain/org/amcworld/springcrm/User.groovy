/*
 * User.groovy
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

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId


/**
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@GrailsCompileStatic
@EqualsAndHashCode(includes = 'username')
class User implements Serializable {

    //-- Constants ------------------------------

    private static final long serialVersionUID = 1L


    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'username', 'firstName', 'lastName', 'phone', 'phoneHome', 'mobile',
        'fax', 'email'
    ].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        username blank: false, unique: true
        password blank: false, password: true
        firstName blank: false
        lastName blank: false
        phone nullable: true, maxSize: 40
        phoneHome nullable: true, maxSize: 40
        mobile nullable: true, maxSize: 40
        fax nullable: true, maxSize: 40
        email blank: false, email: true
        allowedModules nullable: true
    }
    static embedded = ['settings']
    static hasMany = [authorities: RoleGroup]
    static mapping = {
        sort 'username'
        username index: true, indexAttributes: [unique: true, dropDups: true]
    }
    static transients = [
        'allowedModulesAsSet', 'allowedModulesNames', 'fullName'
    ]


    //-- Fields ---------------------------------

    /**
     * Whether or not this user account has expired.
     */
    boolean accountExpired

    /**
     * Whether or not this user account has been locked.
     */
    boolean accountLocked

    /**
     * The roles which have been associated to this user.
     */
    Set<RoleGroup> authorities

    /**
     * The timestamp when this user has been created.
     */
    Date dateCreated

    /**
     * The e-mail address of this user.
     */
    String email

    /**
     * Whether or not this user is enabled.
     */
    boolean enabled = true

    /**
     * The fax number of this user.
     */
    String fax

    /**
     * The first name of this user.
     */
    String firstName

    /**
     * The ID of the user.
     */
    ObjectId id

    /**
     * The last name of this user.
     */
    String lastName

    /**
     * The timestamp when this user has been modified.
     */
    Date lastUpdated

    /**
     * The mobile phone number of this user.
     */
    String mobile

    /**
     * The encrypted password of this user.
     */
    String password

    /**
     * Whether or not the password has expired.
     */
    boolean passwordExpired

    /**
     * The office phone number of this user.
     */
    String phone

    /**
     * The home phone number of this user.
     */
    String phoneHome

    /**
     * The settings of this user.
     */
    Map<String, Object> settings = [: ]

    /**
     * The user name of this user used for authentication.
     */
    String username

    /**
     * Whether or not this user is administrator and has access permissions to
     * all components of this software.
     */
    boolean admin


    //-- TODO ------------------
    /**
     * A comma separated list of module names this user may access.  This value
     * is only used if this user is no administrator.
     */
    String allowedModules


    //-- Constructors ---------------------------

    /**
     * Creates an empty user.
     */
    User() {}

    /**
     * Creates a user using the data of the given one.
     *
     * @param user  the given user
     * @since       2.0
     */
    User(User user) {
        username = user.username
        firstName = user.firstName
        lastName = user.lastName
        phone = user.phone
        phoneHome = user.phoneHome
        mobile = user.mobile
        fax = user.fax
        email = user.email
        enabled = user.enabled
        authorities = user.authorities
        settings = user.settings
    }


    //-- Properties -----------------------------

    /**
     * Gets a set of allowed modules of this user.
     *
     * @return  the allowed modules
     */
    EnumSet<Module> getAllowedModulesAsSet() {
        if (!allowedModules?.trim()) {
            return EnumSet.noneOf(Module)
        }

        Module.modulesByName allowedModules.split(',') as List
    }

    /**
     * Sets a set of allowed modules of this user.
     *
     * @param modules   the allowed modules that should be set; may be
     *                  {@code null}
     */
    void setAllowedModulesAsSet(EnumSet<Module> modules) {
        allowedModules = modules?.join(',') ?: ''
    }

    /**
     * Gets a set of the names of the allowed modules of this user.
     *
     * @return  the names of the allowed modules
     * @since   2.0
     */
    Set<String> getAllowedModulesNames() {
        EnumSet<Module> modules = allowedModulesAsSet
        Set<String> res = new HashSet<String>(modules.size())
        for (Module module : modules) {
            res << module.toString()
        }

        res
    }

    /**
     * Sets the set of the names of the allowed modules of the user.
     *
     * @param moduleNames   the names of the allowed modules that should be
     *                      set
     * @since               2.0
     */
    void setAllowedModulesNames(Set<String> moduleNames) {
        allowedModulesAsSet = Module.modulesByName(moduleNames)
    }

    /**
     * Gets the full name of the user.  The full name consists of the first
     * and the last name of the user separated by a space character.
     *
     * @return  the full name of the user
     */
    String getFullName() {
        String fn = firstName?.trim()
        String ln = lastName?.trim()

        StringBuilder buf = new StringBuilder()
        if (fn) buf << fn
        if (fn && ln) buf << ' '
        if (ln) buf << ln

        buf.toString()
    }


    //-- Public methods -------------------------

    @Override
    String toString() {
        fullName
    }
}
