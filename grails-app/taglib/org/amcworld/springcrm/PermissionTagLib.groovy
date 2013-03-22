/*
 * PermissionTagLib.groovy
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
 * The class {@code PermissionTagLib} contains to check for login and
 * permissions.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class PermissionTagLib {

    //-- Public methods -------------------------

	/**
	 * Creates a link to a controller but checks if the user has permissions
	 * for it. If so the link is generated using <code>createLink</code>,
	 * otherwise an empty string is returned.
	 *
	 * @attr controller REQUIRED	the controller to link to
	 * @attr action					the action to link to
	 */
	def createControllerLink = { attrs, body ->
		if (session.user.checkAllowedControllers([attrs.controller])) {
			out << createLink(attrs)
		}
	}

	/**
	 * Renders the body of the tag if the currently logged in user is
	 * administrator.
	 */
	def ifAdmin = { attrs, body ->
		if (session.user?.admin) {
			out << body()
		}
	}

	/**
	 * Renders the body of the tag if the currently logged in user has
	 * permission to view the controllers with the given names. Administrators
	 * always have access to the specified modules.
	 *
	 * @attr controllers REQUIRED	the given controller names; you can specify
	 * 								either a single string or a list of strings
	 */
	def ifControllerAllowed = { attrs, body ->
		def controllers = attrs.controllers
		if (!(controllers instanceof List)) {
			controllers = [controllers]
		}
		if (session.user.checkAllowedControllers(controllers)) {
			out << body()
		}
	}

	/**
	 * Renders the body of the tag if the currently logged in user has
	 * permission to view the modules with the given names. Administrators
	 * always have access to the specified modules.
	 *
	 * @attr modules REQUIRED	the given module names; you can specify either
	 * 							a single string or a list of strings
	 */
	def ifModuleAllowed = { attrs, body ->
		def modules = attrs.modules
		if (!(modules instanceof List)) {
			modules = [modules]
		}
		if (session.user?.checkAllowedModules(modules)) {
			out << body()
		}
	}
}
