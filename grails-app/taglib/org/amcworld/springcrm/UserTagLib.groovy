package org.amcworld.springcrm

class UserTagLib {

	/**
	 * Renders an area to display the currently logged in user.
	 */
	def loginControl = {
		if (request.getSession(false) && session.user) {
			out << "${message(code: 'default.welcome', args: [session.user.fullName])} "
			out << "${link(controller: 'user', action: 'logout'){message(code: 'default.logout')}}"
		}
	}

	/**
	 * Renders the value of a user setting.
	 * 
	 * @attr key REQUIRED	the name of the user setting
	 */
	def userSetting = { attrs, body ->
		out << session.user?.settings[attrs.key]
	}
}
