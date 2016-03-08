/*
 * ProxyHttpParserSpec.groovy
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


package org.amcworld.springcrm.google

import java.nio.charset.Charset
import spock.lang.Specification


class ProxyHttpParserSpec extends Specification {

    //-- Instance variables ---------------------

    private ProxyHttpParser parser =
        new ProxyHttpParser(GoogleSync.JSON_FACTORY)


    //-- Feature methods ------------------------

    def 'Parse and close reader with token response'() {
        given: 'some content'
        String content = '''200 OK
tokenResponse={"access_token":"access4040$Token-4711","expires_in":3600,"refresh_token":"refresh8403%Token-2041","scope":"fooBarScope","token_type":"bearer"}
'''
        def reader = new StringReader(content)

        when: 'I parse this reader'
        def res = parser.parseAndClose(reader, HashMap)

        then: 'I get a map with valid data'
        res instanceof Map
        3 == res.size()
        200 == res.code
        'OK' == res.message
        null != res.tokenResponse
        'access4040$Token-4711' == res.tokenResponse.getAccessToken()
        3600L == res.tokenResponse.getExpiresInSeconds()
        'refresh8403%Token-2041' == res.tokenResponse.getRefreshToken()
        'fooBarScope' == res.tokenResponse.getScope()
        'bearer' == res.tokenResponse.getTokenType()
    }

    def 'Parse and close reader with proxy error'() {
        given: 'some content'
        String content = '550 SERVER_ERROR'
        def reader = new StringReader(content)

        when: 'I parse this reader'
        def res = parser.parseAndClose(reader, HashMap)

        then: 'I get a map with valid data'
        res instanceof Map
        2 == res.size()
        550 == res.code
        'SERVER_ERROR' == res.message
    }

    def 'Parse and close input stream'() {
        given: 'some content'
        String content = '''200 OK
tokenResponse={"access_token":"access4040$Token-4711","expires_in":3600,"refresh_token":"refresh8403%Token-2041","scope":"fooBarScope","token_type":"bearer"}
'''
        def charset = Charset.forName('UTF-8')
        def stream = new ByteArrayInputStream(content.getBytes(charset.toString()))
        def reader = new StringReader(content)

        when: 'I parse this reader'
        def res = parser.parseAndClose(stream, charset, HashMap)

        then: 'I get a map with valid data'
        res instanceof Map
        3 == res.size()
        200 == res.code
        'OK' == res.message
        null != res.tokenResponse
        'access4040$Token-4711' == res.tokenResponse.getAccessToken()
        3600L == res.tokenResponse.getExpiresInSeconds()
        'refresh8403%Token-2041' == res.tokenResponse.getRefreshToken()
        'fooBarScope' == res.tokenResponse.getScope()
        'bearer' == res.tokenResponse.getTokenType()
    }
}
