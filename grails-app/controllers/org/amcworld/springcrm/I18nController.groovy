/*
 * I18nController.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


/**
 * The class {@code I18nController} contains actions that produce a localized
 * JavaScript file from message keys defined in
 * {@code /js/i18n/i18n-source.js}.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class I18nController {

    //-- Public methods -------------------------

    def index() {
        def msgs = [: ]
        loadMessages servletContext.getResourceAsStream('/js/i18n/i18n-source.js'), msgs
        render contentType: 'text/javascript; charset=utf-8', view: 'index', model: [messages: msgs]
    }


    //-- Non-public methods ---------------------

    /**
     * Loads the message keys from the given input stream and produces a map of
     * JavaScript string constants which are ready to create a JavaScript file.
     *
     * @param input the given input stream containing the message keys
     * @param msgs  a map which is filled with the generated JavaScript strings
     */
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
                msgs[key.replace('.', '_')] = renderMessage(key)
            }
        }
    }

    /**
     * Renders the given message key as localized JavaScript string.
     *
     * @param key   the given message key
     * @return      the localized JavaScript string
     * @since       1.3
     */
    protected String renderMessage(String key) {
        String msg = message(code: key)
        if (msg != null) {
            msg = msg.replace '\\', '\\\\'
            msg = msg.replace '"', '\\"'
            msg = "\"${msg}\""
        }
        msg
    }

    /**
     * Renders the given message keys with the stated prefix as localized
     * JavaScript strings.
     *
     * @param l         the given array of message keys
     * @param keyPrefix an optional prefix used before each message keys
     * @return          a computed JavaScript object containing the localized
     *                  messages
     */
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
            msgs[jsKey] = renderMessage(msgKey)
        }

        StringBuilder buf = new StringBuilder('{')
        for (String key : msgs.keySet().sort()) {
            buf << key << ':' << msgs[key] << ','
        }
        buf << '}'
        buf.toString()
    }
}
