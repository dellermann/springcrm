/*
 * ViewTagLib.groovy
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

import grails.util.GrailsNameUtils
import java.util.regex.Pattern
import org.springframework.validation.FieldError


/**
 * The class {@code ViewTagLib} contains various tags which are needed in the
 * views.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class ViewTagLib {

    //-- Instance variables ---------------------

    CalendarEventService calendarEventService
    UserService userService


    //-- Public methods -------------------------

    /**
     * Renders a number input field and an associated auto number checkbox.
     *
     * @attr value  the value of the number field
     * @attr prefix  the prefix to display in front of the number field
     * @attr suffix  the suffix to display after the number field
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
     * Creates a link to the former page (back link), if available, or creates
     * a link using the given attributes.
     *
     * @attr action     the name of the action to use in the link, if not
     *                  specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not
     *                  specified the current controller will be linked
     * @attr id         the id to use in the link
     * @attr fragment   the link fragment (often called anchor tag) to use
     * @attr mapping    the named URL mapping to use to rewrite the link
     * @attr params     a map containing URL query parameters
     * @attr url        a map containing the action, controller, id etc.
     * @attr absolute   if set to "true" will prefix the link target address
     *                  with the value of the grails.serverURL property from
     *                  Config, or http://localhost:<port> if no value in
     *                  Config and not running in production.
     * @attr base       sets the prefix to be added to the link target address,
     *                  typically an absolute server URL. This overrides the
     *                  behaviour of the absolute property, if both are
     *                  specified.
     */
    def backLink = { attrs, body ->
        if (params.returnUrl) {
            attrs.url = params.returnUrl
        }
        out << link(attrs, body)
    }

    /**
     * Renders a link which uses the currently active calendar view as action
     * or to the former page (back link).  The tag accepts the same attributes
     * like {@code <g:backLink>}.
     */
    def calendarViewBackLink = { attrs, body ->
        attrs.controller = 'calendarEvent'
        attrs.action = calendarEventService.currentCalendarView
        out << backLink(attrs, body)

    }

    /**
     * Renders a link which uses the currently active calendar view as action.
     * The tag accepts the same attributes like {@code <g:link>}.
     */
    def calendarViewLink = { attrs, body ->
        attrs.controller = 'calendarEvent'
        attrs.action = calendarEventService.currentCalendarView
        out << link(attrs, body)
    }

    /**
     * Returns the URL of the former page (back link), if available, or creates
     * the URL using the given attributes.
     *
     * @attr action     the name of the action to use in the link, if not
     *                  specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not
     *                  specified the current controller will be linked
     * @attr id         the id to use in the link
     * @attr fragment   the link fragment (often called anchor tag) to use
     * @attr mapping    the named URL mapping to use to rewrite the link
     * @attr params     a map containing URL query parameters
     * @attr url        a map containing the action, controller, id etc.
     * @attr absolute   if set to "true" will prefix the link target address
     *                  with the value of the grails.serverURL property from
     *                  Config, or http://localhost:<port> if no value in
     *                  Config and not running in production.
     * @attr base       sets the prefix to be added to the link target address,
     *                  typically an absolute server URL. This overrides the
     *                  behaviour of the absolute property, if both are
     *                  specified.
     */
    def createBackLink = { attrs, body ->
        if (params.returnUrl) {
            out << params.returnUrl
        } else {
            createLink attrs, body
        }
    }

    /**
     * Renders the currency symbol from the application configuration.
     */
    def currency = {
        Locale locale = userService.currentLocale
        Currency currency = getCurrency(locale)
        out << ((currency == null) ? '' : currency.getSymbol(locale))
    }

    /**
     * Renders a date/time input field where the user may enter date and time
     * values according to the formatting rules of the current locale.
     *
     * @attr name REQUIRED  the name of the input field
     * @attr value      the value of the input field; this may be either a
     *             Date or a Calendar object
     * @attr id        the ID of the input field; defaults to name
     * @attr precision    the precision of the date/time input fields;
     *             possible values are "year", "month", "day", "hour",
     *             or "minute"
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
     * Formats the given number as currency using the currency symbol from the
     * application configuration.
     *
     * @attr number REQUIRED   the number to format
     * @attr minFractionDigits the minimum number of digits allowed in the
     *                         fraction portion of a number; defaults to either
     *                         the default number of fraction digits for the
     *                         currency of the selected locale or the number of
     *                         fraction digits defined in the configuration
     *                         (the latter has precedence)
     * @attr groupingUsed      whether or not grouping will be used in this
     *                         format; defaults to true
     * @attr displayZero       if true zero is displayed as number, otherwise
     *                         an empty string is generated; defaults to false
     * @attr numberOnly        if true the formatted value is display without
     *                         the currency symbol; defaults to false
     */
    def formatCurrency = { attrs, body ->
        def number = attrs.number
        if (number || attrs.displayZero) {
            Locale locale = userService.currentLocale
            Currency currency = getCurrency(locale)
            Integer minFractionDigits = ConfigHolder.instance['numFractionDigits'] as Integer
            if (minFractionDigits == null) {
                minFractionDigits = currency.defaultFractionDigits
            }

            def map = new HashMap(attrs)
            map.number = number ?: 0
            map.type = attrs.numberOnly ? 'number' : 'currency'
            map.locale = locale
            if (currency != null) {
                map.currencyCode = currency.currencyCode
            }
            map.groupingUsed = attrs.groupingUsed ?: true
            map.minFractionDigits = attrs.minFractionDigits ?: minFractionDigits
            out << formatNumber(map)
        } else {
            out << ''
        }
    }

    /**
     * Renders a bar of letters which allows the user to switch to a page where
     * the elements with the property value with the respective initial letter
     * reside.
     *
     * @attr clazz REQUIRED     the class instance of the domain class that
     *                          letters are to render
     * @attr property REQUIRED  the name of the property that values are used
     *                          to obtain the initial letters
     * @attr controller         the controller which is called when the user
     *                          clicks a letter; if not specified the current
     *                          controller name is used
     * @attr action             the action which is called when the user clicks
     *                          a letter; if not specified the current action
     *                          name is used
     * @attr where              an optional HSQL WHERE clause which is used in
     *                          the SQL query for the initial letters
     * @attr numLetters         the number of letters which are combined to one
     *                          link; defaults to 1
     * @attr separator          the separator used to represent ranges of
     *                          letters like A-C; if not specified the letters
     *                          are not represented as range
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
     * @attr value  the value to convert; if not specified the body of the tag
     *              is used
     */
    def nl2br = { attrs, body ->
        String s = (attrs.value ?: body()) ?: ''
        out << s.replaceAll(/\r\n/, '<br />').replaceAll(/[\r\n]/, '<br />')
    }

    /**
     * Renders the errors of child items of the given bean.  If you specify a
     * body it may be used to output the error messages using either the
     * variable in attribute <code>var</code> or <code>it</code>.  If you omit
     * the body the error message are rendered one after another with a space
     * between.
     *
     * @attr bean       REQUIRED the bean where to look for input errors
     * @attr field      the name of the field which input errors are looked
     *                  for; if missing <code>items</code> is used
     * @attr code       the code in the message resources which is used to
     *                  localize the general error message; argument 0 is the
     *                  one-based item position, argument 1 the localized name
     *                  of the input field, and argument 2 the localized error
     *                  message
     * @attr prefix     the message prefix to localize the input field names
     *                  (without trailling dot)
     * @attr var        the name of the variable which contains the localized
     *                  error message when rendering the body; if missing, the
     *                  variable <code>it</code> is used
     */
    def renderItemErrors = { attrs, body ->
        def bean = attrs.bean
        if (bean == null) {
            return
        }

        String field = attrs.field?.toString() ?: 'items'
        String code = attrs.code ?: 'default.invalid.item.message'
        String prefix = attrs.prefix ?: GrailsNameUtils.getPropertyName(bean.getClass())
        def var = attrs.var

        StringBuilder buf = new StringBuilder('^')
        buf << Pattern.quote(field)
        buf << /\[(\d+)\]\.([\w.]+)$/
        Pattern pattern = Pattern.compile(buf.toString())
        List<FieldError> errors = bean.errors.getFieldErrors("${field}[*")
        for (FieldError err : errors) {
            def m = err.field =~ pattern
            if (m) {
                int idx = m[0][1] as int
                String name = m[0][2]
                String fieldName = message(code: "${prefix}.${name}.label", default: name)
                String errorMsg = message(error: err)
                String msg = message(code: code, args: [idx + 1, fieldName, errorMsg])

                def result
                if (body) {
                    if (var) {
                        result = body([(var): msg])
                    } else {
                        result = body(msg)
                    }
                } else {
                    result = msg + ' '
                }
                out << result
            }
        }
    }

    /**
     * Generates the URL of the current page including all request parameters.
     */
    def url = { attrs, body ->
        out << createLink(action: actionName, params: params, absolute: true)
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the currently active currency.
     *
     * @param locale    the given locale
     * @return          the currency; {@code null} if no currency is defined
     * @since           1.3
     */
    protected Currency getCurrency(Locale locale) {
        String currencyId = ConfigHolder.instance['currency'] as String

        /* fix for old currency symbols in table config */
        if (currencyId != null && currencyId.length() != 3) {
            currencyId = 'EUR'
        }

        Currency currency = null
        if (currencyId) {
            try {
                currency = Currency.getInstance(currencyId)
            } catch (IllegalArgumentException ignored) { /* ignored */ }
        }
        if (!currency) {
            try {
                currency = Currency.getInstance(locale)
            } catch (IllegalArgumentException ignored) { /* ignored */ }
        }
        currency ?: Currency.getInstance('EUR')
    }
}
