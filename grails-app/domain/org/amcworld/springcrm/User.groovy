package org.amcworld.springcrm

class User {

    static constraints = {
		userName(blank:false, nullable:false, unique:true)
		password(blank:false, password:true)
		firstName(blank:false)
		lastName(blank:false)
        phone(maxSize:40, nullable:true)
        phoneHome(maxSize:40, nullable:true)
        mobile(maxSize:40, nullable:true)
        fax(maxSize:40, nullable:true)
        email(blank:false, nullable:false, email:true)
		admin()
		allowedModules(nullable:true)
		settings()
		dateCreated()
		lastUpdated()
    }
	static mapping = {
		allowedModules type:'text'
    }
	static transients = [
		'fullName', 'allowedModulesAsList', 'allowedControllers'
	]

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

	String toString() {
		return fullName
	}
}
