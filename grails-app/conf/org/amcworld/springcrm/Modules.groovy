package org.amcworld.springcrm

import org.apache.commons.logging.LogFactory


/**
 * The class <code>Modules</code> defines modules which can be used for
 * permission control in this application.
 * 
 * @author 	Daniel Ellermann
 * @version 0.9.3
 * @since	0.9.3
 */
class Modules {

	//-- Constants ------------------------------

	/**
	 * The modules and their associated controller names which are defined for
	 * this application.
	 */
	protected static final Map<String, List<String>> MODULES = [
		call: ['call'],
		contact: ['organization', 'person'],
		invoice: ['organization', 'invoice'],
		product: ['product'],
		quote: ['organization', 'quote'],
		salesOrder: ['organization', 'salesOrder'],
		service: ['service'],
		user: ['user']
	]

	private static final log = LogFactory.getLog(this)


	//-- Constructors ---------------------------

	/**
	 * Constructor which prevents from instantiation.
	 */
	private Modules() {}


	//-- Public methods -------------------------

	/**
	 * Gets a set of all defined module names.
	 * 
	 * @return	the module names
	 */
	public static Set<String> getModuleNames() {
		return MODULES.keySet()
	}

	/**
	 * Resolves the given module name to a list of controller names.
	 * 
	 * @param name	the given module name
	 * @return		the list of controller name; <code>null</code> if no module
	 * 				with the given name is defined
	 */
	public static List<String> resolveModule(String name) {
		List<String> res = MODULES[name]
		if (!res) {
			log.error "Module ${name} not defined. Add an entry to ${this.name}.MODULES."
		}
		return res?.asImmutable()
	}

	/**
	 * Resolves the given module names to a set of controller names.
	 * 
	 * @param names	the given module names
	 * @return		the set of controller names
	 */
	public static Set<String> resolveModules(List<String> names) {
		Set<String> res = new HashSet<String>()
		for (String name in names) {
			def controllerNames = resolveModule(name)
			if (controllerNames == null) {
				log.error "Module ${name} not defined. Add an entry to ${this.name}.MODULES."
			} else {
				res += controllerNames
			}
		}
		return res
	}
}
