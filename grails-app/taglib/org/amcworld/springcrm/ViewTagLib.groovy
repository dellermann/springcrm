/*
 * ViewTagLib.groovy
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

import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code ViewTagLib} contains various tags which are needed in the
 * views.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class ViewTagLib {

    //-- Public methods -------------------------

	/**
	 * Renders a date/time input field where the user may enter date and time
	 * values according to the formatting rules of the current locale.
	 *
	 * @attr name REQUIRED	the name of the input field
	 * @attr value			the value of the input field; this may be either a
	 * 						Date or a Calendar object
	 * @attr id				the ID of the input field; defaults to name
	 * @attr precision		the precision of the date/time input fields;
	 * 						possible values are "year", "month", "day", "hour",
	 * 						or "minute"
	 */
	def dateInput = { attrs, body ->

		/* obtain precision */
		final PRECISION_RANKINGS = [
			'year': 0, 'month': 10, 'day': 20, 'hour': 30, 'minute': 40
		]
		int precision = PRECISION_RANKINGS['minute']
		if (attrs.precision) {
			precision = PRECISION_RANKINGS[attrs.precision]
		} else if (grailsApplication.config.grails.tags.dateInput.default.precision) {
			precision = PRECISION_RANKINGS[grailsApplication.config.grails.tags.dateInput.default.precision]
		}

		/* obtain attribute values */
        String name = attrs.name
        String id = attrs.id ?: name
		def value = attrs.value
        if (value.toString() == 'none') {
            value = null
        }
        Calendar c = null
        if (value instanceof Calendar) {
            c = value
        } else if (value != null) {
            c = new GregorianCalendar()
			c.setTime(value)
        }

		def formatName
		if (precision >= PRECISION_RANKINGS['hour']) {
			formatName = 'default.format.datetime'
		} else {
			formatName = 'default.format.date'
		}

        /*
         * Because the HTML 5 <input /> tag with type "date", "datetime",
         * "time", and "datetime-local" fields do not support localized
         * date/time strings we use type "text" here. Maybe in future this will
         * be corrected in the HTML 5 standard.
         */
		out.println "<input type=\"hidden\" name=\"${name}\" value=\"${c ? formatDate(date: c, formatName: formatName) : ''}\" />"
		out.println "<input type=\"text\" name=\"${name}_date\" id=\"${id}-date\" value=\"${c ? formatDate(date: c, formatName: 'default.format.date') : ''}\" size=\"10\" class=\"date-input date-input-date\" />"

		if (precision >= PRECISION_RANKINGS['hour']) {
			out.println "<input type=\"text\" name=\"${name}_time\" id=\"${id}-time\" value=\"${c ? formatDate(date: c, formatName: 'default.format.time') : ''}\" size=\"5\" class=\"date-input date-input-time\" />"
		}
	}

	/**
	 * Renders a number input field and an associated auto number checkbox.
	 *
	 * @attr value	the value of the number field
	 * @attr prefix	the prefix to display in front of the number field
	 * @attr suffix	the suffix to display after the number field
	 */
	def autoNumber = { attrs, body ->
		if (attrs.prefix) {
			out << attrs.prefix << '-'
		}
		out << textField(name: 'number', value: attrs.value, size: 10)
		if (attrs.suffix) {
			out << '-' << attrs.suffix
		}
		boolean checked = true
		if (params._autoNumber != null) checked = params.autoNumber
		out << '<span class="auto-number">'
		out << checkBox(name: 'autoNumber', checked: checked)
		out << '<label for="autoNumber">'
		out << message(code: 'default.number.auto.label')
		out << '</label></span>'
	}

	/**
	 * Formats the given number as currency using the currency symbol from the
	 * application configuration.
	 *
	 * @attr number REQUIRED   the number to format
	 * @attr minFractionDigits the minimum number of digits allowed in the
	 *                         fraction portion of a number; defaults to 2
	 * @attr groupingUsed      whether or not grouping will be used in this
	 *                         format; defaults to true
	 * @attr displayZero       if true zero values are displayed, otherwise an
	 *                         empty string is generated
	 */
	def formatCurrency = { attrs, body ->
		def number = attrs.number
		if (number || attrs.displayZero) {
			def map = new HashMap(attrs)
			map.number = number ?: 0
			map.type = 'currency'
			map.currencySymbol =
                ConfigHolder.instance['currency'] as String ?: '€'
			map.groupingUsed = attrs.groupingUsed ?: true
			map.minFractionDigits = attrs.minFractionDigits ?: 2
            map.locale = LCH.locale
			out << formatNumber(map)
		} else {
			out << ''
		}
	}

	/**
	 * Renders the currency symbol from the application configuration.
	 */
	def currency = {
		out << (ConfigHolder.instance['currency'] as String) ?: '€'
	}

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
		int numLetters = (attrs.numLetters ?: 1) as int
		String separator = attrs.separator

		GString sql = "select upper(substring(o.${prop}, 1, 1)) from ${cls.simpleName} as o"
		if (attrs.where) {
			sql += " where ${attrs.where}"
		}
		sql += " group by upper(substring(o.${prop}, 1, 1))"
		List<String> letters = cls.'executeQuery'(sql)

		String availableLetters = message(
			code: 'default.letters', default: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
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
				buf << "<a href=\"${createLink(controller: controller, action: action, params: [letter: availableLetters[i]])}\">"
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
	 * Generates the URL of the current page including all request parameters.
	 */
	def url = { attrs, body ->
		out << createLink(action: actionName, params: params, absolute: true)
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
