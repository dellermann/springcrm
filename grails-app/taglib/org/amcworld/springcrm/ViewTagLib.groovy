/*
 * ViewTagLib.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
import org.codehaus.groovy.grails.web.mapping.UrlMapping
import org.springframework.validation.FieldError
import org.springframework.web.servlet.support.RequestContextUtils as RCU


/**
 * The class {@code ViewTagLib} contains various tags which are needed in the
 * views.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class ViewTagLib {

    //-- Constants ------------------------------

    static final Map<String, String> CONTROLLER_ICON_MAPPING = [
        calendarEvent: 'calendar',
        call: 'phone',
        creditMemo: 'money',
        document: 'file-o',
        dunning: 'fire',
        helpdesk: 'question-circle',
        invoice: 'euro',
        note: 'pencil',
        organization: 'users',
        person: 'male',
        product: 'cog',
        project: 'lightbulb-o',
        purchaseInvoice: 'shopping-cart',
        quote: 'briefcase',
        salesOrder: 'bars',
        service: 'laptop',
        ticket: 'ticket',
        user: 'user'
    ]


    //-- Instance variables ---------------------

    CalendarEventService calendarEventService
    UserService userService


    //-- Public methods -------------------------

    /**
     * Renders a number input field and an associated auto number checkbox.
     *
     * @attr value  the value of the number field
     * @attr prefix the prefix to display in front of the number field
     * @attr suffix the suffix to display after the number field
     */
    def autoNumber = { attrs, body ->
        boolean checked = true
        if (params._autoNumber != null) checked = params.autoNumber

        out << '<div class="auto-number"><div class="input-group">'
        if (attrs.prefix) {
            out << '<span class="input-group-addon">'
            out << attrs.prefix.encodeAsHTML()
            out << '-</span>'
        }
        out << '<input type="number" name="number" id="number" class="form-control" value="'
        out << attrs.value
        out << '" size="10"'
        if (checked) out << ' disabled="disabled"'
        out << ' />'
        if (attrs.suffix) {
            out << '<span class="input-group-addon">-'
            out << attrs.suffix.encodeAsHTML()
            out << '</span>'
        }
        out << '</div>'
        out << '<div class="checkbox">'
        out << '<label class="checkbox-inline">'
        out << checkBox(
            name: 'autoNumber', checked: checked, 'aria-controls': 'number'
        )
        out << message(code: 'default.number.auto.label')
        out << '</label>'
        out << '</div></div>'
    }

    /**
     * Creates a link to the former page (back link), if available, or creates
     * a link using the given attributes.
     *
     * @attr action     the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id         the id to use in the link
     * @attr fragment   the link fragment (often called anchor tag) to use
     * @attr mapping    the named URL mapping to use to rewrite the link
     * @attr params     a map containing URL query parameters
     * @attr url        a map containing the action, controller, id etc.
     * @attr absolute   if set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:<port> if no value in Config and not running in production.
     * @attr base       sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behavior of the absolute property, if both are specified.
     */
    def backLink = { attrs, body ->
        if (params.returnUrl) {
            attrs.url = params.returnUrl
        }
        out << link(attrs, body)
    }

    /**
     * Creates a button with optional icon either as link or a
     * <code>&lt;span></code> element.  If any of the link attributes are
     * specified, a link is generated.  You may either specify the text of the
     * button in the body or use the message attribute.
     *
     * @attr color      the color of the button, e. g. white, green, blue
     * @attr size       the size of the button, e. g. small, medium
     * @attr icon       the icon which should be used, e. g. floppy-o, trash-o
     * @attr class      further CSS classes to apply
     * @attr message    a message code which is used to render the buttont text; if specified, the body will not be evaluated
     * @attr action     the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id         the id to use in the link
     * @attr fragment   the link fragment (often called anchor tag) to use
     * @attr mapping    the named URL mapping to use to rewrite the link
     * @attr params     a map containing URL query parameters
     * @attr url        a map containing the action, controller, id etc.
     * @attr absolute   if set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:<port> if no value in Config and not running in production.
     * @attr base       sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behavior of the absolute property, if both are specified.
     * @attr back       if true and a return URL is set in the parameters a back link is generated
     */
    def button = { attrs, body ->
        StringBuilder buf = new StringBuilder('btn')
        String s = attrs.remove('color')
        if (s) {
            buf << ' btn-' << s
        }
        s = attrs.remove('size')
        if (s) {
            buf << ' btn-' << s
        }
        s = attrs.remove('class')
        if (s) {
            buf << ' ' << s
        }
        String cssClass = buf.toString()

        buf = new StringBuilder()
        s = attrs.remove('icon')
        if (s) {
            buf << '<i class="fa fa-' << s << '"></i> '
        }
        s = attrs.remove('message')
        if (s) {
            def args = attrs.remove('args')
            def defValue = attrs.remove('default')
            buf << message(code: s, args: args, default: defValue)
        } else {
            buf << body()
        }
        String content = buf.toString()

        if (attrs.remove('back') && params.returnUrl) {
            attrs.url = params.returnUrl
        }
        if (attrs.action || attrs.controller || attrs.id || attrs.mapping ||
            attrs.params || attrs.uri || attrs.url)
        {
            def data = attrs + [class: cssClass, role: 'button']
            out << link(data) { content }
        } else {
            out << '<button type="button" class="' << cssClass << '"'
            def id = attrs.remove('elementId')
            if (id) {
                out << ' id="' << id << '"'
            }
            def remainingKeys = attrs.keySet()
            for (key in remainingKeys) {
                out << ' ' << key << '="' << attrs[key]?.encodeAsHTML() << '"'
            }
            out << '>' << content << '</button>'
        }
    }

    /**
     * Renders a link which uses the currently active calendar view as action
     * or to the former page (back link).  The tag accepts the same attributes
     * like {@code <g:button>}.
     */
    def calendarViewBackLink = { attrs, body ->
        attrs.controller = 'calendarEvent'
        attrs.action = calendarEventService.currentCalendarView
        attrs.back = true
        out << button(attrs, body)
    }

    /**
     * Renders a link which uses the currently active calendar view as action.
     * The tag accepts the same attributes like {@code <g:button>}.
     */
    def calendarViewLink = { attrs, body ->
        attrs.controller = 'calendarEvent'
        attrs.action = calendarEventService.currentCalendarView
        out << button(attrs, body)
    }

    /**
     * Returns the URL of the former page (back link), if available, or creates
     * the URL using the given attributes.
     *
     * @attr action     the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id         the id to use in the link
     * @attr fragment   the link fragment (often called anchor tag) to use
     * @attr mapping    the named URL mapping to use to rewrite the link
     * @attr params     a map containing URL query parameters
     * @attr url        a map containing the action, controller, id etc.
     * @attr absolute   if set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:<port> if no value in Config and not running in production.
     * @attr base       sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behavior of the absolute property, if both are specified.
     */
    def createBackLink = { attrs, body ->
        if (params.returnUrl) {
            out << params.returnUrl
        } else {
            out << createLink(attrs, body)
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
     * Renders a Font Awesome icon for the given controller.
     *
     * @attr controller REQUIRED    the given name of the controller
     * @since                       1.4
     */
    def dataTypeIcon = { attrs, body ->
        String controller = attrs.controller
        String icon = CONTROLLER_ICON_MAPPING[controller]
        out << '<i class="fa fa-fw fa-' << icon << ' data-type-icon"'
        out << ' title="' << message(code: "${controller}.label") << '"'
        out << '></i> '
    }

    /**
     * Renders a date/time input field where the user may enter date and time
     * values according to the formatting rules of the current locale.
     *
     * @attr name REQUIRED  the name of the input field
     * @attr value          the value of the input field; this may be either a Date or a Calendar object
     * @attr id             the ID of the input field; defaults to name
     * @attr precision      the precision of the date/time input fields; possible values are "year", "month", "day", "hour", or "minute"
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
        boolean useTime = precision >= PRECISION_RANKINGS['hour']

        /*
         * Because the HTML 5 <input /> tag with type "date", "datetime",
         * "time", and "datetime-local" fields do not support localized
         * date/time strings we use type "text" here. Maybe in future this will
         * be corrected in the HTML 5 standard.
         */
        out << """\
<input type="hidden" name="${name}"
  value="${c ? formatDate(date: c, formatName: formatName) : ''}" />
"""
        if (useTime) {
            out << '<div class="input-group date-time-control">'
        }
        out << """\
<input type="text" id="${id}-date" name="${name}_date"
  value="${c ? formatDate(date: c, formatName: 'default.format.date') : ''}"
  class="form-control date-input-control date-input-date-control"
  maxlength="10" />
"""

        if (useTime) {
            out << """\
<input type="text" id="${id}-time" name="${name}_time"
  value="${c ? formatDate(date: c, formatName: 'default.format.time') : ''}"
  class="form-control date-input-control date-input-time-control"
  maxlength="5" />
</div>
"""
        }
    }

    /**
     * Formats the given number as currency using the currency symbol from the
     * application configuration.
     *
     * @attr number REQUIRED    the number to format
     * @attr minFractionDigits  the minimum number of digits allowed in the fraction portion of a number; defaults to either the default number of fraction digits for the currency of the selected locale or the number of fraction digits defined in the configuration (the latter has precedence)
     * @attr groupingUsed       whether or not grouping will be used in this format; defaults to true
     * @attr displayZero        if true zero is displayed as number, otherwise an empty string is generated; defaults to false
     * @attr numberOnly         if true the formatted value is display without the currency symbol; defaults to false
     * @attr external           if true the value is formatted with the number of fraction digits used in external prices
     */
    def formatCurrency = { attrs, body ->
        def number = attrs.number
        if (number || attrs.displayZero) {
            Locale locale = userService.currentLocale
            Currency currency = getCurrency(locale)
            int fractionDigits = attrs.external \
                ? userService.numFractionDigitsExt \
                : userService.numFractionDigits
            if (attrs.minFractionDigits != null) {
                fractionDigits = attrs.minFractionDigits
            }

            def map = new HashMap(attrs)
            map.number = number ?: 0
            map.type = attrs.numberOnly ? 'number' : 'currency'
            map.locale = locale
            if (currency != null) {
                map.currencyCode = currency.currencyCode
            }
            map.groupingUsed = attrs.groupingUsed ?: true
            map.maxFractionDigits = fractionDigits
            map.minFractionDigits = fractionDigits
            out << formatNumber(map)
        }
    }

    /**
     * Formats the given number of data size with the units B, K, M, G, or T,
     * for example, "2.5 K" or "158 M".
     *
     * @attr number REQUIRED    the number to format
     * @attr groupingUsed       whether or not grouping will be used in this format; defaults to true
     * @since                   1.4
     */
    def formatSize = { attrs, body ->
        def number = attrs.number
        if (number) {
            def units = ['B', 'K', 'M', 'G', 'T']
            String unit = 'B'
            float value = 0.0f
            for (int i = units.size() - 1; i >= 0; i--) {
                def p = 1024i ** i
                if (number >= p) {
                    unit = units[i]
                    value = (number / p) as float
                    break
                }
            }

            out << formatNumber(
                number: value, minFractionDigits: 0, maxFractionDigits: 2,
                type: 'number', locale: userService.currentLocale,
                groupingUsed: attrs.groupingUsed ?: true
            ) << ' ' << unit
        }
    }

    /**
     * Renders a bar of letters which allows the user to switch to a page where
     * the elements with the property value with the respective initial letter
     * reside.
     *
     * @attr clazz REQUIRED     the class instance of the domain class that letters are to render
     * @attr property REQUIRED  the name of the property that values are used to obtain the initial letters
     * @attr controller         the controller which is called when the user clicks a letter; if not specified the current controller name is used
     * @attr action             the action which is called when the user clicks a letter; if not specified the current action name is used
     * @attr params             a map containing URL query parameters
     * @attr where              an optional HSQL WHERE clause which is used in the SQL query for the initial letters
     * @attr numLetters         the number of letters which are combined to one link; defaults to 1
     * @attr separator          the separator used to represent ranges of letters like A-C; if not specified the letters are not represented as range
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
            code: 'default.letterBar.letters',
            default: 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
        )
        int n = availableLetters.size()
        List<String> items = []
        for (int i = 0; i < n; i += numLetters) {
            boolean inList = false
            for (int j = 0; j < numLetters && i + j < n; j++) {
                String letter = availableLetters[i + j]
                inList |= letter in letters
            }
            StringBuilder buf = new StringBuilder('<a href="')
            if (inList) {
                buf << createLink(
                    controller: controller, action: action,
                    params:
                        (attrs.params ?: [: ]) + [letter: availableLetters[i]]
                )
            } else {
                buf << '#'
            }
            buf << '" class="btn btn-default'
            if (!inList) buf << ' disabled'
            buf << '" role="button"'
            if (!inList) buf << ' aria-disabled="true"'
            buf << '>'
            if (separator && numLetters > 2) {
                buf << availableLetters[i] << separator
                buf << availableLetters[Math.min(n, i + numLetters) - 1]
            } else {
                for (int j = 0; j < numLetters && i + j < n; j++) {
                    buf << availableLetters[i + j]
                }
            }
            buf << '</a>'
            items << buf.toString()
        }
        out << '<div class="btn-group btn-group-justified letter-bar" role="group" aria-label="'
        out << message(code: 'default.letterBar.label')
        out << '">' << items.join('') << '</div>'
    }

    /**
     * Renders a list item for installation steps using the given step number
     * and current step.
     *
     * @attr step REQUIRED      the zero-based number of this step
     * @attr current REQUIRED   the zero-based current step that is displayed
     */
    def installStep = { attrs, body ->
        StringBuilder buf = new StringBuilder('<li')
        int step = attrs.step as int
        int current = (attrs.current ?: 0i) as int
        if (step == current) {
            buf << ' class="current"'
        }
        buf << '>' << body() << '</li>'
        out << buf.toString()
    }

    /**
     * Creates a menu button with optional icon either as link or a
     * <code>&lt;span></code> element.  If any of the link attributes are
     * specified, a link is generated.  The menu items <code>&lt;li></code>
     * must be specified in the body of the tag.
     *
     * @attr message REQUIRED   a message code which is used to render the button text
     * @attr color              the color of the button, e. g. white, green, blue
     * @attr size               the size of the button, e. g. small, medium
     * @attr icon               the icon which should be used, e. g. save, trash
     * @attr class              further CSS classes to apply to the first button
     * @attr action             the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller         the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id                 the id to use in the link
     * @attr fragment           the link fragment (often called anchor tag) to use
     * @attr mapping            the named URL mapping to use to rewrite the link
     * @attr params             a map containing URL query parameters
     * @attr url                a map containing the action, controller, id etc.
     * @attr absolute           if set to "true" will prefix the link target address with the value of the grails.serverURL property from Config, or http://localhost:<port> if no value in Config and not running in production.
     * @attr base               sets the prefix to be added to the link target address, typically an absolute server URL. This overrides the behavior of the absolute property, if both are specified.
     */
    def menuButton = { attrs, body ->
        StringBuilder buf = new StringBuilder('button')
        String s = attrs.remove('color')
        if (s) {
            buf << ' ' << s
        }
        s = attrs.remove('size')
        if (s) {
            buf << ' ' << s
        }
        String baseCssClass = buf.toString()
        s = attrs.remove('class')
        if (s) {
            buf << ' ' << s
        }
        String cssClass = buf.toString()

        buf = new StringBuilder()
        s = attrs.remove('icon')
        if (s) {
            buf << '<i class="fa fa-' << s << '"></i>'
        }
        s = attrs.remove('message')
        def args = attrs.remove('args')
        def defValue = attrs.remove('default')
        buf << message(code: s, args: args, default: defValue)
        String content = buf.toString()

        out << '<div class="button-group">'
        if (attrs.remove('back') && params.returnUrl) {
            attrs.url = params.returnUrl
        }
        if (attrs.action || attrs.controller || attrs.id || attrs.mapping ||
            attrs.params || attrs.uri || attrs.url)
        {
            def data = attrs + [class: cssClass]
            out << link(data) { content }
        } else {
            out << '<span class="' << cssClass << '"'
            def id = attrs.remove('elementId')
            if (id) {
                out << ' id="' << id << '"'
            }
            def remainingKeys = attrs.keySet()
            for (key in remainingKeys) {
                out << ' ' << key << '="' << attrs[key]?.encodeAsHTML() << '"'
            }
            out << '>' << content << '</span>'
        }
        out << '<span class="' << baseCssClass << ' dropdown">'
        out << '<i class="fa fa-caret-down"></i></span>'
        out << '<ul class="dropdown-menu">' << body() << '</ul>'
        out << '</div>'
    }

    /**
     * Converts LF and CR to HTML <br /> tags.
     *
     * @attr value  the value to convert; if not specified the body of the tag is used
     */
    def nl2br = { attrs, body ->
        String s = (attrs.value ?: body()) ?: ''
        out << s.replaceAll(/\r\n/, '<br />').replaceAll(/[\r\n]/, '<br />')
    }

    /**
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr params A map containing request parameters
     * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
     * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
     * @attr omitPrev Whether to not show the previous link (if set to true, the previous link will not be shown)
     * @attr omitNext Whether to not show the next link (if set to true, the next link will not be shown)
     * @attr omitFirst Whether to not show the first link (if set to true, the first link will not be shown)
     * @attr omitLast Whether to not show the last link (if set to true, the last link will not be shown)
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr offset Used only if params.offset is empty
     * @attr mapping The named URL mapping to use to rewrite the link
     * @attr fragment The link fragment (often called anchor tag) to use
     * @attr class Any CSS classes that should be added to the pagination container
     */
    Closure paginate = { attrs ->
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }

        def messageSource = grailsAttributes.messageSource
        def locale = RCU.getLocale(request)

        def total = attrs.int('total') ?: 0
        def offset = params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (attrs.int('maxsteps') ?: 10)
        def cssClass = attrs.remove('class')

        if (!offset) offset = (attrs.int('offset') ?: 0)
        if (!max) max = (attrs.int('max') ?: 10)

        def linkParams = [:]
        if (attrs.params) linkParams.putAll(attrs.params)
        linkParams.offset = offset - max
        linkParams.max = max
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order

        def linkTagAttrs = [:]
        def action
        if (attrs.containsKey('mapping')) {
            linkTagAttrs.mapping = attrs.mapping
            action = attrs.action
        } else {
            action = attrs.action ?: params.action
        }
        if (action) {
            linkTagAttrs.action = action
        }
        if (attrs.controller) {
            linkTagAttrs.controller = attrs.controller
        }
        if (attrs.containsKey(UrlMapping.PLUGIN)) {
            linkTagAttrs.put(UrlMapping.PLUGIN, attrs.get(UrlMapping.PLUGIN))
        }
        if (attrs.containsKey(UrlMapping.NAMESPACE)) {
            linkTagAttrs.put(UrlMapping.NAMESPACE, attrs.get(UrlMapping.NAMESPACE))
        }
        if (attrs.id != null) {
            linkTagAttrs.id = attrs.id
        }
        if (attrs.fragment != null) {
            linkTagAttrs.fragment = attrs.fragment
        }
        linkTagAttrs.params = linkParams

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        if (laststep > 1) {
            writer << '<ul class="pagination'
            if (cssClass) writer << ' ' << cssClass
            writer << '" role="group">'
        }

        // display previous link when not on firststep unless omitPrev is true
        if (currentstep > firststep && !attrs.boolean('omitPrev')) {
            linkParams.offset = offset - max
            writer << '<li role="listitem" aria-label="'
            writer << messageSource.getMessage('default.paginate.prev', null, 'Previous', locale)
            writer << '">'
            writer << link(linkTagAttrs.clone()) {
                StringBuilder buf = new StringBuilder('<span aria-hidden="true">')
                buf << (attrs.prev ?: messageSource.getMessage('default.paginate.prev.short', null, '«', locale))
                buf << '</span>'
            } << '</li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {

            // determine begin and endstep paging variables
            int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            int endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep && !attrs.boolean('omitFirst')) {
                linkParams.offset = 0
                writer << '<li role="listitem">' <<
                    link(linkTagAttrs.clone()) {firststep.toString()} <<
                    '</li>'
            }
            //show a gap if beginstep isn't immediately after firststep, and if were not omitting first or rev
            if (beginstep > firststep+1 && (!attrs.boolean('omitFirst') || !attrs.boolean('omitPrev')) ) {
                writer << '<li class="disabled" role="listitem" aria-disabled="true"><a href="#">…</a></li>'
            }

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << """\
<li class="active" role="listitem">
  <span>${i} <span class="sr-only">${messageSource.getMessage('default.paginate.current', null, 'current', locale)}</span></span>
</li>
"""
                }
                else {
                    linkParams.offset = (i - 1) * max
                    writer << '<li role="listitem">' <<
                        link(linkTagAttrs.clone()) {i.toString()} <<
                        '</li>'
                }
            }

            //show a gap if beginstep isn't immediately before firststep, and if were not omitting first or rev
            if (endstep+1 < laststep && (!attrs.boolean('omitLast') || !attrs.boolean('omitNext'))) {
                writer << '<li class="disabled" role="listitem" aria-disabled="true"><a href="#">…</a></li>'
            }
            // display laststep link when endstep is not laststep
            if (endstep < laststep && !attrs.boolean('omitLast')) {
                linkParams.offset = (laststep - 1) * max
                writer << '<li role="listitem">' <<
                    link(linkTagAttrs.clone()) { laststep.toString() } <<
                    '</li>'
            }
        }

        // display next link when not on laststep unless omitNext is true
        if (currentstep < laststep && !attrs.boolean('omitNext')) {
            linkTagAttrs.class = 'nextLink'
            linkParams.offset = offset + max
            writer << '<li role="listitem" aria-label="'
            writer << messageSource.getMessage('default.paginate.next', null, 'Next', locale)
            writer << '">' << link(linkTagAttrs.clone()) {
                StringBuilder buf = new StringBuilder('<span aria-hidden="true">')
                buf << (attrs.prev ?: messageSource.getMessage('default.paginate.next.short', null, '»', locale))
                buf << '</span>'
            } << '</li>'
        }

        if (laststep > 1) writer << '</ul>'
    }

    /**
     * Renders the errors of child items of the given bean.  If you specify a
     * body it may be used to output the error messages using either the
     * variable in attribute <code>var</code> or <code>it</code>.  If you omit
     * the body the error message are rendered one after another with a space
     * between.
     *
     * @attr bean REQUIRED  the bean where to look for input errors
     * @attr field          the name of the field which input errors are looked for; if missing <code>items</code> is used
     * @attr code           the code in the message resources which is used to localize the general error message; argument 0 is the one-based item position, argument 1 the localized name of the input field, and argument 2 the localized error message
     * @attr prefix         the message prefix to localize the input field names (without trailling dot)
     * @attr var            the name of the variable which contains the localized error message when rendering the body; if missing, the variable <code>it</code> is used
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
     * Renders the given search results.
     *
     * @attr searchResult REQUIRED  the search results that should be rendered
     * @attr query                  the search query string
     * @attr parseException         an exception that occurred while parsing the query string
     * @since                       1.4
     */
    def searchResults = { attrs, body ->
        String query = attrs.query?.toString()?.trim()
        def searchResult = attrs.searchResult
        if (!searchResult) return

        List results = searchResult.results
        def parseException = attrs.parseException
        String sort = attrs.sort ?: 'alias'

        StringBuilder buf = new StringBuilder()
        if (query) {
            if (results) {
                buf << '<p class="search-results-number">'
                buf << message(
                    code: 'searchable.results.number',
                    args: [
                        query, searchResult.total, searchResult.offset + 1,
                        results.size() + searchResult.offset
                    ]
                )
                buf << '</p>'
            } else if (!parseException) {
                buf << '<p class="search-results-number search-results-not-found">'
                buf << message(
                    code: 'searchable.results.notFound', args: [query]
                )
                buf << '</p>'
            }
        }

        if (parseException) {
            buf << '<p class="search-results-error">'
            buf << message(code: 'searchable.results.error', args: [query])
            buf << '</p>'
        }

        if (results) {
            buf << '<div class="search-results-results">'
            if (sort == 'alias') {
                Class<?> currentClass = results.first().class
                String className = GrailsNameUtils.getPropertyName(currentClass)
                buf << '<h2>' << message(code: "${className}.plural") << '</h2>'
                buf << '<ul>'
                for (def result in results) {
                    Class<?> clazz = result.class
                    className = GrailsNameUtils.getPropertyName(clazz)
                    if (clazz != currentClass) {
                        currentClass = clazz
                        buf << '</ul>'
                        buf << '<h2>' << message(code: "${className}.plural") << '</h2>'
                        buf << '<ul>'
                    }
                    buf << '<li>'
                    buf << link(
                        controller: className, action: 'show', id: result.id
                    ) {
                        dataTypeIcon(controller: className) + ' ' + result
                    }
                    buf << '<span class="item-actions">'
                    buf << link(
                        controller: className, action: 'edit', id: result.id,
                        params: [returnUrl: url()], 'class': 'bubbling-icon',
                        title: message(code: 'default.btn.edit')
                    ) { '<i class="fa fa-pencil-square-o"></i>' }
                    buf << '</span>'
                    buf << '</li>'
                }
                buf << '</ul>'
            } else {
                int i = 0
                for (def result in results) {
                    String className = GrailsNameUtils.getPropertyName(result.class)
                    buf << '<li>'
                    int score = Math.round(searchResult.scores[i] * 100)
                    int halfs = Math.ceil(score / 10)
                    int fulls = Math.floor(halfs / 2)
                    int empties = Math.floor((10 - halfs) / 2)
                    buf << '<span class="search-results-score" title="'
                    buf << score << ' %">'
                    for (int j = 0; j < fulls; j++) {
                        buf << '<i class="fa fa-star"></i>'
                    }
                    if (fulls * 2 != halfs) {
                        buf << '<i class="fa fa-star-half-o"></i>'
                    }
                    for (int j = 0; j < empties; j++) {
                        buf << '<i class="fa fa-star-o"></i>'
                    }
                    buf << '</span>'
                    buf << link(
                        controller: className, action: 'show', id: result.id
                    ) {
                        dataTypeIcon(controller: className) + ' ' + result
                    }
                    buf << '<span class="search-results-type">('
                    buf << message(code: "${className}.label")
                    buf << ')</span>'
                    buf << '<span class="item-actions">'
                    buf << link(
                        controller: className, action: 'edit', id: result.id,
                        params: [returnUrl: url()], 'class': 'bubbling-icon',
                        title: message(code: 'default.btn.edit')
                    ) { '<i class="fa fa-pencil-square-o"></i>' }
                    buf << '</span>'
                    buf << '</li>'
                    i++
                }
            }
            buf << '</div>'
            buf << '<div class="paginator">'
            buf << paginate(
                total: searchResult.total, action: 'index',
                params: [q: query]
            )
            buf << '</div>'
        }

        out << buf
    }

    /**
     * Renders a normalised title for the current page considering controller
     * and action name.
     *
     * @since 2.0
     */
    def title = { attrs ->
        if (layoutTitle()) {
            out << layoutTitle()
            return
        }

        String entityName = message(code: "${controllerName}.label")

        switch (actionName) {
        case 'edit':
        case 'show':
            def instance = pageScope."${controllerName}Instance"
            out << message(
                code: "default.${actionName}.label",
                args: [instance?.toString() ?: entityName]
            ) << ' - '
            break
        case 'create':
            out << message(code: 'default.create.label', args: [entityName])
            out << ' - '
            break
        }
        out << message(code: "${controllerName}.plural", default: entityName)
    }

    /**
     * Generates the URL of the current page including all request parameters.
     */
    def url = { attrs, body ->
        out << createLink(action: actionName, params: params, absolute: true)
    }


    //-- Non-public methods ---------------------

    private callLink(Map attrs, Object body) {
        TagOutput.captureTagOutput(tagLibraryLookup, 'g', 'link', attrs, body, webRequest)
    }

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
        if (!currencyId || currencyId.length() != 3) {
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
