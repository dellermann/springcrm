package org.amcworld.springcrm

class LoginTagLib {
	def loginControl = {
		if (request.getSession(false) && session.user) {
			out << "${message(code: 'default.welcome', args: [session.user.fullName])} "
			out << "${link(controller: 'user', action: 'logout'){message(code: 'default.logout')}}"
		}
	}
}
