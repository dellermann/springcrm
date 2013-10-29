/*
 * User.groovy
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
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class User implements Cloneable {

    //-- Class variables ------------------------

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
    static hasMany = [rawSettings: UserSetting]
    static mapping = {
        allowedModules type: 'text'
        table 'user_data'
        userName index: 'user_name'
    }
    static transients = [
        'allowedControllers', 'allowedModulesAsList', 'fullName', 'settings'
    ]


    //-- Instance variables ---------------------

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
    Collection<UserSetting> rawSettings
    Set<String> allowedControllers


    //-- Properties -----------------------------

    /**
     * Gets a set of controllers the user is permitted to access.
     *
     * @return  the names of the controllers the user may access
     */
    Set<String> getAllowedControllers() {
        if (allowedControllers == null) {
            List<String> moduleNames = allowedModulesAsList
            allowedControllers =
                moduleNames ? Modules.resolveModules(moduleNames) : []
        }
        allowedControllers
    }

    List<String> getAllowedModulesAsList() {
        allowedModules ? allowedModules.split(',') : []
    }

    String getFullName() {
        "${firstName ?: ''} ${lastName ?: ''}".trim()
    }

    void setAllowedModulesAsList(List<String> l) {
        allowedModules = l.join(',')
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
     * @return              {@code true} if the user can access at least one of
     *                      the given controllers; {@code false} otherwise
     */
    boolean checkAllowedControllers(List<String> controllers) {
        admin || controllers?.intersect(getAllowedControllers())
    }

    /**
     * Checks whether or not the user has permission to access at least one of
     * the given modules.
     *
     * @param modules   the names of the modules to check
     * @return          {@code true} if the user can access at least one of the
     *                  given modules; {@code false} otherwise
     */
    boolean checkAllowedModules(List<String> modules) {
        admin || modules?.intersect(allowedModulesAsList)
    }

    @Override
    User clone() {
        (User) super.clone()
    }

    @Override
    boolean equals(Object o) {
        (o instanceof User) ? o.ident() == ident() : false
    }

    @Override
    int hashCode() {
        ident()
    }

    @Override
    String toString() {
        fullName
    }
}
