package org.amcworld.springcrm

import java.text.Bidi
import java.text.DateFormatSymbols

import java.io.File;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder as LCH

class I18nController {

    def index() {
        def msgs = [ : ]

        File input = new File(servletContext.getRealPath('/js/i18n/i18n-source.js'))
        loadMessages(input, msgs)

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

    protected void loadMessages(File input, Map<String, String> msgs) {
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

    protected String renderMessages(String [] l, String keyPrefix = '')
    {
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
