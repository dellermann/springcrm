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
	 */
	def letterBar = { attrs, body ->
		Class<?> cls = attrs.clazz
		String prop = attrs.property
		String controller = attrs.controller ?: controllerName
		String action = attrs.action ?: actionName
		GString sql = "select upper(substring(o.${prop}, 1, 1)) from ${cls.simpleName} as o"
		if (attrs.where) {
			sql += " where ${attrs.where}"
		}
		sql += " group by upper(substring(o.${prop}, 1, 1))"
		List<String> letters = cls.'executeQuery'(sql)
		String availableLetters = message(code:'default.letters', default:'ABCDEFGHIJKLMNOPQRSTUVWXYZ')
		StringBuilder buf = new StringBuilder('<ul class="letter-bar">')
		for (int i = 0; i < availableLetters.size(); i++) {
			String letter = availableLetters[i]
			boolean inList = letter in letters
			buf << '<li' << (inList ? ' class="available"' : '') << '>'
			if (inList) {
				buf << "<a href=\"${createLink(controller:controller, action:action, params:[letter:letter])}\">"
			}
			buf << letter
			if (inList) {
				buf << '</a>'
			}
			buf << '</li>'
		}
		out << buf << '</ul>'
	}
}
