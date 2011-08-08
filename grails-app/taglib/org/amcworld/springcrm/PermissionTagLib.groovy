package org.amcworld.springcrm

class PermissionTagLib {

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
		if (session.user.checkAllowedModules(modules)) {
			out << body()
		}
	}
}
