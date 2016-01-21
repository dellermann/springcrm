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


/**
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class User implements Cloneable {

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
        admin()
        allowedModules nullable: true
        dateCreated()
        lastUpdated()
    }
    static mapping = {
        allowedModules type: 'text'
        table 'user_data'
        userName index: 'user_name'
    }
    static transients = [
        'allowedControllers', 'allowedModulesAsSet', 'allowedModulesNames',
        'fullName', 'rawSettings', 'settings'
    ]


    //-- Fields ---------------------------------

    String userName
    String password
    String firstName
    String lastName
    String phone
    String phoneHome
    String mobile
    String fax
    String email
    boolean admin
    String allowedModules
    Date dateCreated
    Date lastUpdated
    UserSettings settings
    Set<String> allowedControllers


    //-- Properties -----------------------------

    /**
     * Gets a set of controllers the user is permitted to access.
     *
     * @return  the names of the controllers the user may access
     */
    Set<String> getAllowedControllers() {
        if (allowedControllers == null) {
            allowedControllers = Module.resolveModules(allowedModulesAsSet)
        }

        allowedControllers
    }

    /**
     * Gets a set of allowed modules of this user.
     *
     * @return  the allowed modules
     */
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
     * Sets the set of the names of the allowed modules of this user.
     *
     * @param moduleNames   the names of the allowed modules that should be
     *                      set
     * @since               2.0
     */
    void setAllowedModulesNames(Set<String> moduleNames) {
        allowedModulesAsSet = Module.modulesByName(moduleNames)
    }

    /**
     * Gets the full name of this user.  The full name consists of the first
     * and the last name of the user separated by a space character.
     *
     * @return  the full name of the user
     */
    String getFullName() {
        "${firstName ?: ''} ${lastName ?: ''}".trim()
    }

    List<UserSetting> getRawSettings() {
        UserSetting.findAllByUser this
    }


    //-- Public methods -------------------------

    def afterLoad() {
        settings = new UserSettings(this)
    }

    /**
     * Checks whether or not the user has permission to access at least one of
     * the given controllers.
     *
     * @param controllers   the names of the controllers to check
     * @return              {@code true} if the user may access at least one of
     *                      the given controllers; {@code false} otherwise
     */
    boolean checkAllowedControllers(Set<String> controllers) {
        admin || getAllowedControllers().intersect(controllers)
    }

    /**
     * Checks whether or not the user has permission to access at least one of
     * the given modules.
     *
     * @param modules   the names of the modules to check
     * @return          {@code true} if the user may access at least one of the
     *                  given modules; {@code false} otherwise
     */
    boolean checkAllowedModules(Set<Module> modules) {
        admin || allowedModulesAsSet.intersect(modules)
    }

    @Override
    User clone() {
        (User) super.clone()
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
    String toString() {
        fullName
    }
}
