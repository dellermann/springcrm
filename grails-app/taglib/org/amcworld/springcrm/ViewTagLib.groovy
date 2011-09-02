package org.amcworld.springcrm

class ViewTagLib {

	/**
	 * Renders a bar of letters which allows the user to switch to a page where
	 * the elements with the property value with the respective initial letter
	 * reside.
	 * 
	 * @attr clazz REQUIRED			the class instance of the domain class that
	 * 								letters are to render
	 * @attr property REQUIRED		the name of the property that values are
	 * 								used to obtain the initial letters
	 * @attr controller         	the controller which is called when the
	 * 								user clicks a letter; if not specified the
	 * 								current controller name is used
	 * @attr action					the action which is called when the
	 * 								user clicks a letter; if not specified the
	 * 								current action name is used
	 * @attr where					an optional HSQL WHERE clause which is used
	 * 								in the SQL query for the initial letters
	 * @attr numLetters				the number of letters which are combined to
	 * 								one link; defaults to 1
	 * @attr separator				the separator used to represent ranges of
	 * 								letters like A-C; if not specified the
	 * 								letters are not represented as range
	 */
	def letterBar = { attrs, body ->
		Class<?> cls = attrs.clazz
		String prop = attrs.property
		String controller = attrs.controller ?: controllerName
		String action = attrs.action ?: actionName
		int numLetters = attrs.numLetters as int ?: 1
		String separator = attrs.separator

		GString sql = "select upper(substring(o.${prop}, 1, 1)) from ${cls.simpleName} as o"
		if (attrs.where) {
			sql += " where ${attrs.where}"
		}
		sql += " group by upper(substring(o.${prop}, 1, 1))"
		List<String> letters = cls.'executeQuery'(sql)

		String availableLetters = message(
			code:'default.letters', default:'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
		)
		int n = availableLetters.size()
		def items = []
		for (int i = 0; i < n; i += numLetters) {
			boolean inList = false
			for (int j = 0; j < numLetters && i + j < n; j++) {
				String letter = availableLetters[i + j]
				inList |= letter in letters
			}
			StringBuilder buf = new StringBuilder('<li')
			buf << (inList ? ' class="available"' : '') << '>'
			if (inList) {
				buf << "<a href=\"${createLink(controller:controller, action:action, params:[letter:availableLetters[i]])}\">"
			}
			if (separator && numLetters > 2) {
				buf << availableLetters[i] << separator << availableLetters[Math.min(n, i + numLetters) - 1]
			} else {
				for (int j = 0; j < numLetters && i + j < n; j++) {
					buf << availableLetters[i + j]
				}
			}
			if (inList) {
				buf << '</a>'
			}
			buf << '</li>'
			items << buf.toString()
		}
		out << '<ul class="letter-bar">' << items.join('') << '</ul>'
	}

	/**
	 * Converts LF and CR to HTML <br /> tags.
	 * 
	 * @attr value	the value to convert; if not specified the body of the tag
	 * 				is used
	 */
	def nl2br = { attrs, body ->
		String s = (attrs.value ?: body()) ?: ''
		out << s.replaceAll(/\r\n/, '<br />').replaceAll(/[\r\n]/, '<br />')
	}

	/**
	 * Returns the URL of the current page including all request parameters.
	 */
	def url = { attrs, body ->
		out << createLink(action:actionName, params:params)
	}

	/**
	 * Creates a link to the former page (back link), if available, or creates
	 * a link using the given attributes.
	 * 
	 * @attr action		the name of the action to use in the link, if not
	 * 					specified the default action will be linked
	 * @attr controller	the name of the controller to use in the link, if not
	 * 					specified the current controller will be linked
	 * @attr id 		the id to use in the link
	 * @attr fragment	the link fragment (often called anchor tag) to use
	 * @attr mapping	the named URL mapping to use to rewrite the link
	 * @attr params		a map containing URL query parameters
	 * @attr url		a map containing the action, controller, id etc.
	 * @attr absolute	if set to "true" will prefix the link target address
	 * 					with the value of the grails.serverURL property from
	 * 					Config, or http://localhost:<port> if no value in
	 * 					Config and not running in production.
	 * @attr base		sets the prefix to be added to the link target address,
	 * 					typically an absolute server URL. This overrides the
	 * 					behaviour of the absolute property, if both are
	 * 					specified.
	 */
	def backLink = { attrs, body ->
		if (params.returnUrl) {
			attrs.url = params.returnUrl
		}
		out << link(attrs, body)
	}

	/**
	 * Returns the URL of the former page (back link), if available, or creates
	 * the URL using the given attributes.
	 * 
	 * @attr action		the name of the action to use in the link, if not
	 * 					specified the default action will be linked
	 * @attr controller	the name of the controller to use in the link, if not
	 * 					specified the current controller will be linked
	 * @attr id 		the id to use in the link
	 * @attr fragment	the link fragment (often called anchor tag) to use
	 * @attr mapping	the named URL mapping to use to rewrite the link
	 * @attr params		a map containing URL query parameters
	 * @attr url		a map containing the action, controller, id etc.
	 * @attr absolute	if set to "true" will prefix the link target address
	 * 					with the value of the grails.serverURL property from
	 * 					Config, or http://localhost:<port> if no value in
	 * 					Config and not running in production.
	 * @attr base		sets the prefix to be added to the link target address,
	 * 					typically an absolute server URL. This overrides the
	 * 					behaviour of the absolute property, if both are
	 * 					specified.
	 */
	def createBackLink = { attrs, body ->
		if (params.returnUrl) {
			out << params.returnUrl
		} else {
			createLink(attrs, body)
		}
	}
}
