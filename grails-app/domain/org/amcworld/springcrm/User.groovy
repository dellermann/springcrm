/*
 * User.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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
 * @version 1.0
 */
class User {

    //-- Class variables ------------------------

    static constraints = {
		userName(nullable: false, blank: false, unique: true)
		password(blank: false, password: true)
		firstName(blank: false)
		lastName(blank: false)
        phone(nullable: true, maxSize: 40)
        phoneHome(nullable: true, maxSize: 40)
        mobile(nullable: true, maxSize: 40)
        fax(nullable: true, maxSize: 40)
        email(nullable: false, blank: false, email: true)
		admin()
		allowedModules(nullable: true)
		settings()
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		allowedModules type: 'text'
        settings index: 'settings_idx'
        table 'user_data'
        userName index: 'user_name'
    }
	static transients = [
		'fullName', 'allowedModulesAsList', 'allowedControllers'
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
	Map settings
	Date dateCreated
	Date lastUpdated
	private Set<String> allowedControllers


    //-- Properties -----------------------------

	String getFullName() {
		return "${firstName ?: ''} ${lastName ?: ''}".trim()
	}

	List<String> getAllowedModulesAsList() {
		return allowedModules?.split(',')
	}

	void setAllowedModulesAsList(List<String> l) {
		allowedModules = l.join(',')
	}

	/**
	 * Gets a set of controllers the user is permitted to access.
	 *
	 * @return	the names of the controllers the user may access
	 */
	Set<String> getAllowedControllers() {
		if (allowedControllers == null) {
			List<String> moduleNames = allowedModulesAsList
			allowedControllers =
				moduleNames ? Modules.resolveModules(moduleNames) : null
		}
		return allowedControllers
	}


    //-- Public methods -------------------------

	/**
	 * Checks whether or not the user has permission to access the given
	 * controllers.
	 *
	 * @param controllers	the names of the controllers to check
	 * @return				<code>true</code> if the user can access all the
	 * 						given controllers; <code>false</code> otherwise
	 */
	boolean checkAllowedControllers(List<String> controllers) {
		return admin || controllers?.intersect(getAllowedControllers())
	}

	/**
	 * Checks whether or not the user has permission to access the given
	 * modules.
	 *
	 * @param modules	the names of the modules to check
	 * @return			<code>true</code> if the user can access all the given
	 * 					modules; <code>false</code> otherwise
	 */
	boolean checkAllowedModules(List<String> modules) {
		return admin || modules?.intersect(getAllowedModulesAsList())
	}

    @Override
    boolean equals(Object o) {
        if (o instanceof User) {
            return o.ident() == ident()
        } else {
            return false
        }
    }

    @Override
    public int hashCode() {
        return ident()
    }

    @Override
	String toString() {
		return fullName
	}
}
