/*
 * I18nController.groovy
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

import java.text.Bidi
import java.text.DateFormatSymbols
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code I18nController} contains actions that produce a localized
 * JavaScript file from message keys defined in
 * {@code /js/i18n/i18n-source.js}.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class I18nController {

    //-- Public methods -------------------------

    def index() {
        def msgs = [: ]

        loadMessages(servletContext.getResourceAsStream('/js/i18n/i18n-source.js'), msgs)

        def dfs = DateFormatSymbols.getInstance(LCH.locale)
        msgs['monthNamesLong'] = '[ "' << dfs.months.join('", "') << '" ]'
        msgs['monthNamesShort'] = '[ "' << dfs.shortMonths.join('", "') << '" ]'
        msgs['weekdaysLong'] = '[ "' << dfs.weekdays[1..-1].join('", "') << '" ]'
        msgs['weekdaysShort'] = '[ "' << dfs.shortWeekdays[1..-1].join('", "') << '" ]'

        def cal = Calendar.instance
        msgs['calendarFirstDay'] = cal.firstDayOfWeek
        String s = message(code: 'default.bidi.test')
        msgs['calendarRTL'] = !(new Bidi(s, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isLeftToRight())

        render(contentType: 'text/javascript; charset=utf-8', view: 'index', model: [messages: msgs])
    }


    //-- Non-public methods ---------------------

    protected void loadMessages(InputStream input, Map<String, String> msgs) {
        input.eachLine {
            if (it ==~ /^\s*$/ || it ==~ /^\s*\/\/.*$/ || it ==~ /^\s*[\[\]].*$/) {
                return
            }

            def m = (it =~ /["'](.+)["']/)
            if (!m) {
                return
            }

            String key = m[0][1]
            m = (key =~ /^(.+)\{(.+)\}$/)
            if (m) {
                String parentKey = m[0][1]
                msgs[parentKey.replace('.', '_')] =
                    renderMessages(m[0][2].split(/\s*,\s*/), parentKey)
            } else {
                String msg = message(code: key)
                if (msg != null) msg = "\"${msg}\""
                msgs[key.replace('.', '_')] = msg
            }
        }
    }

    protected String renderMessages(String [] l, String keyPrefix = '') {
        def msgs = [ : ]
        for (String key : l) {
            String msgKey = keyPrefix
            String jsKey = '""'
            if (key != "") {
                msgKey += '.'
                jsKey = key.replace('.', '_')
            }
            msgKey += key
            def msg = message(code: msgKey)
            if (msg != null) msg = "\"${msg}\""
            msgs[jsKey] = msg
        }

        StringBuilder buf = new StringBuilder('{')
        for (String key : msgs.keySet().sort()) {
            buf << key << ':' << msgs[key] << ','
        }
        buf << '}'
        return buf.toString()
    }
}
