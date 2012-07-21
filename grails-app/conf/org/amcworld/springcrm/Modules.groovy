/*
 * Modules.groovy
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

import org.apache.commons.logging.LogFactory


/**
 * The class {@code Modules} defines modules which can be used for permission
 * control in this application.
 *
 * @author  Daniel Ellermann
 * @version 1.2
 */
class Modules {

	//-- Constants ------------------------------

	/**
	 * The modules and their associated controller names which are defined for
	 * this application.
	 */
	protected static final Map<String, List<String>> MODULES = [
        calendar: ['calendarEvent', 'organization'],
		call: ['call'],
		contact: ['organization', 'person'],
		creditMemo: ['organization', 'creditMemo'],
        document: ['document'],
		dunning: ['organization', 'invoice', 'dunning'],
		invoice: ['organization', 'invoice'],
		note: ['note'],
		product: ['product'],
        project: ['organization', 'project'],
		purchaseInvoice: ['purchaseInvoice'],
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
				res.addAll(controllerNames)
			}
		}
		return res
	}
}
