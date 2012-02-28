/*
 * User.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm


/**
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 0.9
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
        table 'user_data'
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

    boolean equals(Object o) {
        if (o instanceof User) {
            return o.ident() == ident()
        } else {
            return false
        }
    }

	String toString() {
		return fullName
	}
}
