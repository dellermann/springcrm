/*
 * ProxyHttpParser.groovy
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


package org.amcworld.springcrm.google

import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.JsonParser
import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;


/**
 * The class {@code ProxyHttpParser} represents a parser for responses of the
 * AMC World proxy.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class ProxyHttpParser implements HttpParser {

    //-- Instance variables ---------------------

    JsonFactory jsonFactory


    //-- Constructors ---------------------------

    /**
     * Creates a new parser instance.
     *
     * @param jsonFactory   the JSON parser factory used to create JSON parsers
     *                      for the received token responses
     */
    ProxyHttpParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory
    }


    //-- Public methods -------------------------

    @Override
    String getContentType() {
        return 'text/html'
    }

    @Override
    <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        Map res = null
        if (Map.class.isAssignableFrom(dataClass)) {
            res = dataClass.newInstance()
            if (response.isSuccessStatusCode()) {
                Reader r = new InputStreamReader(response.content)
                String line = r.readLine()
                String [] parts = line.split()
                res.put('code', parts[0] as Short)
                res.put('message', parts[1])
                while ((line = r.readLine()) != null) {
                    parts = line.split(/=/, 2)
                    if (parts.length == 2) {
                        String key = parts[0]
                        def value = parts[1]
                        if (key == 'tokenResponse') {
                            value = parseTokenResponse(value)
                        }
                        res.put(key, value)
                    }
                }
            } else {
                res.put('code', response.statusCode)
            }
        }
        return res
    }


    //-- Non-public methods ---------------------

    /**
     * Parses the given JSON string to a token response.
     *
     * @param content   the given JSON string
     * @return          the parsed token response
     */
    private TokenResponse parseTokenResponse(String content) {
        JsonParser parser = jsonFactory.createJsonParser(content)
        return parser.parseAndClose(GoogleTokenResponse, null)
    }
}
