/*
 * ProxyHttpParser.groovy
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


package org.amcworld.springcrm.google

import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.JsonParser
import com.google.api.client.util.GenericData
import com.google.api.client.util.ObjectParser
import com.google.api.client.util.Preconditions
import com.google.api.client.util.Types
import groovy.transform.CompileStatic
import java.lang.reflect.Type
import java.nio.charset.Charset


/**
 * The class {@code ProxyHttpParser} represents a parser for responses of the
 * AMC World proxy.  Each response from the proxy consists of at least one
 * lines.  The first line represents the status code and a message separated by
 * a whitespace, for example {@code 200 OK}.  All other lines contain key/value
 * pairs in the form {@code key=value}.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.0
 */
@CompileStatic
final class ProxyHttpParser implements ObjectParser {

    //-- Instance variables ---------------------

    /**
     * The JSON parser factory used to create JSON parsers for the received
     * token responses.
     */
    final JsonFactory jsonFactory


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
    <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        (T) parseAndClose(reader, (Type) dataClass)
    }


    @Override
    Object parseAndClose(Reader reader, Type dataType) throws IOException {
        Preconditions.checkArgument(
            dataType instanceof Class<GenericData>,
            'dataType has to be of type Class<GenericData>'
        )

        GenericData res = Types.newInstance((Class<GenericData>) dataType)

        /* read first line (status code and message) */
        String line = reader.readLine()
        String [] parts = line.split()
        res.set('code', parts[0] as Short)
            .set('message', parts[1])

        /* read further lines (key/value pairs) */
        while ((line = reader.readLine()) != null) {
            parts = line.split(/=/, 2)
            if (parts.length == 2) {
                String key = parts[0]
                def value = parts[1]
                switch (key) {
                case 'tokenResponse':
                    value = parseTokenResponse(value)
                    break
                case 'data':
                    value = jsonFactory.fromString(value, HashMap)
                    break
                }
                res.set key, value
            }
        }

        res
    }

    @Override
    <T> T parseAndClose(InputStream stream, Charset charset,
                        Class<T> dataClass)
        throws IOException
    {
        (T) parseAndClose(stream, charset, (Type) dataClass)
    }


    @Override
    Object parseAndClose(InputStream stream, Charset charset, Type dataType)
        throws IOException
    {
        InputStreamReader reader = new InputStreamReader(stream, charset)
        parseAndClose reader, dataType
    }


    //-- Non-public methods ---------------------

    /**
     * Parses the given JSON string to a token response.
     *
     * @param content   the given JSON string
     * @return          the parsed token response
     */
    protected TokenResponse parseTokenResponse(String content) {
        JsonParser parser = jsonFactory.createJsonParser(content)
        parser.parseAndClose TokenResponse
    }
}
