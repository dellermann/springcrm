/*
 * User.groovy
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

import groovy.transform.CompileStatic


/**
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class User {

    //-- Class fields ---------------------------

    static constraints = {
        userName blank: false, unique: true
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
    static mapping = {
        allowedModules type: 'text'
        table 'user_data'
        userName index: 'user_name'
    }
    static transients = [
        'allowedModulesAsSet', 'allowedModulesNames', 'fullName',
        'rawSettings', 'settings'
    ]


    //-- Fields ---------------------------------

    /**
     * The user name of this user used for authentication.
     */
    String userName

    /**
     * The encrypted password of this user.
     */
    String password

    /**
     * The first name of this user.
     */
    String firstName

    /**
     * The last name of this user.
     */
    String lastName

    /**
     * The office phone number of this user.
     */
    String phone

    /**
     * The home phone number of this user.
     */
    String phoneHome

    /**
     * The mobile phone number of this user.
     */
    String mobile

    /**
     * The fax number of this user.
     */
    String fax

    /**
     * The e-mail address of this user.
     */
    String email

    /**
     * Whether or not this user is administrator and has access permissions to
     * all components of this software.
     */
    boolean admin

    /**
     * A comma separated list of module names this user may access.  This value
     * is only used if this user is no administrator.
     */
    String allowedModules

    /**
     * The timestamp when this sales item has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this sales item has been modified.
     */
    Date lastUpdated

    /**
     * The underlying user settings which allow accessing the settings of this
     * user as a {@code Map}.
     */
    private UserSettings settings


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
        userName = user.userName
        firstName = user.firstName
        lastName = user.lastName
        phone = user.phone
        phoneHome = user.phoneHome
        mobile = user.mobile
        fax = user.fax
        email = user.email
        admin = user.admin
        allowedModules = user.allowedModules
        settings = user.settings
    }


    //-- Properties -----------------------------

    /**
     * Gets a set of allowed modules of this user.
     *
     * @return  the allowed modules
     */
    @CompileStatic
    EnumSet<Module> getAllowedModulesAsSet() {
        if (!allowedModules) {
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
    @CompileStatic
    void setAllowedModulesAsSet(EnumSet<Module> modules) {
        allowedModules = modules?.join(',') ?: ''
    }

    /**
     * Gets a set of the names of the allowed modules of this user.
     *
     * @return  the names of the allowed modules
     * @since   2.0
     */
    @CompileStatic
    Set<String> getAllowedModulesNames() {
        EnumSet<Module> modules = allowedModulesAsSet
        Set<String> res = new HashSet<String>(modules.size())
        for (Module module : modules) {
            res << module.toString()
        }

        res
    }

    /**
     * Sets the set of the names of the allowed modules of this user.
     *
     * @param moduleNames   the names of the allowed modules that should be
     *                      set
     * @since               2.0
     */
    @CompileStatic
    void setAllowedModulesNames(Set<String> moduleNames) {
        allowedModulesAsSet = Module.modulesByName(moduleNames)
    }

    /**
     * Gets the full name of this user.  The full name consists of the first
     * and the last name of the user separated by a space character.
     *
     * @return  the full name of the user
     */
    @CompileStatic
    String getFullName() {
        String fn = firstName?.trim()
        String ln = lastName?.trim()
        StringBuilder buf = new StringBuilder()
        if (fn) buf << fn
        if (fn && ln) buf << ' '
        if (ln) buf << ln

        buf.toString()
    }

    /**
     * Gets the raw settings associated to this user.
     *
     * @return  the list of raw settings objects
     */
    List<UserSetting> getRawSettings() {
        UserSetting.findAllByUser this
    }

    /**
     * Gets the settings of this user.
     *
     * @return  the user settings
     */
    @CompileStatic
    UserSettings getSettings() {
        settings
    }


    //-- Public methods -------------------------

    /**
     * Called after this user object has been inserted into the underlying
     * persistence layer.  The method initializes the embedded
     * {@code UserSettings} object.
     *
     * @since 2.1
     */
    @CompileStatic
    def afterInsert() {
        settings = new UserSettings(this)
    }

    /**
     * Called after this user object has been loaded completely from
     * persistence layer.  The method initializes the embedded
     * {@code UserSettings} object.
     */
    @CompileStatic
    def afterLoad() {
        settings = new UserSettings(this)
    }

    @Override
    boolean equals(Object o) {
        o instanceof User && o.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    @CompileStatic
    String toString() {
        fullName
    }
}
